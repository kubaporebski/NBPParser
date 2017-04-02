package pl.parser.nbp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass 
{
  /**
   * Main entry point.
   * @param args - arguments passed to program. Correct order and meaning: <br>
   *  [0] - currency code <br>
   *  [1] - start date<br>
   *  [2] - end date <br>
   * When no arguments are given, simple help is printed.
   */
  public static void main(String[] args)
  {
    try
    {
      setLogger();
      
      if (args.length == 3)
      {
        Parser parser = new Parser(args);
        parser.handle();
      }
      else
      {
        printHelp();
      }
    }
    catch (Exception ex)
    {
      Logger.getGlobal().severe(ex.getMessage());
    }
  }
  
  /**
   * Set default logger. 
   * Method sets default logging level of SEVERE. <br>
   * When there is a VM argument in format -Dlog.level=... it sets level to value of that argument.
   */
  private static void setLogger()
  {
    Level logLevel = Level.SEVERE;
    String newLogLevel = System.getProperty("log.level"); 
    if (newLogLevel != null)
      logLevel = Level.parse(newLogLevel.toUpperCase());
    
    Logger.getGlobal().setLevel(logLevel);
  }
  
  /**
   * Just simple method to print help.
   */
  private static void printHelp()
  {
    System.out.println("Usage: java -jar NBPParser.jar [CURRENCY_CODE] [DATE_FROM:YYYY-MM-DD] [DATE_TO:YYYY-MM-DD]");
  }  
}
