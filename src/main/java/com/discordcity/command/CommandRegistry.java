package com.discordcity.command;

import com.discordcity.command.impl.CommandPing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.discordcity.command.impl.CommandPurchaseTile;
import com.discordcity.command.impl.CommandViewCity;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

public class CommandRegistry {

    private List<Command> registeredCommands = new ArrayList<Command>();

    public CommandRegistry(String prefix) throws IOException {
        this.registerCommands(prefix);
    }

    private void registerCommands(String prefix) throws IOException {
        this.registerCommand(new CommandPing());
        this.registerCommand(new CommandViewCity());
        this.registerCommand(new CommandPurchaseTile());

        for(Command command : this.registeredCommands) {
            command.setPrefix(prefix);
        }
    }

    private void registerCommand(Command command) {
        this.registeredCommands.add(command);
    }

    public void callMatchingCommandForQuery(String prefix, Message query, MySql database) {
        String queryContent = query.getContentRaw();
        String unprefixedContent = queryContent.replaceFirst(prefix, "");

        String[] queryWords = unprefixedContent.split(" ");
        String queryIdentifier = queryWords[0];

        for(Command possibleMatch : this.registeredCommands) {
            if(possibleMatch.identifierMatches(queryIdentifier)) {
                String[] queryArguments = unprefixedContent.replaceFirst(queryIdentifier + " ", "").split(" ");

                possibleMatch.use(query, queryArguments, database);
            }
        }

    }

}
