import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.appium.java_client.android.AndroidDriver;

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
public class Assignment04B_Youtube {
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");
        
        String browserName = "";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "MYCLOUD.perfectomobile.com";
        capabilities.setCapability("user", "");
        capabilities.setCapability("password", "");
        
        capabilities.setCapability("platformName", "Android");


        
        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");
        
        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);
        capabilities.setCapability("scriptName", "Assignment04_YouTube");
        
       AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
//      IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
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
            reportiumClient.testStart("YouTube assignment", new TestContext("assignment", "youtube"));
            
            // write your code here


            reportiumClient.stepStart("Open You Tube");

               // close the app in case it is running from previous iteration
                try {
                    Map<String, Object> params2 = null;
                    params2 = new HashMap<>();
                    params2.put("name", "YouTube");
                    driver.executeScript("mobile:application:close", params2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Map<String, Object> params = new HashMap<>();
                params.put("name", "YouTube");
                driver.executeScript("mobile:application:open", params);
                driver.context("NATIVE_APP");
                 maxVolume(driver);
                 Thread.sleep(3000);
            reportiumClient.stepEnd();
            reportiumClient.stepStart("Navigate to audio search");
                driver.findElementByXPath("//*[@content-desc=\"Search\"]").click();
                driver.findElementByXPath("//*[@resource-id=\"com.google.android.youtube:id/voice_search\"]").click();
                reportiumClient.stepStart("Run Audio Search");
                // Create audio file from String
                String key = "PRIVATE:mysong.wav";
                 Map<String, Object> params6 = new HashMap<>();
                 params6.put("text","Metallica Master of Puppets");
                 params6.put("repositoryFile",key);
                driver.executeScript("mobile:text:audio", params6);
                // inject Audio to device
                Map<String, Object> params1 = new HashMap<>();
                params1.put("key",key);
                params1.put("wait","wait");
                 driver.executeScript("mobile:audio:inject", params1);

                 driver.findElementByXPath("//*[@content-desc=\"Play album\"]").click();

                // Play Audio and validate

                Map<String, Object> params4 = new HashMap<>();
                params4.put("timeout", "30");
                params4.put("duration","1");
                String audioR = (String) driver.executeScript("mobile:checkpoint:audio", params4);

                Map<String, Object> params2 = null;
                params2 = new HashMap<>();
                params2.put("name", "YouTube");
                driver.executeScript("mobile:application:close", params2);


            if(audioR.equalsIgnoreCase("true")) {
                reportiumClient.reportiumAssert("Audio is playing",true);
                reportiumClient.testStop(TestResultFactory.createSuccess());

            } else {
                reportiumClient.reportiumAssert("Audio failed",false);
                reportiumClient.testStop(TestResultFactory.createFailure("Audio failed"));
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



    private static void maxVolume(RemoteWebDriver driver) {
        int i = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("keySequence", "VOL_UP");
        for (i = 0; i < 12; i++)
            driver.executeScript("mobile:presskey", params);
    }
    
}
