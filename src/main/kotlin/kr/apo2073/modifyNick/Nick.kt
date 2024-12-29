package kr.apo2073.modifyNick

import kr.apo2073.modifyNick.events.PlayerEvents
import kr.apo2073.modifyNick.utilities.Econ
import kr.apo2073.modifyNick.utilities.NickManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener

class Nick : JavaPlugin(), PluginMessageListener {
    companion object {
        lateinit var instance:Nick
        lateinit var econ: Economy
    }
    override fun onEnable() {
        instance=this
        saveDefaultConfig()
        server.pluginManager.registerEvents(NickManager(), this)
        server.pluginManager.registerEvents(PlayerEvents(), this)
        server.messenger.registerIncomingPluginChannel(Nick.instance, "nick:open",  this)
        getCommand("닉네임")?.setExecutor { sender, _, _, strings ->
            NickManager().openUI(sender as Player)
            true
        }
//        server.servicesManager.register(
//            net.milkbowl.vault.economy.Economy::class.java,
//            Econ(this),
//            this,
//            ServicePriority.Normal
//        )
        if (!setupEconomy()) {
            logger.severe("vault또는 경제 플러그인을 설치하세요")
            server.pluginManager.disablePlugin(this)
            return
        }
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel == "nick:open") {
            NickManager().openUI(player)
        }
    }

    private fun setupEconomy(): Boolean {
        val vaultPlugin = server.pluginManager.getPlugin("Vault")
        if (vaultPlugin == null || !vaultPlugin.isEnabled) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        econ = rsp.provider
        return true
    }
}
