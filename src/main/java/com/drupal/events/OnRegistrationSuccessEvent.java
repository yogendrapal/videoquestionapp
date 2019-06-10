package com.drupal.events;



import org.springframework.context.ApplicationEvent;

public class OnRegistrationSuccessEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	String userId;
	

	public OnRegistrationSuccessEvent(String userId) {
		super(userId);
		this.userId = userId;
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
