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
package org.automateit.web.validator;

import org.apache.log4j.Logger;

import org.automateit.web.pages.BasePage;
import org.automateit.data.DataDrivenInput;

/**
 * This class is the base class for all view validators.
 * 
 * The validator simply iterates through 0-500 indicated values, with names
 * that have a unique prefix.
 * 
 * It uses text searching in the response / rendered page.
 * 
 * @author mburnside@Automate It!
 */
public class BaseViewValidator {
    
    /**
     * Logging class
     */
    private final Logger logger = Logger.getLogger(BaseViewValidator.class);

    /**
     * Execute the page content validation.
     * 
     * @param basePage
     * @param dataDrivenInput
     * @param keyPrefix
     * 
     * @throws ValidatorException 
     */
    public void execute(BasePage basePage, DataDrivenInput dataDrivenInput, String keyPrefix) throws ValidatorException {
        
        try {
            
            if(basePage == null) throw new ValidatorException("BasePage object is null. Please initialize properly.");
            
            if(dataDrivenInput == null) throw new ValidatorException("DataDrivenInput object is null. Please initialize properly.");
            
            if(keyPrefix == null) throw new ValidatorException("keyPrefix is null. Please initialize properly.");
  
            logger.info("Starting validation for data with key prefix: " + keyPrefix);
            
            for(int i = 0; i < 500; i++) {
                
                if(dataDrivenInput.hasDataId(keyPrefix + String.valueOf(i))) {
                    
                    try { basePage.assertText(dataDrivenInput.returnInputDataForDataIdAndColumnNumber(keyPrefix + String.valueOf(i), 1)); }
                    catch(Exception e) { throw new ValidatorException("Assertion/validation failed for: " + dataDrivenInput.returnInputDataForDataIdAndColumnNumber(keyPrefix + String.valueOf(i), 1)); }
                
                }
                
            }
            
            logger.info("Completed validation without errors for data with key prefix: " + keyPrefix);
    
        }
        catch(Exception e) { throw new ValidatorException(e.getMessage(), e); }
  
    }
    
}
