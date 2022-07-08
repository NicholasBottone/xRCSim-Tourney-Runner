import dotenv from "dotenv";
import open from "open";
import { createSpinner } from "nanospinner";
import chalk from "chalk";
import { setupConnection } from "./googleSheet";
import gradient from "gradient-string";
import { version } from "../package.json";

const envVars = [
  "GOOGLE_SHEET_CLIENT_EMAIL",
  "GOOGLE_SHEET_PRIVATE_KEY",
  "GOOGLE_SHEET_DOC_ID",
];

export default async function welcome() {
  console.clear();
  console.log(gradient.pastel(`xRC Tourney Runner ${version}`));
  console.log(chalk.cyan("Copyright (c) 2022 Nicholas Bottone"));
  console.log();

  let spinner = createSpinner("Loading environment variables").start();

  dotenv.config();

  for (const envVar of envVars) {
    if (!process.env[envVar]) {
      spinner.error();
      console.log(chalk.red(`Missing environment variable: ${envVar}`));
      await open(
        "https://github.com/NicholasBottone/xRCSim-Tourney-Runner/wiki/Set-up-.env",
        { wait: true }
      );
      process.exit(1);
    }
  }

  spinner.success();

  spinner = createSpinner("Connecting to Google Sheets").start();

  let sheets;

  try {
    sheets = await setupConnection();
  } catch (e) {
    spinner.error();
    console.log(chalk.red(e));
    await open(
      "https://github.com/NicholasBottone/xRCSim-Tourney-Runner/wiki/Set-up-Google-Sheet",
      { wait: true }
    );
    process.exit(1);
  }

  spinner.success();

  const sheetsUrl = `https://docs.google.com/spreadsheets/d/${process.env.GOOGLE_SHEET_DOC_ID}`;
  console.log(chalk.green(`Connected to Google Sheets: ${sheetsUrl}`));
  console.log();

  return sheets;
}
