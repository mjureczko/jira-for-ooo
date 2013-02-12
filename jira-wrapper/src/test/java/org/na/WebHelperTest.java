package org.na;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebHelperTest extends WebHelper {
	
	public WebHelperTest() throws Exception {
		super();
	}
	
	@Test
	public void testDownload() throws Exception {
		WebHelper web = new WebHelper();
		JiraCfgDto cfg = new JiraCfgDto();
		cfg.setJiraUrl("https://hibernate.onjira.com");
		cfg.setJiraQuery("project = \"Hibernate ORM\" AND status in (Open, \"In Progress\")");
		cfg.setJiraTmpMax("5");
		String result = web.download(cfg);
		assertTrue(result.startsWith("<html xmlns:o=\"urn:schemas-microsoft-com:office:office"));
	}
	
	@Test
	public void testEncodeUrl() throws Exception {
		String url = "project = \"MyProj\"";
		WebHelper web = new WebHelper();
		String encoded = web.encodeUrl(url);
		assertEquals("project+%3D+%22MyProj%22", encoded);
	}
}
