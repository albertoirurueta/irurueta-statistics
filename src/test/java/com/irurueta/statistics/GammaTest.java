/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.Gamma
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

public class GammaTest {
    
    public static final double ABSOLUTE_ERROR = 1e-8;
    public static final int NUMBER_OF_TRIALS = 10;
    
    public GammaTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}
    
    @Test
    public void testConstructorAndGetGln(){
        Gamma g = new Gamma();
        
        assertEquals(g.getGln(), 0.0, 0.0);
    }
    
    @Test
    public void testGammln(){
        //gamma function is related to factorial for positive natural
        //numbers so that gamma(x) = (x-1)!. Gammln returns its logarithm
        
        assertEquals(Gamma.gammln(1.0), 0.0, 0.0);
        assertEquals(Gamma.gammln(2.0), Math.log(1.0), 0.0);
        assertEquals(Gamma.gammln(3.0), Math.log(2.0 * 1.0), ABSOLUTE_ERROR);
        assertEquals(Gamma.gammln(4.0), Math.log(3.0 * 2.0 * 1.0), 
                ABSOLUTE_ERROR);
        assertEquals(Gamma.gammln(5.0), Math.log(4.0 * 3.0 * 2.0 * 1.0), 
                ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            Gamma.gammln(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testFactrl(){
        assertEquals(Gamma.factrl(0), 1.0, 0.0);
        assertEquals(Gamma.factrl(1), 1.0, 0.0);
        assertEquals(Gamma.factrl(2), 2.0, 0.0);
        assertEquals(Gamma.factrl(3), 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(4), 4.0 * 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(5), 5.0 * 4.0 * 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(6), 6.0 * 5.0 * 4.0 * 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(7), 7.0 * 6.0 * 5.0 * 4.0 * 3.0 * 2.0, 0.0);
        
        //Force IllegalArgumentException
        try{
            Gamma.factrl(-1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            Gamma.factrl(171);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testFactln(){
        //gamma function is related to factorial by gamma(x) = (x - 1)!
        
        for(int i = 1; i < Gamma.MAX_CACHED_LOG_FACTORIALS + 1; i++){
            assertEquals(Gamma.gammln(i), Gamma.factln(i - 1), 0.0);
        }
        
        //Force IllegalArgumentException
        try{
            Gamma.factln(-1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testBico(){
        
        for(int n = 0; n < NUMBER_OF_TRIALS; n++){
            for(int k = 0; k < n; k++){
                assertEquals(Gamma.bico(n, k), 
                        Gamma.factrl(n) / 
                        (Gamma.factrl(k) * Gamma.factrl(n - k)), 
                        ABSOLUTE_ERROR);
            }
        }
        
        //Force IllegalArgumentException
        try{
            Gamma.bico(-1, NUMBER_OF_TRIALS);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            Gamma.bico(NUMBER_OF_TRIALS, -1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            Gamma.bico(NUMBER_OF_TRIALS, NUMBER_OF_TRIALS + 1);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testBeta(){
        
        assertEquals(Gamma.beta(1.0, 2.0),
                Gamma.factrl(0) * Gamma.factrl(1) /
                Gamma.factrl(1 + 2 - 1), ABSOLUTE_ERROR);
        assertEquals(Gamma.beta(2.0, 3.0),
                Gamma.factrl(1) * Gamma.factrl(2) /
                Gamma.factrl(2 + 3 - 1), ABSOLUTE_ERROR);
        assertEquals(Gamma.beta(3.0, 4.0),
                Gamma.factrl(2) * Gamma.factrl(3) /
                Gamma.factrl(3 + 4 - 1), ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            Gamma.beta(0.0, 1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            Gamma.beta(1.0, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testGammpAndGammq() throws MaxIterationsExceededException{
        Gamma g = new Gamma();
        
        assertEquals(g.gammp(1.0, 2.0), 1.0 - g.gammq(1.0, 2.0), 
                ABSOLUTE_ERROR);
        assertEquals(g.gammp(2.0, 3.0), 1.0 - g.gammq(2.0, 3.0), 
                ABSOLUTE_ERROR);
        assertEquals(g.gammp(3.0, 4.0), 1.0 - g.gammq(3.0, 4.0), 
                ABSOLUTE_ERROR);
        
        assertEquals(g.gammp(1.0, 0.0), 0.0, 0.0);
        
        try{
            assertEquals(g.gammp(1.0, Double.POSITIVE_INFINITY), 1.0, 
                    ABSOLUTE_ERROR);
            fail("MaxIterationsExceededException expected but not thrown");
        }catch(MaxIterationsExceededException e){}
    }
    
    @Test
    public void testInvgammp() throws MaxIterationsExceededException{
        Gamma g = new Gamma();
        
        assertEquals(g.invgammp(g.gammp(1.0, 2.0), 1.0), 2.0, 
                ABSOLUTE_ERROR);
        assertEquals(g.invgammp(g.gammp(2.0, 3.0), 2.0), 3.0, 
                ABSOLUTE_ERROR);
        assertEquals(g.invgammp(g.gammp(3.0, 4.0), 3.0), 4.0, 
                ABSOLUTE_ERROR);        
    }
}