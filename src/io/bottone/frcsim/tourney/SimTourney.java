package io.bottone.frcsim.tourney;

import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * FRCSim Tourney Runner Copyright (C) 2020 Nicholas Bottone
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author Nicholas Bottone
 * @version SRC.2.0
 */
public class SimTourney {

	public static final String VERSION = "SRC.2.0";

	public static final int POWER_CELLS_RP = 120;
	public static final int ENDGAME_RP = 65;

	private static JLabel label;

	public static void main(String[] args) {

		JFrame frame = new JFrame("Bottone.io Sim Tourney " + VERSION);

		label = new JLabel("Sim Tourney " + VERSION);
		label.setFont(new Font("", Font.PLAIN, 22));
		frame.add(label);

		JTextField matchField = new JTextField("Q01", 6);
		matchField.setFont(new Font("", Font.PLAIN, 40));
		matchField.addActionListener(e -> {
			try {
				// What match are we on?
				boolean qual = matchField.getText().charAt(0) == 'Q';
				String matchNum;
				if (qual) // qualification match
					matchNum = "" + (int) Double.parseDouble(matchField.getText().substring(1));
				else // playoff/elim match
					matchNum = matchField.getText();

				PrintWriter red = new PrintWriter("TourneyData\\RedTeams.txt");
				PrintWriter blue = new PrintWriter("TourneyData\\BlueTeams.txt");
				PrintWriter match = new PrintWriter("TourneyData\\MatchNumber.txt");
				PrintWriter redRankings = new PrintWriter("TourneyData\\RedRanks.txt");
				PrintWriter blueRankings = new PrintWriter("TourneyData\\BlueRanks.txt");

				// Which teams are playing this match?
				String[] redTeams;
				String[] blueTeams;

				Scanner schedule = new Scanner(new File("schedule.csv"));
				schedule.nextLine();
				while (true) {
					String[] s = schedule.nextLine().split(",");
					if (s[0].equals(matchNum)) {
						redTeams = new String[] { s[2], s[3], s[4] };
						blueTeams = new String[] { s[5], s[6], s[7] };
						break;
					}
				}
				schedule.close();

				// Import full team list (and rankings)
				List<String> teams = Files.readAllLines(Paths.get("teams.csv"));
				String[] redRanks = new String[3];
				String[] blueRanks = new String[3];

				// Find currently playing teams in full team list
				for (int i = 0; i < 3; i++) {
					if (redTeams[i].endsWith("*") || redTeams[i].endsWith("!"))
						redTeams[i].subSequence(0, redTeams[i].length() - 1);
					for (String s : teams) {
						if (s.length() < 5 || s.startsWith("Player ID"))
							continue;
						Team team = new Team(s);
						if (team.getId().equals(redTeams[i])) {
							redTeams[i] = team.getName();
							redRanks[i] = team.getRankingDescription();
							break;
						}
					}
				}
				for (int i = 0; i < 3; i++) {
					if (blueTeams[i].endsWith("*") || blueTeams[i].endsWith("!"))
						blueTeams[i].subSequence(0, blueTeams[i].length() - 1);
					for (String s : teams) {
						if (s.length() < 5 || s.startsWith("Player ID"))
							continue;
						Team team = new Team(s);
						if (team.getId().equals(blueTeams[i])) {
							blueTeams[i] = team.getName();
							blueRanks[i] = team.getRankingDescription();
							break;
						}
					}
				}

				// Export red alliance team names to stream
				red.println(StringUtils.center(redTeams[0].substring(0, Math.min(13, redTeams[0].length())), 13));
				red.println(StringUtils.center(redTeams[1].substring(0, Math.min(13, redTeams[1].length())), 13));
				red.print(StringUtils.center(redTeams[2].substring(0, Math.min(13, redTeams[2].length())), 13));

				// Export blue alliance team names to stream
				blue.println(StringUtils.center(blueTeams[0].substring(0, Math.min(13, blueTeams[0].length())), 13));
				blue.println(StringUtils.center(blueTeams[1].substring(0, Math.min(13, blueTeams[1].length())), 13));
				blue.print(StringUtils.center(blueTeams[2].substring(0, Math.min(13, blueTeams[2].length())), 13));

				// Export red alliance team ranks to stream
				redRankings.println(redRanks[0]);
				redRankings.println(redRanks[1]);
				redRankings.print(redRanks[2]);

				// Export blue alliance team ranks to stream
				blueRankings.println(blueRanks[0]);
				blueRankings.println(blueRanks[1]);
				blueRankings.print(blueRanks[2]);

				// Export match name to stream
				if (qual)
					match.print("Qualification " + matchNum);
				else
					match.print(matchNum);

				red.close();
				blue.close();
				match.close();
				redRankings.close();
				blueRankings.close();

				label.setText("Loaded Q" + matchNum);
			} catch (Exception ex) {
				ex.printStackTrace();
				label.setText(ex.getMessage());
			}
		});
		frame.add(matchField);

		JButton saveMatchA = new JButton("Save Match A");
		saveMatchA.setFont(new Font("", Font.PLAIN, 24));
		saveMatchA.addActionListener(e -> {
			try {
				syncMatchData("DataA", matchField.getText());
			} catch (Exception ex) {
				ex.printStackTrace();
				label.setText(ex.getMessage());
			}
		});
		frame.add(saveMatchA);

		JButton saveMatchB = new JButton("Save Match B");
		saveMatchB.setFont(new Font("", Font.PLAIN, 24));
		saveMatchB.addActionListener(e -> {
			try {
				syncMatchData("DataB", matchField.getText());
			} catch (Exception ex) {
				ex.printStackTrace();
				label.setText(ex.getMessage());
			}
		});
		frame.add(saveMatchB);

		JButton pushRankings = new JButton("Sync/Refresh");
		pushRankings.setFont(new Font("", Font.PLAIN, 18));
		pushRankings.addActionListener(e -> {
			try {
				refreshEverything();
			} catch (Exception ex) {
				ex.printStackTrace();
				label.setText(ex.getMessage());
			}
		});
		frame.add(pushRankings);

		frame.setSize(280, 280);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);

	}

	private static void syncMatchData(String directory, String matchField) throws IOException {
		label.setText("Saving...");
		Runtime.getRuntime().exec("python3 srcPull.py");
		Timer timer = new Timer(5000, e -> {
			try {
				saveMatchData(directory, matchField);
				label.setText("Saved " + matchField + " on " + directory);
			} catch (Exception ex) {
				ex.printStackTrace();
				label.setText(ex.getMessage());
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	private static void saveMatchData(String directory, String matchField) {
		try {
			// Which match are we playing?
			if (matchField.charAt(0) != 'Q')
				return;
			String matchNum = "" + (int) Double.parseDouble(matchField.substring(1));

			// Which teams played this match?
			String[] redTeams;
			String[] blueTeams;

			Scanner schedule = new Scanner(new File("schedule.csv"));
			schedule.nextLine();
			while (true) {
				String[] s = schedule.nextLine().split(",");
				if (s[0].equals(matchNum)) {
					redTeams = new String[] { s[2], s[3], s[4] };
					blueTeams = new String[] { s[5], s[6], s[7] };
					break;
				}
			}
			schedule.close();

			// Import the full team list
			List<Team> teams = new ArrayList<>();
			for (String s : Files.readAllLines(Paths.get("teams.csv"))) {
				if (s.length() < 5 || s.startsWith("Player ID"))
					continue;
				teams.add(new Team(s));
			}

			// Find the indexes of the teams that played this match in the full team list
			int[] redIndex = new int[3];
			int[] blueIndex = new int[3];

			for (int i = 0; i < 3; i++) {
				if (redTeams[i].endsWith("*") || redTeams[i].endsWith("!"))
					redTeams[i].subSequence(0, redTeams[i].length() - 1);
				for (Team team : teams) {
					if (team.getId().equals(redTeams[i] + "")) {
						redTeams[i] = team.getName();
						redIndex[i] = teams.indexOf(team);
						break;
					}
				}
			}
			for (int i = 0; i < 3; i++) {
				if (blueTeams[i].endsWith("*") || blueTeams[i].endsWith("!"))
					blueTeams[i].subSequence(0, blueTeams[i].length() - 1);
				for (Team team : teams) {
					if (team.getId().equals(blueTeams[i] + "")) {
						blueTeams[i] = team.getName();
						blueIndex[i] = teams.indexOf(team);
						break;
					}
				}
			}

			// Import match data for both alliances and calculate RPs
			Scanner scan = new Scanner(new File(directory + "\\ScoreR.txt"));
			int scoreRed = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\ScoreB.txt"));
			int scoreBlue = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\EndR.txt"));
			int endRed = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\EndB.txt"));
			int endBlue = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\PC_R.txt"));
			int cellsRed = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\PC_B.txt"));
			int cellsBlue = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\AutoR.txt"));
			int autoRed = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\AutoB.txt"));
			int autoBlue = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\TeleR.txt"));
			int teleRed = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\TeleB.txt"));
			int teleBlue = scan.nextInt();
			scan.close();

			scan = new Scanner(new File(directory + "\\OPR.txt"));
			List<String> redTeamList = Arrays.asList(redTeams);
			List<String> redAlphabetized = Arrays.asList(new String[] { null, null, null });
			Collections.copy(redAlphabetized, redTeamList);
			Collections.sort(redAlphabetized);
			List<String> blueTeamList = Arrays.asList(blueTeams);
			List<String> blueAlphabetized = Arrays.asList(new String[] { null, null, null });
			Collections.copy(blueAlphabetized, blueTeamList);
			Collections.sort(blueAlphabetized);
			System.out.println(redTeamList + " -- " + redAlphabetized);
			System.out.println(blueTeamList + " -- " + blueAlphabetized);
			int[] oprRed = new int[3];
			int[] oprBlue = new int[3];
			for (int i = 0; i < 3; i++) {
				try {
					String[] oprLine = scan.nextLine().split(": ");
					oprRed[redTeamList.indexOf(redAlphabetized.get(i))] = (int) Double.parseDouble(oprLine[1]);
				} catch (Exception ex) {
					System.out.println("We didn't have a full 6 player match");
					oprRed[redTeamList.indexOf(redAlphabetized.get(i))] = 0;
				}
			}
			for (int i = 0; i < 3; i++) {
				try {
					String[] oprLine = scan.nextLine().split(": ");
					oprBlue[blueTeamList.indexOf(blueAlphabetized.get(i))] = (int) Double.parseDouble(oprLine[1]);
				} catch (Exception ex) {
					System.out.println("We didn't have a full 6 player match");
					oprBlue[blueTeamList.indexOf(blueAlphabetized.get(i))] = 0;
				}
			}
			scan.close();

			// Build match model
			int matchNumber = (int) Double.parseDouble(matchNum);
			Match match = new Match(matchNumber, autoRed, endRed, teleRed, cellsRed, scoreRed, autoBlue, endBlue,
					teleBlue, cellsBlue, scoreBlue, Team.EMPTY_TEAMS, Team.EMPTY_TEAMS, oprRed, oprBlue);

			List<String> matchesLines = Files.readAllLines(Paths.get("matches.csv"));

			PrintWriter matchesWriter = new PrintWriter("matches.csv");
			for (int i = 0; i < matchesLines.size(); i++) {
				if (i == matchNumber) {
					matchesWriter.println(match);
				} else {
					matchesWriter.println(matchesLines.get(i));
				}
			}
			matchesWriter.close();

			pushChanges();

			// Output new rankings to stream
			PrintWriter redRankings = new PrintWriter("TourneyData\\RedRanks.txt");
			PrintWriter blueRankings = new PrintWriter("TourneyData\\BlueRanks.txt");

			redRankings.println(teams.get(redIndex[0]).getRankingDescription());
			redRankings.println(teams.get(redIndex[1]).getRankingDescription());
			redRankings.print(teams.get(redIndex[2]).getRankingDescription());

			blueRankings.println(teams.get(blueIndex[0]).getRankingDescription());
			blueRankings.println(teams.get(blueIndex[1]).getRankingDescription());
			blueRankings.print(teams.get(blueIndex[2]).getRankingDescription());

			redRankings.close();
			blueRankings.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			label.setText(ex.getMessage());
		}
	}

	private static void refreshEverything() throws IOException {
		label.setText("Syncing...");
		Runtime.getRuntime().exec("python3 srcPull.py");
		Timer timer = new Timer(5000, e -> {
			try {
				pushChanges();
				label.setText("Synced!");
			} catch (Exception ex) {
				ex.printStackTrace();
				label.setText(ex.getMessage());
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	private static void pushChanges() throws IOException {
		// Import the full team list
		HashMap<String, Team> teams = new HashMap<>(); // key = id, value = Team object
		for (String s : Files.readAllLines(Paths.get("teams.csv"))) {
			if (s.length() < 5 || s.startsWith("Player ID"))
				continue;
			Team team = new Team(s);
			teams.put(team.getId(), team);
		}

		// Start collecting all known match data
		ArrayList<Match> matchList = new ArrayList<>();

		// Scan through schedule and match data
		Scanner schedule = new Scanner(new File("schedule.csv"));
		Scanner matches = new Scanner(new File("matches.csv"));
		schedule.nextLine();
		if (matches.hasNextLine())
			matches.nextLine();
		while (schedule.hasNextLine()) { // for each match
			String[] s = schedule.nextLine().split(",");
			Team[] redTeams = new Team[3];
			Team[] blueTeams = new Team[3];
			for (int i = 0; i < 3; i++) {
				String team = s[2 + i];
				if (team.endsWith("*")) {
					redTeams[i] = teams.get(team.substring(0, team.length() - 1));
					redTeams[i].addSurrogateMatch((int) Double.parseDouble(s[0]));
				} else if (team.endsWith("!")) {
					redTeams[i] = teams.get(team.substring(0, team.length() - 1));
					redTeams[i].addRedCardMatch((int) Double.parseDouble(s[0]));
				} else {
					redTeams[i] = teams.get(team);
				}
			}
			for (int i = 0; i < 3; i++) {
				String team = s[5 + i];
				if (team.endsWith("*")) {
					blueTeams[i] = teams.get(team.substring(0, team.length() - 1));
					blueTeams[i].addSurrogateMatch((int) Double.parseDouble(s[0]));
				} else if (team.endsWith("!")) {
					blueTeams[i] = teams.get(team.substring(0, team.length() - 1));
					blueTeams[i].addRedCardMatch((int) Double.parseDouble(s[0]));
				} else {
					blueTeams[i] = teams.get(team);
				}
			}

			Match match;
			if (matches.hasNextLine()) { // match has some data already
				match = new Match(matches.nextLine(), redTeams, blueTeams);
			} else { // no data yet, must be generated
				match = new Match((int) Double.parseDouble(s[0]), redTeams, blueTeams);
			}
			matchList.add(match);
		}
		schedule.close();
		matches.close();

		// Empty all data from team objects in preparation for recalculations
		for (Team team : teams.values()) {
			team.emptyData();
		}

		// Start outputting to matches.csv
		PrintWriter matchesWriter = new PrintWriter("matches.csv");
		matchesWriter.println("Match,Red1,Red2,Red3,Blue1,Blue2,Blue3,,Red Score,Blue Score,,Red RPs,"
				+ "Blue RPs,,Red Auto,Red Teleop,Red Endgame,Red Power Cells,,Blue Auto,Blue Teleop,"
				+ "Blue Endgame,Blue Power Cells,,R1 Contribution,R2 Contribution,R3 Contribution,"
				+ "B1 Contribution,B2 Contribution,B3 Contribution");

		for (Match match : matchList) {
			matchesWriter.println(match);
			for (int i = 0; i < 3; i++) {
				// Calculate all team stats
				if (match.isPlayed()) {
					match.getRedTeams()[i].addMatchData(match.getRedRPs(), match.getRedAutoPoints(),
							match.getRedEndgamePoints(), match.getRedTeleopPoints(), match.getRedMatchPoints(),
							match.getRedPowerCells(), match.getRedContribution()[i], match.getMatchNumber());
					match.getBlueTeams()[i].addMatchData(match.getBlueRPs(), match.getBlueAutoPoints(),
							match.getBlueEndgamePoints(), match.getBlueTeleopPoints(), match.getBlueMatchPoints(),
							match.getBluePowerCells(), match.getBlueContribution()[i], match.getMatchNumber());
				}
			}
		}
		matchesWriter.close();

		// Save these rankings
		PrintWriter teamList = new PrintWriter("teams.csv");
		PrintWriter allRankings = new PrintWriter("TourneyData\\AllRanks.txt");

		teamList.println("Player ID,Player Name,Rank,Ranking Score,Auto,Endgame,Teleop,Match"
				+ ",,Avg Contributed,,Matches Played,,Ranking Points,Auto Points,Endgame Points,"
				+ "Teleop Points,Match Points,Total Contribution,,Avg PCs");
		allRankings.println();
		allRankings.println();
		allRankings.println();
		allRankings.println();
		allRankings.println();
		allRankings.println();
		allRankings.println();
		allRankings.println();

		ArrayList<Team> sortedTeams = new ArrayList<>(teams.values());
		Collections.sort(sortedTeams);

		for (int i = 0; i < sortedTeams.size(); i++) {
			Team team = sortedTeams.get(i);
			team.setRank(i + 1);
			teamList.println(team);
			allRankings.println(team.getRankingDescription());
		}

		teamList.close();
		allRankings.close();

		Runtime.getRuntime().exec("python3 srcPush.py");
	}

}
