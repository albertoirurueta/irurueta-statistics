/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.GaussianRandomizer
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 3, 2012
 */
package com.irurueta.statistics;

import java.util.Random;

/**
 * Generates pseudo-random values following a Gaussian distribution having
 * the specified mean and standard deviation. By default mean is equal to
 * DEFAULT_MEAN and the standard deviation is equal to 
 * DEFAULT_STANDARD_DEVIATION.
 */
public class GaussianRandomizer extends Randomizer{
    
    /**
     * Contains mean value to be used for random value generation
     */
    private double mean;
    
    /**
     * Standard deviation value to be used for random value generation
     */
    private double standardDeviation;
    
    /**
     * Specifies mean value to be used for random value generation if no mean
     * is provided.
     */
    public static double DEFAULT_MEAN = 0.0;
    
    /**
     * Specifies standard deviation to be used for random value generation if
     * none is provided.
     */
    public static double DEFAULT_STANDARD_DEVIATION = 1.0;

    /**
     * Constructor.
     * Because neither mean or standard deviation are provided, values
     * DEFAULT_MEAN and DEFAULT_STANDARD_DEVIATION will be used instead.
     * @param internalRandom Internal Random instance in charge of generating
     * pseudo-random values
     * @throws NullPointerException thrown if provided internal random is null
     */
    public GaussianRandomizer(Random internalRandom) 
            throws NullPointerException{
        super(internalRandom);
        mean = DEFAULT_MEAN;
        standardDeviation = DEFAULT_STANDARD_DEVIATION;
    }
    
    /**
     * Constructor
     * @param internalRandom Internal Random instance if charge of generating
     * pseudo-random values
     * @param mean Mean value of generated Gaussian values
     * @param standardDeviation Standard deviation of generated Gaussian values
     * @throws IllegalArgumentException  thrown if provided standard deviation
     * is negative or zero.
     * @throws NullPointerException if provided internal Random instance is null
     */
    public GaussianRandomizer(Random internalRandom, double mean, 
            double standardDeviation) throws IllegalArgumentException, 
            NullPointerException{
        super(internalRandom);
        
        if(standardDeviation <= 0.0) throw new IllegalArgumentException();
        this.mean = mean;
        this.standardDeviation = standardDeviation;                
    }
    
    /**
     * Returns mean value to be used for Gaussian random value generation.
     * @return Mean value
     */
    public double getMean(){
        return mean;
    }
    
    /**
     * Sets mean value to be used for Gaussian random value generation.
     * @param mean Mean value
     */
    public void setMean(double mean){
        this.mean = mean;
    }
    
    /**
     * Returns standard deviation to be used for Gaussian random value 
     * generation.
     * @return Standard deviation
     */
    public double getStandardDeviation(){
        return standardDeviation;
    }
    
    /**
     * Sets standard deviation to be used for Gaussian random value generation
     * @param standardDeviation Standard deviation.
     * @throws IllegalArgumentException exception thrown if provided standard
     * deviation is negative or zero.
     */
    public void setStandardDeviation(double standardDeviation) 
            throws IllegalArgumentException{
        
        if(standardDeviation <= 0.0) throw new IllegalArgumentException();
        this.standardDeviation = standardDeviation;
    }

    /**
     * Returns next random boolean value. The probability of returning true
     * is equal to obtaining a Gaussian value below the mean, which is 50%
     * @return Next boolean value
     */    
    @Override
    public boolean nextBoolean(){
        return nextBoolean(mean);
    }
    
    /**
     * Returns next random boolean value. The probability of returning true
     * is equal to obtaining a Gaussian value below the provided threshold, 
     * which is equal to erfc function
     * @param threshold Threshold to determine whether returned values will be
     * true or false
     * @return Next random boolean value
     */
    public boolean nextBoolean(double threshold){
        return (standardDeviation * getInternalRandom().nextGaussian() + mean) < 
                threshold;
    }
    
    /**
     * Fills provided array with random booleans where the probability 
     * of returning true is equal to obtaining a Gaussian value below the 
     * provided threshold, which is equal to erfc function.
     * @param array array to be filled.
     * @param threshold Threshold to determine whether generated values will be
     * true or false
     */
    public void fill(boolean[] array, double threshold){
        for(int i = 0; i < array.length; i++) array[i] = nextBoolean(threshold);
    }
    
    /**
     * Returns array of booleans having provided length and containing random
     * boolean balues. The probability of returning true is equal to
     * obtaining a Gaussian value below the provided threshold, which is equal
     * to erfc funciton.
     * @param length Length of array to be created.
     * @param threshold Threshold to determine whether returned values will be
     * true or false.
     * @return Array of random booleans
     * @throws IllegalArgumentException if provided length is zero or negative
     */
    public boolean[] nextBooleans(int length, double threshold)
        throws IllegalArgumentException{
        
        if(length <= 0) throw new IllegalArgumentException();
        
        boolean[] array = new boolean[length];
        fill(array, threshold);
        return array;
    }
    
    /**
     * Returns next random Gaussian integer value having provided mean and
     * standard deviation
     * @return  Next random Gaussian integer value
     */
    @Override
    public int nextInt(){
        return (int)(standardDeviation * getInternalRandom().nextGaussian() + 
                mean);
    }    

    /**
     * Returns next random Gaussian long value having provided mean and
     * standard deviation
     * @return  Next random Gaussian long value
     */    
    @Override
    public long nextLong(){        
        return (long)(standardDeviation * getInternalRandom().nextGaussian() + 
                mean);
    }

    /**
     * Returns next random Gaussian floating-point value having provided mean 
     * and standard deviation
     * @return  Next random Gaussian floating-point value
     */        
    @Override
    public float nextFloat(){
        return (float)(standardDeviation * getInternalRandom().nextGaussian() + 
                mean);
    }

    /**
     * Returns next random Gaussian double precision floating-point value having
     * provided mean and standard deviation
     * @return  Next random Gaussian double precision floating-point value
     */            
    @Override
    public double nextDouble(){
        return standardDeviation * getInternalRandom().nextGaussian() + mean;
    }    
    
    /**
     * Returns the randomizer type of this instance
     * @return Randomizer type
     */    
    @Override
    public RandomizerType getType(){
        return RandomizerType.GAUSSIAN_RANDOMIZER;
    }
}
