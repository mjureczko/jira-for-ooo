/**
 * 
 */
package org.na;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author marian
 * 
 */
public class WebHelper {
	
	private HttpClient httpClient = new HttpClient();
	private static String sessionId;
	
	public String download(JiraCfgDto cfg) throws HttpException, IOException {
		// String login = "readonlyjira";
		// String pass = "na_top/secret";
		// PostMethod post = new
		// PostMethod("http://www.networkedassets.net/jira/login.jsp");
		PostMethod post = new PostMethod(cfg.getJiraUrl() + "/login.jsp");
		post.setRequestHeader(new Header("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:15.0) Gecko/20100101 Firefox/15.0.1"));
		post.setParameter("os_username", cfg.getJiraUser());
		post.setParameter("os_password", cfg.getJiraPass());
		httpClient.executeMethod(post);
		post.setFollowRedirects(false);
		Header header = post.getResponseHeader("Set-Cookie");
		sessionId = header.getElements()[0].getValue();
		
		// String xlsStream =
		// "http://www.networkedassets.net/jira/sr/jira.issueviews:searchrequest-excel-current-fields/temp/SearchRequest.xls?jqlQuery=project+%3D+%22Enterprise+Resource+Management%22+AND+Sprint+in+%28openSprints%28%29%29+AND+status+in+%28Open%2C+%22In+Progress%22%29&tempMax=1000";
		String xlsStream = cfg.getJiraUrl()
				+ "/sr/jira.issueviews:searchrequest-excel-current-fields/temp/SearchRequest.xls?jqlQuery="
				+ cfg.getJiraQuery() + "&tempMax=" + cfg.getJiraTmpMax();
		GetMethod get = new GetMethod(xlsStream);
		get.setRequestHeader(new Header("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:15.0) Gecko/20100101 Firefox/15.0.1"));
		get.setRequestHeader("Cookie", "JSESSIONID=" + sessionId);
		httpClient.executeMethod(get);
		
		return get.getResponseBodyAsString();
	}
}
