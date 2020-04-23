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
public class Match {

	private boolean played;

	private int matchNumber;

	private int redAutoPoints;
	private int redEndgamePoints;
	private int redTeleopPoints;
	private int redPowerCells;
	private int redMatchPoints;

	private int blueAutoPoints;
	private int blueEndgamePoints;
	private int blueTeleopPoints;
	private int bluePowerCells;
	private int blueMatchPoints;

	private Team[] redTeams;
	private Team[] blueTeams;

	private int[] redContribution;
	private int[] blueContribution;

	public Match(int matchNumber, Team[] redTeams, Team[] blueTeams) {
		played = false;

		this.matchNumber = matchNumber;

		this.redTeams = redTeams;
		this.blueTeams = blueTeams;
	}

	public Match(String line, Team[] redTeams, Team[] blueTeams) {
		String[] split = line.split(",");

		this.matchNumber = Integer.parseInt(split[0]);

		this.redTeams = redTeams;
		this.blueTeams = blueTeams;

		if (split.length < 30) {
			played = false;
			return;
		}

		played = true;

		this.redMatchPoints = Integer.parseInt(split[8]);
		this.blueMatchPoints = Integer.parseInt(split[9]);

		this.redAutoPoints = Integer.parseInt(split[14]);
		this.redTeleopPoints = Integer.parseInt(split[15]);
		this.redEndgamePoints = Integer.parseInt(split[16]);
		this.redPowerCells = Integer.parseInt(split[17]);

		this.blueAutoPoints = Integer.parseInt(split[19]);
		this.blueTeleopPoints = Integer.parseInt(split[20]);
		this.blueEndgamePoints = Integer.parseInt(split[21]);
		this.bluePowerCells = Integer.parseInt(split[22]);

		this.redContribution = new int[] { Integer.parseInt(split[24]), Integer.parseInt(split[25]),
				Integer.parseInt(split[26]) };
		this.blueContribution = new int[] { Integer.parseInt(split[27]), Integer.parseInt(split[28]),
				Integer.parseInt(split[29]) };
	}

	public Match(int matchNumber, int redAutoPoints, int redEndgamePoints, int redTeleopPoints, int redPowerCells,
			int redMatchPoints, int blueAutoPoints, int blueEndgamePoints, int blueTeleopPoints, int bluePowerCells,
			int blueMatchPoints, Team[] redTeams, Team[] blueTeams, int[] redContribution, int[] blueContribution) {
		played = true;

		this.matchNumber = matchNumber;
		this.redAutoPoints = redAutoPoints;
		this.redEndgamePoints = redEndgamePoints;
		this.redTeleopPoints = redTeleopPoints;
		this.redPowerCells = redPowerCells;
		this.redMatchPoints = redMatchPoints;
		this.blueAutoPoints = blueAutoPoints;
		this.blueEndgamePoints = blueEndgamePoints;
		this.blueTeleopPoints = blueTeleopPoints;
		this.bluePowerCells = bluePowerCells;
		this.blueMatchPoints = blueMatchPoints;
		this.redTeams = redTeams;
		this.blueTeams = blueTeams;
		this.redContribution = redContribution;
		this.blueContribution = blueContribution;
	}

	@Override
	public String toString() {
		if (!played) {
			return matchNumber + "," + redTeams[0].getName() + "," + redTeams[1].getName() + "," + redTeams[2].getName()
					+ "," + blueTeams[0].getName() + "," + blueTeams[1].getName() + "," + blueTeams[2].getName();
		}

		return matchNumber + "," + redTeams[0].getName() + "," + redTeams[1].getName() + "," + redTeams[2].getName()
				+ "," + blueTeams[0].getName() + "," + blueTeams[1].getName() + "," + blueTeams[2].getName() + ",,"
				+ redMatchPoints + "," + blueMatchPoints + ",," + getRedRPs() + "," + getBlueRPs() + ",,"
				+ redAutoPoints + "," + redTeleopPoints + "," + redEndgamePoints + "," + redPowerCells + ",,"
				+ blueAutoPoints + "," + blueTeleopPoints + "," + blueEndgamePoints + "," + bluePowerCells + ",,"
				+ redContribution[0] + "," + redContribution[1] + "," + redContribution[2] + "," + blueContribution[0]
				+ "," + blueContribution[1] + "," + blueContribution[2];
	}

	public int getRedRPs() {
		return (redMatchPoints > blueMatchPoints ? 2 : redMatchPoints == blueMatchPoints ? 1 : 0)
				+ (redPowerCells >= SimTourney.POWER_CELLS_RP ? 1 : 0)
				+ (redEndgamePoints >= SimTourney.ENDGAME_RP ? 1 : 0);
	}

	public int getBlueRPs() {
		return (blueMatchPoints > redMatchPoints ? 2 : blueMatchPoints == redMatchPoints ? 1 : 0)
				+ (bluePowerCells >= SimTourney.POWER_CELLS_RP ? 1 : 0)
				+ (blueEndgamePoints >= SimTourney.ENDGAME_RP ? 1 : 0);
	}

	public boolean isPlayed() {
		return played;
	}

	public int getMatchNumber() {
		return matchNumber;
	}

	public int getRedAutoPoints() {
		return redAutoPoints;
	}

	public int getRedEndgamePoints() {
		return redEndgamePoints;
	}

	public int getRedTeleopPoints() {
		return redTeleopPoints;
	}

	public int getRedMatchPoints() {
		return redMatchPoints;
	}

	public int getBlueAutoPoints() {
		return blueAutoPoints;
	}

	public int getBlueEndgamePoints() {
		return blueEndgamePoints;
	}

	public int getBlueTeleopPoints() {
		return blueTeleopPoints;
	}

	public int getBlueMatchPoints() {
		return blueMatchPoints;
	}

	public Team[] getRedTeams() {
		return redTeams;
	}

	public Team[] getBlueTeams() {
		return blueTeams;
	}

	public int[] getRedContribution() {
		return redContribution;
	}

	public int[] getBlueContribution() {
		return blueContribution;
	}

	public int getRedPowerCells() {
		return redPowerCells;
	}

	public int getBluePowerCells() {
		return bluePowerCells;
	}

}
