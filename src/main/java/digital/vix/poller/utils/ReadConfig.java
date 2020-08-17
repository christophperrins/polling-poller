package digital.vix.poller.utils;

import java.io.IOException;
import java.util.Properties;

public class ReadConfig {

	private String fileLocation = "application.properties";
	private Properties properties = new Properties();

	public ReadConfig() {
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream(fileLocation));
		} catch (IOException e) {
			System.out.println("Check application.properties is found in resources folder");
		}
	}

	public String getPropertyValue(String property) {
		return properties.getProperty(property);
	}

}
