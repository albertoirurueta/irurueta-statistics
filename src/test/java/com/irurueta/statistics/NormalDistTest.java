/*
 * Copyright (C) 2015 Alberto Irurueta Carro (alberto@irurueta.com)
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

import static org.junit.jupiter.api.Assertions.*;

class NormalDistTest {

    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;

    private static final double ABSOLUTE_ERROR = 1e-6;
    private static final double LARGE_ABSOLUTE_ERROR = 1e-3;

    private static final int N_SAMPLES = 1000000;

    private static final double RELATIVE_ERROR = 0.10;

    private static final int TIMES = 10;

    @Test
    void testConstructor() {
        // test empty constructor
        NormalDist dist = new NormalDist();

        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        // test constructor with mean and standard deviation
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

        dist = new NormalDist(mean, std);

        // check correctness
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(std, dist.getStandardDeviation(), 0.0);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new NormalDist(mean, 0.0));
    }

    @Test
    void testGetSetMean() {
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var dist = new NormalDist();

        // check default value
        assertEquals(0.0, dist.getMean(), 0.0);

        // set new value
        dist.setMean(mean);

        // check correctness
        assertEquals(mean, dist.getMean(), 0.0);
    }

    @Test
    void testGetSetStandardDeviation() {
        final var randomizer = new UniformRandomizer();
        final var std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

        final var dist = new NormalDist();

        // check default value
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        // set new value
        dist.setStandardDeviation(std);

        // check correctness
        assertEquals(std, dist.getStandardDeviation(), 0.0);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> dist.setStandardDeviation(0.0));
    }

    @Test
    void testGetSetVariance() {
        final var randomizer = new UniformRandomizer();
        final var v = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

        final var dist = new NormalDist();

        // check default value
        assertEquals(1.0, dist.getVariance(), ABSOLUTE_ERROR);

        // set new value
        dist.setVariance(v);

        // check correctness
        assertEquals(v, dist.getVariance(), ABSOLUTE_ERROR);
        assertEquals(Math.sqrt(v), dist.getStandardDeviation(), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> dist.setVariance(0.0));
    }

    @Test
    void testGetP() {
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

        final var x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var dist = new NormalDist(mean, std);

        assertEquals(NormalDist.p(x, mean, std), dist.p(x), 0.0);

        assertEquals(0.398942280401432678 / std, dist.p(mean), ABSOLUTE_ERROR);
        assertEquals(0.398942280401432678 / std, NormalDist.p(mean, mean, std), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> NormalDist.p(x, mean, 0.0));
    }

    @Test
    void testCdf() {
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

        final var x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var dist = new NormalDist(mean, std);

        assertEquals(NormalDist.cdf(x, mean, std), dist.cdf(x), 0.0);

        assertEquals(0.00135, dist.cdf(mean - 3.0 * std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.02275, dist.cdf(mean - 2.0 * std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.15866, dist.cdf(mean - std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.5, dist.cdf(mean), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.84134, dist.cdf(mean + std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.97725, dist.cdf(mean + 2.0 * std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.99865, dist.cdf(mean + 3.0 * std), LARGE_ABSOLUTE_ERROR);


        assertEquals(0.00135, NormalDist.cdf(mean - 3.0 * std, mean, std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.02275, NormalDist.cdf(mean - 2.0 * std, mean, std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.15866, NormalDist.cdf(mean - std, mean, std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.5, NormalDist.cdf(mean, mean, std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.84134, NormalDist.cdf(mean + std, mean, std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.97725, NormalDist.cdf(mean + 2.0 * std, mean, std), LARGE_ABSOLUTE_ERROR);
        assertEquals(0.99865, NormalDist.cdf(mean + 3.0 * std, mean, std), LARGE_ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> NormalDist.cdf(x, mean, 0.0));
    }

    @Test
    void testInvcdf() {
        int numValid = 0;
        for (int t = 0; t < TIMES; t++) {
            final var randomizer = new UniformRandomizer();
            final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            final var std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

            final var x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            final var p = randomizer.nextDouble(); //value between 0.0 and 1.0

            final var dist = new NormalDist(mean, std);

            if (Math.abs(dist.invcdf(dist.cdf(x)) - x) > ABSOLUTE_ERROR) {
                continue;
            }
            if (Math.abs(dist.cdf(dist.invcdf(p)) - p) > ABSOLUTE_ERROR) {
                continue;
            }
            assertEquals(dist.invcdf(dist.cdf(x)), x, ABSOLUTE_ERROR);
            assertEquals(dist.cdf(dist.invcdf(p)), p, ABSOLUTE_ERROR);

            assertEquals(x, NormalDist.invcdf(NormalDist.cdf(x, mean, std), mean, std), ABSOLUTE_ERROR);
            assertEquals(p, NormalDist.cdf(NormalDist.invcdf(p, mean, std), mean, std), ABSOLUTE_ERROR);

            // Force IllegalArgumentException
            assertThrows(IllegalArgumentException.class, () -> NormalDist.invcdf(p, mean, 0.0));
            assertThrows(IllegalArgumentException.class, () -> NormalDist.invcdf(0.0, mean, std));
            assertThrows(IllegalArgumentException.class, () -> NormalDist.invcdf(1.0, mean, std));
            assertThrows(IllegalArgumentException.class, () -> dist.invcdf(0.0));
            assertThrows(IllegalArgumentException.class, () -> dist.invcdf(1.0));

            numValid++;
            break;
        }

        assertTrue(numValid > 0);
    }

    @Test
    void testMahalanobisDistance() {
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);

        final var x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var dist = new NormalDist(mean, std);

        assertEquals(dist.mahalanobisDistance(x), NormalDist.mahalanobisDistance(x, mean, std), 0.0);

        double tmp = Math.abs(x - mean) / std;
        assertEquals(tmp, dist.mahalanobisDistance(x), ABSOLUTE_ERROR);
        assertEquals(tmp, NormalDist.mahalanobisDistance(x, mean, std), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> NormalDist.mahalanobisDistance(x, mean, 0.0));
    }

    @Test
    void testPropagateSinusoidal() {
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var standardDeviation = randomizer.nextDouble(1e-6, 1e-3);

        final var dist = new NormalDist(mean, standardDeviation);
        final var evaluator = new NormalDist.DerivativeEvaluator() {
            @Override
            public double evaluate(final double x) {
                return Math.sin(x);
            }

            @Override
            public double evaluateDerivative(final double x) {
                return Math.cos(x);
            }
        };

        var result = new NormalDist();
        NormalDist.propagate(evaluator, mean, standardDeviation, result);

        // check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);


        result = NormalDist.propagate(evaluator, mean, standardDeviation);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);


        result = new NormalDist();
        NormalDist.propagate(evaluator, dist, result);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);


        result = NormalDist.propagate(evaluator, dist);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);


        result = new NormalDist();
        dist.propagateThisDistribution(evaluator, result);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);

        result = dist.propagateThisDistribution(evaluator);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);

        // generate a large number of Gaussian random samples and propagate them
        // through function f(x) = sin(x)
        final var gaussRandomizer = new GaussianRandomizer(mean, standardDeviation);
        double x;
        double y;
        double resultMean = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < N_SAMPLES; i++) {
            x = gaussRandomizer.nextDouble();
            y = evaluator.evaluate(x);

            resultMean += y / (double) N_SAMPLES;
            sqrSum += y * y / (double) N_SAMPLES;
        }

        final var resultStandardDeviation = Math.sqrt(sqrSum - resultMean * resultMean);

        assertEquals(resultMean, result.getMean(), RELATIVE_ERROR * Math.abs(resultMean));
        assertEquals(resultStandardDeviation, result.getStandardDeviation(),
                RELATIVE_ERROR * resultStandardDeviation);
    }

    @Test
    void testPropagatePoly() {
        final var randomizer = new UniformRandomizer();
        final var mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var standardDeviation = randomizer.nextDouble(1e-6, 1e-3);

        final var dist = new NormalDist(mean, standardDeviation);
        final var evaluator = new NormalDist.DerivativeEvaluator() {
            @Override
            public double evaluate(final double x) {
                return x * x;
            }

            @Override
            public double evaluateDerivative(final double x) {
                return 2.0 * x;
            }
        };

        var result = new NormalDist();
        NormalDist.propagate(evaluator, mean, standardDeviation, result);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);


        result = NormalDist.propagate(evaluator, mean, standardDeviation);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);

        result = new NormalDist();
        NormalDist.propagate(evaluator, dist, result);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);

        result = NormalDist.propagate(evaluator, dist);

        // check correctness
        assertEquals(evaluator.evaluate(mean), result.getMean(), ABSOLUTE_ERROR);
        assertEquals(Math.abs(evaluator.evaluateDerivative(mean) * standardDeviation), result.getStandardDeviation(),
                ABSOLUTE_ERROR);

        // generate a large number of Gaussian random samples and propagate them
        // through function f(x) = x^2
        final var gaussRandomizer = new GaussianRandomizer(mean, standardDeviation);
        double x;
        double y;
        double resultMean = 0.0;
        double sqrSum = 0.0;
        for (int i = 0; i < N_SAMPLES; i++) {
            x = gaussRandomizer.nextDouble();
            y = evaluator.evaluate(x);

            resultMean += y / (double) N_SAMPLES;
            sqrSum += y * y / (double) N_SAMPLES;
        }

        final var resultStandardDeviation = Math.sqrt(sqrSum - resultMean * resultMean);

        assertEquals(resultMean, result.getMean(), RELATIVE_ERROR * Math.abs(resultMean));
        assertEquals(resultStandardDeviation, result.getStandardDeviation(),
                RELATIVE_ERROR * resultStandardDeviation);
    }
}
