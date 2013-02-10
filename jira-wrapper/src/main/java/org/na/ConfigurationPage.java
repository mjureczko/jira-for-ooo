/**
 * 
 */
package org.na;

import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * @author marian
 * 
 */
public class ConfigurationPage {
	
	private XComponentContext context;
	
	public ConfigurationPage(XComponentContext context) {
		this.context = context;
	}
	
	public JiraCfgDto readConfig() throws Exception {
		XSpreadsheet cfgSheet = getFirstSpreadsheet();
		cfgSheet.getCellByPosition(1, 1).setFormula("first,first");
		cfgSheet.getCellByPosition(1, 2).setFormula("first,second");
		
		return null;
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
		return (XSpreadsheet)UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
	}
}
