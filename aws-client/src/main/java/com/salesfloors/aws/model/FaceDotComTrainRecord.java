package com.salesfloors.aws.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class FaceDotComTrainRecord {
    
	private String uid;
	
	@JsonProperty("training_set_size")
    private long trainingSetSize;
	
	@JsonProperty("last_trained")
    private long lastTrained;
    
	@JsonProperty("training_in_progress")
    private boolean trainingInProgress;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public long getTrainingSetSize() {
		return trainingSetSize;
	}

	public void setTrainingSetSize(long trainingSetSize) {
		this.trainingSetSize = trainingSetSize;
	}

	public long getLastTrained() {
		return lastTrained;
	}

	public void setLastTrained(long lastTrained) {
		this.lastTrained = lastTrained;
	}

	public boolean isTrainingInProgress() {
		return trainingInProgress;
	}

	public void setTrainingInProgress(boolean trainingInProgress) {
		this.trainingInProgress = trainingInProgress;
	}
    
}
