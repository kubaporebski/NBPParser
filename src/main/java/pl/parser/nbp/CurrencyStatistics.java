package pl.parser.nbp;

import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

import pl.parser.nbp.data.ExchangeRatesTable;

public class CurrencyStatistics
{
  private double average;
  
  private double stdDeviation;
  
  /**
   * Gets average of bid rates.
   * @return
   */
  public double getAverage()
  {
    return average;
  }

  private void setAverage(double average)
  {
    this.average = average;
  }

  /**
   * Gets standard deviation of ask rates.
   * @return
   */
  public double getStdDeviation()
  {
    return stdDeviation;
  }

  private void setStdDeviation(double stdDeviation)
  {
    this.stdDeviation = stdDeviation;
  }
  
  /**
   * Performs calculations over ready list of bid & ask rates.
   * @param table - input table with data
   * @return object of CurrencyStatitics class with calculated data
   */
  public static CurrencyStatistics calculate(List<ExchangeRatesTable> table)
  {
    CurrencyStatistics result = new CurrencyStatistics();
    result.calculateData(table);
    return result;
  }
  
  /** Method to calculate statistics based on input table data. */
  private void calculateData(List<ExchangeRatesTable> table)
  {
    double[] bidRates = table
      .stream()
      .map((ert) -> ert.getPosition().getBidRate())
      .mapToDouble(Double::doubleValue)
      .toArray();
    
    double[] askRates = table
        .stream()
        .map((ert) -> ert.getPosition().getAskRate())
        .mapToDouble(Double::doubleValue)
        .toArray();
    
    double mean = StatUtils.mean(bidRates);
    setAverage(mean);
    
    double stdDev = Math.sqrt(StatUtils.variance(askRates, average));
    setStdDeviation(stdDev);
  }
}
