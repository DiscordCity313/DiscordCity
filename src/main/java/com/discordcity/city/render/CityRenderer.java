package com.discordcity.city.render;

import com.discordcity.city.City;
import com.discordcity.city.tile.CityTileType;
import net.dv8tion.jda.core.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class CityRenderer {

    private BufferedImage displayImage;

    private Graphics2D graphics;

    private int displayWidth;
    private int displayHeight;

    private int tileWidth;
    private int tileHeight;

    private final String CACHE_PATH = ("./assets");

    private HashMap<CityTileType, Image> spriteCache = new HashMap<CityTileType, Image>();

    private Image uiSprite;
    private Image headerSprite;

    public CityRenderer(int tileWidth, int tileHeight) throws IOException {
        this.displayWidth = 240;
        this.displayHeight = 240;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.setupSpriteCache();

        this.uiSprite = ImageIO.read(new File(this.CACHE_PATH + "/background.png"));
        this.headerSprite = ImageIO.read(new File(this.CACHE_PATH + "/header.png"));
    }

    public File render(City city, User user) throws IOException {
        this.setupDisplay();

        this.renderStats(city, user);

        this.renderTilemap(city);

        File cityFile = new File(this.CACHE_PATH + "/" + city.getOwnerUserId() + ".png");

        if(ImageIO.write(this.displayImage, "png", cityFile)) {
            return cityFile;
        }

        return null;
    }

    private void setupSpriteCache() throws IOException {
        for(CityTileType cityTileType : CityTileType.values()) {
            this.spriteCache.put(cityTileType, ImageIO.read(new File("./assets/" + cityTileType.SPRITE_NAME)));
        }
    }

    private void setupDisplay() {
        this.displayImage = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_RGB);

        this.graphics = this.displayImage.createGraphics();
    }

    private void renderStats(City city, User user) {
        this.graphics.drawImage(this.uiSprite, 0, 0, null);
        this.graphics.drawImage(this.headerSprite, 0, 0, null);

        int offsetX = this.tileWidth * 3 + this.tileWidth / 2;

        this.graphics.setColor(Color.YELLOW);
        this.graphics.drawString(user.getName() + "'s City", offsetX, 16);
        this.graphics.setColor(Color.WHITE);
        this.graphics.drawString("Population: " + city.getPopulation() + "/" + city.getMaxPopulation(), offsetX, 32);
        this.graphics.drawString("Funds: $" + city.getFunds(), offsetX, 48);
        this.graphics.drawString("Unemployment: " + city.getUnemployment()  + "%", offsetX, 64);
    }

    private void renderTilemap(City city) {
        int offsetX = this.uiSprite.getHeight(null) / 2 - (city.getTileGridWidth() * this.tileWidth / 2);
        int offsetY = this.uiSprite.getHeight(null) / 3 + this.tileWidth / 2;

        for(int row = 0; row < city.getTileGridHeight(); row++) {
            int renderRow = row + 1;
            int rowDisplayY = (renderRow * this.tileHeight) + offsetY - this.tileHeight / 4;

            this.graphics.drawString("" + (row + 1), offsetX - this.tileWidth / 2, rowDisplayY);

            for(int column = 0; column < city.getTileGridWidth(); column++) {
                int columnDisplayX = (column * this.tileWidth) + offsetX + tileWidth / 4;

                this.graphics.drawString("" + (column + 1), columnDisplayX, offsetY);

                int tileIndex = (row * city.getTileGridWidth() + column);

                Image tileSprite = this.spriteCache.get(city.getTileForIndex(tileIndex));

                int tilePositionX = column * this.tileWidth;
                int tilePositionY = row * this.tileHeight;

                tilePositionX += offsetX;
                tilePositionY += offsetY;

                this.graphics.drawImage(tileSprite, tilePositionX, tilePositionY, null);
            }
        }
    }

}
