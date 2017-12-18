Automate It! Web/Mobile Test Automation Framework v1.2.0 - 16 December 2017

Thank you for downloading and using most flexible and powerful Java-based test automation framework using Selenium/Appium for web/mobile application testing.

Comments for improvement are very welcome.

Features:

1) Drive automated tasks using latest and greatest Selenium WebDriver and Appium Mobile Drivers using a configuration file 
2) Obfuscation of selenium API / no test code changes for testing web and mobile applications
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

Third Party API Dependency Versions:

FirefoxDriver
-------------
v0.19.1
https://github.com/mozilla/geckodriver/releases
https://github.com/mozilla/geckodriver/releases/tag/v0.19.1

ChromeDriver
-------------
v2.34
https://sites.google.com/a/chromium.org/chromedriver/downloads
https://chromedriver.storage.googleapis.com/index.html?path=2.34/

SafariDriver
------------
Latest version of SafariDriver comes embedded in latest Safari browser

Selenium Server
---------------
v3.7.1
https://goo.gl/hvDPsK
http://www.seleniumhq.org/download/

Appium Drivers
--------------
v6.0.0-BETA
http://appium.io/downloads.html
https://search.maven.org/#search%7Cga%7C1%7Cg%3Aio.appium%20a%3Ajava-client

Log4J
------
v1.2.17




