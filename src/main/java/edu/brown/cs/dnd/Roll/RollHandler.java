package edu.brown.cs.dnd.Roll;

import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.Data.StringResult;
import edu.brown.cs.dnd.REPL.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a CommandHandler for rolling the D&D dice.
 */
public class RollHandler implements Handler {
  private static final String SYNTAX_ERROR_MESSAGE =
          "ERROR: expected syntax in the form "
                  + "of roll xdy, such as 4d6, or an integer.";

  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("roll", new RollCommand());
  }

  /**
   * Class representing the roll command in the REPL.
   */
  private class RollCommand implements Command {
    @Override
    public Result run(String[] args) throws InvalidInputException {
      if (args.length <= 1) {
        return new Result(ReturnType.NONE, new ArrayList<>());
      }

      StringBuilder roll = new StringBuilder();

      for (int i = 1; i < args.length; i++) {
        roll.append(args[i]);
      }

      String[] dice = roll.toString().split("[+\\-]");

      try {
        int[] diceRolls = rollDice(dice);
        Pattern plusOrMinus = Pattern.compile("[+\\-]");
        Matcher m = plusOrMinus.matcher(roll);

        int result = diceRolls[0];
        int nextIndex = 1;
        while (m.find() && nextIndex < diceRolls.length) {
          String nextOperator = m.group();

          switch (nextOperator) {
            case "+":
              result += diceRolls[nextIndex];
              break;
            case "-":
              result -= diceRolls[nextIndex];
              break;
            default:
              break;
          }
          nextIndex++;
        }

        return new Result(ReturnType.STRING, Arrays.asList(new StringResult(
            "Result: " + result)));
      } catch (NumberFormatException e) {
        throw new InvalidInputException(SYNTAX_ERROR_MESSAGE);
      }
    }

    /**
     * Takes in a string in the form of xdy and returns the corresponding
     * roll amount. For instance, 2d8 will return a number between 2 and 16.
     *
     * @param dice String representation of dice to be rolled.
     * @return The final roll in form of an int.
     */
    private int[] rollDice(String[] dice) throws InvalidInputException {
      int[] sums = new int[dice.length];

      for (int i = 0; i < dice.length; i++) {
        sums[i] = rollDie(dice[i]);
      }

      return sums;
    }

    private int rollDie(String die) throws InvalidInputException {
      String[] nums = die.split("d");

      if (nums.length == 1) {
        try {
          return Integer.parseInt(die);
        } catch (NumberFormatException e) {
          throw new InvalidInputException(SYNTAX_ERROR_MESSAGE);
        }
      }

      int numDice = Integer.parseInt(nums[0]);
      int diceFaces = Integer.parseInt(nums[1]);
      int sum = 0;

      for (int i = 0; i < numDice; i++) {
        sum += Math.random() * diceFaces + 1;
      }

      return sum;
    }
  }
}
