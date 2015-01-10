package io.github.cptshock.BrightProxy;

import java.net.InetAddress;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class main extends JavaPlugin implements Listener {
	public void onEnable() {
		getLogger().info("§2sBrightProxy Loading..");
		getLogger().info("§2sStarting listener....");
		Bukkit.getPluginManager().registerEvents(this, this);
		getLogger().info("§2sListener Started.");
		getLogger().info("§2sBrightProxy Succefully enabled!");
		saveDefaultConfig();
	}

	public void onDisable() {
		getLogger().info("Â§cBrightProxy disabled.");
	}

	public Boolean ProxyCheck(String IP, String Url, String trigger)
			throws Exception {
		String res = "";
		Scanner ProxyChecker = new Scanner(new URL(Url + IP).openStream());
		while (ProxyChecker.hasNextLine()) {
			res = res + ProxyChecker.nextLine();
		}
		ProxyChecker.close();
		if (res.contains(trigger)) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	public Boolean BotScoutProxyCheck(String IP, String Url, String trigger,
			String ApiKey) throws Exception {
		String res = "";
		Scanner ProxyChecker = new Scanner(
				new URL(Url + IP + ApiKey).openStream());
		while (ProxyChecker.hasNextLine()) {
			res = res + ProxyChecker.nextLine();
		}
		ProxyChecker.close();
		if (res.contains(trigger)) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLoginEvent(AsyncPlayerPreLoginEvent e) throws Exception {
		String player = e.getName();
		String playerip = e.getAddress().getHostAddress();
		Boolean isProxy = Boolean.valueOf(false);
		if (ProxyCheck(playerip, "http://www.stopforumspam.com/api?ip=", "yes")
				.booleanValue()) {
			isProxy = Boolean.valueOf(true);
		}
		if (ProxyCheck(playerip,
				"http://winmxunlimited.net/api/proxydetection/v1/query/?ip=",
				"Public").booleanValue()) {
			isProxy = Boolean.valueOf(true);
		}
		if (playerip.startsWith("173.")) {
			isProxy = Boolean.valueOf("true");
		}
		if (isProxy.booleanValue()) {
			getLogger().info(
					"[BrightProxy]" + player + " (" + playerip + ")"
							+ " tried to connect using a proxy!");
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, getConfig()
					.getString("Kick_Message"));
			for (Player p : Bukkit.getOnlinePlayers()) {
				if ((p.hasPermission("brightproxy.notify"))
						&& (getConfig().getBoolean("Notify"))) {
					p.sendMessage(ChatColor.GRAY + player + " (" + playerip
							+ ")" + " tried to connect using a proxy!");
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("brightproxy")) {
			onDisable();
			onEnable();
			reloadConfig();
			sender.sendMessage("Â§cBrightProxy config reloaded!");
		}
		return false;
	}
}