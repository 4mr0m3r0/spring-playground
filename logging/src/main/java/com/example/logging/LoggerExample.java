package com.example.logging;

import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggerExample {
    private static final org.apache.commons.logging.Log commonsLoggingLog = org.apache.commons.logging.LogFactory.getLog(LoggerExample.class);

    private static final org.slf4j.Logger slf4JLog =  LoggerFactory.getLogger(LoggerExample.class);

    private static final java.util.logging.Logger javaUtilLog = java.util.logging.Logger.getLogger(LoggerExample.class.getName());

    private static final org.apache.logging.log4j.Logger log4jLog = org.apache.logging.log4j.LogManager.getLogger(LoggerExample.class);

    @PostConstruct
    public void logSomething() {
        commonsLoggingLog.info("Via CommonsLogging");
        slf4JLog.info("Via Slf4J");
        javaUtilLog.info("Via Java Util");
        log4jLog.info("Via Log4j");

        commonsLoggingLog.error("Something bad", new IOException("Comms Down"));
    }
}
