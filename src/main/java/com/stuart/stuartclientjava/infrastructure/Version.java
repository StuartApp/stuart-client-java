package com.stuart.stuartclientjava.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {

    public String getCurrent() {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");
        Properties prop = new Properties();
        try {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty("version");
    }
}
