/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.MaxIterationsExceededException
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date May 24, 2015
 */
package com.irurueta.statistics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaxIterationsExceededExceptionTest {
    
    public MaxIterationsExceededExceptionTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}

    @Test
    public void testConstructor(){
        MaxIterationsExceededException ex;
        assertNotNull(ex = new MaxIterationsExceededException());
        
        ex = null;
        assertNotNull(ex = new MaxIterationsExceededException("message"));
        
        ex = null;
        assertNotNull(ex = new MaxIterationsExceededException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new MaxIterationsExceededException("message", 
                new Exception()));
    }
}
