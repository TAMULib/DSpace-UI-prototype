/* 
 * AppWebSocketConfig.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import edu.tamu.app.controller.interceptor.AppStompInterceptor;
import edu.tamu.framework.config.CoreWebSocketConfig;

/** 
 * Web Socket Configuration.
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class AppWebSocketConfig extends CoreWebSocketConfig {
	
	/**
	 * Stomp interceptor bean.
	 * 
	 * @return		StompInterceptor
	 * 
	 */
	@Bean
	public AppStompInterceptor appStompInterceptor() {
		return new AppStompInterceptor();
	}
	
	/**
	 * Configure client inbound channel. Sets stomp interceptor. 
	 * 
	 * @param       registration   	ChannelRegistration
	 * 
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(appStompInterceptor());
	}

}

