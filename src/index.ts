import dotenv from "dotenv";
import fs from "fs";
import path from "path";
import { GoogleSpreadsheet } from "google-spreadsheet";
import chalk from "chalk";

dotenv.config();

console.log(chalk.green("Starting..."));

// Setup connection to google spreadsheet

const creds = {
  client_email: process.env.GOOGLE_SHEET_CLIENT_EMAIL || "",
  private_key:
    process.env.GOOGLE_SHEET_PRIVATE_KEY?.replace(/\\n/g, "\n") || "",
};

const doc = new GoogleSpreadsheet(process.env.GOOGLE_SHEET_DOC_ID || "");
