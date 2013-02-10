/**
 * 
 */
package org.na;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * @author marian
 *
 */
public class CalcIOService {
	
	private XComponentContext context;
	
	public CalcIOService(XComponentContext context){
		this.context = context;
	}
	
	public void load(String fileNameWithPath) throws Exception {

        XMultiComponentFactory xRemoteServiceManager = null;
        
//		// connect and retrieve a remote service manager and component context
//        XComponentContext xLocalContext =
//            com.sun.star.comp.helper.Bootstrap.createInitialComponentContext(null);
//        XMultiComponentFactory xLocalServiceManager = xLocalContext.getServiceManager();
//        Object urlResolver = xLocalServiceManager.createInstanceWithContext(
//            "com.sun.star.bridge.UnoUrlResolver", xLocalContext );
//        XUnoUrlResolver xUnoUrlResolver = (XUnoUrlResolver) UnoRuntime.queryInterface( 
//            XUnoUrlResolver.class, urlResolver );
//        Object initialObject = xUnoUrlResolver.resolve( 
//            "uno:socket,host=localhost,port=2083;urp;StarOffice.ServiceManager" );
//        XPropertySet xPropertySet = (XPropertySet)UnoRuntime.queryInterface(
//            XPropertySet.class, initialObject);
//        Object context = xPropertySet.getPropertyValue("DefaultContext"); 
//        xRemoteContext = (XComponentContext)UnoRuntime.queryInterface(
//            XComponentContext.class, context);
        xRemoteServiceManager = context.getServiceManager();
		 
		 // get Desktop instance
         Object desktop = xRemoteServiceManager.createInstanceWithContext (
             "com.sun.star.frame.Desktop", context);
		   
		 // query the XComponentLoader interface from the Desktop service
		 XComponentLoader xComponentLoader = (XComponentLoader)UnoRuntime.queryInterface(
		     XComponentLoader.class, desktop);
		 
		 // define load properties according to com.sun.star.document.MediaDescriptor		 
		 /* or simply create an empty array of com.sun.star.beans.PropertyValue structs:
		    PropertyValue[] loadProps = new PropertyValue[0]
		  */
		 
		 // the boolean property Hidden tells the office to open a file in hidden mode
		 PropertyValue[] loadProps = new PropertyValue[1];
		 loadProps[0] = new PropertyValue();
		 loadProps[0].Name = "Hidden";
		 loadProps[0].Value = new Boolean(false);
		 String loadUrl = "file:///"+fileNameWithPath;
		 
		 // load
		 xComponentLoader.loadComponentFromURL(loadUrl, "_blank", 0, loadProps); 
	}
}
