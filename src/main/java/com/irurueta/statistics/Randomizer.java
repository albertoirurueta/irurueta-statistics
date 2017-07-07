/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.Randomizer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 3, 2012
 */
package com.irurueta.statistics;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Parent class of all Randomizers. Specific subclasses exist for different 
 * distribution types (currently only uniform and gaussian distributions are
 * implemented).
 * This class provides static methods to ease the instantiation of a Randomizer.
 * For instance, a UniformRandomizer can be created like this:
 * <pre>
 * {@code 
 * Randomizer.create(RandomizerType.UNIFORM_RANDOMIZER);
 * }
 * </pre>
 * whereas a GaussianRandomizer can be created like this:
 * <pre>
 * {@code 
 * Randomizer.create(RandomizerType.GAUSSIAN_RANDOMIZER);
 * }
 * </pre>
 * @author albertoirurueta
 */
public abstract class Randomizer {
    
    /**
     * Instance in charge of generating pseudo-random values. Secure instances
     * can be used if the generated values need to be ensured to be "more" 
     * random at the expense of higher computational cost
     */
    private Random internalRandom;
    
    /**
     * Indicates that by default non secured random instance is used if none
     * is provided
     */
    public static final boolean USE_SECURE_RANDOM_BY_DEFAULT = false;
    
    /**
     * Indicates default randomizer type if non is provided. By default uniform
     * randomizers are created
     */
    public static final RandomizerType DEFAULT_RANDOMIZER_TYPE = 
            RandomizerType.UNIFORM_RANDOMIZER;
    
    /**
     * Constructor
     * @param internalRandom Instance in charge of generating pseudo-random 
     *      values
     * @throws NullPointerException if provided value is null
     */
    public Randomizer(Random internalRandom) throws NullPointerException{
        
        if(internalRandom == null) throw new NullPointerException();
        this.internalRandom = internalRandom;
    }
    
    /**
     * Returns internal instance in charge of generating pseudo-random values
     * @return instance in charge of generating pseudo-random values.
     */
    public Random getInternalRandom(){
        return internalRandom;
    }
    
    /**
     * Sets internal instance in charge of generating pseudo-random values.
     * @param internalRandom Instance in charge of generating pseudo-random 
     *      values
     * @throws NullPointerException if provided value is null
     */
    public void setInternalRandom(Random internalRandom) 
            throws NullPointerException{
        
        if(internalRandom == null) throw new NullPointerException();
        this.internalRandom = internalRandom;
    }
    
    /**
     * Sets the seed for the internal randomizer.
     * A seed determines the sequence of pseudo-random numbers to be generated.
     * Whenever the same seed is used on different executions, the generated
     * sequence will be the same.
     * To alleviate this issue and allow the generation of "more" random values
     * the system clock can be used as the seed.
     * Example:
     * <pre>
     * {@code 
     * Randomizer randomizer = Randomizer.create();
     * randomizer.setSeed(System.currentTimeMillis());
     * }
     * </pre>
     * Notice that using the clock as seed is not a secure solution to generate
     * random values. If a secure solution is required (i.e. for encryption 
     * purposes, then a SecureRandom instance must be provided as the internal 
     * random instance)
     * @param seed Value to be used as seed
     */
    public void setSeed(long seed){
        internalRandom.setSeed(seed);
    }
    
    /**
     * Returns next random boolean value following a given distribution 
     * depending on the randomizer type.
     * @return Next random boolean value
     */
    public abstract boolean nextBoolean();
    
    /**
     * Fills provided array with random booleans
     * @param array Array to be filled.
     */
    public void fill(boolean[] array){
        for(int i = 0; i < array.length; i++) array[i] = nextBoolean();
    }
    
    /**
     * Returns array of booleans 
     * @param length Length of array to be returned
     * @return Array of uniform booleans
     * @throws IllegalArgumentException if provided value is zero or negative
     */
    public boolean[] nextBooleans(int length) throws IllegalArgumentException{
        if(length <= 0) throw new IllegalArgumentException();
        boolean[] array = new boolean[length];
        fill(array);
        return array;
    }
    
    /**
     * Returns next random integer value following a given distribution 
     * depending on the randomizer type.
     * @return Next random integer value
     */
    public abstract int nextInt();
    
    /**
     * Fills provided array with random integer values.
     * @param array Array to be filled.
     */
    public void fill(int[] array){
        for(int i = 0; i < array.length; i++) array[i] = nextInt();
    }
    
    /**
     * Returns array of random integers.
     * @param length Length of array to be returned.
     * @return Array of random integers
     * @throws IllegalArgumentException if provided value is zero or negative 
     */
    public int[] nextInts(int length) throws IllegalArgumentException{
        if(length <= 0) throw new IllegalArgumentException();
        int[] array = new int[length];
        fill(array);
        return array;
    }
    
    /**
     * Returns next random long value following a given distribution depending
     * on the randomizer type.
     * @return Next random integer value
     */
    public abstract long nextLong();
    
    /**
     * Fills provided array with random long values.
     * @param array Array to be filled.
     */
    public void fill(long[] array){
        for(int i = 0; i < array.length; i++) array[i] = nextLong();
    }

    /**
     * Returns array of random long values.
     * @param length Length of array to be returned.
     * @return Array of random long values.
     * @throws IllegalArgumentException if provided value is zero or negative
     */
    public long[] nextLongs(int length) throws IllegalArgumentException{
        if(length <= 0) throw new IllegalArgumentException();
        long[] array = new long[length];
        fill(array);
        return array;
    }
    
    /**
     * Returns next random floating-point value following a given distribution
     * depending on the randomizer type.
     * @return Next random integer value
     */
    public abstract float nextFloat();
    
    /**
     * Fills provided array with random floating point values
     * @param array Array to be filled
     */
    public void fill(float[] array){
        for(int i = 0; i < array.length; i++) array[i] = nextFloat();
    }
    
    /**
     * Returns array of floating point values.
     * @param length Length of array to be returned.
     * @return Array of random float values.
     * @throws IllegalArgumentException if provided value is zero or negative
     */
    public float[] nextFloats(int length) throws IllegalArgumentException{
        if(length <= 0) throw new IllegalArgumentException();
        float[] array = new float[length];
        fill(array);
        return array;
    }
    
    /**
     * Returns next random double precision floating-point value following a
     * given distribution depending on the randomizer type.
     * @return Next random double precision floating-point value
     */
    public abstract double nextDouble();
    
    /**
     * Fills provided array with random double precision floating point values
     * @param array Array to be filled
     */
    public void fill(double[] array){
        for(int i = 0; i < array.length; i++) array[i] = nextDouble();
    }
    
    /**
     * Returns array of double precision floating point values.
     * @param length Length of array to be returned.
     * @return Array of random double values.
     * @throws IllegalArgumentException if provided value is zero or negative
     */
    public double[] nextDoubles(int length) throws IllegalArgumentException{
        if(length <= 0) throw new IllegalArgumentException();
        double[] array = new double[length];
        fill(array);
        return array;
    }
    
    /**
     * Returns the randomizer type of this instance
     * @return Randomizer type
     */    
    public abstract RandomizerType getType();
    
    /**
     * Creates a new Randomizer instance using DEFAULT_RANDOMIZER_TYPE and
     * USE_SECURE_RANDOM_By_DEFAULT to determine what type of random 
     * distribution is to be used and whether the random generator must be
     * secure or not.
     * @return A Randomizer instance using default randomizer type and secure 
     * mode
     */
    public static Randomizer create(){
        return create(DEFAULT_RANDOMIZER_TYPE);
    }
    
    /**
     * Creates a new Randomizer instance using DEFAULT_RANDOMIZER_TYPE. The
     * instantiated Randomizer will generate secure values depending on provided
     * parameter. When secure random generator is requested, generated values
     * tend to be more "random" at the expense of larger computational cost.
     * Secure mode should only be used on specific applications, such as
     * cryptography, and hence the name of the parameter.
     * @param useSecureRandom Parameter indicating whether generated values will
     * be used for security purposes such as cryptography.
     * @return A Randomizer instance using provided secure mode
     */
    public static Randomizer create(boolean useSecureRandom){
        return create(DEFAULT_RANDOMIZER_TYPE, useSecureRandom);
    }
    
    /**
     * Creates a new Randomizer instance using provided internal randomizer 
     * instance.
     * @param internalRandomizer Internal instance to be used for generation of
     * pseudo-random values
     * @return A Randomizer instance using provided internal randomizer.
     * @throws NullPointerException Exception thrown if provided internal 
     * randomizer is null
     */
    public static Randomizer create(Random internalRandomizer) 
            throws NullPointerException{
        return create(DEFAULT_RANDOMIZER_TYPE, internalRandomizer);
    }
    
    /**
     * Creates a new Randomizer instance using provided randomizer type and
     * the default secure mode specified by USE_SECURE_RANDOM_BY_DEFAULT
     * @param type Randomizer type to be used when creating an instance
     * @return A Randomizer instance using provided randomizer type.
     */
    public static Randomizer create(RandomizerType type){
        return create(type, USE_SECURE_RANDOM_BY_DEFAULT);
    }
    
    /**
     * Creates a new Randomizer instance using provided randomizer type and
     * provided secure mode.
     * @param type Randomizer type to be used when creating an instance.
     * @param useSecureRandom Boolean indicating whether generated values will
     * be secure for specific purposes such as cryptography. Secure mode 
     * generates more "random" values at the expense of higher computational 
     * cost
     * @return A Randomizer instance using provided randomizer type and secure
     * mode
     */
    public static Randomizer create(RandomizerType type, 
            boolean useSecureRandom){        
        if(useSecureRandom) return create(type, new SecureRandom());
        else return create(type, new Random());
    }
    
    /**
     * Creates a new Randomizer instance using provided randomizer type and
     * internal randomizer.
     * @param type Randomizer type to be used when creating an instance.
     * @param internalRandom Internal random instance to be used to generate
     * pseudo-random values
     * @return A Randomizer instance using provided randomizer type and internal
     * random instance.
     * @throws NullPointerException Exception thrown if internal random is null
     */
    public static Randomizer create(RandomizerType type, 
            Random internalRandom) throws NullPointerException{
        
        if(internalRandom == null) throw new NullPointerException();
        
        switch(type){
            case GAUSSIAN_RANDOMIZER:
                return new GaussianRandomizer(internalRandom);
            case UNIFORM_RANDOMIZER:
            default:
                return new UniformRandomizer(internalRandom);
        }
    }
}