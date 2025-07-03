# GeyserConnect

[![forthebadge made-with-java](https://forthebadge.com/images/badges/made-with-java.svg)](https://java.com/)

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://github.com/GeyserMC/GeyserConnect/actions/workflows/build.yml/badge.svg)](https://github.com/GeyserMC/GeyserConnect/actions/workflows/build.yml)
[![Discord](https://img.shields.io/discord/613163671870242838.svg?color=%237289da&label=discord)](http://discord.geysermc.org/)
[![HitCount](http://hits.dwyl.com/GeyserMC/GeyserConnect.svg)](http://hits.dwyl.io/GeyserMC/GeyserConnect)

GeyserConnect is an easy way for Bedrock Edition clients to connect to any Java Edition servers without having to run anything.

## What is GeyserConnect?
GeyserConnect is an extension for Geyser that allows for a list of Minecraft: Java Edition servers to be displayed and accessed through 1 public Geyser instance. It is effectively give the customisability of [BedrockConnect](https://github.com/Pugmatt/BedrockConnect) to [Geyser](https://github.com/GeyserMC/Geyser).

If you wish to use DNS redirection please see the [bind9](bind9) folder in this repository.

## Configuration
GeyserConnect has several options that can be configured in the `config.yml` file, found in the GeyserConnect extension folder (`/extensions/GeyserConnect/config.yml`) after running Geyser with the extension for the first time.

Here are some of the key options:

*   `welcome-file`: Specifies a file to be displayed to players when they join.
*   `hard-player-limit`: If true, GeyserConnect will kick players if the Geyser instance's player limit is reached.
*   `allow-offline-bedrock-players`: (Default: `false`) When set to `true`, GeyserConnect will allow Bedrock players to connect even if Geyser has identified them as being in 'offline mode' (e.g., not authenticated with Xbox Live). This setting relies on Geyser itself being configured to permit such offline Bedrock players. If this is `false`, GeyserConnect will deny connections from Bedrock clients that Geyser considers to be in offline mode.
*   `servers`: A list of predefined servers that will be displayed to all players.
*   `custom-servers`:
    *   `enabled`: Allows users to add their own custom servers to their personal list.
    *   `storage-type`: Defines how custom server lists are stored (e.g., `json`, `sqlite`, `mysql`).
*   `vhost`:
    *   `enabled`: Enables the virtual host functionality, allowing players to connect directly to a specific server by using a specially formatted server address (e.g., `myjavaserver.com._p25565.connect.example.com`).
    *   The vhost format supports specifying an offline target server using `_o` (e.g., `myjavaserver.com._o.connect.example.com`).

For more details on all options, please refer to the generated `config.yml` file and its comments.

## Commands
All commands are prefixed ingame with `/geyserconnect` or in console with `geyserconnect`

| Command                            | Description                                  | Example                                                | Console only       |
|------------------------------------|----------------------------------------------|--------------------------------------------------------|--------------------|
| `menu`                             | Reconnect and get back to the menu.          | `/geyserconnect menu`                                  | :x:                |
| `messageall (chat\|gui) <message>` | Send a message to all online users.          | `/geyserconnect messageall gui This is a test message` | :heavy_check_mark: |
| `transferall <ip> [passAsVhost]`   | Transfer all online users to another server. | `/geyserconnect transferall gc.example.com true`       | :heavy_check_mark: |
