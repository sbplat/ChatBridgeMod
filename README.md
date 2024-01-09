# ChatBridgeMod

ChatBridge is a 1.8.9 (forge) / 1.19.3 (fabric) Minecraft mod that enables bidirectional IRC between your own Minecraft client and Discord (or your own chat server!).

## Getting Started

1. Install [Python 3.8+](https://www.python.org/downloads/) and then install the discord.py module.
```sh
> pip install discord.py
```
2. Download the [latest release](https://github.com/sbplat/ChatBridgeMod/releases) or [build](#building-from-source) it yourself.
3. Copy the mod to your mods folder (.minecraft/mods).
4. Start your instance of Minecraft. ChatBridge will automatically create a configuration file in .minecraft/config. Edit this configuration file accordingly (see the [configuration](#configuration) section for more information).
5. Run the Minecraft command `/chatbridgereload` to force ChatBridge to reload its configurations. You have successfully installed ChatBridge!

## Commands

#### chatbridgebind

Aliases: `bind`
Set the [bind](#bind) option via a command.

#### chatbridgeonline

Aliases: `online`
Sends the `/online` command to ChatBridge's server. All other ChatBridge clients will reply with their in game usernames.

#### chatbridgereload

Aliases: None
Force ChatBridge to reload its configurations.

#### chatbridgesendmessage

Aliases: `send`
Send a message **only** to the Minecraft server. ChatBridge will ignore the message.

#### chatbridgesendrawmessage

Aliases: `raw`
Send a message **only** to ChatBridge's server. The message will not be recieved by the Minecraft server.

## Configuration

Once you've launched Minecraft, ChatBridge will automatically create a configuration file in .minecraft/config. The file is called `chatbridge.cfg` if you're using Forge with 1.8.9 and `chatbridge.json` if you're using Fabric with 1.19.3.
To configure ChatBridge, open the configuration file with any text editor and modify the values.

### Settings

#### bind

The chat bind option. Possible values are
1. `SERVER`: the client's chat messages go straight to the Minecraft server. ChatBridge is essentially disabled when this value is set.
2. `CHATBRIDGE`: the client's chat messeges **only** go to ChatBridge's server and **do not** get sent to the Minecraft server.
3. `CHATBRIDGE_AND_SERVER`: the client's chat messages get sent to both the Minecraft server as well as ChatBridge's server.

#### chatMessageFormat

The chat message format option. ChatBridge will display messages that it recieves from ChatBridge's server using this format. `{0}` represents the name of the sender and `{1}` represents the sender's message.

#### channelID

The Discord relay channel ID. ChatBridge will route messages to and from this Discord channel. To get the ID of a channel, first enable "Developer Mode" in Discord by going to Settings > Advanced > Developer Mode. Then, right click the channel you want to copy the ID for and press Copy Channel ID.

#### token

The Discord bot token. To obtain a Discord bot token, follow the steps [here](https://discordpy.readthedocs.io/en/stable/discord.html). Make sure the bot has access to the channel that you set the Channel ID for.

Once you've made your changes, save the file. Then, go back to Minecraft and run the command `/chatbridgereload` for your changes to take effect.

## Building From Source

### 1.8.9 (forge)

1. Install Git and JDK 8. Make sure the environment variable, `JAVA_HOME`, is set to the JDK 8 installation path.
2. Clone this repository.
```sh
> git clone https://github.com/sbplat/ChatBridgeMod.git
```
3. Navigate into the `ChatBridgeMod` directory.
```sh
> cd ChatBridgeMod
```
4. Switch the branch to `forge-1.8.9`.
```sh
> git checkout forge-1.8.9
```
5. Build ChatBridge.
```sh
> gradlew build
```

### 1.19.3 (fabric)

1. Install Git and JDK 17. Make sure the environment variable, `JAVA_HOME`, is set to the JDK 17 installation path.
2. Clone this repository.
```sh
> git clone https://github.com/sbplat/ChatBridgeMod.git
```
3. Navigate into the `ChatBridgeMod` directory.
```sh
> cd ChatBridgeMod
```
4. Switch the branch to `fabric-1.19.3`.
```sh
> git checkout fabric-1.19.3
```
5. Build ChatBridge.
```sh
> gradlew build
```

## Custom Chat Server

The chat server implementation is located in `src/main/resources/assets/chatbridge/bot.py`. This file can be modified to use a different chat server. Once you've made the changes, [build](#building-from-source) the project again.

## Troubleshooting

ChatBridge will output errors to the console.
