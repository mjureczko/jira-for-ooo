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

import javax.swing.JOptionPane;

import lombok.extern.log4j.Log4j;

import com.sun.star.uno.XComponentContext;

/**
 * @author Marian Jureczko
 * 
 */
@Log4j
public class AddonLogic {
	
	private XComponentContext context;
	private FileHelper fileHelepr = new FileHelper();
	private ConfigurationSheet cfgPage;
	
	public AddonLogic(XComponentContext context) throws Exception {
		this.context = context;
		cfgPage = new ConfigurationSheet(context);
	}
	
	public void fetchDataFromJira() throws Exception {
		log.info("START");
		JiraCfgDto cfg = cfgPage.readConfig();
		WebHelper webHelper = new WebHelper();
		String xlsxContent = webHelper.download(cfg);
		log.info("Downloaded content: " + xlsxContent);
		String tmpFileName = fileHelepr.saveInTempFile(xlsxContent, ".xls");
		log.info("Temporary data saved in " + tmpFileName);
		ResultsSheet results = new ResultsSheet(context, cfg);
		
		CalcIOService calc = new CalcIOService(context);
		results.processResults(calc.load(tmpFileName));
		
		JOptionPane.showMessageDialog(null, "The data has been collected");
	}
	
}
