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
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/07/26 21:40:28 $
 * 
 * $Log: Configuration.java,v $
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.application;

import java.util.Properties;
import java.io.*;
import org.apache.log4j.Logger;
import net.sf.jalita.ui.automation.FormAutomationSet;



/**
 * Configuration, read from the propertyfile, and global Constants
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
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



    /** Validates the configuration values */
    public void checkConfiguration() throws ConfigurationException {
        try {
            Object obj = getSessionInitFormAutomation().newInstance();
            if (!(obj instanceof FormAutomationSet)) {
                throw new ConfigurationException("Wrong Value for + " + PROP_SESSION_INIT_FORMAUTOMATION + "!");
            }

            // Validate if integer values are correct converted
            getServerCleanupInterval();
            getServerPort();
            getSessionTimeOut();
        }
        catch (Exception ex) {
            log.error(ex);
            throw new ConfigurationException(ex.getMessage());
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
            c = net.sf.jalita.test.misc.TestAutomation.class;
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
