package com.passivecaptcha.bot.bota.util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ThreadLocalRandom;

public class VariantEnvironment implements BrowserEnvironment {

    private static final int[][] RESOLUTIONS = {

            {1920,1080},
            {1600,900},
            {1536,864},
            {1440,900},
            {1366,768},
            {1280,720}

    };

    private static final String[] ZOOM_LEVELS = {

            "90%",
            "100%",
            "110%"

    };

    @Override
    public void configure(WebDriver driver) {

        // Random Resolution
        int[] resolution =
                RESOLUTIONS[
                        ThreadLocalRandom.current()
                                .nextInt(RESOLUTIONS.length)
                ];

        driver.manage().window().setSize(
                new Dimension(
                        resolution[0],
                        resolution[1]
                )
        );

        // Random Window Position
        driver.manage().window().setPosition(
                new Point(
                        random(0,150),
                        random(0,80)
                )
        );

        // Browser Startup Delay
        sleep(random(1000,3000));

        // Random Zoom
        String zoom =
                ZOOM_LEVELS[
                        ThreadLocalRandom.current()
                                .nextInt(ZOOM_LEVELS.length)
                ];

        ((JavascriptExecutor)driver)
                .executeScript(
                        "document.body.style.zoom='" + zoom + "';"
                );

        // Small idle after configuration
        sleep(random(500,1500));
    }

    private int random(int min,int max){

        return ThreadLocalRandom.current()
                .nextInt(min,max+1);

    }

    private void sleep(int millis){

        try{

            Thread.sleep(millis);

        }catch(InterruptedException e){

            Thread.currentThread().interrupt();

        }

    }

}