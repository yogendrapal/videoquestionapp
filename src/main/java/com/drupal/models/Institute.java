package com.drupal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Institute {

/**
* Stores the instituteId of institute
*/
@Id
String id;

/**
* Stores the name of institute
*/
String name;

/**
* Stores the address of institute
*/
String address;

/**
* Stores the phone number of institute
*/
String phone;

/**
* Stores the password of institute
*/
String password;

/**
* Stores the email of institute
*/
String email;

/**
* Stores the status of email verification
*/
boolean emailVerified = false;


/**
* Constructor to set the below parameters for a institute
* 
* @param name takes the name of the institute
* @param address takes the address of the institute
* @param phone takes the phone number of the institute
* @param password takes the password of the institute
* @param email takes the email of the institute
*/
public Institute(String name, String address, String phone, String password, String email) {
super();
this.name = name;
this.address = address;
this.phone = phone;
this.password = password;
this.email = email;
}

/**
* Returns the instituteId of institute
* @returns instituteId
*/
public String getId() {
return id;
}

/**
* Sets the instituteId of the institute
* @param id takes instituteId of the institute
*/
public void setId(String id) {
this.id = id;
}

/**
* @returns name of the institute
*/
public String getName() {
return name;
}

/**
* Sets the name of the institute
* @param name takes name of the institute
*/
public void setName(String name) {
this.name = name;
}

/**
* @returns address of the institute
*/
public String getAddress() {
return address;
}

/**
* Sets the address of the institute
* @param address takes address of the institute
*/
public void setAddress(String address) {
this.address = address;
}



/**
* @returns status of email verification of the institute
*/
public boolean isEmailVerified() {
return emailVerified;
}


/**
*  Sets the status of emailVerification of the institute
* @param emailVerified takes status of email verification
*/
public void setEmailVerified(boolean emailVerified) {
this.emailVerified = emailVerified;
}


/**
* @returns email of the institute
*/
public String getEmail() {
return email;
}

/**
*  Sets the email of the institute
* @param email takes email of the institute
*/
public void setEmail(String email) {
this.email = email;
}

/**
* Sets below parameters for the institute
* 
* @param name takes name of the institute
* @param address takes address of the institute
* @param phone takes phone number of the institute
* @param password takes password of the institute
*/
public Institute(String name, String address, String phone, String password) {
super();
this.name = name;
this.address = address;
this.phone = phone;
this.password = password;
}

/**
* @returns password of the institute
*/
public String getPassword() {
return password;
}

/**
*  Sets the password of the institute
* @param password takes password of the institute
*/
public void setPassword(String password) {
this.password = password;
}

/**
* @returns phone of the institute
*/
public String getPhone() {
return phone;
}

/**
* Sets the phone number of the institute
* @param phone takes phone number of the institute
*/
public void setPhone(String phone) {
this.phone = phone;
}

/**
*Returns all the details of the institute
*/
@Override
public String toString() {
return "Institute [Id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + "]";
}



}
