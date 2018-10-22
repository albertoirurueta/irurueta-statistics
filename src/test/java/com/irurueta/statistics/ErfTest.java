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

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ErfTest {
    
    private static final double ABSOLUTE_ERROR = 1e-6;
    
    private static final double MIN_RANDOM_VALUE = -100.0;
    private static final double MAX_RANDOM_VALUE = 100.0;
    
    public ErfTest() { }
    
    @BeforeClass
    public static void setUpClass() { }
    
    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }

    @Test
    public void testErf() {
        assertEquals(Erf.erf(0.0), 0.0, ABSOLUTE_ERROR);
        assertEquals(Erf.erf(Double.POSITIVE_INFINITY), 1.0, ABSOLUTE_ERROR);
        assertEquals(Erf.erf(Double.MAX_VALUE), 1.0, ABSOLUTE_ERROR);
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        assertEquals(Erf.erf(-value), -Erf.erf(value), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testErfc(){
        assertEquals(Erf.erfc(0.0), 1.0, ABSOLUTE_ERROR);
        assertEquals(Erf.erfc(Double.POSITIVE_INFINITY), 0.0, ABSOLUTE_ERROR);
        assertEquals(Erf.erfc(Double.MAX_VALUE), 0.0, ABSOLUTE_ERROR);
        
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double value = randomizer.nextDouble(MIN_RANDOM_VALUE, 
                MAX_RANDOM_VALUE);
        
        assertEquals(Erf.erfc(-value), 2.0 - Erf.erfc(value), ABSOLUTE_ERROR);
    }
    
    @Test
    public void testInverfc(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double value = randomizer.nextDouble(); //between 0.0 and 1.0
        
        assertEquals(Erf.erfc(Erf.inverfc(value)), value, ABSOLUTE_ERROR);
    }
    
    @Test
    public void testInverf(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double value = randomizer.nextDouble(); //between 0.0 and 1.0

        assertEquals(Erf.erf(Erf.inverf(value)), value, ABSOLUTE_ERROR);
    }
}
