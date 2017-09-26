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

/**
 * This class is used as a special exception to identify a problem when 
 * validating page rendering text or response content.
 * 
 * @author mburnside@Automate It!
 */
public class ValidatorException extends Exception {
    
    /**
     * Default Constructor.
     * 
     * Constructs a new exception with null as its detail message.
     */
    public ValidatorException() { super(); }
    
    /**
     * Copy constructor.
     * 
     * Constructs a new exception with the specified detail message.
     * 
     * @param message
     */
    public ValidatorException(String message) { super(message); }
    
    /**
     * Copy constructor.
     * 
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param message
     * @param cause
     */
    public ValidatorException(String message, Throwable cause) { super(message, cause); }
    
    /**
     * Copy constructor.
     * 
     * Constructs a new exception with the specified detail message, cause, 
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
    
    /**
     * Copy constructor.
     * 
     * Constructs a new exception with the specified cause and a detail message 
     * of (cause==null ? null : cause.toString()) (which typically contains the 
     * class and detail message of cause).
     * 
     * @param cause
     */
    public ValidatorException(Throwable cause) { super(cause); }
    
}