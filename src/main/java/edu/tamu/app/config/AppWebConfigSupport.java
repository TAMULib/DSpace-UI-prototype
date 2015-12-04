/* 
 * AppWebConfigSupport.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import edu.tamu.app.controller.interceptor.AppRestInterceptor;
import edu.tamu.framework.config.CoreWebConfigSupport;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Configuration
public class AppWebConfigSupport extends CoreWebConfigSupport {
    
    @Autowired
    private AppRestInterceptor appRestInterceptor;
    
    @Override
    public Object getRestInterceptor() {
        return appRestInterceptor;
    }

}