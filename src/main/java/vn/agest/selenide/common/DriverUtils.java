package vn.agest.selenide.common;

import com.codeborne.selenide.Selenide;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;

@Log4j
public class DriverUtils {

    public static void waitToLoadPage() {
        try {
            Selenide.Wait().until(driver ->
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
            );
        } catch (TimeoutException e) {
            log.info("Page load timeout");
            throw new RuntimeException("Page load timeout", e);
        }
    }
}
