/**
 * 
 */
package org.na;

import java.util.List;

import javax.swing.JOptionPane;

import com.google.common.collect.Lists;
import com.sun.star.uno.XComponentContext;

/**
 * @author marian
 *
 */
public class AddonLogic {
	
	private XComponentContext context;
	private WebHelper webHelper = new WebHelper();
	private FileHelper fileHelepr = new FileHelper();
	private ConfigurationPage cfgPage;
	
	public AddonLogic(XComponentContext context) {
		this.context = context;
		cfgPage = new ConfigurationPage(context);
	}
	
	public void fetchDataFromJira() throws Exception{	
		cfgPage.readConfig();
		String xlsxContent = webHelper.download();
		String tmpFileName = fileHelepr.saveInTempFile(xlsxContent, ".xls");
		
		CalcIOService calc = new CalcIOService(context);
		calc.load(tmpFileName);

		List<String> strings = Lists.newArrayList("aaa", "bbb");
		JOptionPane.showMessageDialog(null, strings.toString());
	}
}
