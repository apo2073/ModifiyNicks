package kr.apo2073.modifyNick.utilities

import kr.apo2073.lib.Items.ItemBuilder
import kr.apo2073.modifyNick.ModifyNick
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.wesjd.anvilgui.AnvilGUI
import net.wesjd.anvilgui.AnvilGUI.ResponseAction
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class NickManager: Listener {
    private var craftPlayerClass: Class<*>? = null
    private val version: String =
        ModifyNick.instance.server.javaClass.getName().replace(".", ",").split(",".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[3]

    init {
        craftPlayerClass = Class.forName("org.bukkit.craftbukkit.$version.entity.CraftPlayer")
    }

    private fun config():YamlConfiguration {
        val file=file()
        val config=YamlConfiguration.loadConfiguration(file)
        return config
    }
    private fun file():File {
        val file=File("${ModifyNick.instance.dataFolder}", "info.yml")
        return file
    }

    fun openUI(player: Player) {
        AnvilGUI.Builder().itemLeft(ItemBuilder(Material.NAME_TAG).setDisplayName("새 이름을 입력 하세요").build())
            .onClick { slot, stateSnapshot ->
                try {
                    if (slot != 2) {
                        return@onClick emptyList<ResponseAction>()
                    } else {
                        val item = stateSnapshot.outputItem
                        val nick = PlainTextComponentSerializer.plainText().serialize(item.displayName())
                            .removePrefix("[").removeSuffix("]")
                        if (nick.isBlank()) {
                            return@onClick arrayListOf(ResponseAction.replaceInputText("올바른 닉네임을 입력하세요"))
                        }

                        hideAll(player)
                        setNick(player, nick)
                        showAll(player)
                        player.sendMessage("§l[ §a§l::§f§l ] §f§l닉네임 변경됨 : $nick")

                        config().set("${player.uniqueId}.count", config().getInt("${player.uniqueId}.count", 0) + 1)
                        config().set("${player.uniqueId}.nick", nick)

                        return@onClick arrayListOf(ResponseAction.close())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@onClick arrayListOf(ResponseAction.replaceInputText(e.message))
                }
            }.text("새로운 닉네임 입력 ( ${config().getInt("${player.uniqueId}.count", 0) + 1}회 )")
            .title("닉네임 변경").plugin(ModifyNick.instance).open(player)
        try {
            config().save(file())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setNick(player: Player, nickname: String) {
        try {
            val craftPlayer: Any = this.craftPlayerClass?.cast(player) ?: return
            val method: Method = this.craftPlayerClass?.getMethod("getProfile") ?: return
            val gameProfile = method.invoke(craftPlayer)
            val field = gameProfile.javaClass.getDeclaredField("name")
            field.isAccessible = true
            field[gameProfile] = nickname

            player.playerListName(Component.text(nickname))
            player.displayName(Component.text(nickname))
            player.customName(Component.text(nickname))
            player.isCustomNameVisible=true

            player.hidePlayer(ModifyNick.instance, player)
            ModifyNick.instance.server.scheduler.runTaskLater(ModifyNick.instance, Runnable {
                player.showPlayer(ModifyNick.instance, player)
            }, 2L)

        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }
    }


    private fun hideAll(player: Player) {
        for (loopPlayer in ModifyNick.instance.server.onlinePlayers) {
            loopPlayer.hidePlayer(ModifyNick.instance, player)
        }
    }

    private fun showAll(player: Player) {
        for (loopPlayer in ModifyNick.instance.server.onlinePlayers) {
            loopPlayer.showPlayer(ModifyNick.instance, player)
        }
    }
}

//((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenWindow(1, Containers.ANVIL, new ChatComponentText("IT WORKS!")));
