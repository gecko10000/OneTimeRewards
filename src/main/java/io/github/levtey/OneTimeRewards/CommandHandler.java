package io.github.levtey.OneTimeRewards;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.CommandParser;

public class CommandHandler {
	
	private final OneTimeRewards plugin;
	
	public CommandHandler(OneTimeRewards plugin) {
		this.plugin = plugin;
		ArgType<Reward> rewardType = new ArgType<>("reward", name -> OneTimeRewards.rewards.computeIfAbsent(name, k -> new Reward(name)))
				.tabStream(sender -> OneTimeRewards.rewards.keySet().stream());
		new CommandParser(plugin.getResource("command.rdcml"))
		.setArgTypes(rewardType)
		.parse().register("", this);
	}
	
	@CommandHook("grant")
	public void grant(CommandSender sender, Reward reward, Player player) {
		reward.execute(player);
	}
	
	@CommandHook("reset")
	public void reset(CommandSender sender, Reward reward, Player player) {
		reward.reset(player);
	}
	
	@CommandHook("add")
	public void add(CommandSender sender, Reward reward, String command) {
		if (command == null) {
			ItemStack inHand = sender instanceof Player ? ((Player) sender).getInventory().getItemInMainHand() : null;
			if (inHand == null || inHand.getType() == Material.AIR) {
				sender.sendMessage(ChatColor.RED + "You need to hold an item to do this.");
				return;
			}
			sender.sendMessage(ChatColor.GREEN + "Added item to " + reward.addItem(inHand).getName() + " successfully.");
		} else {
			sender.sendMessage(ChatColor.GREEN + "Added command to " + reward.addCommand(command).getName() + " successfully.");
		}
		plugin.getManager().save();
	}
	
	@CommandHook("setmax")
	public void setmax(CommandSender sender, Reward reward, int max) {
		sender.sendMessage(ChatColor.GREEN + "Set " + reward.setMax(max).getName() + "'s max claims to " + max + ".");
		plugin.getManager().save();
	}
	
	@CommandHook("reload")
	public void reload(CommandSender sender) {
		plugin.reload();
		sender.sendMessage(ChatColor.GREEN + "Configs reloaded.");
	}

}
