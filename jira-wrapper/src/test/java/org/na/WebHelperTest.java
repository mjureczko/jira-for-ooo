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
