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
public class RandomizerTest {

    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double ABSOLUTE_ERROR = 0.01;

    private static final int LENGTH = 100;

    public RandomizerTest() { }

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }
    
    @Test
    public void testCreate() {
        
        Randomizer randomizer;
        
        //test create without parameters
        randomizer = Randomizer.create();
        if (Randomizer.USE_SECURE_RANDOM_BY_DEFAULT) {
            assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        } else {
            assertNotNull(randomizer.getInternalRandom());
        }        
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);
        
        //test create with secure parameter
        randomizer = Randomizer.create(false);
        assertNotNull(randomizer.getInternalRandom());
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);

        randomizer = Randomizer.create(true);
        assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);
        
        //test create with Random
        Random random = new SecureRandom();
        randomizer = Randomizer.create(random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);
        
        //Force NullPointerException
        randomizer = null;
        try {
            randomizer = Randomizer.create((Random)null);
            fail("NullPointerExceptioen expected but not thrown");
        } catch (NullPointerException ignore) { }
        //noinspection all
        assertNull(randomizer);
        
        //test create with RandomizerType
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER);
        if (Randomizer.USE_SECURE_RANDOM_BY_DEFAULT) {
            assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        } else {
            assertNotNull(randomizer.getInternalRandom());
        }        
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER);
        if (Randomizer.USE_SECURE_RANDOM_BY_DEFAULT) {
            assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        } else {
            assertNotNull(randomizer.getInternalRandom());
        }        
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        
        //test create with RandomizerType and secure parameter
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                false);
        assertNotNull(randomizer.getInternalRandom());
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                true);
        assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                false);
        assertNotNull(randomizer.getInternalRandom());
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                true);
        assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        
        //test create with RandomizerType and internal Random
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER, 
                random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        //Force NullPointerException        
        randomizer = null;
        try {
            randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                    null);
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) { }
        //noinspection all
        assertNull(randomizer);
        try {
            randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                    null);
            fail("NullPointerException expected but not thrown");
        } catch (NullPointerException ignore) { }
        //noinspection all
        assertNull(randomizer);
        
    }
    
    @Test
    public void testGetSetInternalRandomizer() {
        Random random = new Random();        
        Randomizer randomizer = Randomizer.create(random);
        
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
        Randomizer randomizer = Randomizer.create();
        long seed = 0;
        randomizer.setSeed(seed);
    }

    @Test
    public void testNextBoolean() {
        Randomizer randomizer = Randomizer.create();

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
    public void testFillWithBooleans() {
        Randomizer randomizer = Randomizer.create();

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
        Randomizer randomizer = Randomizer.create();

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

        //Force IllegalArgumentException
        try {
            randomizer.nextBooleans(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextInt() {
        Randomizer randomizer = Randomizer.create();

        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt();
            assertTrue(randomValue < Integer.MAX_VALUE &&
            randomValue > -Integer.MAX_VALUE);
        }
    }

    @Test
    public void testFillWithIntegers() {
        Randomizer randomizer = Randomizer.create();

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
        Randomizer randomizer = Randomizer.create();

        int[] array = randomizer.nextInts(LENGTH);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE &&
                    array[i] > -Integer.MAX_VALUE);
        }

        //Force IllegalArgumentException
        try {
            randomizer.nextInts(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextLong() {
        Randomizer randomizer = Randomizer.create();

        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong();
            assertTrue(randomValue < Long.MAX_VALUE &&
                    randomValue > -Long.MAX_VALUE);
        }
    }

    @Test
    public void testFillWithLongs() {
        Randomizer randomizer = Randomizer.create();

        long[] array = new long[LENGTH];
        randomizer.fill(array);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE &&
                    array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    public void testNextLongs() {
        Randomizer randomizer = Randomizer.create();

        long[] array = randomizer.nextLongs(LENGTH);

        //check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE &&
                    array[i] > -Long.MAX_VALUE);
        }

        //Force IllegalArgumentException
        try {
            randomizer.nextLongs(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextFloat() {
        Randomizer randomizer = Randomizer.create();

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

        //check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    public void testFillWithFloats() {
        Randomizer randomizer = Randomizer.create();

        float[] array = new float[LENGTH];

        //check correctness
        double minValue = 0.0;
        double maxValue = 1.0;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //check correctnes of results by checking against the expected mean and
        //variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    public void testNextFloats() {
        Randomizer randomizer = Randomizer.create();

        double minValue = 0.0;
        double maxValue = 1.0;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        float[] array;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextFloats(LENGTH);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += (double)array[j];
                sqrSum += (double)array[j] * (double)array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //check correctnes of results by checking against the expected mean and
        //variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextFloats(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testNextDouble() {
        Randomizer randomizer = Randomizer.create();

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

        //check correctness of results by checking against the expected mean
        //and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    public void testFillWithDoubles() {
        Randomizer randomizer = Randomizer.create();

        double[] array = new double[LENGTH];

        //check correctness
        double minValue = 0.0;
        double maxValue = 1.0;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //check correctnes of results by checking against the expected mean and
        //variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    public void testNextDoubles() {
        Randomizer randomizer = Randomizer.create();

        double minValue = 0.0;
        double maxValue = 1.0;

        double meanValue = 0.5 * (minValue + maxValue);
        double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0, sqrSum = 0.0;
        double[] array;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextDoubles(LENGTH);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        double estimatedMeanValue = sum / (double)(NUM_SAMPLES * LENGTH);
        double estimatedVariance = (sqrSum - (double)(NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double)(NUM_SAMPLES * LENGTH) - 1.0);

        //check correctnes of results by checking against the expected mean and
        //variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance,
                estimatedVariance * RELATIVE_ERROR);

        //Force IllegalArgumentException
        try {
            randomizer.nextDoubles(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException ignore) { }
    }
}
