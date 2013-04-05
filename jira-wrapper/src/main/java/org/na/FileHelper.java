/**
 * 
 */
package org.na;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Marian Jureczko
 * 
 */
public class FileHelper {
	
	public String saveInTempFile(String fileContent, String suffix) throws IOException {
		File file = File.createTempFile("jira-wrapper", suffix);
		FileWriter fstream = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(fileContent);
		out.close();
		return file.getAbsolutePath();
	}
}
