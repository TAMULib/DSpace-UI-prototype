/* 
 * DSpaceIRRepoImpl.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model.repo.impl;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import edu.tamu.app.model.IR;
import edu.tamu.app.model.repo.DSpaceIRRepo;
import edu.tamu.app.model.repo.DSpaceIRRepoCustom;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
public class DSpaceIRRepoImpl implements DSpaceIRRepoCustom {
	
	@Value("${dspace.rest.username}")
	private String username;
	
	@Value("${dspace.rest.password}")
	private String password;

	@Autowired
	private DSpaceIRRepo dSpaceIRRepo;
	
	@Override
	public IR create(String name, URL url){
		IR dSpaceIR = null;
		if(dSpaceIRRepo.getDSpaceIRByUrl(url)==null) {
			dSpaceIR = new IR(name, url);getClass();
			dSpaceIR.authenticateRest(username, password);
			dSpaceIRRepo.save(dSpaceIR);
		}
		return dSpaceIR;
	}

}