package kr.apo2073.modifyNick

import kr.apo2073.modifyNick.events.PlayerEvents
import kr.apo2073.modifyNick.utilities.NickManager
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ModifyNick : JavaPlugin() {
    companion object {lateinit var instance:ModifyNick}
    override fun onEnable() {
        instance=this
        saveDefaultConfig()

        server.pluginManager.registerEvents(NickManager(), this)
        server.pluginManager.registerEvents(PlayerEvents(), this)
        getCommand("닉네임")?.setExecutor { sender, _, _, strings ->
            NickManager().openUI(sender as Player)
            true
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
