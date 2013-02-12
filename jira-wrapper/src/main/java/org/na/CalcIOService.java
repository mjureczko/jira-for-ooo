/**
 * 
 */
package org.na;

import javax.swing.JOptionPane;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * @author marian
 * 
 */
public class CalcIOService {
	
	private XComponentContext context;
	
	public CalcIOService(XComponentContext context) {
		this.context = context;
	}
	
	public XSpreadsheet load(String fileNameWithPath) throws Exception {
		XMultiComponentFactory xRemoteServiceManager = context.getServiceManager();
		
		Object desktop = xRemoteServiceManager.createInstanceWithContext(
				"com.sun.star.frame.Desktop", context);
		
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
				XComponentLoader.class, desktop);
		
		PropertyValue[] loadProps = new PropertyValue[1];
		loadProps[0] = new PropertyValue();
		loadProps[0].Name = "Hidden";
		loadProps[0].Value = new Boolean(true);
		String loadUrl = "file:///" + fileNameWithPath;
		
		XComponent newCalc = xComponentLoader.loadComponentFromURL(loadUrl, "_blank", 0, loadProps);
		XSpreadsheetDocument sheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(
				XSpreadsheetDocument.class, newCalc);
		XSpreadsheets spreadsheets = sheetDocument.getSheets();
		Object sheet = spreadsheets.getByName(spreadsheets.getElementNames()[0]);
		XSpreadsheet xsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
		return xsheet;
	}
}
