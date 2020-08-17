package digital.vix.poller.service;

import java.sql.SQLException;

import digital.vix.poller.Configuration;
import digital.vix.poller.utils.ReadConfig;

public class SendHeartbeat implements Runnable {

	private DataService dataService;
	private ReadConfig config;

	public SendHeartbeat(DataService dataService, ReadConfig config) {
		super();
		this.dataService = dataService;
		this.config = config;
	}

	public void run() {
		while (true) {
			try {
				dataService.sendHeartbeat();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(Integer.parseInt(config.getPropertyValue(Configuration.heartbeatRefresh)));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public DataService getDataService() {
		return dataService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

}
