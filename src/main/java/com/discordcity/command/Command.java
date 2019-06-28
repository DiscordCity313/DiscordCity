package com.discordcity.command;

import com.discordcity.database.MySql;
import net.dv8tion.jda.core.entities.Message;

public abstract class Command {

    private String[] identifiers;

    private String arguments;

    private String description;

    private final String ERROR_MESSAGE = ("Please use the help command for assistance");

    private String prefix;

    private boolean unlisted;

    private String exampleUsage;

    public Command(String[] identifiers, String arguments, String description) {
        this.identifiers = identifiers;
        this.arguments = arguments;
        this.description = description;
    }

    public Command(String[] identifiers, String description) {
        this.identifiers = identifiers;
        this.arguments = ("");
        this.description = description;
    }

    public Command(String identifier, String description) {
        this.identifiers = new String[] {identifier};
        this.arguments = ("");
        this.description = description;
    }

    public abstract void use(Message message, String[] arguments, MySql database);

    public void reply(Message message, String response) {
        message.getTextChannel().sendMessage(response).queue();
    }

    public void replyError(Message message, String response) {
        message.getTextChannel().sendMessage(response + "\n" + this.ERROR_MESSAGE).queue();
    }

    public boolean identifierMatches(String identifier) {
        for(String possibleMatch : this.identifiers) {
            if(possibleMatch.equalsIgnoreCase(identifier)) {
                return true;
            }
        }

        return false;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrimaryIdentifier() {
        return this.identifiers[0];
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isUnlisted() {
        return this.unlisted;
    }

    public void setUnlisted(boolean unlisted) {
        this.unlisted = unlisted;
    }

    public String getArguments() {
        return this.arguments;
    }

    public String getExampleUsage() {
        return this.exampleUsage;
    }

    public void setExampleUsage(String exampleUsage) {
        this.exampleUsage = exampleUsage;
    }

    public boolean hasExampleUsage() {
        return this.getExampleUsage() != null;
    }

}
