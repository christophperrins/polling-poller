package digital.vix.poller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;

import digital.vix.poller.models.ServerData;

public class Poller implements Runnable {

	private volatile boolean exit = false;

	private ServerData serverData;
	private DataService dataService;

	public Poller(ServerData serverData, DataService dataService) {
		this.serverData = serverData;
		this.dataService = dataService;
	}

	public void run() {
		while (!exit) {
			try {
				Thread.sleep(serverData.getFrequency());
				URL url = new URL(serverData.getEndpoint());
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("accept", "*/*");
				con.setConnectTimeout(30000);
				con.setReadTimeout(30000);
				con.setInstanceFollowRedirects(true);
				long startTime = System.currentTimeMillis();
				con.connect();
				long endTime = System.currentTimeMillis();
				long time= endTime-startTime;
				int statusCode = con.getResponseCode();
				con.disconnect();
				dataService.sendLocationHistory(serverData, statusCode, time);
				System.out.println(serverData.getEndpoint() + " " + time +"ms" +" " + statusCode);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}		
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public ServerData getServerData() {
		return serverData;
	}

	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}

	public DataService getDataService() {
		return dataService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}
	
	
	

}
