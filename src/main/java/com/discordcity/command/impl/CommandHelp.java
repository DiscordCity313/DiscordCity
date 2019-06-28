package com.discordcity.command.impl;

import com.discordcity.command.Command;
import com.discordcity.command.CommandRegistry;
import com.discordcity.database.Sqlite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandHelp extends Command {

    private CommandRegistry commandRegistry;

    public CommandHelp(CommandRegistry commandRegistry) {
        super("help", "Displays the help page");
        this.commandRegistry = commandRegistry;
        this.setUnlisted(true);
    }

    @Override
    public void use(Message message, String[] arguments, Sqlite database) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String welcomeMessage = ("Welcome to Discord City! Your goal is to build and grow your own little city on Discord.");

        embedBuilder.addField("Welcome!", welcomeMessage, false);

        embedBuilder.addField("Commands", this.generateCommandsText(), false);

        String gameDescription = ("Once you've placed at least one house, people will begin moving in over time.");
        gameDescription += (" The more people you have, the more tax revenue is collected.");
        gameDescription += (" If unemployment goes above 30%, people will stop moving in.");
        gameDescription += (" To lower unemployment, build some industry and provide jobs for your people!");
        gameDescription += ("\n\nTo check in on your city's progress, type " + this.getPrefix() + "city");

        embedBuilder.addField("How to Play", gameDescription, false);

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    private String generateCommandsText() {
        String commandsText = ("");

        for(Command command : this.commandRegistry.getRegisteredCommands()) {
            if(!command.isUnlisted()) {
                String identifierFull = (this.getPrefix() + command.getPrimaryIdentifier());
                commandsText += ("\n" + identifierFull + " " + command.getArguments() + "\n - " + command.getDescription() + "\n");

                if(command.hasExampleUsage()) {
                    commandsText += ("\n- Example usage:\n" + command.getExampleUsage() + "\n");
                }
            }
        }

        return commandsText;
    }

}
