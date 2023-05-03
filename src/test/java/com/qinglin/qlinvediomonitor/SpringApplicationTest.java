package com.qinglin.qlinvediomonitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SpringApplicationTest
 * @Description
 * @date 2023/5/3 11:47
 */
@Slf4j
public class SpringApplicationTest extends QlinVedioMonitorApplicationTests{


    @Test
    public void readXml() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("haarcascade_frontalface_alt.xml");
        System.out.println(classPathResource.getFile().getAbsolutePath());
    }
}
