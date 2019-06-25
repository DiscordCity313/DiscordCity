package com.discordcity.command;

import com.discordcity.command.impl.CommandPing;

import java.util.ArrayList;
import java.util.List;

import com.discordcity.command.impl.CommandViewCity;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

public class CommandRegistry {

    private List<Command> registeredCommands = new ArrayList<Command>();

    public CommandRegistry() {
        this.registerCommands();
    }

    private void registerCommands() {
        this.registerCommand(new CommandPing());
        this.registerCommand(new CommandViewCity());
    }

    private void registerCommand(Command command) {
        this.registeredCommands.add(command);
    }

    public void callMatchingCommandForQuery(String prefix, Message query, MySql database) {
        String queryContent = query.getContentRaw();

        String[] queryWords = queryContent.replaceFirst(prefix, "").split(" ");
        String queryIdentifier = queryWords[0];

        for(Command possibleMatch : this.registeredCommands) {
            if(possibleMatch.identifierMatches(queryIdentifier)) {
                String[] queryArguments = queryWords.toString().replaceFirst(queryIdentifier, "").split(" ");

                possibleMatch.use(query, queryArguments, database);
            }
        }

    }

}
