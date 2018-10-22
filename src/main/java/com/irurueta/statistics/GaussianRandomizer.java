/*
 * Copyright (C) 2012 Alberto Irurueta Carro (alberto@irurueta.com)
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

/**
 * Generates pseudo-random values following a Gaussian distribution having
 * the specified mMean and standard deviation. By default mMean is equal to
 * DEFAULT_MEAN and the standard deviation is equal to 
 * DEFAULT_STANDARD_DEVIATION.
 */
public class GaussianRandomizer extends Randomizer {

    /**
     * Specifies mMean value to be used for random value generation if no mMean
     * is provided.
     */
    public static final double DEFAULT_MEAN = 0.0;

    /**
     * Specifies standard deviation to be used for random value generation if
     * none is provided.
     */
    public static final double DEFAULT_STANDARD_DEVIATION = 1.0;

    /**
     * Contains mMean value to be used for random value generation.
     */
    private double mMean;
    
    /**
     * Standard deviation value to be used for random value generation.
     */
    private double mStandardDeviation;

    /**
     * Constructor.
     * Because neither mMean or standard deviation are provided, values
     * DEFAULT_MEAN and DEFAULT_STANDARD_DEVIATION will be used instead.
     * @param internalRandom Internal Random instance in charge of generating
     * pseudo-random values.
     * @throws NullPointerException thrown if provided internal random is null.
     */
    public GaussianRandomizer(Random internalRandom) 
            throws NullPointerException {
        super(internalRandom);
        mMean = DEFAULT_MEAN;
        mStandardDeviation = DEFAULT_STANDARD_DEVIATION;
    }
    
    /**
     * Constructor.
     * @param internalRandom Internal Random instance if charge of generating
     * pseudo-random values.
     * @param mean Mean value of generated Gaussian values.
     * @param standardDeviation Standard deviation of generated Gaussian values.
     * @throws IllegalArgumentException  thrown if provided standard deviation
     * is negative or zero.
     * @throws NullPointerException if provided internal Random instance is null.
     */
    public GaussianRandomizer(Random internalRandom, double mean, 
            double standardDeviation) throws IllegalArgumentException, 
            NullPointerException {
        super(internalRandom);
        
        if (standardDeviation <= 0.0) {
            throw new IllegalArgumentException();
        }
        mMean = mean;
        mStandardDeviation = standardDeviation;
    }
    
    /**
     * Returns mMean value to be used for Gaussian random value generation.
     * @return Mean value.
     */
    public double getmMean() {
        return mMean;
    }
    
    /**
     * Sets mMean value to be used for Gaussian random value generation.
     * @param mean Mean value.
     */
    public void setmMean(double mean) {
        mMean = mean;
    }
    
    /**
     * Returns standard deviation to be used for Gaussian random value 
     * generation.
     * @return Standard deviation.
     */
    public double getStandardDeviation() {
        return mStandardDeviation;
    }
    
    /**
     * Sets standard deviation to be used for Gaussian random value generation.
     * @param standardDeviation Standard deviation.
     * @throws IllegalArgumentException exception thrown if provided standard
     * deviation is negative or zero.
     */
    public void setStandardDeviation(double standardDeviation)
            throws IllegalArgumentException {
        
        if (standardDeviation <= 0.0) {
            throw new IllegalArgumentException();
        }
        mStandardDeviation = standardDeviation;
    }

    /**
     * Returns next random boolean value. The probability of returning true
     * is equal to obtaining a Gaussian value below the mMean, which is 50%.
     * @return Next boolean value.
     */    
    @Override
    public boolean nextBoolean() {
        return nextBoolean(mMean);
    }
    
    /**
     * Returns next random boolean value. The probability of returning true
     * is equal to obtaining a Gaussian value below the provided threshold, 
     * which is equal to erfc function.
     * @param threshold Threshold to determine whether returned values will be
     * true or false.
     * @return Next random boolean value.
     */
    public boolean nextBoolean(double threshold) {
        return (mStandardDeviation * getInternalRandom().nextGaussian() + mMean) <
                threshold;
    }
    
    /**
     * Fills provided array with random booleans where the probability 
     * of returning true is equal to obtaining a Gaussian value below the 
     * provided threshold, which is equal to erfc function.
     * @param array array to be filled.
     * @param threshold Threshold to determine whether generated values will be
     * true or false.
     */
    public void fill(boolean[] array, double threshold) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextBoolean(threshold);
        }
    }
    
    /**
     * Returns array of booleans having provided length and containing random
     * boolean balues. The probability of returning true is equal to
     * obtaining a Gaussian value below the provided threshold, which is equal
     * to erfc funciton.
     * @param length Length of array to be created.
     * @param threshold Threshold to determine whether returned values will be
     * true or false.
     * @return Array of random booleans.
     * @throws IllegalArgumentException if provided length is zero or negative.
     */
    public boolean[] nextBooleans(int length, double threshold)
        throws IllegalArgumentException {
        
        if (length <= 0) {
            throw new IllegalArgumentException();
        }
        
        boolean[] array = new boolean[length];
        fill(array, threshold);
        return array;
    }
    
    /**
     * Returns next random Gaussian integer value having provided mMean and
     * standard deviation.
     * @return  Next random Gaussian integer value.
     */
    @Override
    public int nextInt() {
        return (int)(mStandardDeviation * getInternalRandom().nextGaussian() +
                mMean);
    }    

    /**
     * Returns next random Gaussian long value having provided mMean and
     * standard deviation.
     * @return  Next random Gaussian long value.
     */    
    @Override
    public long nextLong() {
        return (long)(mStandardDeviation * getInternalRandom().nextGaussian() +
                mMean);
    }

    /**
     * Returns next random Gaussian floating-point value having provided mMean
     * and standard deviation.
     * @return  Next random Gaussian floating-point value.
     */        
    @Override
    public float nextFloat() {
        return (float)(mStandardDeviation * getInternalRandom().nextGaussian() +
                mMean);
    }

    /**
     * Returns next random Gaussian double precision floating-point value having
     * provided mMean and standard deviation.
     * @return  Next random Gaussian double precision floating-point value.
     */            
    @Override
    public double nextDouble() {
        return mStandardDeviation * getInternalRandom().nextGaussian() + mMean;
    }    
    
    /**
     * Returns the randomizer type of this instance.
     * @return Randomizer type.
     */    
    @Override
    public RandomizerType getType() {
        return RandomizerType.GAUSSIAN_RANDOMIZER;
    }
}
