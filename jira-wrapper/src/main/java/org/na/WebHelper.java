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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import lombok.extern.log4j.Log4j;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.na.exceptions.JiraConnectionException;

/**
 * @author Marian Jureczko
 * 
 */
@Log4j
public class WebHelper {
	
	private HttpClient httpClient;
	private static String sessionId;
	private boolean authorisation = false;
	
	public WebHelper() throws GeneralSecurityException, IOException {
		Protocol easyhttps = new Protocol("https",
				(ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
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
			log.info("SessionId: " + sessionId);
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
		
		String body = get.getResponseBodyAsString();
		verifyOutput(body);
		return body;
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
	
	private void verifyOutput(String body) {
		if (body.contains("Error report</title>")) {
			throw new JiraConnectionException(body);
		}
	}
}
