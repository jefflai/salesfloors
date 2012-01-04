package com.salesfloors.aws.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FaceDotComTrainResponse {

    @JsonProperty("no_training_set")
	private List<FaceDotComTrainRecord> noTrainingSet;

	private List<FaceDotComTrainRecord> unchanged;

	private List<FaceDotComTrainRecord> updated;

	private String status;

	private FaceDotComUsage usage;

	public List<FaceDotComTrainRecord> getNoTrainingSet() {
		return noTrainingSet;
	}

	public void setNoTrainingSet(List<FaceDotComTrainRecord> noTrainingSet) {
		this.noTrainingSet = noTrainingSet;
	}

	public List<FaceDotComTrainRecord> getUnchanged() {
		return unchanged;
	}

	public void setUnchanged(List<FaceDotComTrainRecord> unchanged) {
		this.unchanged = unchanged;
	}

	public List<FaceDotComTrainRecord> getUpdated() {
		return updated;
	}

	public void setUpdated(List<FaceDotComTrainRecord> updated) {
		this.updated = updated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FaceDotComUsage getUsage() {
		return usage;
	}

	public void setUsage(FaceDotComUsage usage) {
		this.usage = usage;
	}

}
