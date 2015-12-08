/* 
 * AppContextInitializedHandler.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.tamu.app.SSLUtil;
import edu.tamu.framework.CoreContextInitializedHandler;
import edu.tamu.framework.model.repo.SymlinkRepo;

/** 
 * Handler for when the servlet context refreshes.
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Component
@EnableConfigurationProperties(SymlinkRepo.class)
class AppContextInitializedHandler extends CoreContextInitializedHandler {

    @Autowired
    ApplicationContext applicationContext;
    
    @Override
    protected void before(ContextRefreshedEvent event) {

    }

    @Override
    protected void after(ContextRefreshedEvent event) { 
        
        // use when ssl not available
    	try {
    		SSLUtil.turnOffSslChecking();    	
    	}
    	catch(Exception e) {
    		System.out.println("\nUnable to disable SSL checking.\n");
    	}
    }
}
