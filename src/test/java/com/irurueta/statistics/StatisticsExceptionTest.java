/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.StatisticsException
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

public class StatisticsExceptionTest {
    
    public StatisticsExceptionTest() {}
    
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
        StatisticsException ex;
        assertNotNull(ex = new StatisticsException());
        
        ex = null;
        assertNotNull(ex = new StatisticsException("message"));
        
        ex = null;
        assertNotNull(ex = new StatisticsException(new Exception()));
        
        ex = null;
        assertNotNull(ex = new StatisticsException("message", new Exception()));
    }
}
