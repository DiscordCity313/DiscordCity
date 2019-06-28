package com.discordcity.command;

import com.discordcity.city.City;
import com.discordcity.city.CityBuilder;
import com.discordcity.city.CityCache;
import com.discordcity.city.render.CityRenderer;
import com.discordcity.database.MySql;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class CityCommand extends Command {

    private HashMap<String, City> cityMessageCache = new HashMap<String , City>();

    private String resendMessageTriggerReaction = ("⏭");

    private CityRenderer cityRenderer;

    public CityCommand(String[] identifiers, String arguments, String description) throws IOException {
        super(identifiers, arguments, description);
        this.setupCityRenderer();
    }

    public CityCommand(String[] identifiers, String description) throws IOException {
        super(identifiers, description);
        this.setupCityRenderer();
    }

    private void setupCityRenderer() throws IOException {
        CityBuilder cityBuilder = CityBuilder.getInstance();

        this.cityRenderer = new CityRenderer(cityBuilder.TILE_WIDTH, cityBuilder.TILE_HEIGHT);
    }

    public City getCityForUser(String userId, MySql database) throws SQLException {
        CityCache cityCache = CityCache.getInstance();

        City userCity = null;

        if(cityCache.isCityCached(userId)) {
            userCity = cityCache.getCityFromCache(userId);
        } else {
            userCity = new CityBuilder().getCityForUser(userId, database);
            cityCache.addCityToCache(userId, userCity);
        }

        int secondsSinceLastUpdate = userCity.getSecondsSinceLastUpdate(database);
        userCity.updateCityForTime(secondsSinceLastUpdate, database);

        return userCity;
    }

    public City getParentCityForMessage(String messageId) {
        return this.cityMessageCache.get(messageId);
    }

    public void registerParentCityForMessage(Message message, City city) {
        this.cityMessageCache.put(message.getId(), city);

        message.addReaction(this.resendMessageTriggerReaction).queue();
    }

    @Override
    public void useReaction(GenericGuildMessageReactionEvent event, MySql database) {
        super.useReaction(event, database);
        if(!event.getMember().getUser().isBot()) {
            City messageCity = this.getParentCityForMessage(event.getMessageId());

            if (messageCity != null) {
                this.sendCityDisplayMessage("", event.getChannel(), event.getMember().getUser().getId(), database);
            }
        }
    }

    public void sendCityDisplayMessage(String extraMessageContent, TextChannel textChannel, String authorId, MySql database) {
        try {
            City userCity = this.getCityForUser(authorId, database);

            File cityImage = this.cityRenderer.render(userCity);

            Message cityMessage = null;

            if(extraMessageContent.length() > 0) {
                Message extraContentMessage = new MessageBuilder().append(extraMessageContent).build();
                cityMessage = textChannel.sendFile(cityImage, extraContentMessage).complete();
            } else {
                cityMessage = textChannel.sendFile(cityImage).complete();
            }

            this.registerParentCityForMessage(cityMessage, userCity);
        } catch(SQLException sqlException) {
            this.replyError(textChannel, "There was an issue retrieving your city information");
            sqlException.printStackTrace();
        } catch(IOException renderException) {
            this.replyError(textChannel, "There was an issue rendering your city");
        }
    }

}
