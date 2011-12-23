package com.salesfloors.aws;

import com.amazonaws.auth.AWSCredentials;

public class AwsConfig implements AWSCredentials {

	protected String aWSAccessKeyId;
	protected String aWSSecretKey;
	protected String bucket;

	public AwsConfig(String aWSAccessKeyId, String aWSSecretKey, String bucket) {
		this.aWSAccessKeyId = aWSAccessKeyId;
		this.aWSSecretKey = aWSSecretKey;
		this.bucket = bucket;
	}

	@Override
	public String getAWSAccessKeyId() {
		return aWSSecretKey;
	}

	@Override
	public String getAWSSecretKey() {
		return aWSAccessKeyId;
	}

	public String getBucket() {
		return bucket;
	}

}
