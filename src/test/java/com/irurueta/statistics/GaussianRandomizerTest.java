/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.statistics.GaussianRandomizer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 6, 2012
 */
package com.irurueta.statistics;

import java.security.SecureRandom;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class GaussianRandomizerTest {
    
    public static final int NUM_SAMPLES = 1000000;
    public static final double RELATIVE_ERROR = 0.05;
    public static final double ABSOLUTE_ERROR = 0.01;

    public static final double MEAN = 5;
    public static final double STANDARD_DEVIATION = 100.0;
    
    public GaussianRandomizerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testConstructor(){
        GaussianRandomizer randomizer;
        
        //test 1st constructor
        randomizer = new GaussianRandomizer(new Random());
        assertNotNull(randomizer);
        assertEquals(randomizer.getMean(), GaussianRandomizer.DEFAULT_MEAN, 
                0.0);
        assertEquals(randomizer.getStandardDeviation(), 
                GaussianRandomizer.DEFAULT_STANDARD_DEVIATION, 0.0);
        
        randomizer = new GaussianRandomizer(new SecureRandom());
        assertNotNull(randomizer);
        assertEquals(randomizer.getMean(), GaussianRandomizer.DEFAULT_MEAN, 
                0.0);
        assertEquals(randomizer.getStandardDeviation(), 
                GaussianRandomizer.DEFAULT_STANDARD_DEVIATION, 0.0);
        
        
        //Force NullPointerException
        randomizer = null;
        try{
            randomizer = new GaussianRandomizer(null);
            fail("NullPointerException was expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(randomizer);
     
        
        
        //tet 2nd constructor
        randomizer = new GaussianRandomizer(new Random(), MEAN, 
                STANDARD_DEVIATION);
        assertNotNull(randomizer);
        assertEquals(randomizer.getMean(), MEAN, 0.0);
        assertEquals(randomizer.getStandardDeviation(), STANDARD_DEVIATION, 
                0.0);
        
        //Force IllegalArgumentException
        randomizer = null;
        try{
            randomizer = new GaussianRandomizer(new Random(), MEAN, 
                    -STANDARD_DEVIATION);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(randomizer);
    }
    
    @Test
    public void testGetSetMean(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        
        //check default mean
        assertEquals(randomizer.getMean(), GaussianRandomizer.DEFAULT_MEAN, 
                0.0);
        
        //set new mean
        randomizer.setMean(MEAN);
        
        //check correctness
        assertEquals(randomizer.getMean(), MEAN, 0.0);
    }
    
    @Test
    public void testGetSetStandardDeviation(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        
        //check default mean
        assertEquals(randomizer.getStandardDeviation(), 
                GaussianRandomizer.DEFAULT_STANDARD_DEVIATION, 0.0);
        
        //set new standard deviation
        randomizer.setStandardDeviation(STANDARD_DEVIATION);
        
        //check correctness
        assertEquals(randomizer.getStandardDeviation(), STANDARD_DEVIATION, 
                0.0);
        
        //Force IllegalArgumentException
        try{
            randomizer.setStandardDeviation(-STANDARD_DEVIATION);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testNextBoolean(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(), 
                MEAN, STANDARD_DEVIATION);
        
        int falseCounter = 0, trueCounter = 0;
        for(int i = 0; i < NUM_SAMPLES; i++){
            if(randomizer.nextBoolean()) trueCounter++;
            else falseCounter++;
        }
        
        //check that both true and false are equally probable
        assertEquals((double)trueCounter / (double)NUM_SAMPLES, 0.5, 
                ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)NUM_SAMPLES, 0.5, 
                ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNextBooleanWithThreshold(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(), 
                MEAN, STANDARD_DEVIATION);
        
        double threshold = MEAN + STANDARD_DEVIATION;
        //by setting a threshold at 2 standard deviation distance from MEAN, 
        //then by using the erfc function we can determine that true will be
        //generates 84% of the time and false 16%
        double probTrue = 0.8413;
        double probFalse = 1.0 - probTrue;
        
        int falseCounter = 0, trueCounter = 0;
        for(int i = 0; i < NUM_SAMPLES; i++){
            if(randomizer.nextBoolean(threshold)) trueCounter++;
            else falseCounter++;
        }
        
        assertEquals((double)trueCounter / (double)NUM_SAMPLES, probTrue, 
                ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)NUM_SAMPLES, probFalse, 
                ABSOLUTE_ERROR);
        
    }
    
    /*
    @Test
    public void testFillWithBooleansAndThreshold(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextBooleansAndThreshold(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextInt(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation;
        int value;
        for(int i = 0; i < NUM_SAMPLES; i++){
            value = randomizer.nextInt();
            mean += (double)value / (double)NUM_SAMPLES;
            sqrSum += (double)value * (double)value / (double)NUM_SAMPLES;
        }
        
        standardDeviation = Math.sqrt(sqrSum - mean);
        
        assertEquals(mean, MEAN, RELATIVE_ERROR * MEAN);
        assertEquals(standardDeviation, STANDARD_DEVIATION, 
                RELATIVE_ERROR * STANDARD_DEVIATION);        
    }
    
    @Test
    public void testNextLong(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation;
        long value;
        for(int i = 0; i < NUM_SAMPLES; i++){
            value = randomizer.nextLong();
            mean += (double)value / (double)NUM_SAMPLES;
            sqrSum += (double)value * (double)value / (double)NUM_SAMPLES;
        }
        
        standardDeviation = Math.sqrt(sqrSum - mean);
        
        assertEquals(mean, MEAN, RELATIVE_ERROR * MEAN);
        assertEquals(standardDeviation, STANDARD_DEVIATION, 
                RELATIVE_ERROR * STANDARD_DEVIATION);        
    }

    @Test
    public void testNextFloat(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation;
        float value;
        for(int i = 0; i < NUM_SAMPLES; i++){
            value = randomizer.nextFloat();
            mean += (double)value / (double)NUM_SAMPLES;
            sqrSum += (double)value * (double)value / (double)NUM_SAMPLES;
        }
        
        standardDeviation = Math.sqrt(sqrSum - mean);
        
        assertEquals(mean, MEAN, RELATIVE_ERROR * MEAN);
        assertEquals(standardDeviation, STANDARD_DEVIATION, 
                RELATIVE_ERROR * STANDARD_DEVIATION);        
    }

    @Test
    public void testNextDouble(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation, value;
        for(int i = 0; i < NUM_SAMPLES; i++){
            value = randomizer.nextDouble();
            mean += (double)value / (double)NUM_SAMPLES;
            sqrSum += (double)value * (double)value / (double)NUM_SAMPLES;
        }
        
        standardDeviation = Math.sqrt(sqrSum - mean);
        
        assertEquals(mean, MEAN, RELATIVE_ERROR * MEAN);
        assertEquals(standardDeviation, STANDARD_DEVIATION, 
                RELATIVE_ERROR * STANDARD_DEVIATION);        
    }
        
    @Test
    public void testGetType(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
    }
    
    @Test
    public void testGetSetInternalRandomizer(){
        Random random = new Random();
        GaussianRandomizer randomizer = new GaussianRandomizer(random);
        
        assertSame(random, randomizer.getInternalRandom());
        
        //set new random
        random = new SecureRandom();
        randomizer.setInternalRandom(random);
        
        //check correctness
        assertSame(random, randomizer.getInternalRandom());
        
        //Force NullPointerException
        try{
            randomizer.setInternalRandom(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}        
    }
    
    @Test
    public void testSetSeed(){
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        long seed = 0;
        randomizer.setSeed(seed);
    }
    
}
