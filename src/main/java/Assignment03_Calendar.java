import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.appium.java_client.ios.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This template is for users that use DigitalZoom Reporting (ReportiumClient).
 * For any other use cases please see the basic template at https://github.com/PerfectoCode/Templates.
 * For more programming samples and updated templates refer to the Perfecto Documentation at: http://developers.perfectomobile.com/
 */
public class Assignment03_Calendar {
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");
        
        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "ps.perfectomobile.com";
        capabilities.setCapability("user", "");
        capabilities.setCapability("password", "");
        
        //TODO: Change your device ID
        capabilities.setCapability("platformName", "iOS");


        
        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");
        
        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);
        capabilities.setCapability("scriptName", "Assignment03_Calendar");
        
  //     AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
      IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        // Reporting client. For more details, see http://developers.perfectomobile.com/display/PD/Reporting
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
        .withProject(new Project("My Project", "1.0"))
        .withJob(new Job("My Job", 45))
        .withContextTags("tag1")
        .withWebDriver(driver)
        .build();
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
        
        try {
            reportiumClient.testStart("Calendar assignment", new TestContext("assignment", "calendar"));
            
            // write your code here


            reportiumClient.stepStart("Open Calendar Event");
                Map<String, Object> params2 = null;
               // close the app in case it is running from previous iteration
                try {
                    params2 = new HashMap<>();

                    params2.put("name", "Calendar");
                    driver.executeScript("mobile:application:close", params2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Map<String, Object> params = new HashMap<>();
                params.put("name", "Calendar");
                driver.executeScript("mobile:application:open", params);
                driver.context("NATIVE_APP");
                driver.findElementByName("Add").click();
                Map<String, Object> params3 = new HashMap<>();
                params3.put("content", "new event");
                params3.put("timeout", 20);
                String res = (String) driver.executeScript("mobile:checkpoint:text", params3);

            reportiumClient.stepEnd();
            reportiumClient.stepStart("Add Location & title");

                  WebElement a = (WebElement) driver.findElementsByClassName("XCUIElementTypeTextField").get(1);
                    a.sendKeys("fun day");
                    Thread.sleep(5000);
                    driver.findElementByName("Location").sendKeys("London");
                   driver.findElementByName("Done").click();

            reportiumClient.stepEnd();
            reportiumClient.stepStart("Set Start Time");

                driver.findElementByName("Starts").click();
                Thread.sleep(2000);

                WebElement hour = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(1);
                String name = hour.getAttribute("value");
                System.out.println("hour:" + name);
                hour.sendKeys("9");
                Thread.sleep(2000);
                WebElement minute = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(2);
                String min = minute.getAttribute("value");
                System.out.println("minute:" + min);
                minute.sendKeys("45");


            reportiumClient.stepEnd();

            reportiumClient.stepStart("Set End Time");
                driver.findElementByName("Ends").click();
                WebElement hourEnd = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(1);
                String hourEndStr = hour.getAttribute("value");
                System.out.println("hour:" + hourEndStr);
                hourEnd.sendKeys("11");
                Thread.sleep(2000);
                WebElement minuteEnd = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(2);
                String minEnd = minute.getAttribute("value");
                System.out.println("minute:" + minEnd);
                minute.sendKeys("15");



            reportiumClient.stepEnd();
            reportiumClient.stepStart("Recurring Event");
                driver.findElementByName("Repeat").click();
                driver.findElementByName("Every Month").click();
                driver.findElementByName("End Repeat").click();
                driver.findElementByName("On Date").click();
                Thread.sleep(4000);
                WebElement monthEndObject = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(0);
                String monthEnd = minute.getAttribute("value");
                System.out.println("minute:" + monthEnd);
                 monthEndObject.sendKeys("December");
              Thread.sleep(2000);
                WebElement dayEndObject = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(1);
                String day = minute.getAttribute("value");
                System.out.println("minute:" + day);
                 dayEndObject.sendKeys("5");
                Thread.sleep(2000);


                WebElement year = (WebElement) driver.findElementsByClassName("XCUIElementTypePickerWheel").get(2);
                String yearEnd = minute.getAttribute("value");
                System.out.println("yearEnd:" + yearEnd);
                year.sendKeys("2020");
                 Thread.sleep(4000);
                // return to main event screen
                 driver.findElementByName("New Event").click();

            reportiumClient.stepEnd();
            reportiumClient.stepStart("Add Travel time");
                driver.findElementByName("Travel Time").click();
                WebElement travelToggle = (WebElement) driver.findElementsByClassName("XCUIElementTypeSwitch").get(0);
                travelToggle.click();
                driver.findElementByName("2 hours").click();
                driver.findElementByName("New Event").click();
            reportiumClient.stepEnd();
            reportiumClient.stepStart("Close application");

              driver.executeScript("mobile:application:close", params2);

            reportiumClient.stepEnd();

            reportiumClient.testStop(TestResultFactory.createSuccess());
        } catch (Exception e) {
            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();
        } finally {
            try {
                driver.quit();
                
                // Retrieve the URL to the DigitalZoom Report (= Reportium Application) for an aggregated view over the execution
                String reportURL = reportiumClient.getReportUrl();
                
                // Retrieve the URL to the Execution Summary PDF Report
                String reportPdfUrl = (String)(driver.getCapabilities().getCapability("reportPdfUrl"));
                // For detailed documentation on how to export the Execution Summary PDF Report, the Single Test report and other attachments such as
                // video, images, device logs, vitals and network files - see http://developers.perfectomobile.com/display/PD/Exporting+the+Reports
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Run ended");
    }
    
    private static void switchToContext(RemoteWebDriver driver, String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        Map<String,String> params = new HashMap<String,String>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }
    
    private static String getCurrentContextHandle(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
        return context;
    }
    
    private static List<String> getContextHandles(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
        return contexts;
    }

    private static void pickerwheelStep(IOSDriver driver, WebElement element, String direction, double offset) {
        Map<String, Object> params = new HashMap<>();
        params.put("order", direction);
        params.put("offset", offset);
       // params.put("element", ((RemoteWebElement) element).getId());
        driver.executeScript("mobile: selectPickerWheelValue", params);
    }
    
}
