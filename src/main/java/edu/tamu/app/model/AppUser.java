/* 
 * AppUser.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import edu.tamu.framework.model.AbstractCoreUserImpl;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Entity
public class AppUser extends AbstractCoreUserImpl {
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	public AppUser() {
		super();
	}
	
	public AppUser(Long uin) {
		super(uin);
	}
	
	/**
	 * 
	 * @return      firstName
	 * 
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param      firstName        String
	 * 
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return      lastName
	 * 
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param      lastName        String
	 * 
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
