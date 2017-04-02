package pl.parser.nbp;

import static pl.parser.nbp.utils.TestUtils.arr;

import java.io.File;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.parser.nbp.utils.Parameters.ParametersException;

public class ParserTest
{
  private final static double DELTA = 0.001;
  
  @Rule 
  public ExpectedException exception = ExpectedException.none();
  
  @Test
  public void handle1Test() throws Exception
  {
    Parser p = getParser("EUR", "2013-01-28", "2013-01-31", 
        (stats) -> {
          // expected correct data are located at: http://api.nbp.pl/api/exchangerates/rates/C/EUR/2013-01-28/2013-01-31
          Assert.assertEquals(4.1505, stats.getAverage(), DELTA);
          Assert.assertEquals(0.0144, stats.getStdDeviation(), DELTA);
        });
    p.handle();
  }
  
  @Test
  public void handle2Test() throws Exception
  {
    Parser p = getParser("USD", "2013-06-01", "2014-06-01", 
        (stats) -> {
          // expected correct data are located at: http://api.nbp.pl/api/exchangerates/rates/C/USD/2013-06-01/2014-06-01
          Assert.assertEquals(3.0808, stats.getAverage(), DELTA);
          Assert.assertEquals(0.0910, stats.getStdDeviation(), DELTA);
        });
    p.handle();
  }
  
  @Test
  /**
   * Just an example how to use Parser class to produce CSV files.
   * @throws Exception
   */
  public void exportToCSVTest() throws Exception
  {
    final String CSV_HEADER = "Avg;StdDev\r\n";
    final File tmpFile = File.createTempFile("nbpparser", ".csv");
    
    Parser p = new Parser(arr("CHF", "2014-05-02", "2014-06-01"))
    {
      @Override
      protected PrintStream getPrintStream()
      {
        try
        {
          return new PrintStream(tmpFile);
        }
        catch (Exception ex)
        {
          Assert.fail("Something wrong with creating stream");
          return null;
        }
      }
      
      @Override
      protected void printHandler(CurrencyStatistics stats)
      {
        NumberFormat nf = getNumberFormatter();
        println(CSV_HEADER);
        String nextLine = String.format("%s;%s", 
            nf.format(stats.getAverage()), nf.format(stats.getStdDeviation()));
        println(nextLine);
      }
      
    };
    p.handle();
    
    String contents = FileUtils.readFileToString(tmpFile);
    Assert.assertNotNull(contents);
    Assert.assertTrue(contents.startsWith(CSV_HEADER));
  }
  
  /** Creates MockParser useful for testing purposes. */
  private Parser getParser(String code, String dateFrom, String dateTo, Consumer<CurrencyStatistics> handler) 
  {
    try
    {
      return new MockParser(handler, arr(code, dateFrom, dateTo));
    }
    catch (Exception ex)
    {
      Assert.fail("MUST NOT throw any exception");
      return null;
    }
  }

  static class MockParser extends Parser
  {
    private final Consumer<CurrencyStatistics> handler;

    public MockParser(Consumer<CurrencyStatistics> handler, String[] args) throws ParametersException
    {
      super(args);
      this.handler = handler;
    }
    
    @Override
    protected void printHandler(CurrencyStatistics stats)
    {
      handler.accept(stats);
    }
  }
}
