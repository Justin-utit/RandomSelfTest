package com.japanese.selftest;

import java.io.IOException;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * @author Kiran
 *
 */
public class MyLogger {

    public MyLogger() {
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("MyLog");
        Appender fh = null;
        try {
            fh = new FileAppender(new SimpleLayout(), "MyLogFile.log");
            logger.addAppender(fh);
            fh.setLayout(new SimpleLayout());
            logger.info("My first log");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Hi How r u?");
    }
}