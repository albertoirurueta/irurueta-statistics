/*
 * Copyright (C) 2012 Alberto Irurueta Carro (alberto@irurueta.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.irurueta.statistics;

import org.junit.*;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.Assert.*;

public class GaussianRandomizerTest {
    
    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double ABSOLUTE_ERROR = 0.01;

    private static final double MEAN = 5;
    private static final double STANDARD_DEVIATION = 100.0;

    private static final int LENGTH = 100;
    
    public GaussianRandomizerTest() { }

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }
    
    @Test
    public void testConstructor() {
        GaussianRandomizer randomizer;
        
        //test 1st constructor
        randomizer = new GaussianRandomizer(new Random());
        assertNotNull(randomizer);
        assertEquals(randomizer.getmMean(), GaussianRandomizer.DEFAULT_MEAN,
                0.0);
        assertEquals(randomizer.getStandardDeviation(),
                GaussianRandomizer.DEFAULT_STANDARD_DEVIATION, 0.0);
        
        randomizer = new GaussianRandomizer(new SecureRandom());
        assertNotNull(randomizer);
        assertEquals(randomizer.getmMean(), GaussianRandomizer.DEFAULT_MEAN,
                0.0);
        assertEquals(randomizer.getStandardDeviation(),
                GaussianRandomizer.DEFAULT_STANDARD_DEVIATION, 0.0);
        
        
        //Force NullPointerException
        randomizer = null;
        try {
            randomizer = new GaussianRandomizer(null);
            fail("NullPointerException was expected but not thrown");
        } catch (NullPointerException ignore) { }
        assertNull(randomizer);
     
        
        
        //tet 2nd constructor
        randomizer = new GaussianRandomizer(new Random(), MEAN, 
                STANDARD_DEVIATION);
        assertNotNull(randomizer);
        assertEquals(randomizer.getmMean(), MEAN, 0.0);
        assertEquals(randomizer.getStandardDeviation(), STANDARD_DEVIATION,
                0.0);
        
        //Force IllegalArgumentException
        randomizer = null;
        try {
            randomizer = new GaussianRandomizer(new Random(), MEAN, 
                    -STANDARD_DEVIATION);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        assertNull(randomizer);
    }
    
    @Test
    public void testGetSetMean() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        
        //check default mean
        assertEquals(randomizer.getmMean(), GaussianRandomizer.DEFAULT_MEAN,
                0.0);
        
        //set new mean
        randomizer.setmMean(MEAN);
        
        //check correctness
        assertEquals(randomizer.getmMean(), MEAN, 0.0);
    }
    
    @Test
    public void testGetSetStandardDeviation() {
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
        try {
            randomizer.setStandardDeviation(-STANDARD_DEVIATION);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testNextBoolean() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(), 
                MEAN, STANDARD_DEVIATION);
        
        int falseCounter = 0, trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            if (randomizer.nextBoolean()) trueCounter++;
            else falseCounter++;
        }
        
        //check that both true and false are equally probable
        assertEquals((double)trueCounter / (double)NUM_SAMPLES, 0.5, 
                ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)NUM_SAMPLES, 0.5, 
                ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNextBooleanWithThreshold() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(), 
                MEAN, STANDARD_DEVIATION);
        
        double threshold = MEAN + STANDARD_DEVIATION;
        //by setting a threshold at 2 standard deviation distance from MEAN, 
        //then by using the erfc function we can determine that true will be
        //generates 84% of the time and false 16%
        double probTrue = 0.8413;
        double probFalse = 1.0 - probTrue;
        
        int falseCounter = 0, trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            if (randomizer.nextBoolean(threshold)) trueCounter++;
            else falseCounter++;
        }
        
        assertEquals((double)trueCounter / (double)NUM_SAMPLES, probTrue, 
                ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)NUM_SAMPLES, probFalse, 
                ABSOLUTE_ERROR);
    }

    @Test
    public void testFillWithBooleansAndThreshold() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        boolean[] array = new boolean[LENGTH];

        double threshold = MEAN + STANDARD_DEVIATION;
        //by setting a threshold at 2 standard deviation distance from MEAN,
        //then by using the erfc function we can determine that true will be
        //generates 84% of the time and false 16%
        double probTrue = 0.8413;
        double probFalse = 1.0 - probTrue;

        int falseCounter = 0, trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, threshold);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) trueCounter++;
                else falseCounter++;
            }
        }

        assertEquals((double)trueCounter / (double)(NUM_SAMPLES * LENGTH),
                probTrue, ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)(NUM_SAMPLES * LENGTH),
                probFalse, ABSOLUTE_ERROR);
    }

    @Test
    public void testNextBooleansAndThreshold() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        boolean[] array;

        double threshold = MEAN + STANDARD_DEVIATION;
        //by setting a threshold at 2 standard deviation distance from MEAN,
        //then by using the erfc function we can determine that true will be
        //generates 84% of the time and false 16%
        double probTrue = 0.8413;
        double probFalse = 1.0 - probTrue;

        int falseCounter = 0, trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextBooleans(LENGTH, threshold);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) trueCounter++;
                else falseCounter++;
            }
        }

        assertEquals((double)trueCounter / (double)(NUM_SAMPLES * LENGTH),
                probTrue, ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)(NUM_SAMPLES * LENGTH),
                probFalse, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNextInt() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation;
        int value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
    public void testNextLong() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation;
        long value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
    public void testNextFloat() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation;
        float value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
    public void testNextDouble() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);
        
        double mean = 0.0, sqrSum = 0.0, standardDeviation, value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            value = randomizer.nextDouble();
            mean += value / (double)NUM_SAMPLES;
            sqrSum += value * value / (double)NUM_SAMPLES;
        }
        
        standardDeviation = Math.sqrt(sqrSum - mean);
        
        assertEquals(mean, MEAN, RELATIVE_ERROR * MEAN);
        assertEquals(standardDeviation, STANDARD_DEVIATION, 
                RELATIVE_ERROR * STANDARD_DEVIATION);        
    }
        
    @Test
    public void testGetType() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
    }
    
    @Test
    public void testGetSetInternalRandomizer() {
        Random random = new Random();
        GaussianRandomizer randomizer = new GaussianRandomizer(random);
        
        assertSame(random, randomizer.getInternalRandom());
        
        //set new random
        random = new SecureRandom();
        randomizer.setInternalRandom(random);
        
        //check correctness
        assertSame(random, randomizer.getInternalRandom());
        
        //Force NullPointerException
        try {
            randomizer.setInternalRandom(null);
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) { }
    }
    
    @Test
    public void testSetSeed() {
        GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        long seed = 0;
        randomizer.setSeed(seed);
    }
    
}
