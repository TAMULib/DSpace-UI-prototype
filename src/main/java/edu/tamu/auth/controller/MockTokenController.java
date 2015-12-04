/* 
 * MockTokenController.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.auth.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.tamu.framework.aspect.annotation.SkipAop;
import edu.tamu.auth.model.jwt.JWTtoken;

/** 
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@RestController
@RequestMapping("/mockauth")
public class MockTokenController {

    @Autowired
    private Environment env;
        
    @Value("${auth.security.jwt.secret-key}")
    private String secret_key;
    
    @Value("${auth.security.jwt.expiration}")
    private Long expiration;
    
    @Value("${shib.keys}")
    private String[] shibKeys;
    
    private static final Logger logger = Logger.getLogger(MockTokenController.class);
    
    /**
     * Token endpoint. Returns a token with credentials from Shibboleth in payload.
     *
     * @param       params          @RequestParam() Map<String,String>
     * @param       headers         @RequestHeader() Map<String,String>
     *
     * @return      ModelAndView
     *
     * @exception   InvalidKeyException
     * @exception   NoSuchAlgorithmException
     * @exception   IllegalStateException
     * @exception   UnsupportedEncodingException
     * @exception   JsonProcessingException
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * 
     */
    @RequestMapping("/token")
    @SkipAop
    public ModelAndView token(@RequestParam() Map<String,String> params, @RequestHeader() Map<String,String> headers) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException, JsonProcessingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        
        String referer = params.get("referer");
        if(referer == null) System.err.println("No referer in header!!");
        
        ModelAndView tokenResponse = null;
        try {
            tokenResponse=  new ModelAndView("redirect:" + referer + "?jwt=" + makeToken(params.get("mock"), headers).getTokenAsString());
        } catch (InvalidKeyException | JsonProcessingException | NoSuchAlgorithmException | IllegalStateException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return tokenResponse;
    }
    
    /**
     * Refresh endpoint. Returns a new token with credentials from Shibboleth in payload.
     *
     * @param       params          @RequestParam() Map<String,String>
     * @param       headers         @RequestHeader() Map<String,String>
     *
     * @return      JWTtoken
     *
     * @exception   InvalidKeyException
     * @exception   NoSuchAlgorithmException
     * @exception   IllegalStateException
     * @exception   UnsupportedEncodingException
     * @exception   JsonProcessingException
     * 
     */
    @RequestMapping("/refresh")
    @SkipAop
    public JWTtoken refresh(@RequestParam() Map<String,String> params, @RequestHeader() Map<String,String> headers) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException, JsonProcessingException {
        return makeToken(params.get("mock"), headers);
    }
    
    /**
     * Constructs a token from selected Shibboleth headers.
     *
     * @param       headers         Map<String, String>
     *
     * @return      JWTtoken
     *
     * @exception   InvalidKeyException
     * @exception   NoSuchAlgorithmException
     * @exception   IllegalStateException
     * @exception   UnsupportedEncodingException
     * @exception   JsonProcessingException
     * 
     */
    @SkipAop
    private JWTtoken makeToken(String mockUser, Map<String, String> headers) throws InvalidKeyException, JsonProcessingException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {       
        if (mockUser == null) {
            mockUser = "justesting";
        }
        logger.info("Creating token for mock " + mockUser);
        
        JWTtoken token = new JWTtoken(secret_key, expiration);
        
        if(mockUser.equals("assumed")) {
            for(String k : shibKeys) {
                String p = headers.get(env.getProperty("shib."+k, ""));
                token.makeClaim(k, p);
                logger.info("Adding " + k +": " + p + " to JWT.");
            }
        }
        else if(mockUser.equals("admin")) {
            token.makeClaim("netid", "aggieJack");
            token.makeClaim("uin", "123456789");
            token.makeClaim("lastName", "Daniel");
            token.makeClaim("firstName", "Jack");
            token.makeClaim("email", "aggieJack@tamu.edu");
        }
        else {
            token.makeClaim("netid", "bobBoring");
            token.makeClaim("uin", "987654321");
            token.makeClaim("lastName", "Boring");
            token.makeClaim("firstName", "Bob");
            token.makeClaim("email", "bobBoring@tamu.edu");
        }
        
        return token;       
    }

}