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

import com.sun.star.frame.XDesktop;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * @author Marian Jureczko
 * 
 */
public class ResultsSheet {
	
	private XComponentContext context;
	private XSpreadsheet resultsSheet;
	private final int YSHIFT = 30;
	
	public ResultsSheet(XComponentContext context, JiraCfgDto cfg) throws Exception {
		this.context = context;
		resultsSheet = getSheetByName(cfg.getDestinationSheet());
	}
	
	public void processResults(XSpreadsheet loaded) throws IndexOutOfBoundsException {
		int xDimension = readXDimension(loaded);
		int yDimension = readYDimension(loaded);
		copyToResults(xDimension, yDimension, loaded);
	}
	
	private XSpreadsheet getSheetByName(String name) throws Exception {
		XDesktop desktop = (XDesktop) UnoRuntime.queryInterface(
				XDesktop.class,
				context.getServiceManager().createInstanceWithContext("com.sun.star.frame.Desktop",
						context));
		XComponent component = desktop.getCurrentComponent();
		XSpreadsheetDocument sheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(
				XSpreadsheetDocument.class, component);
		try {
			Object sheet = sheetDocument.getSheets().getByName(name);
			return (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
		} catch (java.lang.Exception e) {
			throw new Exception("No sheet with name " + name, e);
		}
		
	}
	
	private int readXDimension(XSpreadsheet load) {
		int y = 3;
		int x = 0;
		String content = null;
		do {
			try {
				content = load.getCellByPosition(++x, y).getFormula();
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		} while (content != null && !"".equals(content));
		return x;
	}
	
	private int readYDimension(XSpreadsheet load) {
		int y = 3;
		int x = 0;
		String content = null;
		do {
			try {
				content = load.getCellByPosition(x, ++y).getFormula();
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		} while (content != null && !"".equals(content));
		return --y;
	}
	
	private void copyToResults(int xDimension, int yDimension, XSpreadsheet loaded)
			throws IndexOutOfBoundsException {
		for (int x = 0; x < xDimension; x++) {
			for (int y = 3; y < yDimension; y++) {
				String content = loaded.getCellByPosition(x, y).getFormula();
				resultsSheet.getCellByPosition(x, y + YSHIFT).setFormula(content);
			}
		}
		
	}
}
