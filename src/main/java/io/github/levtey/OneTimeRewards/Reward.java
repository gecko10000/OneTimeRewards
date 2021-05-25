package io.github.levtey.OneTimeRewards;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigMappable;
import redempt.redlib.configmanager.annotations.ConfigPath;
import redempt.redlib.configmanager.annotations.ConfigValue;

@ConfigMappable
public class Reward {
	
	@ConfigPath
	private String name;
	
	@ConfigValue("max-claims")
	private int max = 1;
	
	@ConfigValue("commands")
	private List<String> commands = ConfigManager.list(String.class);
	
	@ConfigValue("items")
	private List<ItemStack> items = ConfigManager.list(ItemStack.class);
	
	protected Reward() {}
	
	public Reward(String name) {
		this.name = name;
	}
	
	public Reward addCommand(String command) {
		commands.add(command);
		return this;
	}
	
	public Reward addItem(ItemStack item) {
		items.add(item);
		return this;
	}
	
	public Reward setMax(int max) {
		this.max = max;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean execute(Player player) {
		PersistentDataContainer pdc = player.getPersistentDataContainer();
		if (alreadyExecuted(pdc)) return false;
		commands.stream()
		.map(str -> str.replace("%player%", player.getName()))
		.forEach(str -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str));
		player.getInventory().addItem(items.toArray(new ItemStack[0])).forEach((index, extra) -> player.getWorld().dropItem(player.getLocation(), extra));
		pdc.set(rewardKey(), PersistentDataType.INTEGER, pdc.getOrDefault(rewardKey(), PersistentDataType.INTEGER, 0) + 1);
		return true;
	}
	
	public void reset(Player player) {
		player.getPersistentDataContainer().set(rewardKey(), PersistentDataType.INTEGER, 0);
	}
	
	private boolean alreadyExecuted(PersistentDataContainer pdc) {
		return pdc.getOrDefault(rewardKey(), PersistentDataType.INTEGER, 0) >= max;
	}
	
	private NamespacedKey rewardKey() {
		return NamespacedKey.fromString("onetimerewards:" + name);
	}

}
