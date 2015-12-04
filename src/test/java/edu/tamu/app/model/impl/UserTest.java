package edu.tamu.app.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.repo.UserRepo;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebServerInit.class)
public class UserTest {
	
	@Autowired
	private UserRepo userRepo;
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void testCreateAndDelete() {
		
		userRepo.create(123456789l);		
		AppUser testUser1 = userRepo.getUserByUin(123456789l);				
		assertTrue("Test User1 was not added.", testUser1.getUin().equals(123456789l));
		
		userRepo.delete(testUser1);				
		assertEquals("Test User1 was not removed.", 0, userRepo.count());
	}
	
	@Test
	public void testDuplicateUser() {
		
		userRepo.create(123456789l);		
		AppUser testUser1 = userRepo.getUserByUin(123456789l);				
		assertTrue("Test User1 was not added.", testUser1.getUin().equals(123456789l));
		
		userRepo.create(123456789l);
				
		assertEquals("Duplicate UIN found.", 1, userRepo.count());
	}
			
	@After
	public void cleanUp() {
		for(AppUser user : userRepo.findAll())
			userRepo.delete(user);
	}
}
