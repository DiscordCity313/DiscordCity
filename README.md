# DiscordCity
Build your own mini city on Discord! Made for Discord Hack Week 2019.

## Invite & Website
Invite the bot: https://discordapp.com/oauth2/authorize?client_id=592764423572029455&scope=bot&permissions=32832

Website: https://discordcity.raspberry.gg/

## How to Play

Type **!c help** for a quick tutorial

## Building & Running from Source

An installation of Java will be required to build and run the bot.

**Step 1 -**

Either clone the repository with git or download as zip via GitHub (Clone or Download > Download ZIP).

**Step 2 -**

Go into the **DiscordCity** directory (extract it if you chose a zip), then open the **config.json** file and fill in your desired bot token

**Step 3 -**

On Linux or OSX, execute **./gradlew fullBuild**. On Windows, execute **gradlew.bat fullBuild**.

Once the build is complete, the bot and all required files will be generated into the **DiscordCity/build/libs** directory.

The bot can be started by running the generated jar file (java -jar discordcity-build-1.0-SNAPSHOT.jar).

## Preview

![preview](https://i.imgur.com/AWiM8oW.gif)