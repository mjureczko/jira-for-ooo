/**
 * 
 */
package org.na;

import lombok.extern.log4j.Log4j;

import com.sun.star.frame.XDesktop;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * @author Marian Jureczko
 * 
 */
@Log4j
public class ConfigurationSheet {
	
	private static final int VALUES_COLUMN = 1;
	private static final int NAMES_COLUMN = 0;
	private XComponentContext context;
	private XSpreadsheet cfgSheet;
	private final String JIRA_URL = "Jira url";
	private final String JIRA_USER = "User";
	private final String JIRA_PASS = "Password";
	private final String JIRA_QUERY = "Jira query";
	private final String JIRA_TMPMAX = "Jira tmpMax parameter (Jira default is 1000)";
	private final String SHEET_NAME = "Destination sheet name";
	
	public ConfigurationSheet(XComponentContext context) throws Exception {
		this.context = context;
		cfgSheet = getFirstSpreadsheet();
	}
	
	public JiraCfgDto readConfig() throws Exception {
		renderCfgPage();
		return readCfgFromSheet();
	}
	
	private JiraCfgDto readCfgFromSheet() throws IndexOutOfBoundsException {
		JiraCfgDto cfg = new JiraCfgDto();
		cfg.setJiraUrl(cfgSheet.getCellByPosition(VALUES_COLUMN, 0).getFormula());
		cfg.setJiraUser(cfgSheet.getCellByPosition(VALUES_COLUMN, 1).getFormula());
		cfg.setJiraPass(cfgSheet.getCellByPosition(VALUES_COLUMN, 2).getFormula());
		cfg.setJiraQuery(cfgSheet.getCellByPosition(VALUES_COLUMN, 3).getFormula());
		cfg.setJiraTmpMax(cfgSheet.getCellByPosition(VALUES_COLUMN, 4).getFormula());
		cfg.setDestinationSheet(cfgSheet.getCellByPosition(VALUES_COLUMN, 5).getFormula());
		
		log.info("url: " + cfg.getJiraUrl() + " user:" + cfg.getJiraUser() + " pass:"
				+ cfg.getJiraPass() + " query:" + cfg.getJiraQuery() + " tmp:"
				+ cfg.getJiraTmpMax() + " dest:" + cfg.getDestinationSheet());
		return cfg;
	}
	
	private XSpreadsheet getFirstSpreadsheet() throws Exception {
		XSpreadsheets spreadsheets;
		XDesktop desktop = (XDesktop) UnoRuntime.queryInterface(
				XDesktop.class,
				context.getServiceManager().createInstanceWithContext("com.sun.star.frame.Desktop",
						context));
		XComponent component = desktop.getCurrentComponent();
		XSpreadsheetDocument sheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(
				XSpreadsheetDocument.class, component);
		spreadsheets = sheetDocument.getSheets();
		Object sheet = spreadsheets.getByName(spreadsheets.getElementNames()[0]);
		return (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
	}
	
	private void renderCfgPage() throws IndexOutOfBoundsException {
		cfgSheet.getCellByPosition(NAMES_COLUMN, 0).setFormula(JIRA_URL);
		cfgSheet.getCellByPosition(NAMES_COLUMN, 1).setFormula(JIRA_USER);
		cfgSheet.getCellByPosition(NAMES_COLUMN, 2).setFormula(JIRA_PASS);
		cfgSheet.getCellByPosition(NAMES_COLUMN, 3).setFormula(JIRA_QUERY);
		cfgSheet.getCellByPosition(NAMES_COLUMN, 4).setFormula(JIRA_TMPMAX);
		cfgSheet.getCellByPosition(NAMES_COLUMN, 5).setFormula(SHEET_NAME);
	}
}
