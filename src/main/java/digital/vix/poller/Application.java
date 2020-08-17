package digital.vix.poller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import digital.vix.poller.models.ServerData;
import digital.vix.poller.service.PollService;
import digital.vix.poller.service.Poller;
import digital.vix.poller.service.SendHeartbeat;
import digital.vix.poller.service.DataService;
import digital.vix.poller.utils.ReadConfig;

public class Application {

	private ReadConfig readConfig;
	private Connection connection;
	private DataService dataService;
	private PollService pollService;

	public Application(ReadConfig readConfig, Connection connection, DataService pullServerDataService,
			PollService pollService) {
		this.readConfig = readConfig;
		this.connection = connection;
		this.dataService = pullServerDataService;
		this.pollService = pollService;
	}

	public void run() throws SQLException, NumberFormatException, InterruptedException {
		new Thread(new SendHeartbeat(dataService, readConfig)).start();
		
		ArrayList<ServerData> old = new ArrayList<ServerData>();
		while (true) {
			ArrayList<ServerData> newData = (ArrayList<ServerData>) dataService.loadServerData();
			for (ServerData serverData: newData) {
//				System.out.println(serverData.toString());
			}
			
			Thread.sleep(Integer.valueOf(readConfig.getPropertyValue(Configuration.milliSecondsBetweenRefresh)));

			stopPollingServerData(old, newData);
			startPollingNewServerData(old, newData);

			old = newData;
		}
	}

	public void stopPollingServerData(ArrayList<ServerData> old, ArrayList<ServerData> newData) {
		HashSet<ServerData> oldClone = new HashSet<ServerData>((List<ServerData>) old.clone());
		oldClone.removeAll(newData);
		for (ServerData serverData : oldClone) {
			System.out.println("Stopping: " + serverData.getEndpoint());
			pollService.stopPolling(serverData);
		}
	}

	public void startPollingNewServerData(ArrayList<ServerData> old, ArrayList<ServerData> newData) {
		HashSet<ServerData> newDataClone = new HashSet<ServerData>((List<ServerData>) newData.clone());
		newDataClone.removeAll(old);
		for (ServerData serverData : newDataClone) {
			System.out.println("startPolling: " + serverData.getEndpoint());
			pollService.startPolling(serverData);
		}
	}

	public ReadConfig getReadConfig() {
		return readConfig;
	}

	public void setReadConfig(ReadConfig readConfig) {
		this.readConfig = readConfig;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
