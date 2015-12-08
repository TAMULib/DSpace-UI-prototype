/* 
 * ThemeController.java 
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.Data;
import edu.tamu.framework.model.ApiResponse;

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
@ApiMapping("/theme")
public class ThemeController 
{
	@Autowired
	private ObjectMapper objectMapper;
	
    @Value("${app.ui.path}")
    private String uiPath;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
	@ApiMapping("/update")
	public ApiResponse update(@Data String data) throws IOException {
		JsonNode themeValues = objectMapper.readTree(data).get("themeValues");
		
		Path path = Paths.get(resourceLoader.getResource("classpath:static/theming.scss.template").getURI());
		Charset charset = StandardCharsets.UTF_8;
		String content = new String(Files.readAllBytes(path),charset);

		Iterator<Entry<String, JsonNode>> values = themeValues.fields();

		while(values.hasNext()) {
			Entry<String, JsonNode> currentRow = values.next();
			String field = currentRow.getKey();
			String value = currentRow.getValue().asText();
			String fieldValue = value != "" ? value:"inherit";
			content = content.replaceAll("\\{"+field+"\\}", fieldValue);
		}
		
		try {
		    
			File file = new File(resourceLoader.getResource("classpath:static/theming.scss").getURI());

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("\n\nTheme SCSS updated\n\n");
			return new ApiResponse(SUCCESS,"Theme updated");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ApiResponse(ERROR,"Failed to update theme");
	}

}
