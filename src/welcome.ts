import dotenv from "dotenv";
import { createSpinner } from "nanospinner";
import chalk from "chalk";
import { setupConnection } from "./googleSheet";
import gradient from "gradient-string";

const envVars = [
  "GOOGLE_SHEET_CLIENT_EMAIL",
  "GOOGLE_SHEET_PRIVATE_KEY",
  "GOOGLE_SHEET_DOC_ID",
];

export default async function welcome() {
  console.clear();
  console.log(gradient.pastel("xRC Tourney Runner"));
  console.log(chalk.cyan("Copyright (c) 2022 Nicholas Bottone"));
  console.log();

  let spinner = createSpinner("Loading environment variables").start();

  dotenv.config();

  for (const envVar of envVars) {
    if (!process.env[envVar]) {
      spinner.error();
      console.log(chalk.red(`Missing environment variable: ${envVar}`));
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
    process.exit(1);
  }

  spinner.success();
  const sheetsUrl = `https://docs.google.com/spreadsheets/d/${process.env.GOOGLE_SHEET_DOC_ID}`;
  console.log(chalk.green(`Connected to Google Sheets: ${sheetsUrl}`));
  console.log();

  return sheets;
}
