package kr.apo2073.modifyNick.utilities

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class Econ(private val plugin: JavaPlugin) : Economy {
    private val balances = HashMap<UUID, Double>()
    private val file = File(plugin.dataFolder, "money.yml")
    private val config = YamlConfiguration.loadConfiguration(file)

    init {
        loadData()
    }

    private fun loadData() {
        if (!file.exists()) return
        for (key in config.getKeys(false)) {
            balances[UUID.fromString(key)] = config.getDouble(key)
        }
    }

    private fun saveData() {
        balances.forEach { (uuid, amount) ->
            config.set(uuid.toString(), amount)
        }
        config.save(file)
    }

    override fun isEnabled() = true

    override fun getName() = "ModifyNick"

    override fun hasBankSupport() = false

    override fun fractionalDigits() = 0

    override fun format(amount: Double) = "${amount.toInt()}원"

    override fun currencyNamePlural() = "원"

    override fun currencyNameSingular() = "원"

    override fun hasAccount(player: OfflinePlayer?) = true
    override fun hasAccount(playerName: String?) = true
    override fun hasAccount(player: OfflinePlayer?, worldName: String?) = true
    override fun hasAccount(playerName: String?, worldName: String?) = true

    override fun getBalance(player: OfflinePlayer?): Double {
        return balances[player?.uniqueId] ?: 0.0
    }

    override fun getBalance(playerName: String?): Double {
        val player = Bukkit.getOfflinePlayer(playerName ?: return 0.0)
        return getBalance(player)
    }

    override fun getBalance(player: OfflinePlayer?, worldName: String?) = getBalance(player)
    override fun getBalance(playerName: String?, worldName: String?) = getBalance(playerName)

    override fun has(player: OfflinePlayer?, amount: Double): Boolean {
        return getBalance(player) >= amount
    }

    override fun has(playerName: String?, amount: Double): Boolean {
        return has(Bukkit.getOfflinePlayer(playerName ?: return false), amount)
    }
    override fun has(player: OfflinePlayer?, worldName: String?, amount: Double) = has(player, amount)
    override fun has(playerName: String?, worldName: String?, amount: Double) = has(playerName, amount)

    override fun withdrawPlayer(player: OfflinePlayer?, amount: Double): EconomyResponse {
        if (!has(player, amount)) {
            return EconomyResponse(0.0, getBalance(player), EconomyResponse.ResponseType.FAILURE, "잔액 부족")
        }
        balances[(player?.uniqueId
            ?: return EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "NULL") )] = getBalance(player) - amount
        saveData()
        return EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName ?: return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "플레이어 없음")), amount)
    }

    override fun withdrawPlayer(player: OfflinePlayer?, worldName: String?, amount: Double) = withdrawPlayer(player, amount)
    override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double) = withdrawPlayer(playerName, amount)

    override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        balances[player.uniqueId] = getBalance(player) + amount
        saveData()
        return EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName ?: return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "플레이어 없음")), amount)
    }

    override fun depositPlayer(player: OfflinePlayer, worldName: String?, amount: Double) = depositPlayer(player, amount)
    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double) = depositPlayer(playerName, amount)

    override fun createPlayerAccount(player: OfflinePlayer?): Boolean {
        if (hasAccount(player)) return false
        balances[(player?.uniqueId ?: return false)] = 0.0
        saveData()
        return true
    }

    override fun createPlayerAccount(playerName: String?): Boolean {
        return createPlayerAccount(Bukkit.getOfflinePlayer(playerName ?: return false))
    }
    override fun createPlayerAccount(player: OfflinePlayer?, worldName: String?) = createPlayerAccount(player)
    override fun createPlayerAccount(playerName: String?, worldName: String?) = createPlayerAccount(playerName)

    override fun createBank(name: String?, player: String?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun createBank(name: String?, player: OfflinePlayer?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun deleteBank(name: String?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun bankBalance(name: String?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun bankHas(name: String?, amount: Double) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun bankWithdraw(name: String?, amount: Double) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun bankDeposit(name: String?, amount: Double) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun isBankOwner(name: String?, playerName: String?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun isBankOwner(name: String?, player: OfflinePlayer?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun isBankMember(name: String?, playerName: String?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun isBankMember(name: String?, player: OfflinePlayer?) = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "은행 기능 미지원")
    override fun getBanks() = mutableListOf<String>()
}
