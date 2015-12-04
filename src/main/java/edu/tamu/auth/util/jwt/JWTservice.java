/* 
 * JWTservice.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.auth.util.jwt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString;;

/** 
 * JSON Web Token service.
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
public class  JWTservice {
	
	/**
	 * Constructor.
	 *
	 */
	public JWTservice() {
		
	}
	
	/**
	 * Encodes JSON.
	 *
	 * @param       json			String
	 *
	 * @return		String
	 *
	 */
	public String encodeJSON(String json) {
		return encodeBase64URLSafeString(json.getBytes());  
	}
	
	/**
	 * Hashes signature with secret and returns it encoded.
	 *
	 * @param       sig				String
	 * @param       secret			String
	 *
	 * @return      String
	 *
	 * @exception   NoSuchAlgorithmException
	 * @exception   InvalidKeyException
	 * 
	 */
	public String hashSignature(String sig, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
		
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		
		byte[] signature = sha256_HMAC.doFinal(sig.getBytes());
		
		return encodeBase64URLSafeString(signature); 
	}

}
