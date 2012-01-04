package com.salesfloors.aws.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class FaceDotComUsage {
	
    private int remaining;
    
    private int limit;
    
    @JsonProperty("reset_time_text")
    private String resetTimeText;
    
    @JsonProperty("reset_time")
    private String resetTime;

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getResetTimeText() {
		return resetTimeText;
	}

	public void setResetTimeText(String resetTimeText) {
		this.resetTimeText = resetTimeText;
	}

	public String getResetTime() {
		return resetTime;
	}

	public void setResetTime(String resetTime) {
		this.resetTime = resetTime;
	}
    
}
