package kr.apo2073.modifyNick.events

import kr.apo2073.modifyNick.Nick
import kr.apo2073.modifyNick.utilities.NickManager
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatPreviewEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File

class PlayerEvents: Listener {
    private var file= File("${Nick.instance.dataFolder}", "info.yml")
    private var config= YamlConfiguration.loadConfiguration(file)
    @EventHandler
    fun onJoin(e:PlayerJoinEvent) {
        try {
            file= File("${Nick.instance.dataFolder}", "info.yml")
            config= YamlConfiguration.loadConfiguration(file)
            NickManager().setNick(
                e.player, config
                    .getString("${e.player.uniqueId}.nick", e.player.name).toString()
            )
        } catch (e: Exception) {e.printStackTrace()}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(e: AsyncPlayerChatPreviewEvent) {
        try {
            file = File("${Nick.instance.dataFolder}", "info.yml")
            config = YamlConfiguration.loadConfiguration(file)
            e.format=e.format.replace("${
                Bukkit.getPlayer(e.player.uniqueId)
            }", config.getString("${e.player.uniqueId}.nick", e.player.name).toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}