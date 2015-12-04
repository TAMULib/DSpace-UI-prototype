/* 
 * UserRepo.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.tamu.app.model.AppUser;

/**
 * User repository.
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Repository
public interface UserRepo extends JpaRepository <AppUser, Long>, UserRepoCustom {
	
	/**
	 * Retrieve user by UIN.
	 * 
	 * @param 		uin				Long
	 * 
	 * @return		UserImpl
	 * 
	 */
	public AppUser getUserByUin(Long uin);

}
