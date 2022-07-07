import { GoogleSpreadsheetWorksheet } from "google-spreadsheet";
import { getMatchData } from "./field";
import { isAlreadyPlayed, postMatch, updateMatch } from "./googleSheet";
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
    console.log(chalk.red("Aborting save"));
    return;
  }
  spinner.success();

  spinner = createSpinner("Saving to Google Sheet").start();

  try {
    if (await isAlreadyPlayed(matchesSheet, matchNumber)) {
      await updateMatch(matchesSheet, match);
    } else {
      await postMatch(matchesSheet, match);
    }
  } catch (e) {
    spinner.error();
    console.log(chalk.red(e));
    console.log(chalk.red("Aborting save"));
    return;
  }
  spinner.success();
}
