import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

        import java.io.IOException;
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
public class Assignment01_Wikipedia {

    public static void main(String[] args) throws IOException {
        System.out.println("Run started");
        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "MYCLOUD.perfectomobile.com";
        capabilities.setCapability("user", "");
        capabilities.setCapability("password", "");

        //TODO: Change your device ID
        capabilities.setCapability("platformName", "Android");
        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        // capabilities.setCapability("automationName", "PerfectoMobile");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);

        // Name your script
        capabilities.setCapability("scriptName", "Check term in wikipedia");

        RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // Reporting client. For more details, see http://developers.perfectomobile.com/display/PD/Reporting


        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("Perfecto Training", "1.0"))
                .withContextTags("wiki", "assignment", "core")
                .withWebDriver(driver)
                .build();
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);


        try {

            reportiumClient.testStart("Wiki Assignment", new TestContext("android"));
            reportiumClient.stepStart("open website");
            driver.get("www.wikipedia.org");
            Map<String, Object> params = new HashMap<>();
            params.put("content", "The Free Encyclopedia");
            params.put("timeout", 20);
            String res = (String) driver.executeScript("mobile:checkpoint:text", params);

            if (res.equalsIgnoreCase("true")) {
            } else {
                reportiumClient.reportiumAssert("homepage loaded", false);
            }
            reportiumClient.stepEnd();
            reportiumClient.stepStart("search term");

            driver.findElementByXPath("//*[@id='searchInput']").sendKeys("apple");
            Thread.sleep(4000);

            driver.findElementByXPath("//*[@class=\"pure-button pure-button-primary-progressive\"]").click();
            Thread.sleep(4000);
            Map<String, Object> params2 = new HashMap<>();
            params2.put("content", "fruit");
            params2.put("timeout", 20);
            res = (String) driver.executeScript("mobile:checkpoint:text", params2);

            if (res.equalsIgnoreCase("true")) {
            } else {
                reportiumClient.reportiumAssert("search term loaded", false);
            }


            reportiumClient.stepEnd();
            // The assignment continues the sample:
            reportiumClient.stepStart("Assignment 1");
            // Click on menu, select random entry and add to favourites
            driver.findElementByXPath("//*[@id=\"mw-mf-main-menu-button\"]").click();
            driver.findElementByXPath("//*[text()=\"Random\"]").click();
            driver.findElementByXPath("//*[text()=\"Watch this page\"]").click();
            Map<String, Object> params3 = new HashMap<>();
            params3.put("content", "track this page");
            params3.put("timeout", 20);
            res = (String) driver.executeScript("mobile:checkpoint:text", params3);

            if (res.equalsIgnoreCase("true")) {
            } else {
                reportiumClient.reportiumAssert("Tracking wiki term", false);
            }
            reportiumClient.stepEnd();

            if (res.equalsIgnoreCase("true")) {
                reportiumClient.testStop(TestResultFactory.createSuccess());
            }
            else {
                    reportiumClient.testStop(TestResultFactory.createFailure("test failed"));

                }
        } catch (Exception e) {
            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();
        } finally {
            try {
                driver.quit();

                // Retrieve the URL to the DigitalZoom Report (= Reportium Application) for an aggregated view over the execution
                String reportURL = reportiumClient.getReportUrl();

                // Retrieve the URL to the Execution Summary PDF Report
                String reportPdfUrl = (String) (driver.getCapabilities().getCapability("reportPdfUrl"));
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }

    private static String getCurrentContextHandle(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        String context = (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
        return context;
    }

    private static List<String> getContextHandles(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        List<String> contexts = (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
        return contexts;
    }
}