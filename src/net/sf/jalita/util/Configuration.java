/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Galán y Martins
 * Creation date: 25.04.2003
 *  
 * Revision:      $Revision: 1.3 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2009/02/23 13:44:13 $
 * 
 * $Log: Configuration.java,v $
 * Revision 1.3  2009/02/23 13:44:13  ilgian
 * Added configuration setting to allow a maximum number of connections from the same IP address
 *
 * Revision 1.2  2008/10/09 15:25:15  ilgian
 * Added configuration entries for session width and height
 *
 * Revision 1.1  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.util;

import java.util.Properties;
import java.io.*;
import org.apache.log4j.Logger;



/**
 * Configuration, read from the propertyfile, and global Constants
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.3 $
 */
public class Configuration {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    // globale definitions
    private final static String APP_NAME    = "Jalita";
    private final static String APP_VERSION = "1.0";

    // configuration definitions
    private final static String CONFIG_FILE = "jalita.properties";
    private final static String CONFIG_HEAD = APP_NAME + " Version " + APP_VERSION + " - Configurationfile";

    // property attribute
    private final static String PROP_SERVER_PORT = "server.port";
    private final static String PROP_SERVER_CLEANUP_INTERVAL = "server.cleanup.interval";
    private final static String PROP_SESSION_TIMEOUT = "session.timeout";
    private final static String PROP_SESSION_INIT_FORMAUTOMATION = "session.init.formautomation";
    private final static String PROP_SESSION_INIT_TIMEBEFORECLEARBUFFER = "session.init.timebeforeclearbuffer";
    private final static String PROP_SESSION_WIDTH = "session.init.width";
    private final static String PROP_SESSION_HEIGHT = "session.init.height";
    
    private final static String PROP_SERVER_MAXSESSIONS_PER_HOST = "server.maxsessions.perhost";
    
    // common
    public final static String KEY_CRLF = "\r\n";



    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** singleton-object */
    private static Configuration configuration = null;

    /** configurationfile */
    private static Properties properties = null;



    //--------------------------------------------------------------------------
    // class methods
    //--------------------------------------------------------------------------

    /** Returns the singleton **/
    public static Configuration getConfiguration() {
        if (configuration == null ) {
            configuration = new Configuration();
        }
        return configuration;
    }



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a configuration object */
    private Configuration() {
        log.debug("Creating instance of Configuration");

        // create properties with defaultvalues
        properties = new Properties(getDefaultProperties());

        try {
            // create default configuration property file if not allready existings
            File f = new File(CONFIG_FILE);
            if (!f.exists()) {
                try {
                    FileOutputStream fout = new FileOutputStream(f);
                    getDefaultProperties().store(fout, CONFIG_HEAD);
                }
                catch (Exception ex) {
                    log.error("Defaultconfiguration-file could not be created!", ex);
                }
            }

            // read properties from file
            FileInputStream fin = new FileInputStream(CONFIG_FILE);
            properties.load(fin);
        }
        catch (Exception ex) {
            log.error(ex);
        }

    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** 
     * Here are the Defaultproperties, which will be used if the key is not present
     * in the file. Additionally the defaultproperties will be created here if the
     * propertyfile does not exist.
     */
    private Properties getDefaultProperties() {
        Properties defaultProperties = new Properties();

        // Defaultport for the serverapplication
        defaultProperties.setProperty(PROP_SERVER_PORT, "6040");

        // Timeout for a session [ms]
        defaultProperties.setProperty(PROP_SESSION_TIMEOUT, "3600000");  // 60 Min.

        // Interval, the background cleanup thread pauses
        defaultProperties.setProperty(PROP_SERVER_CLEANUP_INTERVAL, "1000");  // 1 Sec.

        // FormAutomationSet, which will be used for a new session
        defaultProperties.setProperty(PROP_SESSION_INIT_FORMAUTOMATION, "net.sourceforge.jalita.konzentrator.test.TestAutomation");

        // on new connection the timeinterval which will be waited until possibly
        // buffered, but unwanted data comes from the terminal, that should be
        // deleted. Initially needed for Putty, which sends some of its own codes
        defaultProperties.setProperty(PROP_SESSION_INIT_TIMEBEFORECLEARBUFFER, "500");

        return defaultProperties;
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /**
     * Saves the configuration in a propertyfile
     * Attention: An existing file will be overriden (including formatting and usercomments)
     */
    public void save() {
        try {
            File f = new File(CONFIG_FILE);
            FileOutputStream fout = new FileOutputStream(f);
            getDefaultProperties().store(fout, CONFIG_HEAD);
        }
        catch (Exception ex) {
            log.error("Configurationfile could not be saved!", ex);
        }
    }



    //--------------------------------------------------------------------------
    // Getter/Setter of Constant-values
    //--------------------------------------------------------------------------
    
	public String getApplicationName() {
		return APP_NAME;
	}

	public String getAppliationVersion() {
		return APP_VERSION;
	}

	
	
    //--------------------------------------------------------------------------
    // Getter/Setter of Property-values
    //--------------------------------------------------------------------------

    /** port, where the server is listening to new connections */
    public int getServerPort() {
        String port = properties.getProperty(PROP_SERVER_PORT);
        return Integer.parseInt(port);
    }



    /** sets the port, where the server is listening to new connections */
    public void setServerPort(int port) {
        properties.setProperty(PROP_SERVER_PORT, String.valueOf(port));
    }

    
    /** maximum number of sessions per single host */
    public int getServerMaxSessionsPerHost() {
        String maxSessions = properties.getProperty(PROP_SERVER_MAXSESSIONS_PER_HOST);
        return Integer.parseInt(maxSessions);
    }



    /** maximum number of sessions per single host */
    public void setServerMaxSessionsPerHost(int maxSessions) {
        properties.setProperty(PROP_SERVER_MAXSESSIONS_PER_HOST, String.valueOf(maxSessions));
    }
    
    
    public int getSessionHeight() {
        String height = properties.getProperty(PROP_SESSION_HEIGHT);
        if(height == null) height = "7";
        return Integer.parseInt(height);
    }

    /** sets the default height of the forms in client display*/
    public void setSessionHeight(int height) {
        properties.setProperty(PROP_SESSION_HEIGHT, String.valueOf(height));
    }
    
    
    public int getSessionWidth() {
        String width = properties.getProperty(PROP_SESSION_WIDTH);
        if(width == null) width = "20"; 
        return Integer.parseInt(width);
    }

    /** sets the default width of the forms in client display*/
    public void setSessionWidth(int width) {
        properties.setProperty(PROP_SESSION_WIDTH, String.valueOf(width));
    }
    

    /** time for keeping an session without activity connected, independent of an physically connection [ms] */
    public int getSessionTimeOut() {
        String timeout = properties.getProperty(PROP_SESSION_TIMEOUT);
        return Integer.parseInt(timeout);
    }



    /** sets the time for keeping an session without activity connected, independent of an physically connection [ms] */
    public void setSessionTimeOut(int timeout) {
        properties.setProperty(PROP_SESSION_TIMEOUT, String.valueOf(timeout));
    }



    /** this is the time slice of the cyclic session-managment-thread, tells him
      * how long to wait until clean up should be started [ms] */
    public int getServerCleanupInterval() {
        String interval = properties.getProperty(PROP_SERVER_CLEANUP_INTERVAL);
        return Integer.parseInt(interval);
    }



    /** sets the time slice of the cyclic session-managment-thread, tells him
     * how long to wait until clean up should be started [ms] */
    public void setServerCleanupInterval(int interval) {
        properties.setProperty(PROP_SERVER_CLEANUP_INTERVAL, String.valueOf(interval));
    }



    /** The class of the initially FormAutomationSet */
    public Class getSessionInitFormAutomation() {
        String init = properties.getProperty(PROP_SESSION_INIT_FORMAUTOMATION);
        Class c = null;

        try {
            c = Class.forName(init);
        }
        catch (ClassNotFoundException ex) {
            // Simply return null
        }

        return c;
    }



    /** Sets the initially FormAutomationSet class */
    public void setSessionInitFormAutomation(Class init) {
        properties.setProperty(PROP_SESSION_INIT_FORMAUTOMATION, init.toString());
    }



    /** on new connection the timeinterval [ms] which will be waited until possibly
     * buffered, but unwanted data comes from the terminal, that should be
     * deleted. Initially needed for Putty, which sends some of its own codes */
    public int getTimeBeforeClearBuffer() {
        String time = properties.getProperty(PROP_SESSION_INIT_TIMEBEFORECLEARBUFFER);
        return Integer.parseInt(time);
    }



    /** sets the timeinterval [ms] for a pause before the buffer will be cleared on a new connection */
    public void setTimeBeforeClearBuffer(int time) {
        properties.setProperty(PROP_SESSION_INIT_TIMEBEFORECLEARBUFFER, String.valueOf(time));
    }



    /** Returns an userdefined configuration setting.
     * These are settings which start with an 'user.' in the settingsfile */
    public String getUserProperty(String key, String fallback) {
    	return properties.getProperty("user." + key, fallback);
    }



    /** Sets an userdefined configuration setting. */
    public void setUserProperty(String key, String value) {
        properties.setProperty("user." + key, value);
    }

}
