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

public class GaussianRandomizerTest {

    private static final int NUM_SAMPLES = 1000000;
    private static final double RELATIVE_ERROR = 0.05;
    private static final double ABSOLUTE_ERROR = 0.01;

    private static final double MEAN = 5;
    private static final double STANDARD_DEVIATION = 100.0;

    private static final int LENGTH = 100;

    @Test
    public void testConstructor() {
        GaussianRandomizer randomizer;

        // test 1st constructor
        randomizer = new GaussianRandomizer();
        assertNotNull(randomizer);
        assertEquals(GaussianRandomizer.DEFAULT_MEAN, randomizer.getMean(),
                0.0);
        assertEquals(GaussianRandomizer.DEFAULT_STANDARD_DEVIATION,
                randomizer.getStandardDeviation(), 0.0);

        // test 2nd constructor
        randomizer = new GaussianRandomizer(new Random());
        assertNotNull(randomizer);
        assertEquals(GaussianRandomizer.DEFAULT_MEAN, randomizer.getMean(),
                0.0);
        assertEquals(GaussianRandomizer.DEFAULT_STANDARD_DEVIATION,
                randomizer.getStandardDeviation(), 0.0);

        randomizer = new GaussianRandomizer(new SecureRandom());
        assertNotNull(randomizer);
        assertEquals(GaussianRandomizer.DEFAULT_MEAN, randomizer.getMean(),
                0.0);
        assertEquals(GaussianRandomizer.DEFAULT_STANDARD_DEVIATION,
                randomizer.getStandardDeviation(), 0.0);


        // Force NullPointerException
        randomizer = null;
        try {
            randomizer = new GaussianRandomizer(null);
            fail("NullPointerException was expected but not thrown");
        } catch (final NullPointerException ignore) {
        }
        assertNull(randomizer);


        // tet 2nd constructor
        randomizer = new GaussianRandomizer(new Random(), MEAN,
                STANDARD_DEVIATION);
        assertNotNull(randomizer);
        assertEquals(MEAN, randomizer.getMean(), 0.0);
        assertEquals(STANDARD_DEVIATION, randomizer.getStandardDeviation(),
                0.0);

        // Force IllegalArgumentException
        randomizer = null;
        try {
            randomizer = new GaussianRandomizer(new Random(), MEAN,
                    -STANDARD_DEVIATION);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
        assertNull(randomizer);
    }

    @Test
    public void testGetSetMean() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random());

        // check default mean
        assertEquals(GaussianRandomizer.DEFAULT_MEAN, randomizer.getMean(),
                0.0);

        // set new mean
        randomizer.setMean(MEAN);

        // check correctness
        assertEquals(MEAN, randomizer.getMean(), 0.0);
    }

    @Test
    public void testGetSetStandardDeviation() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random());

        // check default mean
        assertEquals(GaussianRandomizer.DEFAULT_STANDARD_DEVIATION,
                randomizer.getStandardDeviation(), 0.0);

        // set new standard deviation
        randomizer.setStandardDeviation(STANDARD_DEVIATION);

        // check correctness
        assertEquals(STANDARD_DEVIATION, randomizer.getStandardDeviation(),
                0.0);

        // Force IllegalArgumentException
        try {
            randomizer.setStandardDeviation(-STANDARD_DEVIATION);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (final IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testNextBoolean() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

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
        assertEquals(0.5, (double) trueCounter / (double) NUM_SAMPLES,
                ABSOLUTE_ERROR);
        assertEquals(0.5, (double) falseCounter / (double) NUM_SAMPLES,
                ABSOLUTE_ERROR);
    }

    @Test
    public void testNextBooleanWithThreshold() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        final double threshold = MEAN + STANDARD_DEVIATION;
        // by setting a threshold at 2 standard deviation distance from MEAN,
        // then by using the erfc function we can determine that true will be
        // generated 84% of the time and false 16%
        final double probTrue = 0.8413;
        final double probFalse = 1.0 - probTrue;

        int falseCounter = 0;
        int trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            if (randomizer.nextBoolean(threshold)) {
                trueCounter++;
            } else {
                falseCounter++;
            }
        }

        assertEquals((double) trueCounter / (double) NUM_SAMPLES, probTrue,
                ABSOLUTE_ERROR);
        assertEquals((double) falseCounter / (double) NUM_SAMPLES, probFalse,
                ABSOLUTE_ERROR);
    }

    @Test
    public void testFillWithBooleansAndThreshold() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        final boolean[] array = new boolean[LENGTH];

        final double threshold = MEAN + STANDARD_DEVIATION;
        // by setting a threshold at 2 standard deviation distance from MEAN,
        // then by using the erfc function we can determine that true will be
        // generates 84% of the time and false 16%
        final double probTrue = 0.8413;
        final double probFalse = 1.0 - probTrue;

        int falseCounter = 0;
        int trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            randomizer.fill(array, threshold);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) {
                    trueCounter++;
                } else {
                    falseCounter++;
                }
            }
        }

        assertEquals((double) trueCounter / (double) (NUM_SAMPLES * LENGTH),
                probTrue, ABSOLUTE_ERROR);
        assertEquals((double) falseCounter / (double) (NUM_SAMPLES * LENGTH),
                probFalse, ABSOLUTE_ERROR);
    }

    @Test
    public void testNextBooleansAndThreshold() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        boolean[] array;

        final double threshold = MEAN + STANDARD_DEVIATION;
        // by setting a threshold at 2 standard deviation distance from MEAN,
        // then by using the erfc function we can determine that true will be
        // generates 84% of the time and false 16%
        final double probTrue = 0.8413;
        final double probFalse = 1.0 - probTrue;

        int falseCounter = 0;
        int trueCounter = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {

            array = randomizer.nextBooleans(LENGTH, threshold);

            for (int j = 0; j < LENGTH; j++) {
                if (array[j]) {
                    trueCounter++;
                } else {
                    falseCounter++;
                }
            }
        }

        assertEquals((double) trueCounter / (double) (NUM_SAMPLES * LENGTH),
                probTrue, ABSOLUTE_ERROR);
        assertEquals((double) falseCounter / (double) (NUM_SAMPLES * LENGTH),
                probFalse, ABSOLUTE_ERROR);
    }

    @Test
    public void testNextInt() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        double mean = 0.0;
        double sqrSum = 0.0;
        final double standardDeviation;
        final int num = 2 * NUM_SAMPLES;
        int value;
        for (int i = 0; i < num; i++) {
            value = randomizer.nextInt();
            mean += (double) value / (double) num;
            sqrSum += (double) value * (double) value / (double) num;
        }

        standardDeviation = Math.sqrt(sqrSum - mean);

        assertEquals(MEAN, mean, RELATIVE_ERROR * MEAN);
        assertEquals(STANDARD_DEVIATION, standardDeviation,
                RELATIVE_ERROR * STANDARD_DEVIATION);
    }

    @Test
    public void testNextLong() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        double mean = 0.0;
        double sqrSum = 0.0;
        final double standardDeviation;
        long value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            value = randomizer.nextLong();
            mean += (double) value / (double) NUM_SAMPLES;
            sqrSum += (double) value * (double) value / (double) NUM_SAMPLES;
        }

        standardDeviation = Math.sqrt(sqrSum - mean);

        assertEquals(MEAN, mean, RELATIVE_ERROR * MEAN);
        assertEquals(STANDARD_DEVIATION, standardDeviation,
                RELATIVE_ERROR * STANDARD_DEVIATION);
    }

    @Test
    public void testNextFloat() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        double mean = 0.0;
        double sqrSum = 0.0;
        final double standardDeviation;
        float value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            value = randomizer.nextFloat();
            mean += (double) value / (double) NUM_SAMPLES;
            sqrSum += (double) value * (double) value / (double) NUM_SAMPLES;
        }

        standardDeviation = Math.sqrt(sqrSum - mean);

        assertEquals(MEAN, mean, RELATIVE_ERROR * MEAN);
        assertEquals(STANDARD_DEVIATION, standardDeviation,
                RELATIVE_ERROR * STANDARD_DEVIATION);
    }

    @Test
    public void testNextDouble() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random(),
                MEAN, STANDARD_DEVIATION);

        double mean = 0.0;
        double sqrSum = 0.0;
        final double standardDeviation;
        double value;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            value = randomizer.nextDouble();
            mean += value / (double) NUM_SAMPLES;
            sqrSum += value * value / (double) NUM_SAMPLES;
        }

        standardDeviation = Math.sqrt(sqrSum - mean);

        assertEquals(MEAN, mean, 2.0 * RELATIVE_ERROR * MEAN);
        assertEquals(STANDARD_DEVIATION, standardDeviation,
                RELATIVE_ERROR * STANDARD_DEVIATION);
    }

    @Test
    public void testGetType() {
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random());
        assertEquals(RandomizerType.GAUSSIAN_RANDOMIZER, randomizer.getType());
    }

    @Test
    public void testGetSetInternalRandomizer() {
        Random random = new Random();
        final GaussianRandomizer randomizer = new GaussianRandomizer(random);

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
        final GaussianRandomizer randomizer = new GaussianRandomizer(new Random());

        assertNotNull(randomizer);

        final long seed = 0;
        randomizer.setSeed(seed);
    }

}
