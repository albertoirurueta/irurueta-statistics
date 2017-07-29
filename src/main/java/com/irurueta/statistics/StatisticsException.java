/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.StatisticsException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date May 24, 2015
 */
package com.irurueta.statistics;

/**
 * Base exception for com.irurueta.statistics package.
 */
public class StatisticsException extends Exception {
    
    /**
     * Constructor.
     */
    public StatisticsException() {
        super();
    }
    
    /**
     * Constructor with String containing message.
     * @param message message indicating the cause of the exception.
     */
    public StatisticsException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause.
     * @param message message describing the cause of the exception.
     * @param cause instance containing the cause of the exception.
     */
    public StatisticsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with cause.
     * @param cause instance containing the cause of the exception.
     */
    public StatisticsException(Throwable cause) {
        super(cause);
    }
}
