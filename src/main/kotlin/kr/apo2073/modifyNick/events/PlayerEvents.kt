package kr.apo2073.modifyNick.events

import kr.apo2073.modifyNick.ModifyNick
import kr.apo2073.modifyNick.utilities.NickManager
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File

class PlayerEvents: Listener {
    private fun config(): YamlConfiguration {
        val file=file()
        val config= YamlConfiguration.loadConfiguration(file)
        return config
    }
    private fun file(): File {
        val file= File("${ModifyNick.instance.dataFolder}", "info.yml")
        return file
    }
    @EventHandler
    fun onJoin(e:PlayerJoinEvent) {
        NickManager().setNick(e.player, config()
            .getString("${e.player.uniqueId}.nick", e.player.name).toString())
        try { config().save(file()) } catch (e:Exception) {e.printStackTrace()}
    }
}