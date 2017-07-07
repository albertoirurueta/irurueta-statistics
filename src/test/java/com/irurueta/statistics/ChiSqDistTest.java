/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.ChiSqDist
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date December 28, 2015
 */
package com.irurueta.statistics;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSqDistTest {
    
    public static final double MIN_RANDOM_VALUE = 0.0;
    public static final double MAX_RANDOM_VALUE = 10.0;
    
    public static final double ABSOLUTE_ERROR = 1e-6;
    public static final double LARGE_ABSOLUTE_ERROR = 1e-3;
    
    public ChiSqDistTest() {}
    
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
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        ChiSqDist dist = new ChiSqDist(nu);
        
        assertEquals(dist.getNu(), nu, 0.0);
        
        //Force IllegalArgumentException
        dist = null;
        try{
            dist = new ChiSqDist(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(dist);
    }
    
    @Test
    public void testGetSetNu(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double nu2 = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        ChiSqDist dist = new ChiSqDist(nu);
        
        //check initial value
        assertEquals(dist.getNu(), nu, 0.0);
        
        //set new value
        dist.setNu(nu2);
        
        //check correctness
        assertEquals(dist.getNu(), nu2, 0.0);
        
        //Force IllegalArgumentException
        try{
            dist.setNu(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testP(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double x2 = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        ChiSqDist dist = new ChiSqDist(nu);
        
        //check 
        assertEquals(dist.p(x2), ChiSqDist.p(x2, nu), 0.0);
        
        assertEquals(dist.p(Double.MAX_VALUE), 0.0, ABSOLUTE_ERROR);
        assertEquals(ChiSqDist.p(Double.MAX_VALUE, nu), 0.0, 
                ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            dist.p(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            ChiSqDist.p(0.0, nu);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            ChiSqDist.p(x2, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testCdf() throws MaxIterationsExceededException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double x2a = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double x2b = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        double x2min = x2a < x2b ? x2a : x2b;
        double x2max = x2a >= x2b ? x2a : x2b;
        
        ChiSqDist dist = new ChiSqDist(nu);
        
        //check correctness
        assertEquals(dist.cdf(x2min), ChiSqDist.cdf(x2min, nu), 0.0);
        assertEquals(dist.cdf(x2max), ChiSqDist.cdf(x2max, nu), 0.0);
        
        assertTrue(dist.cdf(x2min) <= dist.cdf(x2max));
        assertTrue(ChiSqDist.cdf(x2min, nu) <= ChiSqDist.cdf(x2max, nu));
        
        assertTrue(dist.cdf(x2min) >= 0.0 && dist.cdf(x2min) <= 1.0);
        assertTrue(dist.cdf(x2max) >= 0.0 && dist.cdf(x2max) <= 1.0);
        
        //Force IllegalArgumentException
        try{
            dist.cdf(-1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            ChiSqDist.cdf(-1.0, nu);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            ChiSqDist.cdf(x2min, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testInvcdf() throws MaxIterationsExceededException{
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double x2 = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double p = randomizer.nextDouble(); //between 0.0 and 1.0

        ChiSqDist dist = new ChiSqDist(nu);
        
        assertEquals(dist.invcdf(dist.cdf(x2)), x2, ABSOLUTE_ERROR);
        assertEquals(dist.cdf(dist.invcdf(p)), p, ABSOLUTE_ERROR);
        
        assertEquals(ChiSqDist.invcdf(ChiSqDist.cdf(x2, nu), nu), x2, 
                ABSOLUTE_ERROR);
        assertEquals(ChiSqDist.cdf(ChiSqDist.invcdf(p, nu), nu), p, 
                ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            ChiSqDist.invcdf(p, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            ChiSqDist.invcdf(-1.0, nu);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            ChiSqDist.invcdf(1.0, nu);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(-1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
}