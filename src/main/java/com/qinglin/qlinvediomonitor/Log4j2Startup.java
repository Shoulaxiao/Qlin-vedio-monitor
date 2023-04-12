package com.qinglin.qlinvediomonitor;

import org.slf4j.MDC;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname Log4j2Startup
 * @Description
 * @date 2023/4/11 13:56
 */
public class Log4j2Startup {
    private static final String LOG_ROOT = "log.root";

    public static void startUp() {
        Properties prop = new Properties();
        InputStream in = Log4j2Startup.class.getClassLoader().getResourceAsStream("log4j2.properties");
        try {
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MDC.put(LOG_ROOT, prop.getProperty(LOG_ROOT));
    }
}
