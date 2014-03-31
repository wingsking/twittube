package query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class UserQuery {
	
	private Statement statement = null;
	
	public UserQuery(Connection conn) {
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertUser(String email, String pwd){
		String sql = "INSERT INTO User" + "(Email, Password) VALUES('"
				+ email + "', '" + pwd + "')";
		try{
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPassword(String email){
		String sql = "SELECT password FROM User WHERE email='" + email + "';";
		String password = null;
		ResultSet rs = null;
		
		try{
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				password = rs.getString("password");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return password;
	}

}

