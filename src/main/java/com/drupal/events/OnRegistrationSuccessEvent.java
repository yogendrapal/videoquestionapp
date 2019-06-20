package com.drupal.events;
import org.springframework.context.ApplicationEvent;
import com.drupal.StudentRestApiApplication.RegistrationTypes;

public class OnRegistrationSuccessEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	String userId;
	RegistrationTypes type;

	public OnRegistrationSuccessEvent(String userId, RegistrationTypes type) {
		super(userId);
		this.userId = userId;
		this.type = type;
	}
	
	
	
	public RegistrationTypes getType() {
		return type;
	}

	public void setType(RegistrationTypes type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
