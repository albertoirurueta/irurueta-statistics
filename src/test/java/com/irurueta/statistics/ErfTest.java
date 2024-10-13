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

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErfTest {

    private static final double ABSOLUTE_ERROR = 1e-6;

    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;

    @Test
    void testErf() {
        assertEquals(0.0, Erf.erf(0.0), ABSOLUTE_ERROR);
        assertEquals(1.0, Erf.erf(Double.POSITIVE_INFINITY), ABSOLUTE_ERROR);
        assertEquals(1.0, Erf.erf(Double.MAX_VALUE), ABSOLUTE_ERROR);

        final var randomizer = new UniformRandomizer();
        final var value = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        assertEquals(Erf.erf(-value), -Erf.erf(value), ABSOLUTE_ERROR);
    }

    @Test
    void testErfc() {
        assertEquals(1.0, Erf.erfc(0.0), ABSOLUTE_ERROR);
        assertEquals(0.0, Erf.erfc(Double.POSITIVE_INFINITY), ABSOLUTE_ERROR);
        assertEquals(0.0, Erf.erfc(Double.MAX_VALUE), ABSOLUTE_ERROR);

        final var randomizer = new UniformRandomizer();
        final var value = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        assertEquals(2.0 - Erf.erfc(value), Erf.erfc(-value), ABSOLUTE_ERROR);
    }

    @Test
    void testInverfc() {
        final var randomizer = new UniformRandomizer(new Random());
        final var value = randomizer.nextDouble(); //between 0.0 and 1.0

        assertEquals(value, Erf.erfc(Erf.inverfc(value)), ABSOLUTE_ERROR);
    }

    @Test
    void testInverf() {
        final var randomizer = new UniformRandomizer(new Random());
        final var value = randomizer.nextDouble(); //between 0.0 and 1.0

        assertEquals(value, Erf.erf(Erf.inverf(value)), ABSOLUTE_ERROR);
    }
}
