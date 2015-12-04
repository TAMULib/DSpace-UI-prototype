/* 
 * IR.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Entity
public class IR  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column
	private URL url;
	
	@Column(unique = true)
	private String name;
	
	private String restAuthToken;
	
	public IR() {};
	
	public IR(String name, URL url)
	{
		setUrl(url);
		setName(name);		
	}

	public void authenticateRest(String username, String password) {
		
		HttpURLConnection con;
		try {
			
			URL loginUrl = new URL(url.toExternalForm()+"/login");
			
			con = (HttpURLConnection) loginUrl.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			
			 //Send request
		     DataOutputStream wr = new DataOutputStream (con.getOutputStream ());
		     wr.writeBytes ("{\"email\": \"" + username + "\", \"password\": \"" + password +"\"}");
		     wr.flush ();
		     wr.close ();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String inputLine;
			
			StringBuffer strBufRes = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				strBufRes.append(inputLine);
			}
			
			in.close();
			
			setRestAuthToken(strBufRes.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean testIRRestEndpoint()
	{
		URL restTestEndpoint = null;
		try {
			restTestEndpoint = new URL(url.toExternalForm()+"/test");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputStream endpointStream = null;
		try {
			endpointStream = restTestEndpoint.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String responseString="";
		try {
			responseString = IOUtils.toString(endpointStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(responseString.toString().length() > 0)
		{
			System.out.println("Successful test of IR REST endpoint: " + responseString);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRestAuthToken() {
		return restAuthToken;
	}

	public void setRestAuthToken(String restAuthToken) {
		this.restAuthToken = restAuthToken;
	}

}
