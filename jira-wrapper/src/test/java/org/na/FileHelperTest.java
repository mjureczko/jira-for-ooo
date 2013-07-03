/**
 * Copyright (C) 2013 NetworkedAssets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * @author Marian Jureczko
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
