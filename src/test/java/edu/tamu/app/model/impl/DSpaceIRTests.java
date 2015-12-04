package edu.tamu.app.model.impl;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.model.IR;
import edu.tamu.app.model.repo.DSpaceIRRepo;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebServerInit.class)
public class DSpaceIRTests {
    
    @Value("${dspace.rest.url}")
    private String dsapceRestUrl;
	
	@Autowired
	private DSpaceIRRepo dSpaceIRRepo;
	
	private IR ir;
	
	@Before
	public void setUp()
	{
		URL url = null;
		String name = "Localhost DSpace Repository";
		
		try {
			url = new URL(dsapceRestUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ir = dSpaceIRRepo.create(name, url);
	}

	@Test
	public void testTestIRRestEndpoint() {
		assertTrue("The REST endpoint was not active!", ir.testIRRestEndpoint());
	}

}
