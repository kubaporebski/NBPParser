package pl.parser.nbp.data;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExchangeRatesTable
{
  private DateTime publicationDate;
  
  private Position position;
  
  public DateTime getPublicationDate()
  {
    return publicationDate;
  }

  public void setPublicationDate(DateTime publicationDate)
  {
    this.publicationDate = publicationDate;
  }

  public Position getPosition()
  {
    return position;
  }

  public void setPosition(Position position)
  {
    this.position = position;
  }
  
  /**
   * Method to parse XML document. <br>
   * In single document there data of lots of currencies. This method extracts data of only one given currency.
   * @param document - input XML document
   * @param currencyCode - currency code, for which we want the data
   * @return object of ExchangeRatesTable class
   * @throws Exception - error reading data or there are no data of given currency code in document
   */
  public static ExchangeRatesTable parse(Document document, String currencyCode) throws Exception
  {
    /*
    <numer_tabeli>73/C/NBP/2007</numer_tabeli>
    <data_notowania>2007-04-12</data_notowania>
    <data_publikacji>2007-04-13</data_publikacji>
    <pozycja>
      <nazwa_waluty>dolar amerykański</nazwa_waluty>
      <przelicznik>1</przelicznik>
      <kod_waluty>USD</kod_waluty>
      <kurs_kupna>2,8210</kurs_kupna>
      <kurs_sprzedazy>2,8780</kurs_sprzedazy>
    </pozycja>
    */ 
    
    ExchangeRatesTable table = new ExchangeRatesTable();
    XPath xpath = XPathFactory.newInstance().newXPath();
    
    String pubDate = (String) xpath.evaluate("//data_publikacji/text()", document, XPathConstants.STRING);
    table.setPublicationDate(new DateTime(pubDate));
    
    Element xmlCurrencyCode = (Element) xpath.evaluate(String.format("//pozycja/kod_waluty[text()='%s']", currencyCode), document, XPathConstants.NODE);
    if (xmlCurrencyCode != null)
    {
      Position position = new Position();
      Element xmlPosition = (Element) xmlCurrencyCode.getParentNode();
      
      String posBuyRate =  (String) xpath.evaluate("kurs_kupna/text()", xmlPosition, XPathConstants.STRING);
      position.setBidRate(Double.parseDouble(posBuyRate.replace(',', '.')));
      
      String posSellRate = (String) xpath.evaluate("kurs_sprzedazy/text()", xmlPosition, XPathConstants.STRING);
      position.setAskRate(Double.parseDouble(posSellRate.replace(',', '.')));
      
      table.setPosition(position);
    }
    else
      throw new Exception("Element 'kod_waluty' with given currency code was not found");
   
    return table;
  }
  
}
