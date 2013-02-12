/**
 * 
 */
package org.na;

import javax.swing.JOptionPane;

import com.sun.star.uno.XComponentContext;

/**
 * @author Marian Jureczko
 *
 */
public class AddonLogic {
	
	private XComponentContext context;
	private FileHelper fileHelepr = new FileHelper();
	private ConfigurationSheet cfgPage;
	
	public AddonLogic(XComponentContext context) throws Exception {
		this.context = context;
		cfgPage = new ConfigurationSheet(context);
	}
	
	public void fetchDataFromJira() throws Exception{	
		JiraCfgDto cfg = cfgPage.readConfig();
		WebHelper webHelper = new WebHelper();
		String xlsxContent = webHelper.download(cfg);
		String tmpFileName = fileHelepr.saveInTempFile(xlsxContent, ".xls");
		ResultsSheet results = new ResultsSheet(context, cfg);
		
		CalcIOService calc = new CalcIOService(context);
		results.processResults(calc.load(tmpFileName));

		JOptionPane.showMessageDialog(null, "The data has been collected");
	}

}
