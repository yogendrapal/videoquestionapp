package com.drupal.models;

import java.util.ArrayList;
import java.util.List;

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
	 * Stores the devices id that the institute is having
	 */
	List<String> devices;

	/**
	 * Stores the status of email verification
	 */
	boolean emailVerified = false;

	/**
	 * @return the list of devices of the institute
	 */
	public List<String> getDevices() {
		return devices;
	}

	/**
	 * Sets the devices of the institute
	 * 
	 * @param devices takes name of the institute
	 */
	public void setDevices(List<String> devices) {
		if(devices==null)
			this.devices = new ArrayList<String>();
		else
			this.devices = devices;
	}

	
	/**
	 * Default constructor
	 */
	public Institute() {
		
	}
	/**
	 * Constructor to set the below parameters for a institute
	 * 
	 * @param name     takes the name of the institute
	 * @param address  takes the address of the institute
	 * @param phone    takes the phone number of the institute
	 * @param password takes the password of the institute
	 * @param email    takes the email of the institute
	 */
	public Institute(String name, String address, String phone, String password, String email, List<String> devices) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.password = password;
		this.email = email;
		if(devices!=null)
			this.devices = devices;
		else
			this.devices = new ArrayList<String>();
	}

	/**
	 * Returns the instituteId of institute
	 * 
	 * @return instituteId
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the instituteId of the institute
	 * 
	 * @param id takes instituteId of the institute
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the the name of this Institute
	 * @return name of this institute
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the institute
	 * 
	 * @param name takes name of the institute
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return address of the institute
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of the institute
	 * 
	 * @param address takes address of the institute
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return status of email verification of the institute
	 */
	public boolean isEmailVerified() {
		return emailVerified;
	}

	/**
	 * Sets the status of emailVerification of the institute
	 * 
	 * @param emailVerified takes status of email verification
	 */
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	/**
	 * @return email of the institute
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the institute
	 * 
	 * @param email takes email of the institute
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets below parameters for the institute
	 * 
	 * @param name     takes name of the institute
	 * @param address  takes address of the institute
	 * @param phone    takes phone number of the institute
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
	 * @return password of the institute
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the institute
	 * 
	 * @param password takes password of the institute
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return phone of the institute
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone number of the institute
	 * 
	 * @param phone takes phone number of the institute
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Returns all the details of the institute
	 */
	@Override
	public String toString() {
		return "Institute [Id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + "]";
	}

	/**
	 * Adds the <b>deviceId</b> to this institute.
	 * 
	 * @param deviceId the new deviceId to be added
	 */
	public void addDevice(String deviceId) {
		this.devices.add(deviceId);
	}
	
	
	/**
	 * Removes the device with <i>deviceId</i> from this institute.
	 * 
	 * @param deviceId the id of the device to be removed
	 */
	void removeDevice(String deviceId) {
		this.devices.remove(deviceId);
	}
}
