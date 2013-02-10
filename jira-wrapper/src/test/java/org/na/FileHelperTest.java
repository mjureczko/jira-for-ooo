/**
 * 
 */
package org.na;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

/**
 * @author marian
 *
 */
public class FileHelperTest {
	
	/**
	 * Test method for {@link org.na.FileHelper#saveInTempFile(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testSaveInTempFile() throws IOException {
		FileHelper fhelper = new FileHelper();
		String content = "a very short file...\n";
		String path = fhelper.saveInTempFile(content, ".txt");
		assertNotNull(path);
		
		String loaded = "";
		BufferedReader br = new BufferedReader(new FileReader(path));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        loaded = sb.toString();
	    } finally {
	        br.close();
	    }
	    
	    assertEquals(content,loaded);
	}
	
}
