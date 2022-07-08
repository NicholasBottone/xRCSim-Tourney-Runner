import {
  GoogleSpreadsheet,
  GoogleSpreadsheetWorksheet,
} from "google-spreadsheet";
import Match, { headerValues, saveMatchToRow } from "./match";

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

export async function updateMatch(
  matchesSheet: GoogleSpreadsheetWorksheet,
  match: Match
) {
  const rows = await matchesSheet.getRows();

  const row = rows.find((r) => r["Match Number"] == match.matchNumber);
  if (!row) {
    throw new Error(`Could not find match ${match.matchNumber}`);
  }

  saveMatchToRow(match, row);
  await row.save();

  return row;
}

export async function isAlreadyPlayed(
  matchesSheet: GoogleSpreadsheetWorksheet,
  matchNumber: number
) {
  const rows = await matchesSheet.getRows();

  const row = rows.find((r) => r["Match Number"] == matchNumber);
  if (!row) {
    return false;
  }

  return row["Red Score"] != "" || row["Blue Score"] != "";
}

export async function getMatch(
  scheduleSheet: GoogleSpreadsheetWorksheet,
  matchNumber: number
) {
  const rows = await scheduleSheet.getRows();

  const row = rows.find((r) => r["Match Number"] == matchNumber);
  if (!row) {
    throw new Error(`Could not find match ${matchNumber}`);
  }

  return row;
}

export async function copyScheduleToMatchesSheet(
  matchesSheet: GoogleSpreadsheetWorksheet,
  scheduleSheet: GoogleSpreadsheetWorksheet
) {
  const scheduleRows = await scheduleSheet.getRows();
  const matchRows = await matchesSheet.getRows();

  for (const row of scheduleRows) {
    const matchNumber = row["Match Number"];
    if (!Number.isInteger(parseInt(matchNumber))) {
      continue;
    }

    const matchRow = matchRows.find((r) => r["Match Number"] == matchNumber);
    if (!matchRow) {
      await matchesSheet.addRow([
        matchNumber,
        row["Red 1"],
        row["Red 2"],
        row["Red 3"],
        row["Blue 1"],
        row["Blue 2"],
        row["Blue 3"],
      ]);
    } else {
      matchRow["Red 1"] = row["Red 1"];
      matchRow["Red 2"] = row["Red 2"];
      matchRow["Red 3"] = row["Red 3"];
      matchRow["Blue 1"] = row["Blue 1"];
      matchRow["Blue 2"] = row["Blue 2"];
      matchRow["Blue 3"] = row["Blue 3"];
      await matchRow.save();
    }
  }
}
