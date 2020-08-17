package digital.vix.poller.service;

import java.util.ArrayList;

import digital.vix.poller.models.ServerData;

public class PollService {

	private ArrayList<Poller> pollers = new ArrayList<Poller>();
	private DataService dataService;

	public PollService(DataService dataService) {
		this.dataService = dataService;
	}

	public void startPolling(ServerData serverData) {
		Poller poller = new Poller(serverData, dataService);
		Thread thread = new Thread(poller);
		pollers.add(poller);
		thread.start();
	}

	public void stopPolling(ServerData serverData) {
		for(Poller poller: pollers) {
			if (poller.getServerData().equals(serverData)) {
				poller.setExit(true);
				return;
			}
		}
		throw new RuntimeException("Couldnt find poller for " + serverData.getEndpoint());
	}

	public DataService getDataService() {
		return dataService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

}
