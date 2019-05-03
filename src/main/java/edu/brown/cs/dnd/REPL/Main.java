package edu.brown.cs.dnd.REPL;

import java.io.IOException;
import java.io.File;
import java.util.*;

import edu.brown.cs.dnd.Data.*;
import edu.brown.cs.dnd.GUIHandlers.DnDHandlers;
import edu.brown.cs.dnd.Generate.GenerateDungeonHandler;
import edu.brown.cs.dnd.Generate.GenerateEncounterHandler;
import edu.brown.cs.dnd.Generate.GenerateNPCHandler;
import edu.brown.cs.dnd.Roll.RollHandler;
import edu.brown.cs.dnd.Search.SearchHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import freemarker.template.Configuration;

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
    repl.beginParsing();
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
    Spark.exception(Exception.class, new DnDHandlers.ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/d&d", new DnDHandlers.FrontHandler(), freeMarker);
    Spark.post("/query", new DnDHandlers.QueryHandler());

    Spark.get("/dungeonmaker", new DnDHandlers.DummyFrontHandler(),
            freeMarker);
    Spark.post("/dungeon", new DnDHandlers.DungeonHandler());
  }

  /**
   * Method returns the Main's REPL.
   * @return    A REPL that is the Main's REPL
   */
  public static REPL getREPL() {
    return repl;
  }
}
