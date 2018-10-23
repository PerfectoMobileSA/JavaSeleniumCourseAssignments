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
import java.util.ArrayList;


/**
 * This template is for users that use DigitalZoom Reporting (ReportiumClient).
 * For any other use cases please see the basic template at https://github.com/PerfectoCode/Templates.
 * For more programming samples and updated templates refer to the Perfecto Documentation at: http://developers.perfectomobile.com/
 */
public class Assignment04A_Imageinjection {
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println("Run started");
        
        String browserName = "";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "MYCLOUD.perfectomobile.com";
        capabilities.setCapability("user", "");
        capabilities.setCapability("password", "");
        
        capabilities.setCapability("platformName", "iOS");


        
        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");
        
        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);
        capabilities.setCapability("scriptName", "Assignment04A_Image Injection");
        
    //   AndroidDriver driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
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
            reportiumClient.testStart("Image assignment", new TestContext("assignment", "inject"));
            
            // write your code here


            reportiumClient.stepStart("Prepare - app & image");
                //uploading image to repository
            PerfectoLabUtils.uploadMedia("ps.perfectomobile.com", "yaronw@perfectomobile.com", "Aa562041!", "/Users/yaronw/seek_perfection.jpg", "PRIVATE:perfectoSample.png");
               // close the app in case it is running from previous iteration
                try {
                    Map<String, Object> params2 = null;
                    params2 = new HashMap<>();
                    params2.put("name", "RealTimeFilter");
                    driver.executeScript("mobile:application:close", params2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", "RealTimeFilter");
                    driver.executeScript("mobile:application:open", params);
                    driver.context("NATIVE_APP");
                }catch (Exception e) {
                    Map<String, Object> params = new HashMap<>();

                    params.put("file", "PUBLIC:ImageInjection\\RealTimeFilter.ipa");
                    params.put("sensorInstrument", "sensor");
                    driver.executeScript("mobile:application:install", params);

                    Map<String, Object> params2 = null;
                    params2 = new HashMap<>();
                    params2.put("name", "RealTimeFilter");
                    driver.executeScript("mobile:application:open", params2);

                }
            reportiumClient.stepEnd();
            reportiumClient.stepStart("inject image & validate");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("repositoryFile", "PRIVATE:perfectoSample.png");
            params.put("identifier", "Victor.RealTimeFilter");
            Object res = driver.executeScript("mobile:image.injection:start", params);

            Map<String, Object> pars = new HashMap<>();
            pars.put("state", "landscape");
            String reStr = (String) driver.executeScript("mobile:device:rotate", pars);

            Map<String, Object> params3 = new HashMap<>();
            params3.put("content", "perfection");
            params3.put("timeout", 30);


            String result = (String)driver.executeScript("mobile:checkpoint:text", params3);

            if (result.equalsIgnoreCase("true")) {
               reportiumClient.testStop(TestResultFactory.createSuccess());
            } else {
                // in case of failure we will get the log and fail the test.
                Map<String, Object> pars1 = new HashMap<>();
                pars1.put("tail", 100);
                 driver.executeScript("mobile:device:log", pars1);
                 reportiumClient.reportiumAssert("Failed to view image",false);
                reportiumClient.testStop(TestResultFactory.createFailure("Failed to view image"));
            }
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
