package com.maqs.moneytracker.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SetupActivityDto {

	private String activity;
	
	private String result;

	private int points;

	private boolean done;

	public SetupActivityDto() {

	}

	public SetupActivityDto(String activity, String result, int points) {
		this.activity = activity;
		this.result = result;
		this.points = points;		
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return getActivity() + " " + getResult();
	}
}
