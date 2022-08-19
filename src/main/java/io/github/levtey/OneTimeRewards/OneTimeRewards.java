package io.github.levtey.OneTimeRewards;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import redempt.redlib.config.ConfigManager;

public class OneTimeRewards extends JavaPlugin {

	public static Map<String, Reward> rewards = new HashMap<>();
	private ConfigManager manager;
	
	public void onEnable() {
		new CommandHandler(this);
		reload();
	}
	
	public void reload() {
		manager = ConfigManager.create(this).target(OneTimeRewards.class).saveDefaults().load();
	}
	
	public ConfigManager getManager() {
		return manager;
	}

}
