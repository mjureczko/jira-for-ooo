/**
 * 
 */
package org.na;

import lombok.Data;

/**
 * @author marian
 *
 */
@Data
public class JiraCfgDto {
	private String jiraUrl;
	private String jiraUser;
	private String jiraPass;
	private String jiraQuery;
	private String jiraTmpMax;
	private String destinationSheet;
}
