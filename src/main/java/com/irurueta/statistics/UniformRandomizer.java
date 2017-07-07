/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.UniformRandomizer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 3, 2012
 */
package com.irurueta.statistics;

import java.util.Random;

/**
 * Generates pseudo-random values following a uniform distribution.
 */
public class UniformRandomizer extends Randomizer{
    
    /**
     * Constructor
     * @param internalRandom Internal Random instance in charge of generating
     * pseudo-random values
     * @throws NullPointerException thrown if provided internal random is null
     */
    public UniformRandomizer(Random internalRandom) 
            throws NullPointerException{
        super(internalRandom);
    }

    /**
     * Returns next boolean value following a uniform distribution (e.g. the
     * probability of returning either true or false is 50%)
     * @return Next boolean value following a uniform distribution
     */    
    @Override
    public boolean nextBoolean(){
        return getInternalRandom().nextBoolean();
    }
        
    /**
     * Returns next random integer value within the range of integer values and
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @return Next integer value following a uniform distribution
     */
    @Override
    public int nextInt(){
        return getInternalRandom().nextInt();
    }
        
    /**
     * Returns next random integer value within 0 and provided value following
     * a uniform distribution (e.g. each possible value has approximately the 
     * same probability to be picked).
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Next integer value following a uniform distribution
     * @throws IllegalArgumentException if provided values is negative
     */
    public int nextInt(int maxValue) throws IllegalArgumentException{
        return getInternalRandom().nextInt(maxValue);
    }
    
    /**
     * Fills provided array with uniform integer values within 0 and provided
     * maxValue following a uniform distribution
     * @param array Array to be filled
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if provided value is negative
     */
    public void fill(int[] array, int maxValue) throws IllegalArgumentException{
        for(int i = 0; i < array.length; i++) array[i] = nextInt(maxValue);
    }
    
    /**
     * Returns array of random uniform integer values within 0 and provided
     * maxValue.
     * @param length Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random uniform integers
     * @throws IllegalArgumentException if provided values are zero or negative
     */
    public int[] nextInts(int length, int maxValue) 
            throws IllegalArgumentException{
        if(length <= 0) throw new IllegalArgumentException();
        int[] array = new int[length];
        fill(array, maxValue);
        return array;
    }
    
    /**
     * Returns next random integer value between the range of provided values
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Next integer value following a uniform distribution
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     * or equal than minValue
     */
    public int nextInt(int minValue, int maxValue) 
            throws IllegalArgumentException{
        
        if(maxValue <= minValue) throw new IllegalArgumentException();
        
        int diff = maxValue - minValue;
        return getInternalRandom().nextInt(diff) + minValue;
    }
    
    /**
     * Fills provided array with uniform integer values within provided
     * minValue (inclusive) and maxValue (exclusive) following a uniform 
     * distribution
     * @param array Array to be filled
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     * minValue
     */
    public void fill(int[] array, int minValue, int maxValue) 
            throws IllegalArgumentException{
        if(maxValue <= minValue) throw new IllegalArgumentException();
        int diff = maxValue - minValue;
        
        for(int i = 0; i < array.length; i++) 
            array[i] = getInternalRandom().nextInt(diff) + minValue;
    }
    
    /**
     * Returns array of random uniform integer values within provided minValue 
     * and maxValue.
     * @param length Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random uniform integers
     * @throws IllegalArgumentException if provided values are zero or negative
     */    
    public int[] nextInts(int length, int minValue, int maxValue){
        int[] array = new int[length];
        fill(array, minValue, maxValue);
        return array;
    }
    
    /**
     * Returns next random long value within the range of long values and
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @return Next long value following a uniform distribution
     */
    @Override
    public long nextLong(){        
        return getInternalRandom().nextLong();
    }
        
    /**
     * Returns next random long value within 0 and provided value following
     * a uniform distribution (e.g. each possible value has approximately the 
     * same probability to be picked).
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Next long value following a uniform distribution
     * @throws IllegalArgumentException if provided value is negative
     */    
    public long nextLong(long maxValue) throws IllegalArgumentException{
        return nextLong(0, maxValue);
    }
    
    /**
     * Fills provided array with uniform long values within 0 and provided
     * maxValue following a uniform distribution
     * @param array Array to be filled
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if provided value is negative
     */    
    public void fill(long[] array, long maxValue){
        for(int i = 0; i < array.length; i++) array[i] = nextLong(maxValue);
    }
    
    /**
     * Returns array of random uniform long values within 0 and provided
     * maxValue.
     * @param length Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random uniform long integers
     * @throws IllegalArgumentException if provided values are zero or negative
     */    
    public long[] nextLongs(int length, long maxValue){
        long[] array = new long[length];
        fill(array);
        return array;
    }
    
    /**
     * Returns next random long value between the range of provided values
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Next long value following a uniform distribution
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     * or equal than minValue
     */    
    public long nextLong(long minValue, long maxValue) 
            throws IllegalArgumentException{
        
        if(maxValue <= minValue) throw new IllegalArgumentException();
        
        long diff = maxValue - minValue;
        return (Math.abs(getInternalRandom().nextLong()) % diff) + minValue;
    }
    
    /**
     * Fills provided array with uniform long values within provided
     * minValue (inclusive) and maxValue (exclusive) following a uniform 
     * distribution
     * @param array Array to be filled
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     * minValue
     */    
    public void fill(long[] array, long minValue, long maxValue){
        for(int i = 0; i < array.length; i++) 
            array[i] = nextLong(minValue, maxValue);
    }
    
    /**
     * Returns array of random uniform long values within provided minValue 
     * and maxValue.
     * @param length Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random uniform integers
     * @throws IllegalArgumentException if provided values are zero or negative
     */        
    public long[] nextLongs(int length, long minValue, long maxValue){
        long[] array = new long[length];
        fill(array);
        return array;
    }

    /**
     * Returns next random floating-point value within the range 0.0 and 1.0
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @return Next float value following a uniform distribution between 0.0 and 
     * 1.0
     */    
    @Override
    public float nextFloat(){
        return getInternalRandom().nextFloat();
    }    
    
    /**
     * Returns next random floating-point value between 0.0 and provided value
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next floating-point value following a uniform distribution.
     * @throws IllegalArgumentException if provided value is negative.
     */
    public float nextFloat(float maxValue) throws IllegalArgumentException{
        return nextFloat(0.0f, maxValue);
    }
    
    /**
     * Fills provided array with floating point values within 0.0 and provided
     * maxValue following a uniform distribution
     * @param array Array to be filled
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if provided value is negative
     */        
    public void fill(float[] array, float maxValue){
        for(int i = 0; i < array.length; i++)
            array[i] = nextFloat(maxValue);
    }
    
    /**
     * Returns array of random uniform floating point values within 0.0 and 
     * provided maxValue.
     * @param length Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random uniform floating point
     * @throws IllegalArgumentException if provided values are zero or negative
     */        
    public float[] nextFloats(int length, float maxValue){
        float[] array = new float[length];
        fill(array, maxValue);
        return array;
    }
    
    /**
     * Returns next random floating-point value between the range of provided 
     * values following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Next floating-point value following a uniform distribution
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     * or equal than minValue
     */        
    public float nextFloat(float minValue, float maxValue) 
            throws IllegalArgumentException{
        
        if(maxValue <= minValue) throw new IllegalArgumentException();
        
        float diff = maxValue - minValue;
        return getInternalRandom().nextFloat() * diff + minValue;
    }
    
    /**
     * Fills provided array with uniform floating point values within provided
     * minValue (inclusive) and maxValue (exclusive) following a uniform 
     * distribution
     * @param array Array to be filled
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     * minValue
     */        
    public void fill(float[] array, float minValue, float maxValue){
        for(int i = 0; i < array.length; i++) 
            array[i] = nextFloat(minValue, maxValue);
    }
    
    /**
     * Returns array of random uniform floating point values within provided 
     * minValue and maxValue.
     * @param length Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random floating point values
     * @throws IllegalArgumentException if provided values are zero or negative
     */            
    public float[] nextFloats(int length, float minValue, float maxValue){
        float[] array = new float[length];
        fill(array, minValue, maxValue);
        return array;
    }
    
    /**
     * Returns next random double precision floating-point value within the 
     * range 0.0 and 1.0 following a uniform distribution (e.g. each possible 
     * value has approximately the same probability to be picked).
     * @return Next float value following a uniform distribution between 0.0 and 
     * 1.0
     */        
    @Override
    public double nextDouble(){
        return getInternalRandom().nextDouble();
    }
    
    /**
     * Returns next random floating-point value between 0.0 and provided value
     * following a uniform distribution (e.g. each possible value has 
     * approximately the same probability to be picked).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next floating-point value following a uniform distribution.
     * @throws IllegalArgumentException if provided value is negative.
     */
    public double nextDouble(double maxValue) throws IllegalArgumentException{
        return nextDouble(0.0, maxValue);
    }    
    
    /**
     * Fills provided array with double precision floating point values within 
     * 0.0 and provided maxValue following a uniform distribution
     * @param array Array to be filled
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if provided value is negative
     */            
    public void fill(double[] array, double maxValue){
        for(int i = 0; i < array.length; i++) array[i] = nextDouble(maxValue);
    }
    
    /**
     * Returns array of random uniform double precision floating point values 
     * within 0.0 and provided maxValue.
     * @param length Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random uniform double precision floating point values
     * @throws IllegalArgumentException if provided values are zero or negative
     */            
    public double[] nextDoubles(int length, double maxValue){
        double[] array = new double[length];
        fill(array, maxValue);
        return array;
    }
    
    /**
     * Returns next random double precision floating-point value between the 
     * range of provided values following a uniform distribution (e.g. each 
     * possible value has approximately the same probability to be picked).
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Next floating-point value following a uniform distribution
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     * or equal than minValue
     */            
    public double nextDouble(double minValue, double maxValue)
            throws IllegalArgumentException{

        if(maxValue <= minValue) throw new IllegalArgumentException();
        
        double diff = maxValue - minValue;
        return getInternalRandom().nextDouble() * diff + minValue;        
    } 
    
    /**
     * Fills provided array with uniform double precision floating point values 
     * within provided minValue (inclusive) and maxValue (exclusive) following 
     * a uniform distribution
     * @param array Array to be filled
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     * minValue
     */            
    public void fill(double[] array, double minValue, double maxValue){
        for(int i = 0; i < array.length; i++)
            array[i] = nextDouble(minValue, maxValue);
    }
    
    /**
     * Returns array of random uniform floating point values within provided 
     * minValue and maxValue.
     * @param length Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive)
     * @param maxValue Maximum value to be returned (exclusive)
     * @return Array of random floating point values
     * @throws IllegalArgumentException if provided values are zero or negative
     */    
    public double[] nextDoubles(int length, double minValue, double maxValue){
        double[] array = new double[length];
        fill(array, minValue, maxValue);
        return array;
    }
    
    /**
     * Returns the randomizer type of this instance
     * @return Randomizer type
     */
    @Override
    public RandomizerType getType(){
        return RandomizerType.UNIFORM_RANDOMIZER;
    }
}