package io.github.levtey.OneTimeRewards;

import java.util.StringJoiner;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.CommandParser;

public class CommandHandler {
	
	private final OneTimeRewards plugin;
	
	public CommandHandler(OneTimeRewards plugin) {
		this.plugin = plugin;
		new CommandParser(plugin.getResource("command.rdcml"))
		.parse().register("", this);
	}
	
	@CommandHook("grant")
	public void grant(CommandSender sender, String name, Player player) {
		Reward reward = plugin.rewards.get(name);
		if (reward == null) {
			sender.sendMessage(ChatColor.RED + "Invalid reward.");
		}
		reward.execute(player);
	}
	
	@CommandHook("add")
	public void add(CommandSender sender, String name, String...command) {
		if (command == null) {
			ItemStack inHand = sender instanceof Player ? ((Player) sender).getInventory().getItemInMainHand() : null;
			if (inHand == null || inHand.getType() == Material.AIR) {
				sender.sendMessage(ChatColor.RED + "You need to hold an item to do this.");
				return;
			}
			Reward reward = plugin.rewards.getOrDefault(name, new Reward(name));
			plugin.rewards.put(name, reward.addItem(inHand));
			plugin.getManager().save();
			sender.sendMessage(ChatColor.GREEN + "Added item to " + name + " successfully.");
			return;
		}
		StringJoiner joiner = new StringJoiner(" ");
		for (String arg : command) {
			joiner.add(arg);
		}
		Reward reward = plugin.rewards.getOrDefault(name, new Reward(name));
		plugin.rewards.put(name, reward.addCommand(joiner.toString()));
		plugin.getManager().save();
		sender.sendMessage(ChatColor.GREEN + "Added command to " + name + " successfully.");
	}
	
	@CommandHook("reload")
	public void reload(CommandSender sender) {
		plugin.reload();
		sender.sendMessage(ChatColor.GREEN + "Configs reloaded.");
	}

}
