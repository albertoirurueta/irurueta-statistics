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

class RandomizerTest {

    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double ABSOLUTE_ERROR = 0.01;

    private static final int LENGTH = 100;

    @Test
    void testCreate() {

        Randomizer randomizer;

        // test create without parameters
        randomizer = Randomizer.create();
        if (Randomizer.USE_SECURE_RANDOM_BY_DEFAULT) {
            assertInstanceOf(SecureRandom.class, randomizer.getInternalRandom());
        } else {
            assertNotNull(randomizer.getInternalRandom());
        }
        assertEquals(Randomizer.DEFAULT_RANDOMIZER_TYPE, randomizer.getType());

        // test create with secure parameter
        randomizer = Randomizer.create(false);
        assertNotNull(randomizer.getInternalRandom());
        assertEquals(Randomizer.DEFAULT_RANDOMIZER_TYPE, randomizer.getType());

        randomizer = Randomizer.create(true);
        assertInstanceOf(SecureRandom.class, randomizer.getInternalRandom());
        assertEquals(Randomizer.DEFAULT_RANDOMIZER_TYPE, randomizer.getType());

        // test create with Random
        final var random = new SecureRandom();
        randomizer = Randomizer.create(random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(Randomizer.DEFAULT_RANDOMIZER_TYPE, randomizer.getType());

        // Force NullPointerException
        assertThrows(NullPointerException.class, () -> Randomizer.create((Random) null));

        // test create with RandomizerType
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER);
        if (Randomizer.USE_SECURE_RANDOM_BY_DEFAULT) {
            assertInstanceOf(SecureRandom.class, randomizer.getInternalRandom());
        } else {
            assertNotNull(randomizer.getInternalRandom());
        }
        assertEquals(RandomizerType.GAUSSIAN_RANDOMIZER, randomizer.getType());

        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER);
        if (Randomizer.USE_SECURE_RANDOM_BY_DEFAULT) {
            assertInstanceOf(SecureRandom.class, randomizer.getInternalRandom());
        } else {
            assertNotNull(randomizer.getInternalRandom());
        }
        assertEquals(RandomizerType.UNIFORM_RANDOMIZER, randomizer.getType());


        // test create with RandomizerType and secure parameter
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, false);
        assertNotNull(randomizer.getInternalRandom());
        assertEquals(RandomizerType.GAUSSIAN_RANDOMIZER, randomizer.getType());

        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, true);
        assertInstanceOf(SecureRandom.class, randomizer.getInternalRandom());
        assertEquals(RandomizerType.GAUSSIAN_RANDOMIZER, randomizer.getType());

        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER, false);
        assertNotNull(randomizer.getInternalRandom());
        assertEquals(RandomizerType.UNIFORM_RANDOMIZER, randomizer.getType());

        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER, true);
        assertInstanceOf(SecureRandom.class, randomizer.getInternalRandom());
        assertEquals(RandomizerType.UNIFORM_RANDOMIZER, randomizer.getType());

        // test create with RandomizerType and internal Random
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(RandomizerType.GAUSSIAN_RANDOMIZER, randomizer.getType());

        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER, random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(RandomizerType.UNIFORM_RANDOMIZER, randomizer.getType());

        // Force NullPointerException
        assertThrows(NullPointerException.class, () -> Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER,
                null));
        assertThrows(NullPointerException.class, () -> Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                null));
    }

    @Test
    void testGetSetInternalRandomizer() {
        Random random = new Random();
        final var randomizer = Randomizer.create(random);

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
        final var randomizer = Randomizer.create();

        assertNotNull(randomizer);

        final var seed = 0;
        randomizer.setSeed(seed);
    }

    @Test
    void testNextBoolean() {
        final var randomizer = Randomizer.create();

        int falseCounter = 0;
        int trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            if (randomizer.nextBoolean()) {
                trueCounter++;
            } else {
                falseCounter++;
            }
        }

        // check that both true and false are equally probable
        assertEquals(0.5, (double) trueCounter / (double) NUM_SAMPLES, ABSOLUTE_ERROR);
        assertEquals(0.5, (double) falseCounter / (double) NUM_SAMPLES, ABSOLUTE_ERROR);
    }

    @Test
    void testFillWithBooleans() {
        final var randomizer = Randomizer.create();

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
        final var randomizer = Randomizer.create();

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

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextBooleans(-1));
    }

    @Test
    void testNextInt() {
        final var randomizer = Randomizer.create();

        int randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextInt();
            assertTrue(randomValue < Integer.MAX_VALUE && randomValue > -Integer.MAX_VALUE);
        }
    }

    @Test
    void testFillWithIntegers() {
        final var randomizer = Randomizer.create();

        final var array = new int[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE && array[i] > -Integer.MAX_VALUE);
        }
    }

    @Test
    void testNextInts() {
        final var randomizer = Randomizer.create();

        final var array = randomizer.nextInts(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Integer.MAX_VALUE && array[i] > -Integer.MAX_VALUE);
        }

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextInts(-1));
    }

    @Test
    void testNextLong() {
        final var randomizer = Randomizer.create();

        long randomValue;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomValue = randomizer.nextLong();
            assertTrue(randomValue < Long.MAX_VALUE && randomValue > -Long.MAX_VALUE);
        }
    }

    @Test
    void testFillWithLongs() {
        final var randomizer = Randomizer.create();

        final var array = new long[LENGTH];
        randomizer.fill(array);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE && array[i] > -Long.MAX_VALUE);
        }
    }

    @Test
    void testNextLongs() {
        final var randomizer = Randomizer.create();

        final var array = randomizer.nextLongs(LENGTH);

        // check correctness
        for (int i = 0; i < LENGTH; i++) {
            assertTrue(array[i] < Long.MAX_VALUE && array[i] > -Long.MAX_VALUE);
        }

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextLongs(-1));
    }

    @Test
    void testNextFloat() {
        final var randomizer = Randomizer.create();

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

        // check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(meanValue, estimatedMeanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(variance, estimatedVariance, estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    void testFillWithFloats() {
        final var randomizer = Randomizer.create();

        final var array = new float[LENGTH];

        // check correctness
        final var minValue = 0.0;
        final var maxValue = 1.0;

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) /
                ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // check correctness of results by checking against the expected mean and
        // variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    void testNextFloats() {
        final var randomizer = Randomizer.create();

        final var minValue = 0.0;
        final var maxValue = 1.0;

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        float[] array;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            array = randomizer.nextFloats(LENGTH);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += array[j];
                sqrSum += (double) array[j] * (double) array[j];
            }
        }

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // check correctness of results by checking against the expected mean and
        // variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextFloats(-1));
    }

    @Test
    void testNextDouble() {
        final var randomizer = Randomizer.create();

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

        // check correctness of results by checking against the expected mean
        // and variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    void testFillWithDoubles() {
        final var randomizer = Randomizer.create();

        final var array = new double[LENGTH];

        // check correctness
        final var minValue = 0.0;
        final var maxValue = 1.0;

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            randomizer.fill(array);

            for (int j = 0; j < LENGTH; j++) {
                assertTrue(array[j] >= minValue);
                assertTrue(array[j] < maxValue);

                sum += array[j];
                sqrSum += array[j] * array[j];
            }
        }

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // check correctness of results by checking against the expected mean and
        // variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);
    }

    @Test
    void testNextDoubles() {
        final var randomizer = Randomizer.create();

        final var minValue = 0.0;
        final var maxValue = 1.0;

        final var meanValue = 0.5;
        final var variance = 1.0 / 12.0;

        double sum = 0.0;
        double sqrSum = 0.0;
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

        final var estimatedMeanValue = sum / (double) (NUM_SAMPLES * LENGTH);
        final var estimatedVariance = (sqrSum - (double) (NUM_SAMPLES * LENGTH) *
                estimatedMeanValue * estimatedMeanValue) / ((double) (NUM_SAMPLES * LENGTH) - 1.0);

        // check correctness of results by checking against the expected mean and
        // variance of a uniform distribution
        assertEquals(estimatedMeanValue, meanValue, estimatedMeanValue * RELATIVE_ERROR);
        assertEquals(estimatedVariance, variance, estimatedVariance * RELATIVE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> randomizer.nextDoubles(-1));
    }
}
