package pl.parser.nbp;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;

import pl.parser.nbp.data.ExchangeRatesTable;
import pl.parser.nbp.utils.Parameters;
import pl.parser.nbp.utils.Parameters.ParametersException;

public class Parser
{
  private PrintStream out;
  
  private Parameters parameters;
  
  public Parser(String[] args) throws ParametersException
  {
    parameters = Parameters.init(args);
  }
  
  /**
   * Handle the situation. <br>
   * Method downloads data and calculates stats.
   * @throws Exception - error occured during download or calculating statistics
   */
  public void handle() throws Exception
  {
    // retrieve remote data and wait...
    List<ExchangeRatesTable> table = getTable();
    
    // and then calculate what we need...
    CurrencyStatistics stats = CurrencyStatistics.calculate(table);
    
    // finally, we can print results into some output print stream
    printHandler(stats);
  }
  
  /** Gets table list. */
  protected List<ExchangeRatesTable> getTable() throws Exception
  {
    return DataRetriever.getInstance(parameters).getResult();
  }
  
  /**
   * Handler method for printing resulting values.
   * @param stats - already calculated statistics
   */
  protected void printHandler(CurrencyStatistics stats)
  {
    NumberFormat formatter = getNumberFormatter();
    
    println(parameters.getCurrencyCode());
    println(formatter.format(stats.getAverage()));
    println(formatter.format(stats.getStdDeviation()));
  }
  
  /** Print some object to print stream. Add new line at the end. */
  protected void println(Object ob)
  {
    // create single instance of output stream
    if (out == null)
      out = getPrintStream();
    
    out.println(ob);
    out.flush();
  }
  
  /**
   * Gets default output stream to print results onto. <br>
   * @return System.out stream by default
   */
  protected PrintStream getPrintStream()
  {
    return System.out;
  }
  
  /**
   * Gets formatter of double values.
   * @return
   */
  protected NumberFormat getNumberFormatter()
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(4);
    return nf;
  }
}
