package object;

public class Video {
	private String name;
	private String owner;
	private String url;
	private String id;
	private String timestamp;
	private String comment;
	private String rid;
	
	public Video(){};
	
	public Video(String id, String name, String email, String url, String comment){
		this.id = id;
		this.name = name;
		this.owner = email;
		this.url = url;
		//this.timestamp = timestamp;
		this.comment = comment;
	}
	
	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; };
	
	public void setOwner(String email) { this.owner = email; }
	public String getOwner() { return this.owner; };
	
	public void setUrl(String url) { this.url=url; }
	public String getUrl() { return this.url; }
	
	public void setID(String id) { this.id=id; }
	public String getID() { return this.id; }
	
	public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
	public String getTimestamp() { return this.timestamp; }
	
	public void setComment(String comment) { this.comment = comment; }
	public String getComment() { return this.comment; }
	
	public void setRID(String rid) { this.rid=rid; }
	public String getRID() { return this.rid; }
}
