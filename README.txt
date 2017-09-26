Automate It! Web/Mobile test automation framework v1.0 - 28 September 2017

Thank you for downloading and using most flexible and powerful Java-based test automation framework using Selenium/Appium for web/mobile application testing.

Comments for improvement are very welcome.

Features:

1) Abstracts use Selenium 3.X WebDriver and Appium mobile drivers using a configuration file (obfuscation of selenium API / no test code changes) for testing web and mobile applications
2) Ability to configure mandatory waits in test execution
3) Handles jquery/prototype page load waiting
4) Handles complexity of waiting for all ajax call completion
5) Data Input and Output for easy data driven input file reading (Excel; xlsx, xls - pipe-delimited; "|", and comma separated values (CSV)) 
6) Special IE handling of user-prompt authentications
7) Page loading performance statistics for each page created during the test, and a summary analysis of all page loading during the executions of all test cases.
8) Debugging help in reports:
   a) selenium/webdriver command list
   b) Screenshots for mobile screen, web browser, and desktop
   c) video recording of test case execution in .mov or .mp4 format (QuickTime and MPEG-4)
9) Easy text-based page content validation

Use this framework for rapid test case automation without detailed knowledge of selenium or webdriver, avoid re-writing of test code by using a single configuration file,
have a better understanding of application user experience with individual page loading statistics for each page during test case execution,
and quick diagnosis of test failures.

How to build the framework: type "ant" and the code will compile and create a jar file with the classes in the frameworks/dist directory.

Copy the jar files from framework/lib and framework/dist and also copy the framework/conf directory to your test project.

