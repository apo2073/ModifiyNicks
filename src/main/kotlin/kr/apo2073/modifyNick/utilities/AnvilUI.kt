package kr.apo2073.modifyNick.utilities

import kr.apo2073.modifyNick.ModifyNick
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.entity.Player


object AnvilUI {
    fun openUI(player: Player) {
        player.openAnvil(player.location, true)
    }
}