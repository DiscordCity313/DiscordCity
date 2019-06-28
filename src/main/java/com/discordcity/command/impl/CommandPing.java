package com.discordcity.command.impl;

import com.discordcity.command.Command;
import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.entities.Message;

public class CommandPing extends Command {

    public CommandPing() {
        super("ping", "Pings the bot");
        this.setUnlisted(true);
    }

    @Override
    public void use(Message message, String[] arguments, Sqlite database) {
        this.reply(message, "Pong!");
    }

}
