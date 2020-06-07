import gspread
import pandas as pd
import gspread_dataframe
from oauth2client.service_account import ServiceAccountCredentials

sheet_name = "SPREADSHEET_NAME_GOES_HERE"

scope = ['https://spreadsheets.google.com/feeds', 'https://www.googleapis.com/auth/drive']
credentials = ServiceAccountCredentials.from_json_keyfile_name('KEY_FILE_GOES_HERE.json', scope)

gc = gspread.authorize(credentials)
sheet = gc.open(sheet_name)

wks = sheet.worksheet("Ranks")
teams = gspread_dataframe.get_as_dataframe(wks)

wks = sheet.worksheet("Match Breakdowns")
matches = gspread_dataframe.get_as_dataframe(wks)

matches.to_csv('matches.csv', index=False)
teams.to_csv('teams.csv', index=False)
