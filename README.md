# xRC Sim Tourney Runner

Automatically saves match results from xRC Simulator games to a Google Sheet, helping to expedite the process of running tournaments. Currently works for Rapid React simulator competitions. Also includes an [OBS Lua script](https://github.com/NicholasBottone/xRCSim-Tourney-Runner/blob/main/SimSync.lua) for displaying live game data on livestream overlays.

To be used for competitions and tournaments in the Unity-based game [xRC Simulator](http://xrcsimulator.org/). Used in online [SRC events](https://secondrobotics.org).

## Getting Started

- You must have a Google Sheet setup with an API Service Account.
- You must have a `.env` file in the root of the project, see `.env.example` for an example.
- Run `npm install` in the root directory to install all dependencies.
- Run `npm start` to start the CLI application.

For a detailed tutorial, [click here to visit the wiki](https://github.com/NicholasBottone/xRCSim-Tourney-Runner/wiki).

## Contributing to this project

Feel free to create issues to report problems or suggestions. To make a code contribution, clone this repository then create a pull request.

This project is licensed under the GNU GPLv3, meaning that as long as you provide attribution, you can do almost anything you want with this code, except distributing closed source versions.
