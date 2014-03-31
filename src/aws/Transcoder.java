package aws;

import java.util.logging.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.datapipeline.model.ListPipelinesRequest;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CreateJobOutput;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.JobInput;
import com.amazonaws.services.elastictranscoder.model.JobOutput;
import com.amazonaws.services.elastictranscoder.model.ReadPipelineRequest;
import com.amazonaws.services.elastictranscoder.model.ReadPipelineResult;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import config.Config;

public class Transcoder {

	private static AmazonElasticTranscoderClient transcoderClient;

	static {
		AWSCredentials creds = new BasicAWSCredentials(getKey(), getSecret());
		transcoderClient = new AmazonElasticTranscoderClient(creds);
	}
	
	/**
	 * Publishes a comment to the specified entry. The method takes the comment and
	 * builds an SNS PublishRequest object.  Then the comment is published to the topic associated
	 * with the incoming entry.
	 *
	 * @param entry the entry to publish to
	 * @param comment the comment to publish
	 * @return the result returned from AWS
	 */
	public void transcode (String oriVideoName, String userAgent) {
		transcoderClient.setEndpoint("https://elastictranscoder.us-west-2.amazonaws.com");
//		transcoderClient.setRegion(new com.amazonaws.regions.Region("us-west-2");
		
		ListPipelinesRequest req = new ListPipelinesRequest();
		
		ReadPipelineResult result = transcoderClient.readPipeline(new ReadPipelineRequest().withId("1394780003026-tkg787"));
		System.out.println(result.toString());
		
		String presetId = null;
		if (userAgent.equals("iphone")) {
			presetId = "1351620000001-100020";
		} else if (userAgent.equals("mobile")) {
			presetId = "1351620000001-000061";
		} else {
			System.out.println("sth wrong here");
		}
		
		CreateJobRequest request = new CreateJobRequest()
									.withPipelineId("1394780003026-tkg787")
									.withInput(new JobInput()
													.withKey("videos/" + oriVideoName))
									.withOutputKeyPrefix("videos/")
									.withOutput(new CreateJobOutput()
													.withKey(userAgent + "-" + oriVideoName + ".mp4")
													.withPresetId(presetId));
		
		transcoderClient.createJob(request);

		System.out.println("-------Transcoded-------");
		
//		StringBuilder subject = new StringBuilder("New video broadcasted.");
////		subject.append(entry.getTitle()).append("'");
//		request.setSubject(subject.toString());
//
//		StringBuilder body = new StringBuilder("This is the body part.");
////		body.append("The following comment was posted to the post '").append(entry.getTitle()).append("'\n");
////		body.append("Posted by: ").append(comment.getCommenter().getName()).append("\n\n");
////		body.append(comment.getBody());
//
//
//		request.setMessage(body.toString());

	}
	
	public static String getKey () {
		return Config.getConfigByName("accessKey");
	}

	public static String getSecret () {
		return Config.getConfigByName("secretKey");
	}
}
