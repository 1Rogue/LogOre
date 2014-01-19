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
package com.rogue.logore.command;

import com.rogue.logore.LogOre;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Manages commands abstractly for the plugin
 *
 * @since 1.3.0
 * @author 1Rogue
 * @version 1.4.2
 */
public class CommandHandler implements CommandExecutor {

    private final LogOre plugin;
    private final String info;
    private final String authors;

    /**
     * {@link CommandHandler} constructor
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param plugin The main {@link LogOre} instance
     * @param highlight Color code to highlight text with
     * @param normal Color code for normal text
     */
    public CommandHandler(LogOre plugin, String highlight, String normal) {
        StringBuilder i = new StringBuilder();
        i.append(highlight)
                .append(plugin.getDescription().getName())
                .append(normal)
                .append(" v")
                .append(highlight)
                .append(plugin.getDescription().getVersion())
                .append(normal)
                .append(".");
        StringBuilder a = new StringBuilder("Made by ");
        boolean first = true;
        for (String author : plugin.getDescription().getAuthors()) {
            if (first) {
                a.append(highlight)
                        .append(author)
                        .append(normal);
                first = false;
            } else {
                a.append(", ").append(highlight)
                        .append(author)
                        .append(normal);
            }
        }
        
        this.plugin = plugin;
        this.info = i.toString();
        this.authors = a.append(".").toString();

        final CommandHandler chand = this;
        plugin.getCommand("orelog").setExecutor(chand);
    }

    /**
     * Executes the informational command for {@link LogOre}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param sender The command executor
     * @param cmd The command instance
     * @param commandLabel The command name
     * @param args The command arguments
     *
     * @return Success of command, false if no command is found
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0]) && sender.hasPermission("orelog.reload")) {
            if (sender instanceof Player) {
                this.plugin.reload(sender.getName());
            }
            this.plugin.reload();
        } else {
            LogOre.communicate(sender, this.info);
            LogOre.communicate(sender, this.authors);
        }
        return true;
    }

}
