Automate It! Web/Mobile Test Automation Framework v2.2 - 10 Mar 2022

Thank you for downloading and using the most versatile and easy Java-based test automation framework using Selenium/Appium for web/mobile application testing.

Features:

1) Drive automated tasks using latest and greatest Selenium WebDriver and Appium Mobile Drivers using a configuration file 
2) Obfuscation of selenium/appium API's - no test code changes for testing web and mobile applications
3) Ability to configure mandatory waits in test execution
4) Handles jquery/prototype page load waiting
5) Handles complexity of waiting for all ajax call completion
6) Data Input and Output for easy data driven input file reading (Excel; xlsx, xls - pipe-delimited; "|", and comma separated values (CSV)) 
7) Special IE handling of user-prompt authentications
8) Page loading performance statistics for each page created during the test, and a summary analysis of all page loading during the executions of all test cases.
9) Debugging help in reports:
   a) selenium/webdriver command list
   b) Screenshots for mobile screen, web browser, and desktop
   c) video recording of test case execution in .mov or .mp4 format (QuickTime and MPEG-4)
10) Easy text-based page content validation
11) Text-To-Speech conversion and player to send audio commands to Siri, Alexa, etc.
12) Speech-To-Text to listen to response from app and translate to text for response validation.
13) OCR (Optical Character Recognition)
14) Scrolling web pages up/down from Safari, Chrome, Firefox web browsers
15) Special styled reporting using Extent-based reporting framework for really nice and professional test result reports including easy to include screenshots from web and mobile application
16) Alert Handlers in Zabbix, Email, and SMS for alerts of specific user-defined special events during or after tests (failed or skipped) 
17) Cucumber/Gherkin support (see examples: ant cucumber_demo)

This framework allows anyone with basic Java programming skills to write automated tests without needing any knowledge of Selenium or Appium WebDrivers.

a) Avoid re-writing of test code by using a single configuration file to drive tests across all browsers and mobile devices.
b) Have a better understanding of application user experience with individual page loading statistics for each page during test case execution.
c) Quick diagnosis of test failures.

Required Software Installation:

1) Java Development Kit - http://www.oracle.com/technetwork/java/javase/downloads/index.html 
2) Ant - http://ant.apache.org

Instructions to build the framework:

It's very simple to build and use. After downloading the code, type "ant" and the code will compile and create a jar file with the classes in the "dist" directory.

Copy the jar files from "lib" and "dist" and also copy the configuration files "conf" directory to your test project and modify them for your test settings.

Instructions to run the web-app-based testing examples:

1) In the installation directory, type "ant example_safari", "ant example_chrome", "ant_example_firefox" for each browser.

Instructions to run the mobile-app-based testing examples:

1) In the installation directory, type "ant ios", "ant android" for each mobile app. For your specific environment, you may need to edit the configuration files in "conf" directory.

Third Party API Dependency Versions:

FirefoxDriver
-------------
0.30.0
https://github.com/mozilla/geckodriver/releases
https://github.com/mozilla/geckodriver/releases/tag/v0.30.0

ChromeDriver
-------------
99.0.4844.51
https://sites.google.com/chromium.org/driver/downloads
https://chromedriver.storage.googleapis.com/index.html?path=99.0.4844.51/

SafariDriver
------------
Latest version of SafariDriver comes embedded in latest Safari browser

Edge Driver
-----------
99.0.1150.39
https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/

Selenium Server
---------------
4.1.2
http://www.seleniumhq.org/download/

Appium Drivers
--------------
v7.3.0
http://appium.io/downloads.html
https://search.maven.org/search?q=g:io.appium

Log4J
------
Apache Log4j API 2.10.0 API
https://logging.apache.org/log4j/2.x/download.html

JavaMail
--------
 JavaMail API 1.4.5





