/*
 * Copyright (C) 2013 Spencer Alderman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rogue.orelog;

import com.rogue.orelog.command.CommandHandler;
import com.rogue.orelog.config.ConfigValues;
import com.rogue.orelog.config.ConfigurationLoader;
import com.rogue.orelog.listener.ListenerManager;
import com.rogue.orelog.metrics.Metrics;
import com.rogue.orelog.update.Choice;
import com.rogue.orelog.update.UpdateHandler;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main {@link JavaPlugin} class
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class LogOre extends JavaPlugin {

    private final int ID = 0;
    private static String NAME;
    private static final String PREFIX = "&7[&bOreLog&7] ";
    private CommandHandler chandle;
    private ConfigurationLoader cloader;
    private ListenerManager listener;
    private UpdateHandler update;

    /**
     * Loads informational and configurable aspects of {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onLoad() {
        LogOre.NAME = this.getDescription().getFullName();

        this.getLogger().log(Level.INFO, "Loading configuration...");
        this.cloader = new ConfigurationLoader(this);
    }

    /**
     * Enables the separate modules and managers of {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onEnable() {
        try {
            Metrics metrics = new Metrics(this);
            this.getLogger().log(Level.INFO, "Enabling Metrics...");
            metrics.start();
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Error enabling metrics!", ex);
        }

        this.getLogger().log(Level.INFO, "Enabling listeners...");
        this.listener = new ListenerManager(this);

        this.getLogger().log(Level.INFO, "Enabling command handler...");
        this.chandle = new CommandHandler(this, "&b", "&7");

        this.getLogger().log(Level.INFO, "Evaluating update checks...");
        boolean check = this.cloader.getBoolean(ConfigValues.UPDATE_CHECK);
        boolean dl = this.cloader.getBoolean(ConfigValues.UPDATE_DOWNLOAD);
        this.update = new UpdateHandler(this,
                Choice.getChoice(check, dl),
                this.ID,
                this.getFile().getName());
    }

    /**
     * Cleans up plugin resources
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    @Override
    public void onDisable() {
        this.listener.cleanup();
    }

    /**
     * Reloads the plugin
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param names Any players to notify after the reload
     */
    public void reload(final String... names) {
        final String reloadDone = "OreLog reloaded!";
        final LogOre plugin = this;
        this.onDisable();
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                plugin.chandle = null;
                plugin.listener = null;
                plugin.cloader = null;
                plugin.update = null;
                plugin.onLoad();
                plugin.onEnable();
                plugin.getLogger().log(Level.INFO, reloadDone);
                for (String s : names) {
                    LogOre.communicate(plugin.getServer().getPlayer(s), reloadDone);
                }
            }
        }, 10L);
    }

    /**
     * Gets the instance of the plugin in its entirety.
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return The plugin instance
     */
    public static LogOre getPlugin() {
        return (LogOre) Bukkit.getServer().getPluginManager().getPlugin(LogOre.NAME);
    }

    /**
     * Converts pre-made strings to have chat colors in them
     *
     * @param encoded String with unconverted color codes
     * @return string with correct chat colors included
     */
    public static String __(String encoded) {
        return ChatColor.translateAlternateColorCodes('&', encoded);
    }

    /**
     * Sends a message to a provided in-game object
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param sender The in-game object to communicate to
     * @param message The (raw) message to send
     */
    public static void communicate(CommandSender sender, String message) {
        sender.sendMessage(__(PREFIX + message));
    }

    /**
     * Gets the {@link CommandHandler} for {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return The {@link CommandHandler} instance
     */
    public CommandHandler getCommandHandler() {
        return this.chandle;
    }

    /**
     * Gets the {@link ConfigurationLoader} for {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return The {@link ConfigurationLoader} instance
     */
    public ConfigurationLoader getConfiguration() {
        return this.cloader;
    }

    /**
     * Gets the {@link ListenerManager} for {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return The {@link ListenerManager} instance
     */
    public ListenerManager getListenerManager() {
        return this.listener;
    }

    /**
     * Gets the {@link UpdateHandler} for {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return The {@link UpdateHandler} instance
     */
    public UpdateHandler getUpdateHandler() {
        return this.update;
    }

}
