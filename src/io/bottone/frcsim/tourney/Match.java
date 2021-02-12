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
 * @version SRC.3.0
 */
public class Match {

	private boolean played;

	private int matchNumber;

	private int redAutoPoints;
	private int redEndgamePoints;
	private int redTeleopPoints;
	private int redPowerCells;
	private int redMatchPoints;
	private int redControlPanelPoints;

	private int blueAutoPoints;
	private int blueEndgamePoints;
	private int blueTeleopPoints;
	private int bluePowerCells;
	private int blueMatchPoints;
	private int blueControlPanelPoints;

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

		this.matchNumber = (int) Double.parseDouble(split[0]);

		this.redTeams = redTeams;
		this.blueTeams = blueTeams;

		if (split.length < 30) {
			played = false;
			return;
		}

		played = true;

		this.redMatchPoints = (int) Double.parseDouble(split[8]);
		this.blueMatchPoints = (int) Double.parseDouble(split[9]);

		this.redAutoPoints = (int) Double.parseDouble(split[14]);
		this.redTeleopPoints = (int) Double.parseDouble(split[15]);
		this.redEndgamePoints = (int) Double.parseDouble(split[16]);
		this.redPowerCells = (int) Double.parseDouble(split[17]);

		this.blueAutoPoints = (int) Double.parseDouble(split[19]);
		this.blueTeleopPoints = (int) Double.parseDouble(split[20]);
		this.blueEndgamePoints = (int) Double.parseDouble(split[21]);
		this.bluePowerCells = (int) Double.parseDouble(split[22]);

		this.redContribution = new int[] { (int) Double.parseDouble(split[24]), (int) Double.parseDouble(split[25]),
				(int) Double.parseDouble(split[26]) };
		this.blueContribution = new int[] { (int) Double.parseDouble(split[27]), (int) Double.parseDouble(split[28]),
				(int) Double.parseDouble(split[29]) };

		this.redControlPanelPoints = (int) Double.parseDouble(split[31]);
		this.blueControlPanelPoints = (int) Double.parseDouble(split[32]);
	}

	public Match(int matchNumber, int redAutoPoints, int redEndgamePoints, int redTeleopPoints, int redPowerCells,
			int redMatchPoints, int blueAutoPoints, int blueEndgamePoints, int blueTeleopPoints, int bluePowerCells,
			int blueMatchPoints, Team[] redTeams, Team[] blueTeams, int[] redContribution, int[] blueContribution,
			int redControlPanelPoints, int blueControlPanelPoints) {
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
		this.redControlPanelPoints = redControlPanelPoints;
		this.blueControlPanelPoints = blueControlPanelPoints;

		// fix for incorrect CP value on xRC
		if (this.redControlPanelPoints == 10 || this.redControlPanelPoints == 30)
			this.redControlPanelPoints += 5;
		if (this.blueControlPanelPoints == 10 || this.blueControlPanelPoints == 30)
			this.blueControlPanelPoints += 5;
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
				+ "," + blueContribution[1] + "," + blueContribution[2] + ",," + redControlPanelPoints + ","
				+ blueControlPanelPoints;
	}

	public int getRedRPs() {
		return (redMatchPoints > blueMatchPoints ? 2 : redMatchPoints == blueMatchPoints ? 1 : 0)
				+ (redControlPanelPoints >= 35 ? 1 : 0) + (redEndgamePoints >= SimTourney.ENDGAME_RP ? 1 : 0);
	}

	public int getBlueRPs() {
		return (blueMatchPoints > redMatchPoints ? 2 : blueMatchPoints == redMatchPoints ? 1 : 0)
				+ (blueControlPanelPoints >= 35 ? 1 : 0) + (blueEndgamePoints >= SimTourney.ENDGAME_RP ? 1 : 0);
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

	public int getRedControlPanelPoints() {
		return redControlPanelPoints;
	}

	public int getBlueControlPanelPoints() {
		return blueControlPanelPoints;
	}

}
