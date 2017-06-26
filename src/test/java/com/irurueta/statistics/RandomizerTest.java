/**
 * @file
 * This file contains Unit Tests for
 * com.irurueta.statistics.Randomizer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 6, 2012
 */
package com.irurueta.statistics;

import java.security.SecureRandom;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class RandomizerTest {
    
    public RandomizerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testCreate(){
        
        Randomizer randomizer;
        
        //test create without parameters
        randomizer = Randomizer.create();
        if(Randomizer.USE_SECURE_RANDOM_BY_DEFAULT){
            assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        }else{
            assertTrue(randomizer.getInternalRandom() instanceof Random);
        }        
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);
        
        //test create with secure parameter
        randomizer = Randomizer.create(false);
        assertTrue(randomizer.getInternalRandom() instanceof Random);
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);

        randomizer = Randomizer.create(true);
        assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);
        
        //test create with Random
        Random random = new SecureRandom();
        randomizer = Randomizer.create(random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(randomizer.getType(), Randomizer.DEFAULT_RANDOMIZER_TYPE);
        
        //Force NullPointerException
        randomizer = null;
        try{
            randomizer = Randomizer.create((Random)null);
            fail("NullPointerExceptioen expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(randomizer);
        
        //test create with RandomizerType
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER);
        if(Randomizer.USE_SECURE_RANDOM_BY_DEFAULT){
            assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        }else{
            assertTrue(randomizer.getInternalRandom() instanceof Random);
        }        
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER);
        if(Randomizer.USE_SECURE_RANDOM_BY_DEFAULT){
            assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        }else{
            assertTrue(randomizer.getInternalRandom() instanceof Random);
        }        
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        
        //test create with RandomizerType and secure parameter
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                false);
        assertTrue(randomizer.getInternalRandom() instanceof Random);
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                true);
        assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                false);
        assertTrue(randomizer.getInternalRandom() instanceof Random);
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                true);
        assertTrue(randomizer.getInternalRandom() instanceof SecureRandom);
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        
        //test create with RandomizerType and internal Random
        randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(randomizer.getType(), RandomizerType.GAUSSIAN_RANDOMIZER);
        
        randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER, 
                random);
        assertSame(randomizer.getInternalRandom(), random);
        assertEquals(randomizer.getType(), RandomizerType.UNIFORM_RANDOMIZER);
        
        //Force NullPointerException        
        randomizer = null;
        try{
            randomizer = Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER, 
                    null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(randomizer);
        try{
            randomizer = Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER,
                    null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        assertNull(randomizer);
        
    }
    
    @Test
    public void testGetSetInternalRandomizer(){
        Random random = new Random();        
        Randomizer randomizer = Randomizer.create(random);
        
        assertSame(random, randomizer.getInternalRandom());
        
        //set new random
        random = new SecureRandom();
        randomizer.setInternalRandom(random);
        
        //check correctness
        assertSame(random, randomizer.getInternalRandom());
        
        //Force NullPointerException
        try{
            randomizer.setInternalRandom(null);
            fail("NullPointerException expected but not thrown");
        }catch(NullPointerException e){}
        
    }
    
    @Test
    public void testSetSeed(){
        Randomizer randomizer = Randomizer.create();
        long seed = 0;
        randomizer.setSeed(seed);
    }
    
}
