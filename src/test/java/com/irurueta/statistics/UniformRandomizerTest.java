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

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UniformRandomizerTest {

    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double BIG_RELATIVE_ERROR = 0.7;
    private static final double ABSOLUTE_ERROR = 0.01;

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 100;

    private static final int LENGTH = 100;

    @Test
    void testConstructor() {
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
        assertThrows(NullPointerException.class, () -> new UniformRandomizer(null));
    }

    @Test
    void testNextBoolean() {

        final var randomizer = new UniformRandomizer();

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;


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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results.
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * BIG_RELATIVE_ERROR);
    }

    @Test
    void testFillWithBooleans() {
        final var randomizer = new UniformRandomizer();

        final var array = new boolean[LENGTH];
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
        assertEquals(0.5, (double) trueCounter / (double) (NUM_SAMPLES * LENGTH), ABSOLUTE_ERROR);
        assertEquals(0.5, (double) falseCounter / (double) (NUM_SAMPLES * LENGTH), ABSOLUTE_ERROR);
    }

    @Test
    void testNextBooleans() {
        final var randomizer = new UniformRandomizer();

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
        assertEquals(0.5, (double) trueCounter / (double) (NUM_SAMPLES * LENGTH), ABSOLUTE_ERROR);
        assertEquals(0.5, (double) falseCounter / (double) (NUM_SAMPLES * LENGTH), ABSOLUTE_ERROR);
    }

    @Test
    void testNextInt() {
        final var randomizer = new UniformRandomizer();

        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt();
            assertTrue(randomValue < Integer.MAX_VALUE && randomValue > -Integer.MAX_VALUE);
        }
    }

    @Test
    void testFillWithIntegers() {
        final var randomizer = new UniformRandomizer();

        final var array = new int[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE && array[i] > -Integer.MAX_VALUE);
        }
    }

    @Test
    void testNextInts() {
        final var randomizer = new UniformRandomizer();

        final var array = randomizer.nextInts(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE && array[i] > -Integer.MAX_VALUE);
        }
    }

    @Test
    void testNextIntWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextInt(-MAX_VALUE));
    }

    @Test
    void testFillWithIntegersAndMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var array = new int[LENGTH];

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, -MAX_VALUE));
    }

    @Test
    void testNextIntsWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        int[] array;

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextInts(LENGTH, -MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextInts(-LENGTH, MAX_VALUE));
    }

    @Test
    void testNextIntWithRange() {
        final var randomizer = new UniformRandomizer();

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextInt(MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testFillWithIntegersAndRange() {
        final var randomizer = new UniformRandomizer();

        final var array = new int[LENGTH];

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextIntsWithRange() {
        final var randomizer = new UniformRandomizer();

        int[] array;

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextInts(LENGTH, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextLong() {
        final var randomizer = new UniformRandomizer();

        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong();
            assertTrue(randomValue < Long.MAX_VALUE && randomValue > -Long.MAX_VALUE);
        }
    }

    @Test
    void testFillWithLongs() {
        final var randomizer = new UniformRandomizer();

        final var array = new long[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE && array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    void testNextLongs() {
        final var randomizer = new UniformRandomizer();

        final var array = randomizer.nextLongs(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE && array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    void testNextLongWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextLong(-MAX_VALUE));
    }

    @Test
    void testFillWithLongsAndMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var array = new long[LENGTH];

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, -MAX_VALUE));
    }

    @Test
    void testNextLongsWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        long[] array;

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextLongs(LENGTH, -MAX_VALUE));
    }

    @Test
    void testNextLongWithRange() {
        final var randomizer = new UniformRandomizer();

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextLong(MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testFillWithLongsAndRange() {
        final var randomizer = new UniformRandomizer();

        final var array = new long[LENGTH];

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextLongsWithRange() {
        final var randomizer = new UniformRandomizer();

        long[] array;

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextLongs(LENGTH, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextFloat() {
        final var randomizer = new UniformRandomizer();

        final var minValue = 0.0;
        final var maxValue = 1.0;

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    void testFillWithFloats() {
        final var randomizer = new UniformRandomizer();

        final var array = new float[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE && array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    void testNextFloats() {
        final var randomizer = new UniformRandomizer();

        final var array = randomizer.nextFloats(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE && array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    void testNextFloatWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextFloat(-MAX_VALUE));
    }

    @Test
    void testFillWithFloatsAndMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var array = new float[LENGTH];

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, -MAX_VALUE));
    }

    @Test
    void testNextFloatsWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        float[] array;

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextFloats(LENGTH, -MAX_VALUE));
    }

    @Test
    void testNextFloatWithRange() {
        final var randomizer = new UniformRandomizer();

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextFloat(MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testFillFloatsWithRange() {
        final var randomizer = new UniformRandomizer();

        final var array = new float[LENGTH];

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextFloatsWithRange() {
        final var randomizer = new UniformRandomizer();

        float[] array;

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextFloats(LENGTH, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextDouble() {
        final var randomizer = new UniformRandomizer();

        final var minValue = 0.0;
        final var maxValue = 1.0;

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    void testFillWithDoubles() {
        final var randomizer = new UniformRandomizer();

        final var array = new double[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Double.MAX_VALUE && array[i] > -Double.MAX_VALUE);
        }
    }

    @Test
    void testNextDoubles() {
        final var randomizer = new UniformRandomizer();

        final var array = randomizer.nextDoubles(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Float.MAX_VALUE && array[i] > -Float.MAX_VALUE);
        }
    }

    @Test
    void testNextDoubleWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextDouble(-MAX_VALUE));
    }

    @Test
    void testFillWithDoublesAndMaxValue() {
        final var randomizer = new UniformRandomizer();

        final var array = new double[LENGTH];

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, -MAX_VALUE));
    }

    @Test
    void testNextDoublesWithMaxValue() {
        final var randomizer = new UniformRandomizer();

        double[] array;

        final var minValue = 0.0;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextDoubles(LENGTH, -MAX_VALUE));
    }

    @Test
    void testNextDoubleWithRange() {
        final var randomizer = new UniformRandomizer();

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) NUM_SAMPLES;
        final var estimatedVariance = (sqrSum - (double) NUM_SAMPLES * estimatedMeanValue * estimatedMeanValue) /
                ((double) NUM_SAMPLES - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextDouble(MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testFillWithDoublesAndRange() {
        final var randomizer = new UniformRandomizer();

        final var array = new double[LENGTH];

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.fill(array, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testNextDoublesWithRange() {
        final var randomizer = new UniformRandomizer();

        double[] array;

        final var minValue = MIN_VALUE;
        final var maxValue = MAX_VALUE;

        final var meanValue = 0.5 * (minValue + maxValue);
        final var variance = (maxValue - minValue) * (maxValue - minValue) / 12.0;

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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // Check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextDoubles(LENGTH, MAX_VALUE, MIN_VALUE));
    }

    @Test
    void testGetType() {
        final var randomizer = new UniformRandomizer();
        assertEquals(RandomizerType.UNIFORM_RANDOMIZER, randomizer.getType());
    }

    @Test
    void testGetSetInternalRandomizer() {
        Random random = new Random();
        final var randomizer = new UniformRandomizer(random);

        assertSame(random, randomizer.getInternalRandom());

        // set new random
        random = new SecureRandom();
        randomizer.setInternalRandom(random);

        // check correctness
        assertSame(random, randomizer.getInternalRandom());

        // Force NullPointerException
        assertThrows(NullPointerException.class, () -> randomizer.setInternalRandom(null));
    }

    @Test
    void testSetSeed() {
        final var randomizer = new UniformRandomizer(new Random());

        assertNotNull(randomizer);

        final var seed = 0;
        randomizer.setSeed(seed);
    }
}
