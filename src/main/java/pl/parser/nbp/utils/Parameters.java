package pl.parser.nbp.utils;

import org.joda.time.DateTime;

/**
 * Utility class for parsing & holding input parameters
 */
public class Parameters
{
  private String currencyCode;
  
  private DateTime startDate;
  
  private DateTime endDate;
  
  private Parameters() { }
  
  /**
   * Gets currency code.
   * @return
   */
  public String getCurrencyCode()
  {
    return currencyCode;
  }

  private void setCurrencyCode(String currencyCode)
  {
    this.currencyCode = currencyCode;
  }
  
  /**
   * Gets date from.
   * @return
   */
  public DateTime getStartDate()
  {
    return startDate;
  }

  private void setStartDate(DateTime dateFrom)
  {
    this.startDate = dateFrom;
  }

  /**
   * Gets date to.
   * @return
   */
  public DateTime getEndDate()
  {
    return endDate;
  }

  private void setEndDate(DateTime dateTo)
  {
    this.endDate = dateTo;
  }
  
  /**
   * Initialization based on arguments passed from main entry method.
   * @param args - input argumens.
   * @return true - all arguments are correct / false - otherwise
   * @throws ParametersException - when there is error reading input parameters
   */
  public static Parameters init(String[] args) throws ParametersException
  {
    /*
     * Perform some validation beforehand.
     */
    if (args == null || args.length != 3)
      throw new ParametersException("Incorrect count of arguments");
    
    if (args[0] == null || args[0].trim().isEmpty())
      throw new ParametersException("Currency code cannot be null or empty");
    
    if (args[1] == null)
      throw new ParametersException("Start date cannot be null");
    
    if (args[2] == null)
      throw new ParametersException("End date cannot be null");
    
    // always create new instance of class when initializing
    Parameters instance = new Parameters();
    instance.setCurrencyCode(args[0]);
    instance.setStartDate(new DateTime(args[1]));
    instance.setEndDate(new DateTime(args[2]));
    
    // check correct date ranges
    if (instance.getStartDate().compareTo(instance.getEndDate()) > 0)
      throw new ParametersException("Start date must be less than or equal to end date.");
    
    return instance;
  }
  
  public static class ParametersException extends Exception
  {
    private static final long serialVersionUID = -2727092303174542724L;
    
    public ParametersException(String message)
    {
      super(message);
    }
  }
}
