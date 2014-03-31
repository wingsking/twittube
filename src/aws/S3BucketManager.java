package aws;

import config.*;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3BucketManager {
	static private AmazonS3Client s3 = null;
	static private String bucket_name = Config.getConfigByName("bucket");
	static private String folder = Config.getConfigByName("folder");
	
	static {
		AWSCredentials credentials = null;
		try {
			credentials = new PropertiesCredentials(
	    			 S3BucketManager.class.getResourceAsStream("/config/config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s3  = new AmazonS3Client(credentials);
		
	}
	/*
	public S3BucketManager(AmazonS3Client s3Instance, String bucketName) {
		s3 = s3Instance;
		bucket_name = bucketName;
	}
	*/
	public void createBucket() {
		//create bucket
		s3.createBucket(bucket_name);
	}
	
	public String upload(String filepath) {		
		String name="";
		try {
			//put object - bucket, key, value(file)
			System.out.println("Putting object on S3" + filepath);
	
//			Random r = new Random();
			File file = new File(filepath);
			name = file.getName();
			String tmp = name;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
			Date date = new Date();
			name = dateFormat.format(date) + name;
			String key = folder+name;
			
			//File file = new File(name);
			//String key = folder+file.getName();
			s3.putObject(new PutObjectRequest(bucket_name, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
			System.out.println("Log: " + tmp + " is saved as " + bucket_name + "/" + key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public void delVedioByUrl(String url) {
		String key = folder+url;
		s3.deleteObject(bucket_name, key);
		System.out.println("INFO: Delete file" + url + " from S3");
	}

}