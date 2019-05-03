package edu.brown.cs.dnd.GUIHandlers;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.dnd.Data.QueryResult;
import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.Data.StringResult;
import edu.brown.cs.dnd.Dungeon.Dungeon;
import edu.brown.cs.dnd.Dungeon.Rooms.RoomSize;
import edu.brown.cs.dnd.REPL.CommandHandler;
import edu.brown.cs.dnd.REPL.InvalidInputException;
import edu.brown.cs.dnd.REPL.Main;
import spark.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class containing the handlers for the GUI.
 */
public class DnDHandlers {

  private static final Gson GSON = new Gson();

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  public static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Class to handle the initial loading of the GUI.
   */
  public static class FrontHandler implements TemplateViewRoute, Route {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = ImmutableMap.of("title", "Mystery "
              + "Dungeon");
      return new ModelAndView(variables, "mysteryDungeon.ftl");
    }
  }

  /**
   * Handler for user commands in the GUI.
   */
  public static class QueryHandler implements Route {

    @Override
    public String handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String userInput = qm.value("input");

      CommandHandler handler = Main.getREPL().getHandler();
      Result result;
      try {
        result = handler.runCommand(userInput.split("\\s+"));
      } catch (InvalidInputException e) {
        result = new Result(ReturnType.STRING,
                Collections.singletonList(new StringResult(e.getMessage())));
      }

      List<String> prettified = new ArrayList<>();
      List<String> simplified = new ArrayList<>();
      for (QueryResult r : result.getResults()) {
        prettified.add(r.prettify());
        simplified.add(r.simplify());
      }

      Map<String, Object> variables = ImmutableMap.of("result",
              result, "prettified", prettified, "simplified", simplified);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handler for the Dungeon Maker.
   */
  public static class DummyFrontHandler implements TemplateViewRoute, Route {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables =
              ImmutableMap.of("title", "Dungeon Generator");

      return new ModelAndView(variables, "dungeonGenerator.ftl");
    }
  }


  /**
   * Handler to send the randomly-generated dungeon.
   */
  public static class DungeonHandler implements Route {
    @Override
    public String handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      int width = Integer.parseInt(qm.value("width"));
      int height = Integer.parseInt(qm.value("height"));
      String roomSizeString = qm.value("avgRoomSize");
      RoomSize size;
      if (roomSizeString.equals("large")) {
        size = RoomSize.LARGE;
      } else if (roomSizeString.equals("medium")) {
        size = RoomSize.MEDIUM;
      } else {
        size = RoomSize.SMALL;
      }
      Dungeon d = new Dungeon(width, height, size);
      Map<String, Object> variables = ImmutableMap.of("dungeon", d);
      return GSON.toJson(variables);
    }
  }
}
