package com.salesfloors.aws;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.salesfloors.aws.model.FaceDotComTrainResponse;


public class AwsClientSqsTest {
	private AwsClient aws;
	
	private final String jsonFileName = "sampleFaceDotComJson.txt";
	
	@BeforeClass
	public void classSetup() throws IOException {
		aws = new AwsClient();
	}
	
	@BeforeMethod
	public void methodSetup() throws IOException {
		File json = new File(this.getClass().getClassLoader().getResource(jsonFileName).getPath());
		String msg = FileUtils.readFileToString(json);
		aws.enqueueMessage(msg);
	}
	
	@Test
	public void testEnqueue() {
		// msg has already been enqueued by methodSetup()
		List<Message> msg = null;
		try {
	        ReceiveMessageRequest request = new ReceiveMessageRequest(AwsClient.queueUrl).withMaxNumberOfMessages(1);
			msg = aws.getSqs().receiveMessage(request).getMessages();
			Assert.assertEquals(msg.size(), 1);
			Assert.assertTrue(msg.get(0).getBody().contains("899195206@facebook.com"));
		} finally {
			if (msg != null) {
				aws.getSqs().deleteMessage(new DeleteMessageRequest().withQueueUrl(AwsClient.queueUrl)
					.withReceiptHandle(msg.get(0).getReceiptHandle()));
			}
		}
	}
	
	@Test
	public void testDequeue() throws JsonParseException, JsonMappingException, IOException {
		// msg has already been enqueued by methodSetup()
		/*FaceDotComTrainResponse response = aws.dequeueMessage();
		Assert.assertEquals(response.getStatus(), "success");
		response = aws.dequeueMessage();
		Assert.assertNull(response);*/
	}
	
}
