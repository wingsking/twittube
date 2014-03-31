package object;
import query.UserQuery;
import aws.*;

public class User {
	private String email;
	private String pwd;
	
	User(String email, String pwd){
		this.email=email;
		this.pwd=pwd;
	}
	
	User(String email){
		this.email = email;
	}
	
	public void setEmail(String email){ this.email=email;}
	public String getEmail(){ return this.email;}
	
	public void setPwd(String pwd){ this.pwd=pwd;}
	public String getPwd(){ return this.pwd;}
	
	//check if password match email
	public static boolean validate(String email, String pwd){
		UserQuery uq = RDSManager.createUserQuery();
		if(pwd.equals(uq.getPassword(email)))
			return true;
		else 
			return false;
	}
}
