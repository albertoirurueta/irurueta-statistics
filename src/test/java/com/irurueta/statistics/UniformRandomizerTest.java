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

@SuppressWarnings("Duplicates")
public class UniformRandomizerTest {
    
    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double BIG_RELATIVE_ERROR = 0.7;
    private static final double ABSOLUTE_ERROR = 0.01;
    
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 100;

    private static final int LENGTH = 100;
    
    public UniformRandomizerTest() { }

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
        UniformRandomizer randomizer;
        
        randomizer = new UniformRandomizer(new Random());
        assertNotNull(randomizer);
        
        randomizer = new UniformRandomizer(new SecureRandom());
        assertNotNull(randomizer);
        
        //Force NullPointerException
        randomizer = null;
        try {
            randomizer = new UniformRandomizer(null);
            fail("NullPointerException was expected but not thrown");
        } catch (NullPointerException ignore) { }
        assertNull(randomizer);
    }
    
    @Test
    public void testNextBoolean() {
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = 1.0;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;


        //we assign false = 0.0 and true = 1.0
        double sum = 0.0, sqrSum = 0.0, randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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

    @Test
    public void testFillWithBooleans() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        boolean[] array = new boolean[LENGTH];
        int falseCounter = 0, trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array);

            for(int j = 0; j < LENGTH; j++) {
                if (array[j]) trueCounter++;
                else falseCounter++;
            }
        }

        //check that both true and false are equally probable
        assertEquals((double)trueCounter / (double)(NUM_SAMPLES*LENGTH), 0.5,
                ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)(NUM_SAMPLES*LENGTH), 0.5,
                ABSOLUTE_ERROR);
    }

    @Test
    public void testNextBooleans() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        boolean[] array;
        int falseCounter = 0, trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextBooleans(LENGTH);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) trueCounter++;
                else falseCounter++;
            }
        }

        //check that both true and false are equally probable
        assertEquals((double)trueCounter / (double)(NUM_SAMPLES*LENGTH), 0.5,
                ABSOLUTE_ERROR);
        assertEquals((double)falseCounter / (double)(NUM_SAMPLES*LENGTH), 0.5,
                ABSOLUTE_ERROR);
    }
    
    @Test
    public void testNextInt() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt();
            assertTrue(randomValue < Integer.MAX_VALUE && 
                    randomValue > -Integer.MAX_VALUE);
        }                
    }

    @Test
    public void testFillWithIntegers() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array = new int[LENGTH];
        randomizer.fill(array);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE &&
                    array[i] > -Integer.MAX_VALUE);
        }
    }

    @Test
    public void testNextInts() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array = randomizer.nextInts(LENGTH);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE &&
                    array[i] > -Integer.MAX_VALUE);
        }
    }
    
    @Test
    public void testNextIntWithMaxValue() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
        try {
            randomizer.nextInt(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testFillWithIntegersAndMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array = new int[LENGTH];

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextIntsWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array;

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextInts(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextInts(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
        try {
            randomizer.nextInts(-LENGTH, MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testNextIntWithRange() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
        try {
            randomizer.nextInt(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testFillWithIntegersAndRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array = new int[LENGTH];

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextIntsWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array;

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextInts(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextInts(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testNextLong() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong();
            assertTrue(randomValue < Long.MAX_VALUE && 
                    randomValue > -Long.MAX_VALUE);
        }                
    }

    @Test
    public void testFillWithLongs(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array = new long[LENGTH];
        randomizer.fill(array);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE &&
                    array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    public void testNextLongs(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array = randomizer.nextLongs(LENGTH);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE &&
                    array[i] > -Long.MAX_VALUE);
        }
    }
    
    @Test
    public void testNextLongWithMaxValue() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
        try {
            randomizer.nextLong(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testFillWithLongsAndMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array = new long[LENGTH];

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextLongsWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array;

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextLongs(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextLongs(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testNextLongWithRange() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
        try {
            randomizer.nextLong(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testFillWithLongsAndRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array = new long[LENGTH];

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextLongsWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array;

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextLongs(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextLongs(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testNextFloat() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = 1.0;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        float randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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

    @Test
    public void testFillWithFloats(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array = new float[LENGTH];
        randomizer.fill(array);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE &&
                    array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    public void testNextFloats(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array = randomizer.nextFloats(LENGTH);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE &&
                    array[i] > -Float.MAX_VALUE);
        }
    }
    
    @Test
    public void testNextFloatWithMaxValue() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        float randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
        try {
            randomizer.nextFloat(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    } 

    @Test
    public void testFillWithFloatsAndMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array = new float[LENGTH];

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextFloatsWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array;

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextFloats(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextFloats(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextFloatWithRange() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        float randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
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
        try {
            randomizer.nextFloat(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }    

    @Test
    public void testFillFloatsWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array = new float[LENGTH];

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextFloatsWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array;

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextFloats(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextFloats(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testNextDouble() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = 1.0;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextDouble();
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += randomValue;
            sqrSum += randomValue * randomValue;
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

    @Test
    public void testFillWithDoubles(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array = new double[LENGTH];
        randomizer.fill(array);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Double.MAX_VALUE &&
                    array[i] > -Double.MAX_VALUE);
        }
    }

    @Test
    public void testNextDoubles(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array = randomizer.nextDoubles(LENGTH);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE &&
                    array[i] > -Float.MAX_VALUE);
        }
    }
    
    @Test
    public void testNextDoubleWithMaxValue() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = 0.0;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextDouble(MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += randomValue;
            sqrSum += randomValue * randomValue;
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
        try {
            randomizer.nextDouble(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }    

    @Test
    public void testFillWithDoublesAndMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array = new double[LENGTH];

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextDoublesWithMaxValue(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array;

        double minValue = 0.0;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextDoubles(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextDoubles(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextDoubleWithRange() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        
        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;
        
        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;
        
        double sum = 0.0, sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextDouble(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);
            
            sum += randomValue;
            sqrSum += randomValue * randomValue;
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
        try {
            randomizer.nextDouble(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }     

    @Test
    public void testFillWithDoublesAndRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array = new double[LENGTH];

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextDoublesWithRange(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array;

        double minValue = MIN_VALUE;
        double maxValue = MAX_VALUE;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextDoubles(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //Check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextDoubles(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testGetType() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
    }
    
    @Test
    public void testGetSetInternalRandomizer() {
        Random random = new Random();
        UniformRandomizer randomizer = new UniformRandomizer(random);
        
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
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        long seed = 0;
        randomizer.setSeed(seed);
    }
}
