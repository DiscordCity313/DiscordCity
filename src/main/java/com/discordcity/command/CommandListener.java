package com.discordcity.command;

import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;

public class CommandListener extends ListenerAdapter {

    private CommandRegistry commandRegistry;

    private String prefix;

    private Sqlite database;

    public CommandListener(String prefix, Sqlite database) throws IOException {
        this.commandRegistry = new CommandRegistry(prefix);
        this.prefix = prefix;
        this.database = database;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        this.commandRegistry.callMatchingCommandForQuery(this.prefix, event.getMessage(), this.database);
    }

    @Override
    public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event) {
        super.onGenericGuildMessageReaction(event);
        this.commandRegistry.useReaction(event, this.database);
    }
}
