package digital.vix.poller.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtCompleted;

import digital.vix.poller.Configuration;
import digital.vix.poller.models.ServerData;
import digital.vix.poller.utils.ReadConfig;

public class DataService {

	private Connection connection;
	private ReadConfig config;

	public DataService(Connection connection, ReadConfig config) {
		super();
		this.connection = connection;
		this.config = config;
	}

	public List<ServerData> loadServerData() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"select id, name, endpoint, frequency from locations where id in (select location_id from locations_responsibility where poller_id in (select id from pollers where name = ? ))"
				);
		preparedStatement.setString(1, config.getPropertyValue(Configuration.serverName));
		ResultSet rs = preparedStatement.executeQuery();
		ArrayList<ServerData> servers = new ArrayList<ServerData>();
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String endpoint = rs.getString("endpoint");
			int frequency = rs.getInt("frequency");
			servers.add(new ServerData(id, name, endpoint, frequency));
		}
		return servers;
	}
	
	
	public void sendLocationHistory(ServerData serverData, int status, long timeTaken) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
		"insert into locations_history(location_id, poller_id, status, response_time_milliseconds, timedate) Values(?, (SELECT id from pollers where name = ?), ?, ?, ?)" 
		);
		preparedStatement.setInt(1, serverData.getId());
		preparedStatement.setString(2, config.getPropertyValue(Configuration.serverName));
		preparedStatement.setString(3, String.valueOf(status));
		preparedStatement.setLong(4, timeTaken);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		preparedStatement.setString(5, date);
		preparedStatement.execute();
	}
	
	
	public void sendHeartbeat() throws SQLException {
		System.out.println("bump");
		PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT id from pollers where name = ?"
		);
		preparedStatement.setString(1, config.getPropertyValue(Configuration.serverName));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			PreparedStatement updateSql = connection.prepareStatement(
					"UPDATE pollers SET last_heartbeat = ?  where id = ?"
			);
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			String date = sdf.format(new Date());
			updateSql.setString(1, date);
			updateSql.setInt(2, rs.getInt("id"));
			updateSql.execute();
		} else {
			PreparedStatement insertSql = connection.prepareStatement(
					"INSERT INTO pollers(name, last_heartbeat) values( ?, ?)"
			);
			insertSql.setString(1, config.getPropertyValue(Configuration.serverName));
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			String date = sdf.format(new Date());
			insertSql.setString(2, date);
			insertSql.execute();
		}
	}
	
	
}
