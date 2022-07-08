import { GoogleSpreadsheetWorksheet } from "google-spreadsheet";
import { getMatchData } from "./field";
import { copyScheduleToMatchesSheet, updateMatch } from "./googleSheet";
import { createSpinner } from "nanospinner";
import chalk from "chalk";

export async function saveField(
  scheduleSheet: GoogleSpreadsheetWorksheet,
  matchesSheet: GoogleSpreadsheetWorksheet,
  matchNumber: number,
  field: number
) {
  let spinner = createSpinner("Loading match data").start();

  let match;
  try {
    match = await getMatchData(scheduleSheet, `Data${field}`, matchNumber);
  } catch (e) {
    spinner.error();
    console.log(chalk.red(e));
    return;
  }
  spinner.success();

  spinner = createSpinner("Saving to Google Sheet").start();

  try {
    await updateMatch(matchesSheet, match);
  } catch (e) {
    spinner.error();
    console.log(chalk.red(e));
    return;
  }
  spinner.success();
}

export async function buildMatchSheet(
  matchesSheet: GoogleSpreadsheetWorksheet,
  scheduleSheet: GoogleSpreadsheetWorksheet
) {
  const spinner = createSpinner("Building match sheet").start();

  try {
    await copyScheduleToMatchesSheet(matchesSheet, scheduleSheet);
  } catch (e) {
    spinner.error();
    console.log(chalk.red(e));
    return;
  }

  spinner.success();
}
