/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.MaxIterationsExceededException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date May 24, 2015
 */
package com.irurueta.statistics;

/**
 * Exception thrown when maximum number of iterations is exceeded
 */
public class MaxIterationsExceededException extends StatisticsException{
    
    /**
     * Constructor
     */
    public MaxIterationsExceededException(){
        super();
    }
    
    /**
     * Constructor with String containing message
     * @param message message indicating the cause of the exception
     */
    public MaxIterationsExceededException(String message){
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * @param message message describing the cause of the exception
     * @param cause instance containing the cause of the exception
     */
    public MaxIterationsExceededException(String message, Throwable cause){
        super(message, cause);
    }
    
    /**
     * Constructor with cause
     * @param cause instance containing the cause of the exception
     */
    public MaxIterationsExceededException(Throwable cause){
        super(cause);
    }
}
