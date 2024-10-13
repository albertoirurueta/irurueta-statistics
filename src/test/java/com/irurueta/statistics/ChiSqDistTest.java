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

class ChiSqDistTest {

    private static final double MIN_RANDOM_VALUE = 0.0;
    private static final double MAX_RANDOM_VALUE = 10.0;

    private static final double ABSOLUTE_ERROR = 1e-6;

    @Test
    void testConstructor() {
        final var randomizer = new UniformRandomizer();
        final var nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        var dist = new ChiSqDist(nu);

        assertEquals(nu, dist.getNu(), 0.0);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new ChiSqDist(0.0));
    }

    @Test
    void testGetSetNu() {
        final var randomizer = new UniformRandomizer();
        final var nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var nu2 = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var dist = new ChiSqDist(nu);

        // check initial value
        assertEquals(nu, dist.getNu(), 0.0);

        // set new value
        dist.setNu(nu2);

        // check correctness
        assertEquals(nu2, dist.getNu(), 0.0);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> dist.setNu(0.0));
    }

    @Test
    void testP() {
        final var randomizer = new UniformRandomizer();
        final var nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var x2 = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var dist = new ChiSqDist(nu);

        // check
        assertEquals(ChiSqDist.p(x2, nu), dist.p(x2), 0.0);

        assertEquals(0.0, dist.p(Double.MAX_VALUE), ABSOLUTE_ERROR);
        assertEquals(0.0, ChiSqDist.p(Double.MAX_VALUE, nu), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> dist.p(0.0));
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.p(0.0, nu));
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.p(x2, 0.0));
    }

    @Test
    void testCdf() throws MaxIterationsExceededException {
        final var randomizer = new UniformRandomizer();
        final var nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var x2a = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var x2b = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        final var x2min = Math.min(x2a, x2b);
        final var x2max = Math.max(x2a, x2b);

        final var dist = new ChiSqDist(nu);

        // check correctness
        assertEquals(ChiSqDist.cdf(x2min, nu), dist.cdf(x2min), 0.0);
        assertEquals(ChiSqDist.cdf(x2max, nu), dist.cdf(x2max), 0.0);

        assertTrue(dist.cdf(x2min) <= dist.cdf(x2max));
        assertTrue(ChiSqDist.cdf(x2min, nu) <= ChiSqDist.cdf(x2max, nu));

        assertTrue(dist.cdf(x2min) >= 0.0 && dist.cdf(x2min) <= 1.0);
        assertTrue(dist.cdf(x2max) >= 0.0 && dist.cdf(x2max) <= 1.0);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> dist.cdf(-1.0));
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.cdf(-1.0, nu));
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.cdf(x2min, 0.0));
    }

    @Test
    void testInvcdf() throws MaxIterationsExceededException {
        final var randomizer = new UniformRandomizer();
        final var nu = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var x2 = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        final var p = randomizer.nextDouble(); //between 0.0 and 1.0

        final var dist = new ChiSqDist(nu);

        assertEquals(x2, dist.invcdf(dist.cdf(x2)), ABSOLUTE_ERROR);
        assertEquals(p, dist.cdf(dist.invcdf(p)), ABSOLUTE_ERROR);

        assertEquals(x2, ChiSqDist.invcdf(ChiSqDist.cdf(x2, nu), nu), ABSOLUTE_ERROR);
        assertEquals(p, ChiSqDist.cdf(ChiSqDist.invcdf(p, nu), nu), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.invcdf(p, 0.0));
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.invcdf(-1.0, nu));
        assertThrows(IllegalArgumentException.class, () -> ChiSqDist.invcdf(1.0, nu));
        assertThrows(IllegalArgumentException.class, () -> dist.invcdf(-1.0));
        assertThrows(IllegalArgumentException.class, () -> dist.invcdf(1.0));
    }
}
