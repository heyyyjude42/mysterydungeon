package edu.brown.cs.dnd.Roll;

import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.Data.StringResult;
import edu.brown.cs.dnd.REPL.*;

import java.util.Arrays;

public class RollHandler implements Handler {
  private String SYNTAX_ERROR_MESSAGE = "ERROR: expected syntax in the form " +
      "of roll xdy, such as 4d6 or 1d8.";

  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("roll", new RollCommand());
  }

  private class RollCommand implements Command {
    @Override
    public Result run(String[] args) throws InvalidInputException {
      if (args.length != 2) {
        throw new InvalidInputException(SYNTAX_ERROR_MESSAGE);
      }

      String dice = args[1];

      try {
        return new Result(ReturnType.STRING, Arrays.asList(new StringResult(
            "Result: " + rollDice(dice) + "\n")));
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
    private int rollDice(String dice) throws InvalidInputException {
      String[] nums = dice.split("d");

      if (nums.length != 2) {
        throw new InvalidInputException(SYNTAX_ERROR_MESSAGE);
      }

      int numDice = Integer.parseInt(nums[0]);
      int diceFaces = Integer.parseInt(nums[1]);
      int sum = 0;

      for (int i = 0; i < numDice; i++) {
        sum += Math.random() * diceFaces;
      }

      return sum;
    }
  }
}
