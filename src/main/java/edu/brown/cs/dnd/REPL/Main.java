package edu.brown.cs.dnd.REPL;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.*;

import edu.brown.cs.dnd.Data.*;
import edu.brown.cs.dnd.Dungeon.Dungeon;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;
import edu.brown.cs.dnd.Dungeon.Rooms.RoomElement;
import edu.brown.cs.dnd.Dungeon.Rooms.RoomSize;
import edu.brown.cs.dnd.Generate.GenerateDungeonHandler;
import edu.brown.cs.dnd.Generate.GenerateEncounterHandler;
import edu.brown.cs.dnd.Generate.GenerateNPCHandler;
import edu.brown.cs.dnd.Roll.RollHandler;
import edu.brown.cs.dnd.Search.SearchHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

import com.google.gson.Gson;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author dnd
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static REPL repl;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private static final Gson GSON = new Gson();

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    // set up command registration here
    CommandHandler handler = new CommandHandler();
    List<Handler> handlers = new ArrayList<>(Arrays.asList(
        new SearchHandler(),
        new RollHandler(),
        new HelpHandler(),
        new GenerateNPCHandler(),
        new GenerateEncounterHandler(),
        new GenerateDungeonHandler()
    ));

    for (Handler h : handlers) {
      h.registerCommands(handler);
    }

    repl = new REPL(handler);
    repl = new REPL(handler);
    repl.beginParsing();
//    Dungeon d = new Dungeon(100, 100, RoomSize.MEDIUM);
//    for (AbsRoom r : d.getRooms()) {
//      for (RoomElement e : r.getElements()) {
//        System.out.println(e.toString());
//      }
//    }
  }

  /**
   * Creates the FreeMarkerEngine for Spark.
   *
   * @return The FreeMarkerEngine.
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Runs the Spark server with the given port.
   *
   * @param port The port number.
   */
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/d&d", new FrontHandler(), freeMarker);
    Spark.post("/query", new QueryHandler());

    Spark.get("/dungeonmaker", new DummyFrontHandler(), freeMarker);
    Spark.post("/dungeon", new DungeonHandler());
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
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
  private class FrontHandler implements TemplateViewRoute, Route {
    @Override
    public ModelAndView handle(Request request, Response response)
            throws Exception {
      Map<String, Object> variables = ImmutableMap.of("title", "Mystery "
              + "Dungeon");
      return new ModelAndView(variables, "mysteryDungeon.ftl");
    }
  }

  /**
   * Handler for user commands in the GUI.
   */
  private class QueryHandler implements Route {
    @Override
    public String handle(Request request, Response response) throws Exception {
      QueryParamsMap qm = request.queryMap();
      String userInput = qm.value("input");

      CommandHandler handler = repl.getHandler();
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
  private class DummyFrontHandler implements TemplateViewRoute, Route {
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
  private class DungeonHandler implements Route {
    @Override
    public String handle(Request request, Response response) throws Exception {
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
      return Main.GSON.toJson(variables);
    }
  }
}
