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

class GammaTest {

    private static final double ABSOLUTE_ERROR = 1e-8;
    private static final int NUMBER_OF_TRIALS = 10;

    @Test
    void testConstructorAndGetGln() {
        final var g = new Gamma();

        assertEquals(0.0, g.getGln(), 0.0);
    }

    @Test
    void testGammln() {
        // gamma function is related to factorial for positive natural
        // numbers so that gamma(x) = (x-1)!. Gammln returns its logarithm

        assertEquals(0.0, Gamma.gammln(1.0), 0.0);
        assertEquals(Math.log(1.0), Gamma.gammln(2.0), 0.0);
        assertEquals(Math.log(2.0), Gamma.gammln(3.0), ABSOLUTE_ERROR);
        assertEquals(Math.log(3.0 * 2.0 * 1.0), Gamma.gammln(4.0), ABSOLUTE_ERROR);
        assertEquals(Math.log(4.0 * 3.0 * 2.0 * 1.0), Gamma.gammln(5.0), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Gamma.gammln(0.0));
    }

    @Test
    void testFactrl() {
        assertEquals(1.0, Gamma.factrl(0), 0.0);
        assertEquals(1.0, Gamma.factrl(1), 0.0);
        assertEquals(2.0, Gamma.factrl(2), 0.0);
        assertEquals(3.0 * 2.0, Gamma.factrl(3), 0.0);
        assertEquals(4.0 * 3.0 * 2.0, Gamma.factrl(4), 0.0);
        assertEquals(5.0 * 4.0 * 3.0 * 2.0, Gamma.factrl(5), 0.0);
        assertEquals(6.0 * 5.0 * 4.0 * 3.0 * 2.0, Gamma.factrl(6), 0.0);
        assertEquals(7.0 * 6.0 * 5.0 * 4.0 * 3.0 * 2.0, Gamma.factrl(7), 0.0);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Gamma.factrl(-1));
        assertThrows(IllegalArgumentException.class, () -> Gamma.factrl(171));
    }

    @Test
    void testFactln() {
        // gamma function is related to factorial by gamma(x) = (x - 1)!

        for (int i = 1; i < Gamma.MAX_CACHED_LOG_FACTORIALS + 1; i++) {
            assertEquals(Gamma.gammln(i), Gamma.factln(i - 1), 0.0);
        }

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Gamma.factln(-1));
    }

    @Test
    void testBico() {

        for (int n = 0; n < NUMBER_OF_TRIALS; n++) {
            for (int k = 0; k < n; k++) {
                assertEquals(Gamma.bico(n, k), Gamma.factrl(n) / (Gamma.factrl(k) * Gamma.factrl(n - k)),
                        ABSOLUTE_ERROR);
            }
        }

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Gamma.bico(-1, NUMBER_OF_TRIALS));
        assertThrows(IllegalArgumentException.class, () -> Gamma.bico(NUMBER_OF_TRIALS, -1));
        assertThrows(IllegalArgumentException.class, () -> Gamma.bico(NUMBER_OF_TRIALS, NUMBER_OF_TRIALS + 1));
    }

    @Test
    void testBeta() {
        assertEquals(Gamma.factrl(0) * Gamma.factrl(1) / Gamma.factrl(1 + 2 - 1),
                Gamma.beta(1.0, 2.0), ABSOLUTE_ERROR);
        assertEquals(Gamma.factrl(1) * Gamma.factrl(2) / Gamma.factrl(2 + 3 - 1),
                Gamma.beta(2.0, 3.0), ABSOLUTE_ERROR);
        assertEquals(Gamma.factrl(2) * Gamma.factrl(3) / Gamma.factrl(3 + 4 - 1),
                Gamma.beta(3.0, 4.0), ABSOLUTE_ERROR);

        // Force IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Gamma.beta(0.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> Gamma.beta(1.0, 0.0));
    }

    @Test
    void testGammpAndGammq() throws MaxIterationsExceededException {
        final var g = new Gamma();

        assertEquals(1.0 - g.gammq(1.0, 2.0), g.gammp(1.0, 2.0), ABSOLUTE_ERROR);
        assertEquals(1.0 - g.gammq(2.0, 3.0), g.gammp(2.0, 3.0), ABSOLUTE_ERROR);
        assertEquals(g.gammp(3.0, 4.0), 1.0 - g.gammq(3.0, 4.0), ABSOLUTE_ERROR);

        assertEquals(0.0, g.gammp(1.0, 0.0), 0.0);

        // Force MaxIterationsExceededException
        assertThrows(MaxIterationsExceededException.class, () -> g.gammp(1.0, Double.POSITIVE_INFINITY));
    }

    @Test
    void testInvgammp() throws MaxIterationsExceededException {
        final var g = new Gamma();

        assertEquals(2.0, g.invgammp(g.gammp(1.0, 2.0), 1.0), ABSOLUTE_ERROR);
        assertEquals(3.0, g.invgammp(g.gammp(2.0, 3.0), 2.0), ABSOLUTE_ERROR);
        assertEquals(4.0, g.invgammp(g.gammp(3.0, 4.0), 3.0), ABSOLUTE_ERROR);
    }
}
