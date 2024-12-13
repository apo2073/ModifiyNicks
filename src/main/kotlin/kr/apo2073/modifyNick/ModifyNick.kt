package kr.apo2073.modifyNick

import org.bukkit.plugin.java.JavaPlugin

class ModifyNick : JavaPlugin() {
    companion object {lateinit var instance:ModifyNick}
    override fun onEnable() {
        instance=this
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
