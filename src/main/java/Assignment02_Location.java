import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
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
public class Assignment02_Location {

    public static void main(String[] args) throws IOException {
        System.out.println("Run started");
        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "ps.perfectomobile.com";
        capabilities.setCapability("user", "yaronw@perfectomobile.com");
        capabilities.setCapability("password", "Aa562041!");


        /***************************
         * Mobile Capabilities
         */
        capabilities.setCapability("platformName","Android");

        /*************************
         * Desktop capabilities
         *
         */
        /*
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("platformVersion", "10");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "69");
        capabilities.setCapability("resolution", "1280x1024");
        capabilities.setCapability("location", "US East");
        */
        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);

        // Name your script
        //capabilities.setCapability("scriptName", "Check term in wikipedia");

        RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // Reporting client. For more details, see http://developers.perfectomobile.com/display/PD/Reporting


        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("Perfecto Training", "1.0"))
                .withContextTags("location", "assignment", "core")
                .withWebDriver(driver)
                .build();
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);


        try {

            // get the platform of the runnning device
            Map params = new HashMap<>();
            params.put("property", "os");
            String os = (String) driver.executeScript("mobile:handset:info", params);
            System.out.println("the platform is: " + os);

            reportiumClient.testStart("Location Assignment", new TestContext("android"));
            reportiumClient.stepStart("open website");


            driver.get("https://training.perfecto.io/");

            // Branching needed as desktop does not support image analysis
            if(os.equalsIgnoreCase("Windows")) {
                Map<String, Object> params2 = new HashMap<>();
                params2.put("content", "taking the first step");
                params2.put("timeout", 20);
                String res = (String) driver.executeScript("mobile:checkpoint:text", params2);
            }else {
                Map<String, Object> params1 = new HashMap<>();
                params1.put("content", "PRIVATE:trainingHomepage.png"); //remember, this image needs to be created!
                params1.put("timeout", 30);
                Object result1 = driver.executeScript("mobile:checkpoint:image", params1);

                // clicking on hamburger icon, required only for mobile
                driver.findElementByXPath("//*[@class=\"mobile-menu\"]").click();

            }
            reportiumClient.stepEnd();
            reportiumClient.stepStart("location page");
                Map<String, Object> params2 = new HashMap<>();
                params2.put("label", "location");
                params2.put("timeout", 20);
                String res = (String) driver.executeScript("mobile:button-text:click", params2);
            // The location page SOMETIMES asks for approval to share location, in a popup. We need to click allow to approve
                try {
                    driver.findElementByXPath("//*[text()=\"Allow\"]").click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Map<String, Object> params3 = new HashMap<>();
                params3.put("content", "peek");
                params3.put("timeout", 20);
                res = (String) driver.executeScript("mobile:checkpoint:text", params3);


                Map<String, Object> params4 = new HashMap<>();
                params4.put("content", "you are within");
                params4.put("scrolling","scroll");
                res = (String) driver.executeScript("mobile:checkpoint:text", params4);
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