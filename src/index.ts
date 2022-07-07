import inquirer from "inquirer";
import gradient from "gradient-string";
import welcome from "./welcome";
import { saveField } from "./saver";

async function main() {
  const { matchesSheet, scheduleSheet } = await welcome();

  let matchNumber = 1;

  while (true) {
    console.log();
    console.log(gradient.cristal(`Match ${matchNumber}`));

    const res = await inquirer.prompt({
      type: "list",
      name: "action",
      message: "What would you like to do?",
      choices: [
        `Save match ${matchNumber} on field 1`,
        `Save match ${matchNumber} on field 2`,
        "Set match number",
        "Exit",
      ],
    });

    switch (res.action) {
      case `Save match ${matchNumber} on field 1`:
        await saveField(scheduleSheet, matchesSheet, matchNumber, 1);
        matchNumber++;
        break;
      case `Save match ${matchNumber} on field 2`:
        await saveField(scheduleSheet, matchesSheet, matchNumber, 2);
        matchNumber++;
        break;
      case "Set match number":
        const resp = await inquirer.prompt({
          type: "number",
          name: "match",
          message: "Match Number:",
          validate: (input) => Number.isInteger(input) && input >= 1,
        });
        matchNumber = resp.match;
        break;
      case "Exit":
        process.exit(0);
    }
  }
}

main();
