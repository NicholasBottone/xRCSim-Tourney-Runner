import gspread
import pandas as pd
import gspread_dataframe
from oauth2client.service_account import ServiceAccountCredentials

sheet_name = "SPREADSHEET_NAME_GOES_HERE"

scope = ['https://spreadsheets.google.com/feeds', 'https://www.googleapis.com/auth/drive']
credentials = ServiceAccountCredentials.from_json_keyfile_name('KEY_FILE_GOES_HERE.json', scope)

teams = pd.read_csv("teams.csv")
matches = pd.read_csv('matches.csv')

gc = gspread.authorize(credentials)
sheet = gc.open(sheet_name)

wks = sheet.worksheet("Ranks")
gspread_dataframe.set_with_dataframe(wks, teams)

wks = sheet.worksheet("Match Breakdowns")
gspread_dataframe.set_with_dataframe(wks, matches)
