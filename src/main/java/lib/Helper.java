package lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import com.google.gson.Gson;
import org.openqa.selenium.Cookie;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helper extends FluentPage {


    private static ThreadLocal<WebDriver> localDriver = new ThreadLocal<>();
    private static ThreadLocal<Actions> localActions = new ThreadLocal<>();
    private static ThreadLocal<JavascriptExecutor> localJs = new ThreadLocal<>();


    public static void initialize(WebDriver driver){
        localDriver.set(driver);
        localActions.set(new Actions(driver));
        localJs.set((JavascriptExecutor)driver);
    }

    public static void mouseOver(WebElement element){
        localActions.get().moveToElement(element).build().perform();
    }

    public static void doubleClick(WebElement element) {
        localActions.get().doubleClick(element).perform();
    }



    public static  void keyStrokeAltArrowDown() {
        localActions.get().sendKeys(Keys.LEFT_ALT, Keys.ARROW_DOWN).perform();
    }
    public static void keyStrokeAltArrowUp() {
        localActions.get().sendKeys(Keys.chord(Keys.ALT, Keys.ARROW_UP)).perform();
    }
    public static void keyStrokeAltRightArrow() {
        localActions.get().sendKeys(Keys.LEFT_ALT, Keys.ARROW_RIGHT).perform();
    }
    public static void keyStrokeAltLeftArrow() {
        localActions.get().sendKeys(Keys.LEFT_ALT, Keys.ARROW_LEFT).perform();
    }

    public static void executeJavascript(String script,Object... args) {
        localJs.get().executeScript(script, args);

    }

    public static Object executeJavascriptAndReturnObject(String script,Object... args) {
        return localJs.get().executeScript(script,args);
    }

    public static void tearDown(){
        localActions.remove();
        localDriver.remove();

        localJs.remove();
    }


    public static String getScreenShot(String testName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot screenshot = (TakesScreenshot) localDriver.get();
        byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
        String destination = "./target/screenshots/" + testName + "_" + dateName + ".png";
        File dest = new File(destination);
        dest.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(screenshotBytes);
        }
        System.out.println("Screenshot saved at: " + destination + " | Exists: " + dest.exists());
        return destination;
    }


    public static String extractJsLogs()
    {
        String logs="";
        LogEntries logEntries = localDriver.get().manage().logs().get(LogType.BROWSER);
        for(LogEntry entry:logEntries)
        {
            logs=logs+new Date(entry.getTimestamp())+":"+entry.getMessage();
        }
        return logs;

    }

    public static  String [] trimElements(String [] elements)
    {
        for(int i=0;i<elements.length;i++)
        {
            elements[i]=elements[i].trim();
        }

        return elements;
    }

    public static boolean compareLists(List<String> a, List<String> b)
    {
        if(a.size()!=b.size())
            return false;

        List<String> copy1=new ArrayList<>(a);
        List<String> copy2=new ArrayList<>(b);
        Collections.sort(copy1);
        Collections.sort(copy2);

        return copy1.equals(copy2);
    }

    public static String DaysAfterToday(int days)
    {
        String twoDaysAfterDateToday="";
        Date date=new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR,days);

        Date after=c.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy h:MM aa");
        twoDaysAfterDateToday=formatter.format(after);

        return  twoDaysAfterDateToday;

    }

    public static boolean compareDate(String date1,String date2)
    {

        try {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(new SimpleDateFormat("d MMM yyyy h:M aa").parse(date1));
            c2.setTime(new SimpleDateFormat("d MMM yyyy h:M aa").parse(date2));
            return c1.equals(c2);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void scrollIntotop()
    {
        localJs.get().executeScript("window.scrollTo(document.body.scrollHeight,0)");
    }

    public static void scrollDown() {
        localJs.get().executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }



    public static HashMap<String,Object> convertJsonToHashMap(String json)
    {
        Type type=new TypeToken<HashMap<String,Object>>(){}.getType();
        try {
            return new Gson().fromJson(json, type);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void jsClick(FluentWebElement element)
    {
        WebElement webElement=element.getElement();
        String query = "var evObj = document.createEvent('Events'); evObj.initEvent('click', true, false); arguments[0].dispatchEvent(evObj);";
        localJs.get().executeScript(query, webElement);
    }

    public static Map<String,String> getCookiesAsMap(){

        Map<String,String> cookies=new HashMap<>();
        for(Cookie cookie:localDriver.get().manage().getCookies()){
            cookies.put(cookie.getName(),cookie.getValue());
        }
        return cookies;
    }

    /**
     * Save all cookies from the current WebDriver session to a JSON file.
     */
    public static void saveCookiesToFile(WebDriver driver, String filePath) {
        Set<Cookie> cookies = driver.manage().getCookies();
        try {
            String json = new Gson().toJson(cookies);
            Files.write(Paths.get(filePath), json.getBytes());
            System.out.println("[Helper] Cookies saved to: " + filePath);
        } catch (Exception e) {
            System.err.println("[Helper] Failed to save cookies: " + e.getMessage());
        }
    }

    /**
     * Restore cookies from a JSON file to the current WebDriver session.
     * Returns true if successful, false otherwise.
     */
    public static boolean restoreCookiesFromFile(WebDriver driver, String filePath, String domainUrl) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                System.err.println("[Helper] Cookies file not found: " + filePath);
                return false;
            }
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            Set<Cookie> cookies = new Gson().fromJson(json, new com.google.gson.reflect.TypeToken<Set<Cookie>>(){}.getType());
            driver.get(domainUrl); // Must be on the domain before adding cookies
            for (Cookie cookie : cookies) {
                try {
                    driver.manage().addCookie(cookie);
                } catch (Exception e) {
                    System.err.println("[Helper] Failed to add cookie: " + cookie.getName() + ", " + e.getMessage());
                }
            }
            System.out.println("[Helper] Cookies restored from: " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("[Helper] Failed to restore cookies: " + e.getMessage());
            return false;
        }
    }


}
