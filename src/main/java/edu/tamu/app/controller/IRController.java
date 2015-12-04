/* 
 * IRController.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.ERROR;
import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.tamu.app.model.IR;
import edu.tamu.app.model.repo.DSpaceIRRepo;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiVariable;
import edu.tamu.framework.aspect.annotation.Data;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.util.HttpUtility;

/**
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
@Controller
@ApiMapping("/repositories")
public class IRController 
{
	@Value("${dspace.rest.url}")
	private String dsapceRestUrl;
	
	@Autowired
	private HttpUtility httpUtility;
	
	@Autowired 
	private DSpaceIRRepo dSpaceIRRepo;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private void verifyRepo(String repo) {
        if (dSpaceIRRepo.findByName(repo) == null) {
            URL url;
            try {
                url = new URL(dsapceRestUrl);
                dSpaceIRRepo.create(repo, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	@ApiMapping("/all")
	public ApiResponse getAllRepos() {
		Map<String, List<IR>> allRepoMap = new HashMap<String, List<IR>>();
		allRepoMap.put("list", dSpaceIRRepo.findAll());
		return new ApiResponse(SUCCESS, allRepoMap);
	}
	
	@ApiMapping("/create")
	public ApiResponse getAllRepos(@Data String data) {
		
		JsonNode dataNode = null;
		
		try {
			dataNode = objectMapper.readTree(data);
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ApiResponse(ERROR, "Unable to parse JSON");
		}
	
		String name = null;	
		String urlString = null;
		
		if(dataNode.has("name")) name = dataNode.get("name").asText();
		if(dataNode.has("url"))  urlString = dataNode.get("url").asText();
		
		if(name == null || urlString == null) {
			return new ApiResponse(ERROR, "Unable to create IR (name = " + name + ", url = " + urlString + ".)");
		}
		
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ApiResponse(ERROR, "Unable to create URL from " + urlString + ".");
		}
		
		dSpaceIRRepo.create(name, url);
		
		return new ApiResponse(SUCCESS, dSpaceIRRepo.findAll());
	}
	
	
	
	
	@ApiMapping("/{repo}/top-communities")
	public ApiResponse getTopCommunities(@ApiVariable String repo) {
		verifyRepo(repo);
		try {
			String res = httpUtility.makeHttpRequest(dSpaceIRRepo.findByName(repo).getUrl().toString()+"/communities/top-communities", "GET");
			Map<String, JsonNode> topCommunities = new HashMap<String, JsonNode>();
			topCommunities.put("list", objectMapper.readTree(res));
			return new ApiResponse(SUCCESS,"So Happy For YOU", topCommunities);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get communities");
	}
	
	@ApiMapping("/{repo}/breadcrumb/{type}/{id}")
	public ApiResponse getBreadcrumb(@ApiVariable String repo, @ApiVariable String type, @ApiVariable String id) {
		
		verifyRepo(repo);
		
		try {
			
			
			List<Map<String, String>> breadcrumbList = getParent(repo, type, id, new ArrayList<Map<String, String>>());
			Map<String, List<Map<String, String>>> breadcrumbsMap = new HashMap<String, List<Map<String, String>>>();
			
			breadcrumbsMap.put("list", breadcrumbList);
			
			return new ApiResponse(SUCCESS,"So Happy For YOU", breadcrumbsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get breadcrumbs");
	}
	
	@ApiMapping("/{repo}/community/{community}")
	public ApiResponse getCommunity(@ApiVariable String repo, @ApiVariable String community) {
		
		verifyRepo(repo);
		
		try {
			String res = httpUtility.makeHttpRequest(dSpaceIRRepo.findByName(repo).getUrl().toString()+"/communities/"+community+"?expand=collections,subCommunities,parentCommunity", "GET");
			return new ApiResponse(SUCCESS,"So Happy For YOU", objectMapper.readTree(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get the community");
	}
	
	@ApiMapping("/{repo}/collections")
	public ApiResponse getCollections(@ApiVariable String repo) {
		
		verifyRepo(repo);
		
		try {
			String res = httpUtility.makeHttpRequest(dSpaceIRRepo.findByName(repo).getUrl().toString()+"/collections/", "GET");
			Map<String, JsonNode> collections = new HashMap<String, JsonNode>();
			collections.put("list", objectMapper.readTree(res));
			return new ApiResponse(SUCCESS,"So Happy For YOU", collections);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get collections");
	}
	
	@ApiMapping("/{repo}/collection/{collection}")
	public ApiResponse getCollection(@Data String data, @ApiVariable String repo, @ApiVariable String collection) throws JsonProcessingException, IOException {
		String offset = "0";
		if(objectMapper.readTree(data).has("offset"))
			offset = objectMapper.readTree(data).get("offset").asText();
		
		verifyRepo(repo);
		
		try {
			String res = httpUtility.makeHttpRequest(dSpaceIRRepo.findByName(repo).getUrl().toString()+"/collections/"+collection+"?expand=all&offset="+offset , "GET");
			return new ApiResponse(SUCCESS,"So Happy For YOU",objectMapper.readTree(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get collection");
	}
	
	@ApiMapping("/{repo}/collection/{collection}/items")
	public ApiResponse getItems(@ApiVariable String repo, @ApiVariable String collection) {		
		
		verifyRepo(repo);
		
		try {
			String res = httpUtility.makeHttpRequest(dSpaceIRRepo.findByName(repo).getUrl().toString()+"/collections/"+collection+"/items", "GET");
			Map<String, JsonNode> itemsMap = new HashMap<String, JsonNode>();
			itemsMap.put("list", objectMapper.readTree(res));
			return new ApiResponse(SUCCESS,"So Happy For YOU", itemsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get items");
	}
	
	@ApiMapping("/{repo}/itemcreate")
	public ApiResponse createItem(@Data String data, @ApiVariable String repo) {		
		
		verifyRepo(repo);
		
		try {
		    
			JsonNode dataNode = objectMapper.readTree(data);
			
			String collectionId = dataNode.get("collectionId").asText();
			String itemName = dataNode.get("itemName").asText();
			
			IR dspace = dSpaceIRRepo.findByName(repo);
			
			URL createItemUrl = new URL(dspace.getUrl().toString()+"/collections/"+collectionId+"/items");
						
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	        Document doc = docBuilder.newDocument();
	        Element rootElement = doc.createElement("item");
	        doc.appendChild(rootElement);

	        Element metadata = doc.createElement("metadata");
	        rootElement.appendChild(metadata);

	        Element key = doc.createElement("key");
	        key.appendChild(doc.createTextNode("dc.title"));
	        metadata.appendChild(key);
	        
	        Element value = doc.createElement("value");
	        value.appendChild(doc.createTextNode(itemName));
            metadata.appendChild(value);
            
            Element language = doc.createElement("language");
            language.appendChild(doc.createTextNode("en_US"));
            metadata.appendChild(language);
			
	        StringWriter stw = new StringWriter(); 
	        Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
	        serializer.transform(new DOMSource(doc), new StreamResult(stw)); 
			
	        String xml = stw.toString();
			
	        HttpURLConnection connection = (HttpURLConnection) createItemUrl.openConnection();
			
			connection.setRequestMethod("POST");			
			connection.setRequestProperty("Accept", "application/xml");			
			connection.setRequestProperty("Content-Type", "application/xml");
			
			connection.setRequestProperty("Content-Length",  String.valueOf(xml.length()));
			
			connection.setRequestProperty("rest-dspace-token", dspace.getRestAuthToken());
			
			System.out.println("Posting item using token  " + dspace.getRestAuthToken());
			
			connection.setDoOutput(true);
			
			 // Write data
	        OutputStream os = connection.getOutputStream();
	        os.write(xml.getBytes());
					
			 // Read response
	        StringBuilder response = new StringBuilder();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	          
	        String line;
	        while ( (line = br.readLine()) != null) {
	            response.append(line);
	        }
	                 
	        // Close streams
	        br.close();
	        os.close();
			
			return new ApiResponse(SUCCESS,"So Happy For YOU", response.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to create items");
	}
	
	@ApiMapping("/{repo}/item/{item}")
	public ApiResponse getItem(@ApiVariable String repo, @ApiVariable String item) {		
		verifyRepo(repo);
		
		String url = dSpaceIRRepo.findByName(repo).getUrl().toString();
		try {
			String res = httpUtility.makeHttpRequest(url+"/items/"+item+"?expand=all", "GET");
			
			JsonNode resNode = objectMapper.readTree(res);
			
			((ObjectNode) resNode).put("repoUrl", url);
			
			return new ApiResponse(SUCCESS,"So Happy For YOU", resNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ApiResponse(ERROR,"Failed to get item");
	}
	
	private List<Map<String,String>> getParent(String repo, String type, String id, List<Map<String,String>> breadcrumbList) throws IOException {
		
		String parentType = "parentCommunity";
		System.out.println(type);
		if("items".equals(type)) parentType = "parentCollection";
		
		String res = httpUtility.makeHttpRequest(dSpaceIRRepo.findByName(repo).getUrl().toString()+"/"+type+"/"+id+"?expand="+parentType, "GET");
		
		JsonNode crumbNode = objectMapper.readTree(res);
				
		if(crumbNode.get(parentType)!=null && !"null".equals(crumbNode.get(parentType).asText())) {
						
			if(crumbNode.get(parentType).get("type").asText().equals("collection")) type = "collections";
			if(crumbNode.get(parentType).get("type").asText().equals("community")) type = "communities";
			
			Map<String, String> breadcrumb = new HashMap<String,String>();
			breadcrumb.put("label", crumbNode.get(parentType).get("name").asText());
			breadcrumb.put("url", type+"/"+crumbNode.get(parentType).get("id").asText());
			breadcrumbList.add(0, breadcrumb);
			
			breadcrumbList = getParent(repo, type, crumbNode.get(parentType).get("id").asText(), breadcrumbList);
			
		} 
		
		return breadcrumbList;
		
	}
	
}
