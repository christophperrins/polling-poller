package digital.vix.poller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import digital.vix.poller.service.DataService;
import digital.vix.poller.service.PollService;
import digital.vix.poller.utils.ReadConfig;

public class Runner {

	public static void main(String[] args) throws InterruptedException, NumberFormatException, SQLException {
		System.out.println("Loading...");
		ReadConfig config = new ReadConfig();
		String url = config.getPropertyValue(Configuration.databaseUrl);
		String user = config.getPropertyValue(Configuration.databaseUsername);
		String password = config.getPropertyValue(Configuration.databasePassword);

		Connection connection = DriverManager.getConnection(url, user, password);
		
		DataService dataService = new DataService(connection, config);
		
		PollService pollService = new PollService(dataService);
		
		Application application = new Application(config, connection, dataService, pollService);
		application.run();
		
	}
		
}
