/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.RandomizerType
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date April 3, 2012
 */
package com.irurueta.statistics;

/**
 * Indicates type of Randomizer
 */
public enum RandomizerType {

    /**
     * Type used for Uniform randomizers where generated values are uniformly
     * distributed
     */
    UNIFORM_RANDOMIZER,
    
    /**
     * Type used for Gaussian randomizers where generated values are distributed
     * following a Gaussian distribution
     */
    GAUSSIAN_RANDOMIZER
}
