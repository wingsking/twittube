package aws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.GetTopicAttributesRequest;
import com.amazonaws.services.sns.model.ListTopicsRequest;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Topic;

import config.Config;

/**
 * This is a utility class to manage SNS communication for entries and
 * comments.  It's largely responsible for taking the entities passed in
 * to the methods, breaking them down into SNS requests, and then
 * sending those requests to the SNS client.
 *
 * We do not have support here for unsubscribing from a topic.  SNS
 * automatically handles the process of confirming a user's desire
 * to be subscribed.  Each message sent also includes an unsubscribe
 * link.  So this process can largely be dealt with automatically.
 *
 */
public class SNSManager  {

	//SNS can support a variety of protocols, but in this case we only need email
	private static final String EMAIL_PROTOCOL="email";

	private static Logger logger = Logger.getLogger(SNSManager.class.getName());

	/*
	 * The SNS client class is thread safe so we only ever need one static instance.
	 * While you can have multiple instances it is better to only have one because it's
	 * a relatively heavy weight class.
	 */
	private static AmazonSNSClient snsClient;
	private String topicArn; 

	static {
		AWSCredentials creds = new BasicAWSCredentials(getKey(), getSecret());
		snsClient = new AmazonSNSClient(creds);
	}


	/**
	 * Creates the SNS topic associated with an entry.  When the topic is created,
	 * we will get an ARN (Amazon Resource Name) which uniquely identifies the
	 * SNS topic.  We write that ARN to the entry entity so that we can refer to it
	 * later when subscribing commenters, etc.
	 *
	 * @param entry the new entry that's associated with the topic
	 * @return the result returned from AWS
	 */
	public CreateTopicResult createTopic () {
		CreateTopicRequest request = new CreateTopicRequest("NewMsg");
		CreateTopicResult result = snsClient.createTopic(request);
		topicArn = result.getTopicArn();
		System.out.println("------Create Topic------");
		System.out.println(topicArn);
		return result;
	}

	/**
	 * Deletes a previously created topic associated with the entry.
	 *
	 * @param entry the entry to be deleted
	 */
//	public void deleteTopic (Entry entry) {
//		DeleteTopicRequest request = new DeleteTopicRequest(entry.getSnsArn());
//		snsClient.deleteTopic(request);
//	}


	/**
	 * Publishes a comment to the specified entry. The method takes the comment and
	 * builds an SNS PublishRequest object.  Then the comment is published to the topic associated
	 * with the incoming entry.
	 *
	 * @param entry the entry to publish to
	 * @param comment the comment to publish
	 * @return the result returned from AWS
	 */
	public PublishResult publish () {
		snsClient.setEndpoint("https://sns.us-west-2.amazonaws.com");
		
		List<Topic> topics = new ArrayList<Topic>();
		String nextToken = null;
		do {
			ListTopicsRequest req = new ListTopicsRequest();
			if (nextToken != null) req = req.withNextToken(nextToken);
			
			ListTopicsResult result = snsClient.listTopics(req);
			
			nextToken = result.getNextToken();
			topics.addAll(result.getTopics());
		} while (nextToken != null);
		for (Topic topic : topics) {
			System.out.println(topic.toString());
		}
		
		topicArn = topics.get(0).getTopicArn();
		
		PublishRequest request = new PublishRequest(topicArn, 
				"Hello User, we have new broadcasted video!", 
				"New video broadcasted");
//		request.setTopicArn();
		
//		System.out.println("----------Published----------");
		
//		System.out.println(request.getTopicArn());
		
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
		
		System.out.println("----------Published----------");

		return snsClient.publish(request);
	}

	/**
	 * Subscribe a given commenter to future comments posted to the given entry.
	 *
	 * @param entry the entry to subscribe the commenter to
	 * @param commenter the commenter to be subscribed
	 * @return the result returned by AWS
	 */
	public SubscribeResult subscribe (String email) {
//		if (StringUtils.isEmpty(entry.getSnsArn())) {
//			//If ARN isn't set then entry didn't have an SNS topic created with it so we ignore
//			logger.log(Level.WARNING,"Entry did not have an SNS topic associated with it");
//			return null;
//		}
		snsClient.setEndpoint("https://sns.us-west-2.amazonaws.com");
		
		List<Topic> topics = new ArrayList<Topic>();
		String nextToken = null;
		do {
			ListTopicsRequest req = new ListTopicsRequest();
			if (nextToken != null) req = req.withNextToken(nextToken);
			
			ListTopicsResult result = snsClient.listTopics(req);
			
			nextToken = result.getNextToken();
			topics.addAll(result.getTopics());
		} while (nextToken != null);
		for (Topic topic : topics) {
			System.out.println(topic.toString());
		}
		
		topicArn = topics.get(0).getTopicArn();
		
		SubscribeResult result = snsClient.subscribe(topicArn, EMAIL_PROTOCOL, email);
		return result;
	}

//	/**
//	 * This method returns a unique topic name by using the entry id.
//	 * @param entry the entry to get a topic name for
//	 * @return returns the topic name
//	 */
//	private String getTopicName (Entry entry) {
//		return "entry" + StageUtils.getResourceSuffixForCurrentStage() + "-" + entry.getId();
//	}

	public static String getKey () {
		return Config.getConfigByName("accessKey");
	}

	public static String getSecret () {
		return Config.getConfigByName("secretKey");
	}
}
