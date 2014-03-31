package aws;

import java.sql.Connection;
import java.sql.DriverManager;

import config.*;
import query.*;

public class RDSManager {
	public static final String ADDR;
	public static final String DB;
	public static final String USER;
	public static final String PWD;
	
	private static Connection conn=null;
	
	static {	
		ADDR = Config.getConfigByName("addr");
		DB = Config.getConfigByName("db");
		USER = Config.getConfigByName("user");
		PWD = Config.getConfigByName("pwd");
	}
	
	private static Connection createConnection() {
		//Connection conn = null;
		try {
				String url = "jdbc:mysql://" + ADDR + "/" 
		                   + DB + "?user=" + USER +"&password=" + PWD;
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            System.out.println("INFO: Data connection is " + url);
	            conn =DriverManager.getConnection(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}
	
	//return a VideoQuery, which can directly execute queries regarding videos
	public static VideoQuery createVideoQuery(){
		if(conn==null) {
			conn = createConnection();
			if(conn==null)
				System.err.println("ERRO: Fail to connect database!");
		}
		return new VideoQuery(conn);
	}
	
	
	public static UserQuery createUserQuery(){
		if(conn==null) {
			conn = createConnection();
			if(conn==null)
				System.err.println("ERRO: Fail to connect database!");
		}
		return new UserQuery(conn);
	}
	
}
