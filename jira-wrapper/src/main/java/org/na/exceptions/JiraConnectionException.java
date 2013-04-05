/**
 * 
 */
package org.na.exceptions;

/**
 * @author Marian Jureczko
 *
 */
public class JiraConnectionException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 1L;
	
	public JiraConnectionException(String msg) {
		super(msg);
	}
	
}
