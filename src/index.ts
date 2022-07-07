import inquirer from "inquirer";
import gradient from "gradient-string";
import welcome from "./welcome";
// import { getMatchData } from "./field";
// import { isAlreadyPlayed, postMatch, updateMatch } from "./googleSheet";

async function main() {
  /*const { matchesSheet, scheduleSheet } =*/ await welcome();

  let matchNumber = 1;

  while (true) {
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

    console.log(res.action);
  }
}

main();
