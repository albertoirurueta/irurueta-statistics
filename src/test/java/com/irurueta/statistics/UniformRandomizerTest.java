/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.statistics.UniformRandomizer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 6, 2012
 */
package com.irurueta.statistics;

import java.security.SecureRandom;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class UniformRandomizerTest {
    
    public static final int NUM_SAMPLES = 1000000;
    public static final double RELATIVE_ERROR = 0.05;
    public static final double BIG_RELATIVE_ERROR = 0.7;
    
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 100;
    
    public UniformRandomizerTest() {
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
        UniformRandomizer randomizer;
        
        randomizer = new UniformRandomizer(new Random());
        assertNotNull(randomizer);
        
        randomizer = new UniformRandomizer(new SecureRandom());
        assertNotNull(randomizer);
        
        //Force NullPointerException
        randomizer = null;
        try{
            randomizer = new UniformRandomizer(null);
            fail("NullPointerException was expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(randomizer);
    }
    
    @Test
    public void testNextBoolean(){
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = 1.0;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;


        //we assign false = 0.0 and true = 1.0
        double sum = 0.0, sqrSum = 0.0, randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            if(randomizer.nextBoolean()) randomValue = 1.0;
            else randomValue = 0.0;
            
            sum += randomValue;
            sqrSum += randomValue * randomValue;            
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
	double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) / 
                ((double)NUM_SAMPLES - 1.0);

        //Check correctness of results.
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance, 
                estimatedVariance * BIG_RELATIVE_ERROR);
    }
    
    /*
    @Test
    public void testFillWithBooleans(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextBooleans(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextInt(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        int randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextInt();
            assertTrue(randomValue < Integer.MAX_VALUE && 
                    randomValue > -Integer.MAX_VALUE);
        }                
    }
    
    /*
    @Test
    public void testFillWithInts(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextInts(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextIntWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        int randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextInt(MAX_VALUE);
            assertTrue(randomValue >= 0);
            assertTrue(randomValue < MAX_VALUE);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextInt(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    /*
    @Test
    public void testFillWithIntsAndMaxValue(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextIntsWithMaxValue(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextIntWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        int randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextInt(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= MIN_VALUE);
            assertTrue(randomValue < MAX_VALUE);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextInt(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    /*
    @Test
    public void testFillWithIntsAndRange(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextIntsWithRange(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextLong(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        long randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextLong();
            assertTrue(randomValue < Long.MAX_VALUE && 
                    randomValue > -Long.MAX_VALUE);
        }                
    }
    
    /*
    @Test
    public void testFillWithLongs(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextLongs(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextLongWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        long randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextLong(MAX_VALUE);
            assertTrue(randomValue >= 0);
            assertTrue(randomValue < MAX_VALUE);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextLong(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    /*
    @Test
    public void testFillWithLongsAndMaxValue(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextLongsWithMaxValue(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextLongWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        long randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextLong(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= MIN_VALUE);
            assertTrue(randomValue < MAX_VALUE);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextLong(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }
    
    /*
    @Test
    public void testFillWithLongsAndRange(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextLongsWithRange(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextFloat(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = 1.0;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        float randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextFloat();
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);        
    }    
    
    /*
    @Test
    public void testFillWithFloats(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextFloats(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextFloatWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        float randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextFloat(MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextFloat(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    } 
    
    /*
    @Test
    public void testFillWithFloatsAndMaxValue(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextFloatsWithMaxValue(){
        //TODO: make test
    }
    */

    @Test
    public void testNextFloatWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        float randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextFloat(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextFloat(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }    
    
    /*
    @Test
    public void testFillFloatsWithRange(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextFloatsWithRange(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextDouble(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = 1.0;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        double randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextDouble();
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
    }    
    
    /*
    @Test
    public void testFillWithDoubles(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextDoubles(){
        //TODO: make test
    }
    */
    
    @Test
    public void testNextDoubleWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        double randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextDouble(MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextDouble(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }    
    
    /*
    @Test
    public void testFillWithDoublesAndMaxValue(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextDoublesWithMaxValue(){
        //TODO: make test
    }
    */

    @Test
    public void testNextDoubleWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        double randomValue;
        for(int i = 0; i < NUM_SAMPLES; i++){
            randomValue = randomizer.nextDouble(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += (double)randomValue;
            sqrSum += (double)randomValue * (double)randomValue;
        }
        
        double estimatedMeanValue = sum / (double)NUM_SAMPLES;
        double estimatedVariance = (sqrSum - (double)NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)NUM_SAMPLES - 1.0);
        
        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, 
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
        
        //Force IllegalArgumentException
        try{
            randomizer.nextDouble(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        }catch(IllegalArgumentException e){}        
    }     
    
    /*
    @Test
    public void testFillWithDoublesAndRange(){
        //TODO: make test
    }
    */
    
    /*
    @Test
    public void testNextDoublesWithRange(){
        //TODO: make test
    }
    */
    
    @Test
    public void testGetType(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
    }
    
    @Test
    public void testGetSetInternalRandomizer(){
        Random random = new Random();
        UniformRandomizer randomizer = new UniformRandomizer(random);
        
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
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        long seed = 0;
        randomizer.setSeed(seed);
    }
}
