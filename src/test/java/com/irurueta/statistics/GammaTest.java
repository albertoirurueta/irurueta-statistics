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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GammaTest {
    
    private static final double ABSOLUTE_ERROR = 1e-8;
    private static final int NUMBER_OF_TRIALS = 10;

    @Test
    public void testConstructorAndGetGln() {
        final Gamma g = new Gamma();
        
        assertEquals(g.getGln(), 0.0, 0.0);
    }
    
    @Test
    public void testGammln() {
        // gamma function is related to factorial for positive natural
        // numbers so that gamma(x) = (x-1)!. Gammln returns its logarithm
        
        assertEquals(Gamma.gammln(1.0), 0.0, 0.0);
        assertEquals(Gamma.gammln(2.0), Math.log(1.0), 0.0);
        assertEquals(Gamma.gammln(3.0), Math.log(2.0), ABSOLUTE_ERROR);
        assertEquals(Gamma.gammln(4.0), Math.log(3.0 * 2.0 * 1.0), 
                ABSOLUTE_ERROR);
        assertEquals(Gamma.gammln(5.0), Math.log(4.0 * 3.0 * 2.0 * 1.0), 
                ABSOLUTE_ERROR);
        
        // Force IllegalArgumentException
        try {
            Gamma.gammln(0.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testFactrl() {
        assertEquals(Gamma.factrl(0), 1.0, 0.0);
        assertEquals(Gamma.factrl(1), 1.0, 0.0);
        assertEquals(Gamma.factrl(2), 2.0, 0.0);
        assertEquals(Gamma.factrl(3), 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(4), 4.0 * 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(5), 5.0 * 4.0 * 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(6), 6.0 * 5.0 * 4.0 * 3.0 * 2.0, 0.0);
        assertEquals(Gamma.factrl(7), 7.0 * 6.0 * 5.0 * 4.0 * 3.0 * 2.0, 0.0);
        
        // Force IllegalArgumentException
        try {
            Gamma.factrl(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
        try {
            Gamma.factrl(171);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testFactln() {
        // gamma function is related to factorial by gamma(x) = (x - 1)!
        
        for (int i = 1; i < Gamma.MAX_CACHED_LOG_FACTORIALS + 1; i++) {
            assertEquals(Gamma.gammln(i), Gamma.factln(i - 1), 0.0);
        }
        
        // Force IllegalArgumentException
        try {
            Gamma.factln(-1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testBico() {
        
        for (int n = 0; n < NUMBER_OF_TRIALS; n++) {
            for (int k = 0; k < n; k++) {
                assertEquals(Gamma.bico(n, k), 
                        Gamma.factrl(n) / 
                        (Gamma.factrl(k) * Gamma.factrl(n - k)), 
                        ABSOLUTE_ERROR);
            }
        }
        
        // Force IllegalArgumentException
        try {
            Gamma.bico(-1, NUMBER_OF_TRIALS);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
        try {
            Gamma.bico(NUMBER_OF_TRIALS, -1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
        try {
            Gamma.bico(NUMBER_OF_TRIALS, NUMBER_OF_TRIALS + 1);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testBeta() {
        
        assertEquals(Gamma.beta(1.0, 2.0),
                Gamma.factrl(0) * Gamma.factrl(1) /
                Gamma.factrl(1 + 2 - 1), ABSOLUTE_ERROR);
        assertEquals(Gamma.beta(2.0, 3.0),
                Gamma.factrl(1) * Gamma.factrl(2) /
                Gamma.factrl(2 + 3 - 1), ABSOLUTE_ERROR);
        assertEquals(Gamma.beta(3.0, 4.0),
                Gamma.factrl(2) * Gamma.factrl(3) /
                Gamma.factrl(3 + 4 - 1), ABSOLUTE_ERROR);
        
        // Force IllegalArgumentException
        try {
            Gamma.beta(0.0, 1.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
        try {
            Gamma.beta(1.0, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (final IllegalArgumentException ignore) { }
    }
    
    @Test
    public void testGammpAndGammq() throws MaxIterationsExceededException {
        final Gamma g = new Gamma();
        
        assertEquals(g.gammp(1.0, 2.0), 1.0 - g.gammq(1.0, 2.0), 
                ABSOLUTE_ERROR);
        assertEquals(g.gammp(2.0, 3.0), 1.0 - g.gammq(2.0, 3.0), 
                ABSOLUTE_ERROR);
        assertEquals(g.gammp(3.0, 4.0), 1.0 - g.gammq(3.0, 4.0), 
                ABSOLUTE_ERROR);
        
        assertEquals(g.gammp(1.0, 0.0), 0.0, 0.0);
        
        try {
            assertEquals(g.gammp(1.0, Double.POSITIVE_INFINITY), 1.0, 
                    ABSOLUTE_ERROR);
            fail("MaxIterationsExceededException expected but not thrown");
        } catch (final MaxIterationsExceededException ignore) { }
    }
    
    @Test
    public void testInvgammp() throws MaxIterationsExceededException {
        final Gamma g = new Gamma();
        
        assertEquals(g.invgammp(g.gammp(1.0, 2.0), 1.0), 2.0, 
                ABSOLUTE_ERROR);
        assertEquals(g.invgammp(g.gammp(2.0, 3.0), 2.0), 3.0, 
                ABSOLUTE_ERROR);
        assertEquals(g.invgammp(g.gammp(3.0, 4.0), 3.0), 4.0, 
                ABSOLUTE_ERROR);        
    }
}
