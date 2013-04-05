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
