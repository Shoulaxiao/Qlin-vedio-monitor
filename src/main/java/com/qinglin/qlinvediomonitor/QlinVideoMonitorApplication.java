package com.qinglin.qlinvediomonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author shoulaxiao
 */
@SpringBootApplication
@PropertySource(value = {
        "classpath:db.properties",
        "classpath:applicationconf.properties",
})
public class QlinVideoMonitorApplication extends SpringBootServletInitializer implements EnvironmentAware, ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(QlinVideoMonitorApplication.class);

    private Environment environment;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(QlinVideoMonitorApplication.class);
        Log4j2Startup.startUp();
        try {
            springApplication.run(args);
        } catch (Exception e) {
            logger.error("项目启动失败", e);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("===============应用启动成功====================");
        logger.info("-----端口号为：{}", this.environment.getProperty("server.port"));
        logger.info("===============应用启动成功====================");
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        Log4j2Startup.startUp();
        return builder.sources(QlinVideoMonitorApplication.class);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
