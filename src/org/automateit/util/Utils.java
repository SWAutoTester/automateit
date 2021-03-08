/**
 * This file is part of Automate It!'s free and open source web and mobile 
 * application testing framework.
 * 
 * Automate It! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Automate It! is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Automate It!.  If not, see <http://www.gnu.org/licenses/>.
 **/

package org.automateit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.FileWriter;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import java.util.Base64;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.net.URL;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

import org.automateit.data.DataDrivenInputFactory;
import org.automateit.data.DataDrivenInput;
import org.automateit.data.DataArchive;
import org.automateit.data.CSVAppendDataArchive;
import org.automateit.data.CSVDataArchive;

import org.automateit.media.JarvisTextToSpeechConverter;
import org.automateit.media.JLayerAudioPlayer;
import org.automateit.media.JMFAudioPlayer;
import org.automateit.media.JavaSoundAPIAudioPlayer;

import org.automateit.reports.ReportsManager;

import org.automateit.core.CommonWebDriver;

public class Utils {
    
    /**
     * The screenshots directory
     */
    public static final String SCREENSHOTS_DIR = "screenshots";

    /**
     * Page load performance data directory
     */
    public static final String PAGEPERFORMANCE_DIR = "performance";
    
    /**
     * The testing target variable name
     */
    public static final String TESTING_TARGET_VARIABLE_NAME = "target_testing_environment";
    
    /**
     * Linked image file prefix, we need it for proper syntax
     * when adding links to html since we are linking local files.
     */
    public final String LINKIMAGEFILEPREFIX = "file:///";
    
    /**
     * File extensions definitions
     */
    public static final String[] FILE_EXTENSIONS = { "jpg", "JPG", "jpeg", "JPEG", "gif", "GIF", "png", "PNG"};
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(Utils.class);
    
    /**
     * Default Constructor
     */
    public Utils() { }
    
    /**
     * Return the base directory of test execution
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getBaseDirectory() throws Exception {
        
        try { return (new File("")).getAbsolutePath(); }
        catch(Exception e) { throw e; }
           
    }
    
    /**
     * Return the directory where screenshot should be created.
     * 
     * This method always appends a "/" to the end to the end of the string
     * returned.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getBaseScreenshotsDirectory() throws Exception {
        
        try { return getBaseDirectory() + File.separator + SCREENSHOTS_DIR + File.separator; }
        catch(Exception e) { throw e; }
           
    }
    
    /**
     * Return the directory where page performance files should be created.
     * 
     * This method always appends a "/" to the end to the end of the string
     * returned.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getBasePagePerformanceDirectory() throws Exception {
        
        try { return getBaseDirectory() + File.separator + PAGEPERFORMANCE_DIR + File.separator; }
        catch(Exception e) { throw e; }
           
    }
    
    /**
     * This method returns the boolean value of the <code>doReportingOnTestSuccess</code>
     * setting in the properties file.
     * 
     * If no valid setting, this return value is <code>false</code>.
     * 
     * @return 
     */
    public boolean doReportingOnTestSuccess() { 
        
        if(CommonProperties.getInstance().get("doReportingOnTestSuccess") == null) return false;
        return (new Boolean(CommonProperties.getInstance().get("doReportingOnTestSuccess"))); 
    
    }
    
    /**
     * This method returns the boolean value of the <code>doReportingOnTestFail</code>
     * setting in the properties file.
     * 
     * If no valid setting, this return value is <code>false</code>.
     * 
     * @return 
     */
    public boolean doReportingOnTestFail() { 
        
        if(CommonProperties.getInstance().get("doReportingOnTestFail") == null) return false;
        return (new Boolean(CommonProperties.getInstance().get("doReportingOnTestFail"))); 
    
    }
    
    /**
     * Return the first token in the string separated by the given
     * <code>delimiter</code>.
     * 
     * @param s the string to tokenize
     * @param delimiter the speration character(s)
     * 
     * @return The first token 
     */
    public String getFirstToken(String s, String delimiter) {
        
        StringTokenizer st = new StringTokenizer(s, delimiter);
        return st.nextToken();
        
    }
    
    /**
     * A file filename filter that returns all files in a directory with 
     * a ".properties" extension.
     * 
     * @param f The directory
     * 
     * @return
     * 
     * @throws Exception 
     */
    public FilenameFilter getPropertiesFileFilter(File f) throws Exception {
        
        FilenameFilter filter = null;
        
        try {
            
            filter = new FilenameFilter() {
        
                public boolean accept(File directory, String fileName) { return fileName.endsWith(".properties"); }
        
            };
            
            return filter;
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the list of files in the directory.
     * 
     * @param directory
     * 
     * @return The list files with .properties extension
     * 
     * @throws Exception 
     */
    public File[] getListOfPropertiesFilesInDirectory(String directory) throws Exception {
        
        try {
            
            File f = new File(directory);
            
            if(!f.exists()) throw new Exception("Directory does not exist: " + directory);
        
            if(!f.isDirectory()) throw new Exception(directory + ": is not a directory");
              
            File[] files = f.listFiles(getPropertiesFileFilter(f));
            
            return files;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the list of files in the directory.
     * 
     * @param directory
     * 
     * @return The list of files
     * 
     * @throws Exception 
     */
    public File[] getListOfAllFilesInDirectory(String directory) throws Exception {
        
        try {
           
            File f = new File(directory);
            
            if(!f.exists()) throw new Exception("Directory does not exist: " + directory);
        
            if(!f.isDirectory()) throw new Exception(directory + ": is not a directory");
              
            File[] files = f.listFiles();
            
            return files;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the list of files in the directory.
     * 
     * @param directory
     * @param expectedFilename
     * 
     * @return
     */
    public boolean isFileInDirectory(String directory, String expectedFilename) {
        
        try {
            
            File[] files = getListOfAllFilesInDirectory(directory);
           
            for(File file : files) {
                
                log.debug("Checking for file in directory: " + expectedFilename + "|" + file.getName());
                
                if(file.getName().equals(expectedFilename)) return true;
                
            }
            
            // if we get here, then the file or name was not found
            return false;
            
        }
        catch(Exception e) { return false; }
        
    }
    
    /**
     * Load the properties with the search value and expected result data.
     * 
     * @param filename
     * 
     * @return
     * 
     * @throws Exception 
     */
    public Properties loadProperties(String filename) throws Exception {
        
        try {
            
            Properties properties = new Properties();
           
            FileInputStream in = new FileInputStream(filename);
            
            properties.load(in);
            
            in.close();
            
            return properties;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Setup logging with log4j.
     * 
     * Default location id at <code>./conf/log4j.properties</code>
     * 
     * @throws Exception 
     */
    public void setupLogging() throws Exception {
        
        try { setupLogging("./conf/log4j.properties"); }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Setup logging with log4j.
     * 
     * @param log4jPropertiesFile
     * 
     * @throws Exception 
     */
    public void setupLogging(final String log4jPropertiesFile) throws Exception {
        
        try {
            
            Properties props = new Properties();
            
            props.load(new FileInputStream(log4jPropertiesFile));
            
            PropertyConfigurator.configure(props);
           
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Write a string buffer to a file.
     * 
     * @param filename
     * 
     * @param sb
     * 
     * @throws Exception 
     */
    public void writeStringBufferToFile(String filename, StringBuffer sb) throws Exception {
        
        try {
            
            FileWriter writer = new FileWriter(filename, false);

            writer.append(sb.toString() + "\n");
            writer.flush();

            writer.close();
            
            writer = null;

        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a string array for a Map object that contains integer values as keys.
     * 
     * @param map
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String[] getStringArrayFromNumberKeywordMap(Map map) throws Exception {
        
        try {
            
            String[] stringArray = new String[map.size()];
            
            int count = 0;
            
            for(int i = 0; i < map.size(); i++) {
                
                if(map.containsKey(String.valueOf(i))) { 
                    
                    stringArray[count] = (String) map.get(String.valueOf(i));
                    
                    count++;
                    
                }
            }
            
            return stringArray;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Copy a file.
     * 
     * @param fromFile
     * @param toFile
     * 
     * @throws Exception 
     */
    public void copyFile(String fromFile, String toFile) throws Exception {
        
        InputStream input = null;
        
        OutputStream output = null;
            
        try {
   
            input = new FileInputStream(fromFile);
        
            output = new FileOutputStream(toFile);

            byte[] buf = new byte[1024];

            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) output.write(buf, 0, bytesRead);
            
            input.close();
 
            output.close();
            
            input = null;
            
            output = null;
    
        } 
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Rename an existing file to a new filename.
     * 
     * @param originalFilename
     * @param newFilename
     * 
     * @throws Exception 
     */
    public void renameFile(String originalFilename, String newFilename) throws Exception {
        
        try { new File("originalFilename").renameTo(new File("newFilename")); } 
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Duplicate a file to a new file and return an append data archive.
     * 
     * @param fromFile
     * @param toFile
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataArchive duplicateFileAndConvertDataDrivenInputToCVSDataAppendArchive(String fromFile, String toFile) throws Exception {
        
        try {
            
            copyFile(fromFile, toFile);
            
            return new CSVAppendDataArchive();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Compute and add operation between two numbers, integer values 
     * represented as strings.
     * 
     * None, One or both numbers can be negative.
     * 
     * @param x
     * @param y
     * 
     * @return
     * 
     * @throws Exception 
     */
    public int computeAdditon(String x, String y) throws Exception {
        
        if((x == null) || (y == null)) throw new Exception("Unable to compute addition operation because one of the operandsin null; x=" + x +":y=" + y);
        
        try {
            
            // add 2 negative numbers
            if(x.trim().startsWith("-") && y.trim().startsWith("-")) return 0 - Math.abs((new Integer(x.trim())).intValue()) - Math.abs((new Integer(y.trim())).intValue());
            // add x and subtract y
            if(!x.trim().startsWith("-") && y.trim().startsWith("-")) return 0 + Math.abs((new Integer(x.trim())).intValue()) - Math.abs((new Integer(y.trim())).intValue());
            // add x and subtract y
            if(x.trim().startsWith("-") && !y.trim().startsWith("-")) return 0 - Math.abs((new Integer(x.trim())).intValue()) + Math.abs((new Integer(y.trim())).intValue());
            // add x and y
            if(!x.trim().startsWith("-") && !y.trim().startsWith("-")) return 0 + Math.abs((new Integer(x.trim())).intValue()) + Math.abs((new Integer(y.trim())).intValue());
          
            else return -1000;
          
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a URL with username and password embedded in it so that it can
     * be used for automatic logins to authentication-prompted websites.
     * 
     * @param url
     * @param username
     * @param password
     * 
     * @return
     * 
     * @throws Exception 
     */ 
    public String embedUsernameAndPasswordInURL(String url, String username, String password) throws Exception {
    
        try { return (new StringBuffer(url).insert(7, username + ":" + password + "@")).toString(); }    
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * 
     * @param booleanValue
     * @return 
     */
    public boolean getBooleanValueFromString(String booleanValue) {
        
        try { return (new Boolean(booleanValue)).booleanValue(); }
        catch(Exception e) { return false; }
        
    }
    
    /**
     * Setup the data driven testing object so we can drive our tests using a
     * configuration file.
     * 
     * @param dataFile The file of input parameter values
     * @param id The id of the implementation to use
     * 
     * @return A DataDrivenInput instance
     * 
     * @throws Exception 
     */
    public DataDrivenInput setupDataDrivenInput(String dataFile, int id) throws Exception {
        
        try {
            
            DataDrivenInputFactory dataDrivenInputFactory = new DataDrivenInputFactory();
            return dataDrivenInputFactory.getDataDrivenInput(dataFile, id);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Setup the data driven testing object so we can drive our tests using a
     * configuration file. It will guess the file type.
     * 
     * @param dataFile The file of input parameter values
     * 
     * @return A DataDrivenInput instance
     * 
     * @throws Exception 
     */
    public DataDrivenInput setupDataDrivenInput(String dataFile) throws Exception {
        
        int id = DataDrivenInputFactory.CSV; // default ot type 
        
        try {
           
            if(dataFile == null) throw new Exception("Unable to load data from file: " + dataFile);
            
            if(dataFile.contains(".csv")) id = DataDrivenInputFactory.CSV;
            else if(dataFile.contains(".xls")) id = DataDrivenInputFactory.EXCEL;
            else if(dataFile.contains(".txt")) id = DataDrivenInputFactory.PIPEDELIMITED;
            
            DataDrivenInputFactory dataDrivenInputFactory = new DataDrivenInputFactory();
            return dataDrivenInputFactory.getDataDrivenInput(dataFile, id);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * This is a special method only used when trying to replace a URL with a modified
     * URL, replacing the first part of the domain name.
     * 
     * Example:
     * 
     * Original URL: http://stage.test.mycomany.com
     * Modified URL: http://dev.test.mycompany.com
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getModifiedURL(String originalURL, String replaceDomainName) throws Exception { 
        
        String modifiedURL = ""; 
        
        log.debug("Constructing a modified start url based on selected target environment: " + originalURL + "|" + replaceDomainName);
        
        try {
            
            if((replaceDomainName == null) || replaceDomainName.contains("prod")) return originalURL; 
            
            if(originalURL == null) throw new Exception("Unable to return modified URL because original URL is null");
            
            if(replaceDomainName == null) return originalURL; // if the replace domain isn't set, just return the original url
            else if(replaceDomainName.contains("Dev")) replaceDomainName = "dev"; // replace string Dev with dev
            
            // the url contains @ and has an embedded username and password
            if(originalURL.contains("@")) {
                
                StringTokenizer st = new StringTokenizer(originalURL, "@");
                
                modifiedURL = st.nextToken() + "@";
                
                StringTokenizer st2 = new StringTokenizer(st.nextToken(), ".");
                
                st2.nextToken(); // this is the throwaway part of the url
                
                modifiedURL += replaceDomainName;
                
                while(st2.hasMoreTokens()) modifiedURL += "." + st2.nextToken();
                
            }
            else { // the url does not have an embedded password
                
                String protocol = null;
                
                boolean isNotHTTPProtocol = false;
                
                if(!originalURL.startsWith("http")) {
                    
                    isNotHTTPProtocol = true;
                    
                    try { 
                        
                        StringTokenizer st = new StringTokenizer(originalURL, ":");
                        
                        protocol = st.nextToken(); // the protocol is the first token
                    
                        // Create a Pattern object
                        Pattern pattern = Pattern.compile("\\b" + protocol + "\\b");
    
                        // Now create matcher object.
                        Matcher matcher = pattern.matcher(originalURL);
                        
                        originalURL = matcher.replaceFirst("http");
                    
                    }
                    catch(Exception le) { }
                    
                }
                
                URL url = new URL(originalURL);
                
                modifiedURL = url.getProtocol() + "://";
                
                StringTokenizer st = new StringTokenizer(url.getAuthority(), ".");
                
                st.nextToken(); // the throwaway part of the url
                
                modifiedURL += replaceDomainName;
                
                while(st.hasMoreTokens()) modifiedURL += "." + st.nextToken();
                
                modifiedURL += url.getPath();
                
                if(isNotHTTPProtocol) {
                    
                    log.info("Original URL was not http protocol, replace with: " + protocol);
                    
                    try { 
                    
                        // Create a Pattern object
                        Pattern pattern = Pattern.compile("\\bhttp\\b");
    
                        // Now create matcher object.
                        Matcher matcher = pattern.matcher(modifiedURL);
                        
                        modifiedURL = matcher.replaceFirst(protocol);
                       
                    }
                    catch(Exception le) { }
                    
                }
                
            }
            
            log.debug("Returning modified URL: " + modifiedURL);
            
            return modifiedURL;
        
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Extract a value from parentheses.
     * 
     * Example: (Hello) extracts to Hello, "5" extract to 5
     * 
     * @param str
     * 
     * @return 
     * 
     * @throws Exception 
     */
    public String extractValueBetweenParentheses(final String str) throws Exception {
        
        try {
            
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(str);
     
            if(m.find()) return m.group(1).trim();
            else throw new Exception("Unable to extract value between parentheses: " + str);
           
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Delete a file. Use full or relative path.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void deleteFile(String filename) throws Exception {
        
        try { (new File(filename)).delete(); } 
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Delete all files from directory matching file names that start with <code>startsWithFilename</code> 
     * 
     * @param directory
     * @param startsWithFilename
     * 
     * @throws Exception 
     */
    public void deleteAllFilesInDirectoryMatchingFilenameStartsWith(String directory, String startsWithFilename) throws Exception {
        
        try { 
            
            File dir = new File(directory);

            File [] files = dir.listFiles(new FilenameFilter() {
    
                @Override
                public boolean accept(File dir, String name) { return name.startsWith("ScreenRecording"); }

            });
            
            try { for(File file : files) deleteFile(directory + File.separator + file.getName()); }
            catch(Exception le) { }
    
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Return the extension of the file.
     * 
     * @param filename
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getFileExtension(String filename) throws Exception {
        
        String extension = "";
        
        try { 
            
            StringTokenizer st = new StringTokenizer(filename, ".");
            
            while(st.hasMoreTokens()) extension = st.nextToken();
            
            return extension;
        
        } 
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * This method uses text-to-speech synthesizer to convert a string (text) to an audio file.
     * 
     * The default encoding is: "en-us"
     * 
     * @param message
     * @param saveToFilename
     * 
     * @throws Exception 
     */
    public void saveTextToSpeechConversionToAudioFile(String message, String saveToFilename) throws Exception {
        
        try { (new JLayerAudioPlayer()).save(message, saveToFilename); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * This method uses text-to-speech synthesizer to convert a string (text) to an audio file.
     * 
     * The default encoding is: "en-us"
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void playTextToSpeech(String message) throws Exception {
        
        try { 
            
            log.info("Playing voice synthesizer audio message: " + message);
           
            (new JLayerAudioPlayer()).play((new JarvisTextToSpeechConverter()).execute(message));
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * This method uses Java Sound API to play a audio file
     * 
     * @param audioFilename
     * 
     * @throws Exception 
     */
    public void playAudioFileUsingJavaSound(String audioFilename) throws Exception {
        
        try { (new JavaSoundAPIAudioPlayer()).play(audioFilename); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Plays an audio file using Java Media Framework.
     * 
     * @param audioFilename
     * 
     * @throws Exception 
     */
    public void playAudioUsingJavaMediaFramework(String audioFilename) throws Exception {
        
        try { (new JMFAudioPlayer()).play(audioFilename); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Take a screenshot and add it to the test results report
     * 
     * @param destinationDirectory
     * 
     * @throws Exception 
     */
    public void addScreenshotToReport(String destinationDirectory) throws Exception {
               
        try { addScreenshotToReport(destinationDirectory, null); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Take a screenshot and add it to the test results report
     * 
     * @param destinationDirectory
     * @param title
     * 
     * @throws Exception 
     */
    public void addScreenshotToReport(String destinationDirectory, String title) throws Exception {
               
        try { 
            
            Date now = new Date();
            
            String filename = String.valueOf(now.getTime()) + ".png";
            
            String screenshotFilename = destinationDirectory + File.separator + filename;
            
            FileUtils.copyFile(((TakesScreenshot)CommonWebDriver.getInstance().getWebDriver()).getScreenshotAs(OutputType.FILE), new File(screenshotFilename));
            
            if(title == null) ReportsManager.getInstance().addImageToReport(filename);
            else ReportsManager.getInstance().addImageToReport(filename, title);
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Returns a List from lines in a file.
     * 
     * @param filename
     * 
     * @return
     * 
     * @throws Exception 
     */
    public List<String> getListFromFile(String filename) throws Exception {
        
        List<String> list = new ArrayList<String>();
        
        String line = null;
        
        try {
        
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        
            while((line = reader.readLine()) != null) { 
                
                try { list.add(line.trim()); }
                catch(Exception le) { }
                
            }
            
        } 
        catch ( Exception e ) { throw e; }
        
        return list;
        
    }
    
    /**
     * Check for matching text existing in a file.
     * 
     * @param filename
     * @param expectedText
     * 
     * @return 
     */
    public boolean isTextInFile(String filename, String expectedText) {
        
        try {
            
            List<String> list = getListFromFile(filename);
            
            for(String line : list) { if(line.contains(expectedText)) return true; }
            
            // if we get here, the text was not found in the file
            return false;
            
            
        }
        catch(Exception e) { return false; }
    }
    
    /**
     * Remove all of the files in a directory with the file extension matching <code>directoryName</code>
     * 
     * @param directoryName
     * @param extension 
     */
    public void removeAllFilesInDirectoryWithExtension(String directoryName, String extension) {
        
        try {
            
            File folder = new File(directoryName);
                
            File fList[] = folder.listFiles();

            for(File f: fList) { if(f.getName().endsWith("." + extension)) f.delete(); }
         
        }
        catch(Exception e) { }

    }
    
    /**
     * Remove all image files in a directory
     * 
     * @param directoryName 
     */
    public void removeAllImageFilesInDirectory(String directoryName) {
        
        for(String extension: FILE_EXTENSIONS) {
            
            try { removeAllFilesInDirectoryWithExtension(directoryName, extension); }
            catch(Exception e) { }
        
        }
        
    }
    
    /**
     * Execute a shell command.
     * 
     * @param fullCommand
     * 
     * @return output from STDIO
     * 
     * @throws Exception 
     */
    public String executeShellCommand(String fullCommand) throws Exception {
        
        String line = null;
        
        String outputString = null;
        
        try { 
            
            Process p = Runtime.getRuntime().exec(fullCommand);
            
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            StringBuilder output = new StringBuilder();
	
            while((line = stdInput.readLine()) != null) output.append(line + "\n");
            
            try { outputString = output.toString(); }
            catch(Exception le) { }
                
            if(outputString == null) {
                    
                while((line = stdError.readLine()) != null) output.append(line + "\n");
            
                outputString = output.toString();
                
            }
                
            return outputString;
		
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Open URL Connection 
     * 
     * @param url
     * 
     * @return
     * 
     * @throws Exception 
     */
    public InputStreamReader openURLConnection(String url) throws Exception {
        
        try {
           
            URL urlObj = new URL(url);
            
            log.debug("Attempting to open a URL connection at: " + url);
           
            URLConnection connection = (urlObj).openConnection();
            
            log.debug("URL connection is open");
            
            log.debug("User Authentication info: " + urlObj.getUserInfo());
            
            if(urlObj.getUserInfo() != null) {
                
                log.debug("The user has the following authentication credentials: " + urlObj.getUserInfo());
    
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(urlObj.getUserInfo().getBytes()));
    
                connection.setRequestProperty("Authorization", basicAuth);

            }
            
            return new InputStreamReader(connection.getInputStream());
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Access a URL
     * 
     * @param url
     * 
     * @throws Exception 
     */
    public void accessURL(String url) throws Exception {
        
        try { openURLConnection(url); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify the response for the URL
     * 
     * @param url
     * @param expectedTextInResponse
     * 
     * @throws Exception 
     */
    public void verifyResponseFromURL(String url, String[] expectedTextInResponse) throws Exception {
        
        String urlString = "";
        
        try { 
            
             BufferedReader in = new BufferedReader(openURLConnection(url));
             
             String current;
         
             while((current = in.readLine()) != null) urlString += current;
             
             for(int i = 0; i < expectedTextInResponse.length; i++) {
                 
                 if(!urlString.contains(expectedTextInResponse[i])) throw new Exception("The URL response text does not include the expected text: " + expectedTextInResponse[i]);
                 
             }
            
        }
        catch(Exception e) { log.info(urlString); throw e; }
        
    }
    
    /**
     * Verify the response for the URL
     * 
     * @param url
     * @param expectedTextMatchInHTMLResponse
     * 
     * @throws Exception 
     */
    public void verifyResponseFromURL_Regex(String url, String expectedTextMatchInHTMLResponse) throws Exception {
      
        try { verifyTextMatch_Regex(new BufferedReader(openURLConnection(url)), expectedTextMatchInHTMLResponse); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify the text match using regex
     * 
     * @param in
     * @param expectedTextMatchInHTMLResponse
     * 
     * @throws Exception 
     */
    public void verifyTextMatch_Regex(BufferedReader in, String expectedTextMatchInHTMLResponse) throws Exception {
        
        String urlString = "";
        
        try { 
            
             String current;
         
             while((current = in.readLine()) != null) urlString += current;
            
             if(!Pattern.matches("*" + expectedTextMatchInHTMLResponse + "*", urlString)) throw new Exception("The URL response text does not include the expected text: " + expectedTextMatchInHTMLResponse);
               
        }
        catch(Exception e) { log.info(urlString); throw e; }
        
    }
    
    /**
     * Verify the text match
     * 
     * @param in
     * @param expectedTextMatchInHTMLResponse
     * 
     * @throws Exception 
     */
    public void verifyTextMatch(BufferedReader in, String expectedTextMatchInHTMLResponse) throws Exception {
        
        String urlString = "";
        
        try { 
            
             String current;
         
             while((current = in.readLine()) != null) urlString += current;
            
             if(!urlString.contains(expectedTextMatchInHTMLResponse)) throw new Exception("The URL response text does not include the expected text: " + expectedTextMatchInHTMLResponse);
               
        }
        catch(Exception e) { log.info(urlString); throw e; }
        
    }
    
    /**
     * Get a JSON result array
     * 
     * @param url
     * @param rootNodeName
     * 
     * @return 
     * 
     * @throws Exception 
     */
    public JsonArray getJSONResult(String url, String rootNodeName) throws Exception {
       
        log.debug("Getting JSON result for node name: " + rootNodeName + " for URL: " + url);
        
        try { 
            
            JsonReader rdr = Json.createReader(openURLConnection(url));
            
            JsonObject obj = rdr.readObject();
 
            return obj.getJsonArray(rootNodeName);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a data driven input object from system variable values.
     * 
     * @param parameterNames
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataDrivenInput getInputFromSystemVariableValues(String[] parameterNames) throws Exception {
        
        DataArchive dataArchive = new CSVDataArchive(); 
        
        for(String key: parameterNames) {
           
            String[] s = { key, System.getProperty(key) };
            
            dataArchive.addData(s);
            
        }
       
        return getDataDrivenInputFromDataArchive(dataArchive);
        
    }
    
    /**
     * Get a data driven input object from system variable values.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataDrivenInput getInputFromSystemVariableValues() throws Exception {
        
        DataArchive dataArchive = new CSVDataArchive(); 
        
        Map<String, String> env = System.getenv();
        
        for (String envName : env.keySet()) {
            
            String[] s = { envName, env.get(envName) };
            
            dataArchive.addData(s);
           
        }
       
        return getDataDrivenInputFromDataArchive(dataArchive);
        
    }
    
    /**
     * Get a data driven input object from system variable values.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataDrivenInput getInputFromSystemArguments() throws Exception {
        
        DataArchive dataArchive = new CSVDataArchive(); 
        
        Map<String, String> env = System.getenv();
        
        for (String envName : env.keySet()) {
            
            String[] s = { envName, env.get(envName) };
            
            dataArchive.addData(s);
           
        }
       
        return getDataDrivenInputFromDataArchive(dataArchive);
        
    }
    
    /**
     * Get a data driven object from an archive
     * 
     * @param dataArchive
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataDrivenInput getDataDrivenInputFromDataArchive(DataArchive dataArchive) throws Exception {
        
        String tmpFileName = "autotmp.csv";
        
        try {
        
            dataArchive.saveData(tmpFileName);
        
            return setupDataDrivenInput(tmpFileName);
            
        }
        catch(Exception e) { throw e; }
        finally {
            
            try { deleteFile(tmpFileName); }
            catch(Exception le) { }
            
        }
        
    }
    
    /**
     * Get an archive from data driven input
     * 
     * @param dataDrivenInput
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataArchive getDataArchiveFromDataDrivenInput(DataDrivenInput dataDrivenInput) throws Exception {
       
        DataArchive dataArchive = new CSVDataArchive();
        
        List<String> keys = dataDrivenInput.getDataIds();
        
        Iterator<String> keyIterator = keys.iterator();
        
        while (keyIterator.hasNext()) { 
            
            String key = keyIterator.next();
            
            String[] data = { key, dataDrivenInput.returnInputDataForDataIdAndColumnNumber(key, 1) };
            
            dataArchive.addData(data);
    
        }
            
        return dataArchive;
            
    }
    
    /**
     * Get a data driven object from a properties object
     * 
     * @param properties
     * 
     * @return
     * 
     * @throws Exception 
     */
    public DataDrivenInput getDataDrivenInputFromProperties(Properties properties) throws Exception {
        
        DataArchive dataArchive = new CSVDataArchive(); 
       
        for (String propertyName : properties.stringPropertyNames()) {
            
            String[] s = { propertyName, (String)properties.get(propertyName) };
            
            dataArchive.addData(s);
           
        }
        
        return getDataDrivenInputFromDataArchive(dataArchive);
        
    }
    
    /**
     * Get a string array from a delimited string
     * 
     * @param str
     * @param delimiter
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String[] getArrayFromDelimitedString(String str, String delimiter) throws Exception { 
      
        log.debug("Creating a string array from string: " + str + "|" + delimiter);
        
        Vector<String> v = new Vector<String>();
        
        StringTokenizer st = new StringTokenizer(str, delimiter);
       
        while(st.hasMoreElements()) { 
            
            String s = st.nextToken();
            
            log.debug("Adding string: " + s);
            
            v.add(s); 
        
        }
       
        return v.toArray(new String[v.size()]);
        
    }
    
    /**
     * Convience method for all test needing to know what device is being used
     * 
     * @return 
     */
    public boolean isIOS() { return CommonProperties.getInstance().isIOS(); }
    
    /**
     * Convience method for all test needing to know what device is being used
     * 
     * @return 
     */
    public boolean isAndroid() { return CommonProperties.getInstance().isAndroid(); }
    
    /**
     * Reset the app and reinstall
     * 
     * 2 methods are called because ios and android have different
     * desired capabiltiy key names
     */
    public void enableResetApp() {
        
        CommonProperties.getInstance().setReInstallApp(true);
        CommonProperties.getInstance().setAppNoReset(false);
        
    }
    
    /**
     * Do not reset the app and reinstall
     * 
     * 2 methods are called because ios and android have different
     * desired capabiltiy key names
     */
    public void disableResetApp() {
        
        CommonProperties.getInstance().setReInstallApp(false);
        CommonProperties.getInstance().setAppNoReset(true);
        
    }
    
    public List<String> getRegexPatternMatch(String firstCharacter, String secondCharacter, String s) throws Exception {
         
        List<String> matchList = new ArrayList<String>();

        Pattern regex = Pattern.compile("\\" + firstCharacter + "(.*?)\\" + secondCharacter);

        Matcher regexMatcher = regex.matcher(s);

        while (regexMatcher.find()) {//Finds Matching Pattern in String
   
            matchList.add(regexMatcher.group(1));//Fetching Group from String

        }

        return matchList;
    
    }
    
    public void shuffle(String[] array) {
        
        Random random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) swap(array, i - 1, random.nextInt(i));
        
    }
    
    private static void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public void shuffle(File[] array) {
        
        Random random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) swap(array, i - 1, random.nextInt(i));
        
    }
    
    private static void swap(File[] array, int i, int j) {
        File temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
}
