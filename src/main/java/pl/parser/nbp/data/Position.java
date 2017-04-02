package pl.parser.nbp.data;

public class Position
{
  private double bidRate;
  
  private double askRate;

  /** 
   * Gets bid rate.
   * @return
   */
  public double getBidRate()
  {
    return bidRate;
  }

  public void setBidRate(double buyRate)
  {
    this.bidRate = buyRate;
  }
  
  /**
   * Gets ask rate.
   * @return
   */
  public double getAskRate()
  {
    return askRate;
  }

  public void setAskRate(double sellRate)
  {
    this.askRate = sellRate;
  }
}
