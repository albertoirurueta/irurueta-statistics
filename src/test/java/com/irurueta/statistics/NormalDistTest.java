/**
 * @file
 * This file contains unit tests for
 * com.irurueta.statistics.NormalDist
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date December 28, 2015
 */
package com.irurueta.statistics;

import com.irurueta.statistics.NormalDist.DerivativeEvaluator;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistTest {
    
    public static final double MIN_RANDOM_VALUE = -100.0;
    public static final double MAX_RANDOM_VALUE = 100.0;
    
    public static final double ABSOLUTE_ERROR = 1e-6;
    public static final double LARGE_ABSOLUTE_ERROR = 1e-3;
    
    public static final int N_SAMPLES = 1000000;
    
    public static final double RELATIVE_ERROR = 0.10;
    
    public NormalDistTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}
    
    @Test
    public void testConstructor(){
        //test empty constructor
        NormalDist dist = new NormalDist();
        
        assertEquals(dist.getMean(), 0.0, 0.0);
        assertEquals(dist.getStandardDeviation(), 1.0, 0.0);
        
        //test constructor with mean and standard deviation
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        dist = new NormalDist(mean, std);
        
        //check correctness
        assertEquals(dist.getMean(), mean, 0.0);
        assertEquals(dist.getStandardDeviation(), std, 0.0);
        
        //Force IllegalArgumentException
        dist = null;
        try{
            dist = new NormalDist(mean, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        assertNull(dist);
    }
    
    @Test
    public void testGetSetMean(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        
        NormalDist dist = new NormalDist();
        
        //check default value
        assertEquals(dist.getMean(), 0.0, 0.0);
        
        //set new value
        dist.setMean(mean);
        
        //check correctness
        assertEquals(dist.getMean(), mean, 0.0);
    }
    
    @Test
    public void testGetSetStandardDeviation(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        NormalDist dist = new NormalDist();
        
        //check default value
        assertEquals(dist.getStandardDeviation(), 1.0, 0.0);
        
        //set new value
        dist.setStandardDeviation(std);
        
        //check correctness
        assertEquals(dist.getStandardDeviation(), std, 0.0);
        
        //Force IllegalArgumentException
        try{
            dist.setStandardDeviation(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testGetSetVariance() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double var = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        NormalDist dist = new NormalDist();
        
        //check default value
        assertEquals(dist.getVariance(), 1.0, ABSOLUTE_ERROR);
        
        //set new value
        dist.setVariance(var);
        
        //check correctness
        assertEquals(dist.getVariance(), var, ABSOLUTE_ERROR);
        assertEquals(dist.getStandardDeviation(), Math.sqrt(var), 
                ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try {
            dist.setVariance(0.0);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException e) { }
    }
    
    @Test
    public void testGetP(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        double x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        NormalDist dist = new NormalDist(mean, std);
        
        assertEquals(dist.p(x), NormalDist.p(x, mean, std), 0.0);
        
        assertEquals(dist.p(mean), 0.398942280401432678 / std, ABSOLUTE_ERROR);
        assertEquals(NormalDist.p(mean, mean, std), 0.398942280401432678 / std, 
                ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            NormalDist.p(x, mean, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testCdf(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        double x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);

        NormalDist dist = new NormalDist(mean, std);

        assertEquals(dist.cdf(x), NormalDist.cdf(x, mean, std), 0.0);
        
        assertEquals(dist.cdf(mean - 3.0*std), 0.00135, LARGE_ABSOLUTE_ERROR);
        assertEquals(dist.cdf(mean - 2.0*std), 0.02275, LARGE_ABSOLUTE_ERROR);
        assertEquals(dist.cdf(mean - std), 0.15866, LARGE_ABSOLUTE_ERROR);    
        assertEquals(dist.cdf(mean), 0.5, LARGE_ABSOLUTE_ERROR);        
        assertEquals(dist.cdf(mean + std), 0.84134, LARGE_ABSOLUTE_ERROR);
        assertEquals(dist.cdf(mean + 2.0*std), 0.97725, LARGE_ABSOLUTE_ERROR);
        assertEquals(dist.cdf(mean + 3.0*std), 0.99865, LARGE_ABSOLUTE_ERROR);
        
        
        assertEquals(NormalDist.cdf(mean - 3.0*std, mean, std), 0.00135, 
                LARGE_ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(mean - 2.0*std, mean, std), 0.02275,
                LARGE_ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(mean - std, mean, std), 0.15866, 
                LARGE_ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(mean, mean, std), 0.5, 
                LARGE_ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(mean + std, mean, std), 0.84134, 
                LARGE_ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(mean + 2.0*std, mean, std), 0.97725, 
                LARGE_ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(mean + 3.0*std, mean, std), 0.99865,
                LARGE_ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            NormalDist.cdf(x, mean, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testInvcdf(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        double x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double p = randomizer.nextDouble(); //value between 0.0 and 1.0

        NormalDist dist = new NormalDist(mean, std);

        assertEquals(dist.invcdf(dist.cdf(x)), x, ABSOLUTE_ERROR);
        assertEquals(dist.cdf(dist.invcdf(p)), p, ABSOLUTE_ERROR);
        
        assertEquals(NormalDist.invcdf(NormalDist.cdf(x, mean, std), mean, std), 
                x, ABSOLUTE_ERROR);
        assertEquals(NormalDist.cdf(NormalDist.invcdf(p, mean, std), mean, std),
                p, ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            NormalDist.invcdf(p, mean, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            NormalDist.invcdf(0.0, mean, std);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            NormalDist.invcdf(1.0, mean, std);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
        try{
            dist.invcdf(1.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
    
    @Test
    public void testMahalanobisDistance(){
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double std = randomizer.nextDouble(0.0, MAX_RANDOM_VALUE);
        
        double x = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);        
        
        NormalDist dist = new NormalDist(mean, std);
        
        assertEquals(dist.mahalanobisDistance(x), 
                NormalDist.mahalanobisDistance(x, mean, std), 0.0);
        
        assertEquals(dist.mahalanobisDistance(x), Math.abs(x - mean) / std, 
                ABSOLUTE_ERROR);
        assertEquals(NormalDist.mahalanobisDistance(x, mean, std), 
                Math.abs(x - mean) / std, ABSOLUTE_ERROR);
        
        //Force IllegalArgumentException
        try{
            NormalDist.mahalanobisDistance(x, mean, 0.0);
            fail("IllegalArgumentException expected but not thrown");
        }catch(IllegalArgumentException e){}
    }
        
    @Test
    public void testPropagateSinusoidal() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double standardDeviation = randomizer.nextDouble(1e-6, 1e-3);
        
        NormalDist dist = new NormalDist(mean, standardDeviation);
        DerivativeEvaluator evaluator = new NormalDist.DerivativeEvaluator() {
            @Override
            public double evaluate(double x) {
                return Math.sin(x);
            }

            @Override
            public double evaluateDerivative(double x) {
                return Math.cos(x);
            }
        };   
        
        NormalDist result = new NormalDist();
        NormalDist.propagate(evaluator, mean, standardDeviation, result);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        result = NormalDist.propagate(evaluator, mean, standardDeviation);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        result = new NormalDist();
        NormalDist.propagate(evaluator, dist, result);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        result = NormalDist.propagate(evaluator, dist);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);

        
        result = new NormalDist();
        dist.propagateThisDistribution(evaluator, result);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);

        
        result = dist.propagateThisDistribution(evaluator);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        //generate a large number of Gaussian random samples and propagate them
        //through function f(x) = sin(x)
        GaussianRandomizer gaussRandomizer = new GaussianRandomizer(
                new Random(), mean, standardDeviation);
        double x, y;
        double resultMean = 0.0, sqrSum = 0.0, resultStandardDeviation;
        for(int i = 0; i < N_SAMPLES; i++) {
            x = gaussRandomizer.nextDouble();
            y = evaluator.evaluate(x);
            
            resultMean += (double)y / (double)N_SAMPLES;
            sqrSum += (double)y * (double)y / (double)N_SAMPLES;
        }
        
        resultStandardDeviation = Math.sqrt(sqrSum - resultMean * resultMean);
        
        assertEquals(result.getMean(), resultMean, 
                RELATIVE_ERROR * Math.abs(resultMean));
        assertEquals(result.getStandardDeviation(), resultStandardDeviation,
                RELATIVE_ERROR * resultStandardDeviation);
    }
    
    @Test
    public void testPropagatePoly() {
        UniformRandomizer randomizer = new UniformRandomizer(new Random());
        double mean = randomizer.nextDouble(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        double standardDeviation = randomizer.nextDouble(1e-6, 1e-3);
        
        NormalDist dist = new NormalDist(mean, standardDeviation);
        DerivativeEvaluator evaluator = new NormalDist.DerivativeEvaluator() {
            @Override
            public double evaluate(double x) {
                return x * x;
            }

            @Override
            public double evaluateDerivative(double x) {
                return 2.0 * x;
            }
        };   
        
        NormalDist result = new NormalDist();
        NormalDist.propagate(evaluator, mean, standardDeviation, result);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        result = NormalDist.propagate(evaluator, mean, standardDeviation);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        result = new NormalDist();
        NormalDist.propagate(evaluator, dist, result);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        result = NormalDist.propagate(evaluator, dist);
        
        //check correctness
        assertEquals(result.getMean(), evaluator.evaluate(mean), 
                ABSOLUTE_ERROR);
        assertEquals(result.getStandardDeviation(), 
                Math.abs(evaluator.evaluateDerivative(mean) * 
                standardDeviation), ABSOLUTE_ERROR);
        
        
        //generate a large number of Gaussian random samples and propagate them
        //through function f(x) = sin(x)
        GaussianRandomizer gaussRandomizer = new GaussianRandomizer(
                new Random(), mean, standardDeviation);
        double x, y;
        double resultMean = 0.0, sqrSum = 0.0, resultStandardDeviation;
        for(int i = 0; i < N_SAMPLES; i++) {
            x = gaussRandomizer.nextDouble();
            y = evaluator.evaluate(x);
            
            resultMean += (double)y / (double)N_SAMPLES;
            sqrSum += (double)y * (double)y / (double)N_SAMPLES;
        }
        
        resultStandardDeviation = Math.sqrt(sqrSum - resultMean * resultMean);
        
        assertEquals(result.getMean(), resultMean, 
                RELATIVE_ERROR * Math.abs(resultMean));
        assertEquals(result.getStandardDeviation(), resultStandardDeviation,
                RELATIVE_ERROR * resultStandardDeviation);        
    }
}
