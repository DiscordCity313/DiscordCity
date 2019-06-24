package com.discordcity.command.impl;

import com.discordcity.command.Command;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

public class CommandPing extends Command {

    public CommandPing() {
        super("ping", "Pings the bot");
    }

    @Override
    public void use(Message message, String[] arguments, MySql database) {
        this.reply(message, "Pong!");
    }

}
