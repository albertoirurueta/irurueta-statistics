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

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.Assert.*;

public class UniformRandomizerTest {

    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double BIG_RELATIVE_ERROR = 0.7;
    private static final double ABSOLUTE_ERROR = 0.01;

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 100;

    private static final int LENGTH = 100;

    @Test
    public void testConstructor() {
        UniformRandomizer randomizer;

        // test 1st constructor
        randomizer = new UniformRandomizer();
        assertNotNull(randomizer);

        // test 2nd constructor
        randomizer = new UniformRandomizer(new Random());
        assertNotNull(randomizer);

        randomizer = new UniformRandomizer(new SecureRandom());
        assertNotNull(randomizer);

        //Force NullPointerException
        randomizer = null;
        try {
            randomizer = new UniformRandomizer(null);
            fail("NullPointerException was expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        //noinspection ConstantConditions
        assertNull(randomizer);
    }

    @Test
    public void testNextBoolean() {

        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double meanValue = 0.5;
        final double variance = 1.0 / 12.0;


        // we assign false = 0.0 and true = 1.0
        double sum = 0.0;
        double sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            if (randomizer.nextBoolean()) {
                randomValue = 1.0;
            } else {
                randomValue = 0.0;
            }

            sum += randomValue;
            sqrSum += randomValue * randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results.
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * BIG_RELATIVE_ERROR);
    }

    @Test
    public void testFillWithBooleans() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final boolean[] array = new boolean[LENGTH];
        int falseCounter = 0;
        int trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) {
                    trueCounter++;
                } else {
                    falseCounter++;
                }
            }
        }

        // check that both true and false are equally probable
        assertEquals(0.5, (double) trueCounter / (double) (NUM_SAMPLES * LENGTH),
                ABSOLUTE_ERROR);
        assertEquals(0.5, (double) falseCounter / (double) (NUM_SAMPLES * LENGTH),
                ABSOLUTE_ERROR);
    }

    @Test
    public void testNextBooleans() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        boolean[] array;
        int falseCounter = 0;
        int trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextBooleans(LENGTH);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) {
                    trueCounter++;
                } else {
                    falseCounter++;
                }
            }
        }

        // check that both true and false are equally probable
        assertEquals(0.5, (double) trueCounter / (double) (NUM_SAMPLES * LENGTH),
                ABSOLUTE_ERROR);
        assertEquals(0.5, (double) falseCounter / (double) (NUM_SAMPLES * LENGTH),
                ABSOLUTE_ERROR);
    }

    @Test
    public void testNextInt() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt();
            assertTrue(randomValue < Integer.MAX_VALUE &&
                    randomValue > -Integer.MAX_VALUE);
        }
    }

    @Test
    public void testFillWithIntegers() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final int[] array = new int[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE &&
                    array[i] > -Integer.MAX_VALUE);
        }
    }

    @Test
    public void testNextInts() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final int[] array = randomizer.nextInts(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE &&
                    array[i] > -Integer.MAX_VALUE);
        }
    }

    @Test
    public void testNextIntWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt(MAX_VALUE);
            assertTrue(randomValue >= 0);
            assertTrue(randomValue < MAX_VALUE);

            sum += randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextInt(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithIntegersAndMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final int[] array = new int[LENGTH];

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextIntsWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array;

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextInts(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextInts(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        try {
            randomizer.nextInts(-LENGTH, MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextIntWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= MIN_VALUE);
            assertTrue(randomValue < MAX_VALUE);

            sum += randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextInt(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithIntegersAndRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final int[] array = new int[LENGTH];

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextIntsWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        int[] array;

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextInts(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextInts(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextLong() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong();
            assertTrue(randomValue < Long.MAX_VALUE &&
                    randomValue > -Long.MAX_VALUE);
        }
    }

    @Test
    public void testFillWithLongs() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final long[] array = new long[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE &&
                    array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    public void testNextLongs() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final long[] array = randomizer.nextLongs(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE &&
                    array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    public void testNextLongWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong(MAX_VALUE);
            assertTrue(randomValue >= 0);
            assertTrue(randomValue < MAX_VALUE);

            sum += (double) randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextLong(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithLongsAndMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final long[] array = new long[LENGTH];

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double) array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextLongsWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array;

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextLongs(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double) array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextLongs(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextLongWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= MIN_VALUE);
            assertTrue(randomValue < MAX_VALUE);

            sum += (double) randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextLong(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithLongsAndRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final long[] array = new long[LENGTH];

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double) array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextLongsWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        long[] array;

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextLongs(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += (double) array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextLongs(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextFloat() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = 0.0;
        final double maxValue = 1.0;

        final double meanValue = 0.5;
        final double variance = 1.0 / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        float randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextFloat();
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);

            sum += randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    public void testFillWithFloats() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final float[] array = new float[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE &&
                    array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    public void testNextFloats() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final float[] array = randomizer.nextFloats(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE &&
                    array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    public void testNextFloatWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        float randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextFloat(MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);

            sum += randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextFloat(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithFloatsAndMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final float[] array = new float[LENGTH];

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextFloatsWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array;

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextFloats(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextFloats(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextFloatWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        float randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextFloat(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);

            sum += randomValue;
            sqrSum += (double) randomValue * (double) randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextFloat(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillFloatsWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final float[] array = new float[LENGTH];

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextFloatsWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        float[] array;

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextFloats(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextFloats(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextDouble() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = 0.0;
        final double maxValue = 1.0;

        final double meanValue = 0.5;
        final double variance = 1.0 / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextDouble();
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);

            sum += randomValue;
            sqrSum += randomValue * randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    public void testFillWithDoubles() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double[] array = new double[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Double.MAX_VALUE &&
                    array[i] > -Double.MAX_VALUE);
        }
    }

    @Test
    public void testNextDoubles() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double[] array = randomizer.nextDoubles(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE &&
                    array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    public void testNextDoubleWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextDouble(MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);

            sum += randomValue;
            sqrSum += randomValue * randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextDouble(-MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithDoublesAndMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double[] array = new double[LENGTH];

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextDoublesWithMaxValue() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array;

        final double minValue = 0.0;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextDoubles(LENGTH, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextDoubles(LENGTH, -MAX_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextDoubleWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        double randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextDouble(MIN_VALUE, MAX_VALUE);
            assertTrue(randomValue >= minValue);
            assertTrue(randomValue < maxValue);

            sum += randomValue;
            sqrSum += randomValue * randomValue;
        }

        final double estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final double estimatedVariance = (sqrSum - (double) NUM_SAMPLES *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextDouble(MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testFillWithDoublesAndRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        final double[] array = new double[LENGTH];

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.fill(array, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextDoublesWithRange() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        double[] array;

        final double minValue = MIN_VALUE;
        final double maxValue = MAX_VALUE;

        final double meanValue = 0.5 * (minValue + maxValue);
        final double variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextDoubles(LENGTH, MIN_VALUE, MAX_VALUE);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= 0);
                assertTrue(array[j] < MAX_VALUE);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        final double estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final double estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue,
                estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance,
                estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        try {
            randomizer.nextDoubles(LENGTH, MAX_VALUE, MIN_VALUE);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testGetType() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());
        assertEquals(RandomizerType.UNIFORM_RANDOMIZER, randomizer.getType());
    }

    @Test
    public void testGetSetInternalRandomizer() {
        Random random = new Random();
        final UniformRandomizer randomizer = new UniformRandomizer(random);

        assertSame(random, randomizer.getInternalRandom());

        // set new random
        random = new SecureRandom();
        randomizer.setInternalRandom(random);

        // check correctness
        assertSame(random, randomizer.getInternalRandom());

        // Force NullPointerException
        try {
            randomizer.setInternalRandom(null);
            fail("NullPointerException expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
    }

    @Test
    public void testSetSeed() {
        final UniformRandomizer randomizer = new UniformRandomizer(new Random());

        assertNotNull(randomizer);

        final long seed = 0;
        randomizer.setSeed(seed);
    }
}
