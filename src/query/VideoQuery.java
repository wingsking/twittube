package query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import object.Video;


public class VideoQuery {
	
	private Statement statement = null;
	
	public VideoQuery(Connection conn) {
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get all videos as ArrayList
	public ArrayList<Video> execQuery(String sql) {
		ArrayList<Video> videos = new ArrayList<Video>();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				Video v = new Video();
				v.setID(rs.getString("id"));
				v.setName(rs.getString("name"));
				v.setUrl(rs.getString("url"));
				v.setComment(rs.getString("comment"));
				v.setTimestamp(rs.getString("timestamp"));
					
				videos.add(v);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return videos;
	}
	
	public void insertVideo(Video v) {
		String sql = "INSERT INTO Video" + "(name, url, email, timestamp, comment) VALUES('"
				+ v.getName() + "', '" + v.getUrl() + "', '" + v.getOwner() + "', now(), '" +
				v.getComment() + "')";
		try{
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeVideo(String id) {
		String sql = "DELETE FROM Video WHERE id=" + id;
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void replyToVideo(Video v, String rid){
		String sql = "INSERT INTO Reply (rid, name, email, url, timestamp, comment) VALUES('" 
				+ rid + "', '" 
				+ v.getName() + "', '"
				+ v.getOwner() + "', '"
				+ v.getUrl() + "', now(), '" 
				+ v.getComment() + "')";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Video> getVideos(){
		String sql = "SELECT * FROM Video ORDER BY timestamp";
		return execQuery(sql);
	}
	
	public ArrayList<Video> getRepliesByID(String id){
		String sql = "SELECT * FROM Reply WHERE rid='" + id + "' ORDER BY timestamp Desc";
		return execQuery(sql);
	}
	
	public ArrayList<Video> getVideoByID(String rid){
		String sql = "SELECT * FROM Video WHERE id='" + rid + "'";
		return execQuery(sql);
	}
	
	public ArrayList<Video> getVideoByOwner(String email){
		String sql1 = "SELECT * FROM Video WHERE Email='" + email + "' ORDER BY timestamp Desc";
		String sql2 = "SELECT * FROM Reply WHERE Email='" + email + "' ORDER BY timestamp Desc";
		ArrayList<Video> userVideos = new ArrayList<Video>();
		userVideos.addAll(execQuery(sql1));
		userVideos.addAll(execQuery(sql2));
		return userVideos;
	}
}
