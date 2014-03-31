import java.util.ArrayList;

import aws.*;
import object.*;
import query.*;

public class testS3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//S3BucketManager s3 = new S3BucketManager();
		//s3.upload("bunny.flv");
		
		//test mysql, insert, and get videos and get replies
		//Video v = new Video("funfunfun","asd","as", "good good");
		VideoQuery vq = RDSManager.createVideoQuery();
		//vq.replyToVideo(v, "1");
		ArrayList<Video> vs = vq.getVideos();
		for(Video vv:vs)
			System.out.println(vv.getID());
	}

}
