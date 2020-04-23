# FRCSim Tourney Runner
 Keeps track of rankings and schedules for FRCSim competitions.  Also includes an [OBS Lua script](https://github.com/NicholasBottone/FRCSim-Tourney-Runner/blob/master/SimSync.lua) for displaying live game data on livestream overlays.

To be used for competitions and tournaments in the Unity-based game [FTC & FRC Simulator](http://ftcsimulator.org/).  Used in online [SRC events](https://discord.gg/2sZhVU4).

## How to run an event

#### Setting up for the competition

1. Generate a qualification match schedule.  I recommend using [Cheesy Arena by Team 254](https://github.com/Team254/cheesy-arena).  Save the raw schedule file as "schedule.csv" in the same directory as the JAR.
    - See [this file](https://github.com/NicholasBottone/FRCSim-Tourney-Runner/blob/master/example%20CSVs/schedule.csv) as an example.

2. Assemble a list of the players/teams participating, and put the player names and IDs into the "teams.csv" file in the same directory as the JAR.
    - See [this file](https://github.com/NicholasBottone/FRCSim-Tourney-Runner/blob/master/example%20CSVs/teams.csv) as an example.

3. Insert an otherwise empty "matches.csv" file containing only the headings (first row) into the same directory as the JAR.
    - See [this file](https://github.com/NicholasBottone/FRCSim-Tourney-Runner/blob/master/example%20CSVs/matches.csv) as an example.

4. Create an empty folder named "TourneyData" in the same directory as the JAR.  This will be populated with data 

5. Launch the SimTourney JAR executable.  To generate the initial match schedule press the "Refresh" button.  Type "Q1" or "Q01" into the text field and press enter to initialize qualification match number one.  (This will send data to the "TourneyData" folder.)

6. Set your FRCSim game instance to output data files to either the "DataA" or "DataB" subdirectories inside the same directory as your JAR file.  Use both directories (one per server) if you are using two servers for your event.
    - Use `/SET OUTPUT_SCORE_FILES=<directory>` as a spectator to set game output score files.

#### Setting up OBS

1. Open [OBS](https://obsproject.com/).  Under Tools > Scripts, click the plus icon to add a Lua scirpt.  Browse and select "SimSync.lua".

2. Configure the script settings depending on how many servers you plan on using for your competition.  Browse to select the directories that the game uses to output score data.
    - Use `/SET OUTPUT_SCORE_FILES=<directory>` as a spectator to set game output score files.

3. Create and rearrange GDI+ text sources in your OBS scenes to display live game information.  The text sources must match the exact naming conventions of the script.
    - Text Source Naming Conventions for Sim Data 1:
      - "Match Timer", "Red Score", "Blue Score", "Red Penalty Points", "Blue Penalty Points", "Red Auto Points", "Blue Auto Points", "Red Teleop Points", "Blue Teleop Points", "Red Endgame Points", "Blue Endgame Points", "Red Power Cells", "Blue Power Cells", "Red OPR", "Blue OPR"
    - For Sim Data 2-4, simply append a space and the number to the name of the text source.
      - For example for Sim Data 2, a text source should be named "Match Timer 2".

4. Create and rearrange GDI+ text sources for tournament data to be displayed (such as match number).  For each statistic you are interested in, create a new text source, enable the "Read from file" option in its properties, and browse to link it with the associated TXT file in the folder "TourneyData".

#### Running a tournament

1. For each match, ensure that you type the current match number preceeded by "Q" to the text field and press enter to initialize the match.

2. At the conclusion of the match, press "Save Ranks A" (if the data is stored in "DataA") or "Save Ranks B" (if the data is stored in "DataB") to finalize scores and calculate new rankings.  All the CSV files and TXT files will be updated accordingly.

3. If you need to manually edit match data after the match has concluded (such as to override a penalty), you can make such a change in the "matches.csv" file after saving ranks.  Individual point contributions (or "OPR") must be entered manually (or else they default to -999).  After updating, press the "Refresh" on the app.

## Contributing to this project

Feel free to create issues to report problems or suggestions.  To make a code contribution, clone this repository then create a pull request.

This project is licensed under the GNU GPLv3, meaning that as long as you provide attribution, you can do almost anything you want with this code, except distributing closed source versions.
