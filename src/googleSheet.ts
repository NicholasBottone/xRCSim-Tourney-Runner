import {
  GoogleSpreadsheet,
  GoogleSpreadsheetWorksheet,
} from "google-spreadsheet";
import Match, { headerValues, matchToArray, saveMatchToRow } from "./match";

export async function setupConnection() {
  const privateKey = process.env.GOOGLE_SHEET_PRIVATE_KEY?.replace(
    /\\n/g,
    "\n"
  );

  const creds = {
    client_email: process.env.GOOGLE_SHEET_CLIENT_EMAIL || "",
    private_key: privateKey || "",
  };

  const doc = new GoogleSpreadsheet(process.env.GOOGLE_SHEET_DOC_ID || "");
  await doc.useServiceAccountAuth(creds);
  await doc.loadInfo();

  let matchesSheet = doc.sheetsByTitle.Matches;
  if (!matchesSheet) {
    matchesSheet = await doc.addSheet({
      title: "Matches",
      gridProperties: {
        columnCount: headerValues.length,
        frozenRowCount: 1,
        rowCount: 2,
      },
      headerValues: headerValues,
    });
  }

  let scheduleSheet = doc.sheetsByTitle.Schedule;
  if (!scheduleSheet) {
    scheduleSheet = await doc.addSheet({
      title: "Schedule",
      gridProperties: {
        columnCount: 7,
        frozenRowCount: 1,
        rowCount: 2,
      },
      headerValues: [
        "Match Number",
        "Red 1",
        "Red 2",
        "Red 3",
        "Blue 1",
        "Blue 2",
        "Blue 3",
      ],
    });
  }

  return { matchesSheet, scheduleSheet };
}

export async function postMatch(
  matchesSheet: GoogleSpreadsheetWorksheet,
  match: Match
) {
  return matchesSheet.addRow(matchToArray(match));
}

export async function updateMatch(
  matchesSheet: GoogleSpreadsheetWorksheet,
  match: Match
) {
  const rows = await matchesSheet.getRows();

  const row = rows.find((r) => r.matchNumber === match.matchNumber);
  if (!row) {
    throw new Error(`Could not find match ${match.matchNumber}`);
  }

  saveMatchToRow(match, row);
  await row.save();

  return row;
}
