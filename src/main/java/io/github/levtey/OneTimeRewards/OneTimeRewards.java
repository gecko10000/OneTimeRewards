package io.github.levtey.OneTimeRewards;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigValue;

public class OneTimeRewards extends JavaPlugin {
	
	@ConfigValue("rewards")
	public Map<String, Reward> rewards = ConfigManager.map(Reward.class);
	private ConfigManager manager;
	
	public void onEnable() {
		new CommandHandler(this);
		reload();
	}
	
	public void reload() {
		manager = new ConfigManager(this).register(this).saveDefaults().load();
	}
	
	public ConfigManager getManager() {
		return manager;
	}

}
