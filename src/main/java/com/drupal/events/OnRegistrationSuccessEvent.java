package com.drupal.events;
import org.springframework.context.ApplicationEvent;
import com.drupal.StudentRestApiApplication.RegistrationTypes;

/**
 * An event which is fired when a new registration occurs.
 * 
 * This event is handled automatically by {@link RegistrationEmailListener}
 * 
 * @author pratik
 * @author henil
 * @author shweta
 * @author sai
 * @author dweekshita
 * @author bhagya
 *
 */
public class OnRegistrationSuccessEvent extends ApplicationEvent {
private static final long serialVersionUID = 1L;

/**
* Stores the userId of user
*/
String userId;
/**
* Stores the type of user that is personal or institute
*/
RegistrationTypes type;

/**
* Sets the userId and type of the user
* 
* @param userId takes userId of the user
* @param type takes the type of the user
*/
public OnRegistrationSuccessEvent(String userId, RegistrationTypes type) {
super(userId);
this.userId = userId;
this.type = type;
}



/**
* @returns the type of the user
*/
public RegistrationTypes getType() {
return type;
}

/**
* Sets the type of the user
* 
* @param type takes the type of the user
*/
public void setType(RegistrationTypes type) {
this.type = type;
}

/**
* @returns the userId of the user
*/
public String getUserId() {
return userId;
}

/**
* Sets the userId of the user
* 
* @param userId takes the userId of the user
*/
public void setUserId(String userId) {
this.userId = userId;
}

}