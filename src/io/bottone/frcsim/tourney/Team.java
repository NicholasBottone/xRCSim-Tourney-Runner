package io.bottone.frcsim.tourney;

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
 * @version 0.1.0
 */
public class Team implements Comparable<Team> {

	static final Team EMPTY_TEAM = new Team("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
	static final Team[] EMPTY_TEAMS = { EMPTY_TEAM, EMPTY_TEAM, EMPTY_TEAM };

	private String id;
	private String name;

	private int rank;

	// AVERAGE PER MATCH
	private double rankingScore;
	private double autoScore;
	private double endgameScore;
	private double teleopScore;
	private double matchScore;
	private double contributionScore;

	private int matchesPlayed;

	// CUMULATIVE TOTALS OVER ALL MATCHES
	private int rankingPoints;
	private int autoPoints;
	private int endgamePoints;
	private int teleopPoints;
	private int contributionPoints;
	private int matchPoints;

	// AVERAGE STAT
	private double avgPowerCells;

	public Team(String s) {
		String[] ss = s.split(",");

		id = ss[0];
		name = ss[1];

		rank = Integer.parseInt(ss[2]);

		rankingScore = Double.parseDouble(ss[3]);
		autoScore = Double.parseDouble(ss[4]);
		endgameScore = Double.parseDouble(ss[5]);
		teleopScore = Double.parseDouble(ss[6]);
		matchScore = Double.parseDouble(ss[7]);

		contributionScore = Double.parseDouble(ss[9]);

		matchesPlayed = Integer.parseInt(ss[11]);

		rankingPoints = Integer.parseInt(ss[13]);
		autoPoints = Integer.parseInt(ss[14]);
		endgamePoints = Integer.parseInt(ss[15]);
		teleopPoints = Integer.parseInt(ss[16]);
		matchPoints = Integer.parseInt(ss[17]);
		contributionPoints = Integer.parseInt(ss[18]);

		avgPowerCells = Double.parseDouble(ss[20]);

	}

	public void addMatchData(int rp, int auto, int endgame, int teleop, int match, int powerCells, int contribution) {
		rankingPoints += rp;
		autoPoints += auto;
		endgamePoints += endgame;
		teleopPoints += teleop;
		contributionPoints += contribution;
		matchPoints += match;

		avgPowerCells = ((avgPowerCells * matchesPlayed) + powerCells) / ++matchesPlayed;

		rankingScore = ((int) (((double) rankingPoints / matchesPlayed) * 100)) / 100D;
		autoScore = ((int) (((double) autoPoints / matchesPlayed) * 100)) / 100D;
		endgameScore = ((int) (((double) endgamePoints / matchesPlayed) * 100)) / 100D;
		teleopScore = ((int) (((double) teleopPoints / matchesPlayed) * 100)) / 100D;
		contributionScore = ((int) (((double) contributionPoints / matchesPlayed) * 100)) / 100D;
		matchScore = ((int) (((double) matchPoints / matchesPlayed) * 100)) / 100D;
	}

	public void emptyData() {
		rankingScore = 0;
		autoScore = 0;
		endgameScore = 0;
		teleopScore = 0;
		matchScore = 0;

		contributionScore = 0;

		matchesPlayed = 0;

		rankingPoints = 0;
		autoPoints = 0;
		endgamePoints = 0;
		teleopPoints = 0;
		matchPoints = 0;
		contributionPoints = 0;

		avgPowerCells = 0;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,,%s,,%s,,%s,%s,%s,%s,%s,%s,,%s", id, name, rank, rankingScore,
				autoScore, endgameScore, teleopScore, matchScore, contributionScore, matchesPlayed, rankingPoints,
				autoPoints, endgamePoints, teleopPoints, matchPoints, contributionPoints, avgPowerCells);
	}

	public String getRankingDescription() {
		return "[Rank #" + rank + "] " + name + " (RS: " + rankingScore + ")";
	}

	@Override
	public int compareTo(Team team) {

		// Ranking Scores
		int value = (int) ((this.rankingScore - team.rankingScore) * 100);
		if (value != 0)
			return -value;

		// Auto Scores
		value = (int) ((this.autoScore - team.autoScore) * 100);
		if (value != 0)
			return -value;

		// Endgame Scores
		value = (int) ((this.endgameScore - team.endgameScore) * 100);
		if (value != 0)
			return -value;

		// Teleop Scores
		value = (int) ((this.teleopScore - team.teleopScore) * 100);
		if (value != 0)
			return -value;

		// Match Scores
		value = (int) ((this.matchScore - team.matchScore) * 100);
		if (value != 0)
			return -value;

		// Power Cells
		value = (int) ((this.avgPowerCells - team.avgPowerCells) * 100);
		if (value != 0)
			return -value;

		// Player ID
		return Integer.parseInt(this.id) - Integer.parseInt(team.id);

	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getRank() {
		return rank;
	}

	public double getRankingScore() {
		return rankingScore;
	}

	public double getAutoScore() {
		return autoScore;
	}

	public double getEndgameScore() {
		return endgameScore;
	}

	public double getTeleopScore() {
		return teleopScore;
	}

	public double getMatchScore() {
		return matchScore;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public int getRankingPoints() {
		return rankingPoints;
	}

	public int getAutoPoints() {
		return autoPoints;
	}

	public int getEndgamePoints() {
		return endgamePoints;
	}

	public int getTeleopPoints() {
		return teleopPoints;
	}

	public int getMatchPoints() {
		return matchPoints;
	}

	public double getAvgPowerCells() {
		return avgPowerCells;
	}

	public void setRank(int newRank) {
		rank = newRank;
	}

}
