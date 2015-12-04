/* 
 * Wro4jCustomXmlModelManagerFactory.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package wro4jBoot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
public class Wro4jCustomXmlModelManagerFactory extends ConfigurableWroManagerFactory {
    
	private static final Logger log = LoggerFactory.getLogger(Wro4jCustomXmlModelManagerFactory.class);
	
	final private Properties props;

	public Wro4jCustomXmlModelManagerFactory(Properties props) {
		this.props = props;
	}
	
	@Override
	protected Properties newConfigProperties() {
		return props;
	}

	@Override
	protected WroModelFactory newModelFactory() {
		log.debug("loading from /wro.xml");
		return new XmlModelFactory() {
			
		    @Override
			protected InputStream getModelResourceAsStream() throws IOException {
				
			    String resourceLocation = "/wro.xml";
				
			    log.info("Loading resource {}", resourceLocation);
				
				final InputStream stream = getClass().getResourceAsStream(resourceLocation);

				if (stream == null) {
					throw new IOException("Invalid resource requested: " + resourceLocation);
				}

				return stream;
			}
		    
		};
	}

}
