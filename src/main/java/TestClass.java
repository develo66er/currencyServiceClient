import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class TestClass {
    private static final Logger LOG= Logger.getLogger(TestClass.class);
    private static SimpleDateFormat format;
    private static final String excelPath = "/home/natali/Загрузки/report.xls";
    private static final String excelSheetName = "BNM";
    private static int excelNumRows;
    private static final int excelNumCols = 6;
    private static final String startDate = "12.02.2016";
    private static final String endDate = "13.03.2016";
    private WebDriver driver;

    @Test(dataProvider = "dataProvider")
    public void getDocument(String dateValue, String USD, String EUR,String RUB, String RON, String UAH) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, InterruptedException, ParseException {

        Date d = new Date(Long.parseLong(dateValue));
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTime(d);
        Document document = XmlDataLoader.getXMLDocument(calendar);
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/ValCurs/Valute[CharCode='USD']/Value/text()";
        String USDValue = ((NodeList)xPath.compile(expression).evaluate(document, XPathConstants.NODESET)).item(0).getNodeValue();
        expression = "/ValCurs/Valute[CharCode='EUR']/Value/text()";
        String EURValue = ((NodeList)xPath.compile(expression).evaluate(document, XPathConstants.NODESET)).item(0).getNodeValue();
        expression = "/ValCurs/Valute[CharCode='RUB']/Value/text()";
        String RUBValue = ((NodeList)xPath.compile(expression).evaluate(document, XPathConstants.NODESET)).item(0).getNodeValue();
        expression = "/ValCurs/Valute[CharCode='RON']/Value/text()";
        String RONValue = ((NodeList)xPath.compile(expression).evaluate(document, XPathConstants.NODESET)).item(0).getNodeValue();
        expression = "/ValCurs/Valute[CharCode='UAH']/Value/text()";
        String UAHValue = ((NodeList)xPath.compile(expression).evaluate(document, XPathConstants.NODESET)).item(0).getNodeValue();

        Assert.assertEquals(Double.parseDouble(USD),Double.parseDouble(USDValue));
        Assert.assertEquals(Double.parseDouble(EUR),Double.parseDouble(EURValue));
        Assert.assertEquals(Double.parseDouble(RUB),Double.parseDouble(RUBValue));
        Assert.assertEquals(Double.parseDouble(RON),Double.parseDouble(RONValue));
        Assert.assertEquals(Double.parseDouble(UAH),Double.parseDouble(UAHValue));

    }
    @DataProvider(name = "dataProvider")
    public Object[][] primeNumbers() throws Exception {
        //System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+ File.separator +"IEDriver"+ File.separator +"IEDriverServer.exe");
        //System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+ File.separator +"chromeDriver"+ File.separator +"chromedriver");
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        //driver = new InternetExplorerDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.navigate().to("https://www.bnm.md/bdi/pages/reports/dovre/DOVRE7.xhtml");
        format = new SimpleDateFormat("dd.MM.yyyy");
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);
        excelNumRows = XmlDataLoader.loadXMLFromServiceByDateRange(start.getTime(),end.getTime())+5;
        String inp1CssSel = "#dateRepBeg_input";
        String inp2CssSel = "#dateRepEnd_input";
        WebDriverWait wait = new WebDriverWait(driver, 40);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(inp1CssSel)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(inp2CssSel)));
        WebElement el = driver.findElement(By.cssSelector(inp1CssSel));
        el.clear();
        el.sendKeys(startDate);
        el = driver.findElement(By.cssSelector(inp2CssSel));
        el.clear();
        el.sendKeys(endDate);
        driver.findElement(By.xpath("//button[@id='j_idt56_button']")).click();
        driver.findElement(By.xpath("//div[@id='j_idt56_menu']/ul/li[1]/a")).click();


        Robot rb =new Robot();

        Thread.sleep(2000);
        rb.keyPress(KeyEvent.VK_DOWN);
        rb.keyRelease(KeyEvent.VK_DOWN);

        Thread.sleep(2000);
        rb.keyPress(KeyEvent.VK_ENTER);
        rb.keyRelease(KeyEvent.VK_ENTER);


        Thread.sleep(2000);
        Object[][] testObjArray = ExcelUtils.getTableArray(excelPath, excelSheetName, excelNumRows, excelNumCols);
        return (testObjArray);
    }
@AfterClass
 public void afterMethod(){

    driver.quit();
}
}
