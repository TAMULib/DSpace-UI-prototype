/* 
 * UserRepoImpl.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.UserRepoCustom;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
public class UserRepoImpl implements UserRepoCustom {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public AppUser create(Long uin) {
		AppUser user = null;
		if(userRepo.getUserByUin(uin)==null) {
			user = new AppUser(uin);
			userRepo.save(user);
		}
		return user;
	}

}