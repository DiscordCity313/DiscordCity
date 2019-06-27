package com.discordcity.city.render;

import com.discordcity.city.City;
import com.discordcity.city.tile.CityTileType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public CityRenderer(int mapGridWidth, int mapGridHeight, int tileWidth, int tileHeight) throws IOException {
        this.displayWidth = mapGridWidth * tileWidth;
        this.displayHeight = mapGridHeight * tileHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.setupSpriteCache();
    }

    public File render(City city) throws IOException {
        this.setupDisplay();

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

    private void renderTilemap(City city) {
        for(int row = 0; row < city.getTileGridHeight(); row++) {
            for(int column = 0; column < city.getTileGridWidth(); column++) {
                int tileIndex = (row * city.getTileGridWidth() + column);

                Image tileSprite = this.spriteCache.get(city.getTileForIndex(tileIndex));

                int tilePositionX = column * this.tileWidth;
                int tilePosiitonY = row * this.tileHeight;

                this.graphics.drawImage(tileSprite, tilePositionX, tilePosiitonY, null);
            }
        }
    }

}
