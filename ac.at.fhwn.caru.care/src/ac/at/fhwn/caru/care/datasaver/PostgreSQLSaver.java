package ac.at.fhwn.caru.care.datasaver;

import ac.at.fhwn.caru.care.evaluation.EvaluationResult;
import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.IStayPoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;


public class PostgreSQLSaver implements IDataSaver {
	
	private String dbName;
	private String user;
	private String password;

	public PostgreSQLSaver(String dbName, String user, String password) {
		this.dbName = dbName;
		this.password = password;
		this.user = user;
	}
	
	@Override
	public void storeResult(EvaluationResult result) throws Exception {
		Connection connection = DriverManager.getConnection(this.dbName, this.user, this.password);
		
		for(ICluster ic : result.getClusters()) {
			Statement statement = connection.createStatement();
			statement.execute("INSERT INTO public.\"Cluster\" DEFAULT VALUES;", Statement.RETURN_GENERATED_KEYS);
			ResultSet keyset = statement.getGeneratedKeys();
			if (keyset.next()) {
				int id = keyset.getInt(1);
				for (IStayPoint is : ic.getStayPoints()) {					
					PreparedStatement st = connection.prepareStatement("INSERT INTO public.\"Stay\" (x, y, \"inTime\", \"outTime\", cid) VALUES (?, ?, ?, ?, ?)");
					st.setDouble(1, is.getX());
					st.setDouble(2, is.getY());
					st.setTimestamp(3, Timestamp.valueOf(is.getInTimeStamp()));
					st.setTimestamp(4, Timestamp.valueOf(is.getOutTimeStamp()));
					st.setInt(5, id);
					st.executeUpdate();
					st.close();
				}
			}			
		}
		
		connection.close();
	}

}
