/**
 * 
 */
package org.na;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * @author marian
 * 
 */
public class WebHelper {
	
	private HttpClient httpClient;
	private static String sessionId;
	private boolean authorisation = false;
	
	public WebHelper() throws GeneralSecurityException, IOException {
		Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory)new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);
		httpClient = new HttpClient();
	}
	
	public String download(JiraCfgDto cfg) throws HttpException, IOException {

		PostMethod post = new PostMethod(cfg.getJiraUrl() + "/login.jsp");
		post.setRequestHeader(new Header("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:15.0) Gecko/20100101 Firefox/15.0.1"));
		setCedentials(cfg, post);
		if (authorisation) {
			httpClient.executeMethod(post);
			post.setFollowRedirects(false);
			Header header = post.getResponseHeader("Set-Cookie");
			sessionId = header.getElements()[0].getValue();
		}
		
		String xlsStream = cfg.getJiraUrl()
				+ "/sr/jira.issueviews:searchrequest-excel-all-fields/temp/SearchRequest.xls?jqlQuery="
				+ encodeUrl(cfg.getJiraQuery()) + "&tempMax=" + cfg.getJiraTmpMax();
		GetMethod get = new GetMethod(xlsStream);
		get.setRequestHeader(new Header("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:15.0) Gecko/20100101 Firefox/15.0.1"));
		if (authorisation) {
			get.setRequestHeader("Cookie", "JSESSIONID=" + sessionId);
		}
		httpClient.executeMethod(get);
		
		return get.getResponseBodyAsString();
	}
	
	private void setCedentials(JiraCfgDto cfg, PostMethod post) {
		if (cfg.getJiraUser() != null && !"".equals(cfg.getJiraUser())) {
			post.setParameter("os_username", cfg.getJiraUser());
			if (cfg.getJiraPass() != null && !"".equals(cfg.getJiraPass())) {
				post.setParameter("os_password", cfg.getJiraPass());
				authorisation = true;
				return;
			}
		}
		authorisation = false;
	}
	
	protected String encodeUrl(String url) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, "UTF-8");
	}
}
