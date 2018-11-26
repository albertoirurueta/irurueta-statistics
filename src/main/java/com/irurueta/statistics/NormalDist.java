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

/**
 * Contains methods to work with normal (i.e. Gaussian) distributions.
 * Methods of this class use methods of the Erf class.
 * This class is based in code of Numerical Recipes 3rd ed. section 6.14.1.
 */
public class NormalDist {
    /**
     * Square root of 2.
     */
    private static final double SQRT2 = Math.sqrt(2.0);

    /**
     * Square root of 2 divided by 2.
     */
    private static final double HALF_SQRT2 = SQRT2 / 2.0;

    /**
     * Term to normalize Gaussian so that its integral from -infinity to infinity is one.
     */
    private static final double GAUSSIAN_NORM = 1.0 / Math.sqrt(2.0*Math.PI);

    /**
     * Mean value of Gaussian distribution.
     */
    private double mMu;
    
    /**
     * Standard deviation of Gaussian distribution.
     */
    private double mSig;
    
    /**
     * Constructor. Initializes a Gaussian distribution with zero mean and
     * unitary standard deviation (i.e. N(0,1)).
     */
    public NormalDist() {
        mMu = 0.0;
        mSig = 1.0;
    }

    /**
     * Constructor with mean and standard deviation.
     * @param mu mean value of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @throws IllegalArgumentException if provided standard deviation is zero 
     * or negative.
     */
    public NormalDist(double mu, double sig) {
        setStandardDeviation(sig);
        setMean(mu);
    }
    
    /**
     * Gets mean value of Gaussian distribution.
     * @return mean value of Gaussian distribution.
     */
    public double getMean() {
        return mMu;
    }
    
    /**
     * Sets mean value of Gaussian distribution.
     * @param mu mean value of Gaussian distribution.
     */
    public final void setMean(double mu) {
        mMu = mu;
    }
    
    /**
     * Gets standard deviation of Gaussian distribution.
     * @return standard deviation of Gaussian distribution.
     */
    public double getStandardDeviation() {
        return mSig;
    }
    
    /**
     * Sets standard deviation of Gaussian distribution.
     * @param sig standard deviation to be set.
     * @throws IllegalArgumentException if provided standard deviation is zero 
     * or negative.
     */
    public final void setStandardDeviation(double sig) {
        if (sig <= 0.0) {
            throw new IllegalArgumentException();
        }
        mSig = sig;
    }
    
    /**
     * Gets variance of Gaussian distribution.
     * @return variance of Gaussian distribution.
     */
    public double getVariance() {
        return mSig * mSig;
    }
    
    /**
     * Sets variance of Gaussian distribution.
     * @param variance variance of Gaussian distribution.
     * @throws IllegalArgumentException if provided variance is zero or 
     * negative.
     */
    public void setVariance(double variance) {
        if (variance <= 0.0) {
            throw new IllegalArgumentException(
                    "variance must be greater than zero");
        }
        mSig = Math.sqrt(variance);
    }
    
    /**
     * Evaluates the probability density function (p.d.f.) of a Gaussian 
     * distribution having mean mu and standard deviation sig at provided point 
     * x.
     * @param x point where p.d.f. is evaluated.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return evaluation of p.d.f.
     * @throws IllegalArgumentException if provided standard deviation is zero 
     * or negative.
     */
    public static double p(double x, double mu, double sig) {
        if (sig <= 0.0) {
            throw new IllegalArgumentException();
        }
        
        return internalP(x, mu, sig);
    }        

    /**
     * Evaluates the probability density function (p.d.f.) of a Gaussian 
     * distribution having the mean and standard deviation of this instance at 
     * provided point x.
     * @param x point where p.d.f. is evaluated.
     * @return evaluation of p.d.f.
     */
    public double p(double x) {
        return internalP(x, mMu, mSig);
    }
    
    /**
     * Evaluates the cumulative distribution function (c.d.f.) of a Gaussian
     * distribution having mean mu and standard deviation sig at provided point 
     * x.
     * The c.d.f is equivalent to the probability of the Gaussian distribution
     * of having a value less than x, and it is computed as the integral from
     * -infinity to x of the Gaussian p.d.f.
     * @param x point where c.d.f. is evaluated.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return evaluation of c.d.f.
     * @throws IllegalArgumentException if provided standard deviation is zero
     * or negative.
     */
    public static double cdf(double x, double mu, double sig) {
        if (sig <= 0.0) {
            throw new IllegalArgumentException(
                    "standard deviation must be greater than zero");
        }

        return internalCdf(x, mu, sig);
    }

    /**
     * Evaluates the cumulative distribution function (c.d.f.) of a Gaussian 
     * distribution having the mean and standard deviation of this instance at
     * provided point x.
     * The c.d.f is equivalent to the probability of the Gaussian distribution
     * of having a value less than x, and it is computed as the integral from
     * -infinity to x of the Gaussian p.d.f.
     * Because the c.d.f is a probability, it always returns values between 0.0
     * and 1.0.
     * @param x point where c.d.f. is evaluated.
     * @return evaluation of c.d.f.
     */
    public double cdf(double x) {
        return internalCdf(x, mMu, mSig);
    }
    
    /**
     * Evaluates the inverse cumulative distribution function of a Gaussian
     * distribution having mean mu and standard deviation sig at provided point
     * p.
     * Because the c.d.f is a monotonically increasing function with values
     * between 0.0 and 1.0, its inverse is uniquely defined between such range 
     * of values.
     * @param p value to evaluate the inverse c.d.f. at. This value is 
     * equivalent to a probability and must be between 0.0 and 1.0.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return the value x for which the c.d.f. has value p.
     * @throws IllegalArgumentException if provided standard deviation is zero 
     * or negative, or if provided probability value is not between 0.0 and 1.0.
     */
    public static double invcdf(double p, double mu, double sig) {
        if (sig <= 0.0) {
            throw new IllegalArgumentException(
                    "standard deviation must be greater than zero");
        }

        return internalInvcdf(p, mu, sig);
    }

    /**
     * Evaluates the inverse cumulative distribution function of a Gaussian
     * distribution having the mean and standard deviation of this instance at
     * provided point p.
     * Because the c.d.f is a monotonically increasing function with values
     * between 0.0 and 1.0, its inverse is uniquely defined between such range 
     * of values.
     * @param p value to evaluate the inverse c.d.f. at. This value is
     * equivalent to a probability and must be between 0.0 and 1.0.
     * @return the value x for which the c.d.f. has value p.
     * @throws IllegalArgumentException if provided probability value is not 
     * between 0.0 and 1.0.
     */
    public double invcdf(double p) {
        return internalInvcdf(p, mMu, mSig);
    }
    
    /**
     * Computes the Mahalanobis distance of provided point x for provided
     * mean and standard deviation values.
     * @param x point where Mahalanobis distance is evaluated.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return Mahalanobis distance of provided point respect to mean.
     * @throws IllegalArgumentException if provided standard deviation is zero
     * or negative.
     */
    public static double mahalanobisDistance(double x, double mu, double sig) {
        if (sig <= 0.0) {
            throw new IllegalArgumentException(
                    "standard deviation must be greater than zero");
        }
        
        return internalMahalanobisDistance(x, mu, sig);
    }
    
    /**
     * Computes the Mahalanobis distance of provided point x for current mean
     * and standard deviation values.
     * @param x point where Mahalanobis distance is evaluated.
     * @return Mahalanobis distance of provided point respect to current mean.
     */
    public double mahalanobisDistance(double x) {
        return internalMahalanobisDistance(x, mMu, mSig);
    }

    /**
     * Evaluates the probability density function (p.d.f.) of a Gaussian 
     * distribution having mean mu and standard deviation sig at provided point 
     * x.
     * This method is used internally.
     * @param x point where p.d.f. is evaluated.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return evaluation of p.d.f.
     * @throws IllegalArgumentException if provided standard deviation is zero 
     * or negative.
     */
    private static double internalP(double x, double mu, double sig) {
        return (GAUSSIAN_NORM / sig) * Math.exp(-0.5 *
                Math.pow((x - mu) / sig, 2.0));
    }

    /**
     * Evaluates the cumulative distribution function (c.d.f.) of a Gaussian
     * distribution having mean mu and standard deviation sig at provided point 
     * x.
     * The c.d.f is equivalent to the probability of the Gaussian distribution
     * of having a value less than x, and it is computed as the integral from
     * -infinity to x of the Gaussian p.d.f.
     * This method is used internally.
     * @param x point where c.d.f. is evaluated.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return evaluation of c.d.f.
     * @throws IllegalArgumentException if provided standard deviation is zero
     * or negative.
     */
    @SuppressWarnings("all")
    protected static double internalCdf(double x, double mu, double sig) {
        return 0.5 * Erf.erfc(-HALF_SQRT2 * (x - mu) / sig);
    }

    /**
     * Evaluates the inverse cumulative distribution function of a Gaussian
     * distribution having mean mu and standard deviation sig at provided point
     * p.
     * Because the c.d.f is a monotonically increasing function with values
     * between 0.0 and 1.0, its inverse is uniquely defined between such range 
     * of values.
     * This method is used internally.
     * @param p value to evaluate the inverse c.d.f. at. This value is 
     * equivalent to a probability and must be between 0.0 and 1.0.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return the value x for which the c.d.f. has value p.
     * @throws IllegalArgumentException if provided probability value is not 
     * between 0.0 and 1.0.
     */
    @SuppressWarnings("all")
    protected static double internalInvcdf(double p, double mu, double sig)
            throws IllegalArgumentException {
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException(
                    "probability value must be between 0.0 and 1.0");
        }
        return -SQRT2 * sig * Erf.inverfc(2.0 * p) + mu;
    }
    
    /**
     * Computes the Mahalanobis distance of provided point x for provided
     * mean and standard deviation values.
     * @param x point where Mahalanobis distance is evaluated.
     * @param mu mean of Gaussian distribution.
     * @param sig standard deviation of Gaussian distribution.
     * @return Mahalanobis distance of provided point respect to mean.
     */
    private static double internalMahalanobisDistance(double x, double mu, 
            double sig) {
        return Math.abs(x - mu) / sig;
    }
            
    /**
     * Evaluates the derivative and a 1D function at a certain mean point and 
     * computes the non-linear propagation of Gaussian uncertainty through such 
     * function at such point.
     * @param evaluator interface to evaluate derivative of a function at a
     * certain point.
     * @param mean mean of original Gaussian distribution to be propagated.
     * @param standardDeviation standard deviation of original Gaussian 
     * distribution to be propagated.
     * @param result instance where propagated Gaussian distribution will be
     * stored.
     * @see <a href="https://github.com/joansola/slamtb">propagateUncertainty.m at https://github.com/joansola/slamtb</a>
     */
    public static void propagate(DerivativeEvaluator evaluator, double mean, 
            double standardDeviation, NormalDist result) {
        double evaluation = evaluator.evaluate(mean);
        double derivative = evaluator.evaluateDerivative(mean);
        result.setMean(evaluation);
        result.setStandardDeviation(Math.abs(derivative * standardDeviation));
    }
    
    /**
     * Evaluates the derivative and a 1D function at a certain mean point and 
     * computes the non-linear propagation of Gaussian uncertainty through such 
     * function at such point.
     * @param evaluator interface to evaluate derivative of a function at a
     * certain point.
     * @param mean mean of original Gaussian distribution to be propagated.
     * @param standardDeviation standard deviation of original Gaussian
     * distribution to be propagated.
     * @return a new propagated Gaussian distribution.
     * @see <a href="https://github.com/joansola/slamtb">propagateUncertainty.m at https://github.com/joansola/slamtb</a>
     */
    public static NormalDist propagate(DerivativeEvaluator evaluator,
            double mean, double standardDeviation) {
        NormalDist result = new NormalDist();
        propagate(evaluator, mean, standardDeviation, result);
        return result;
    }
    
    /**
     * Evaluates the derivative and a 1D function at a certain mean point and 
     * computes the non-linear propagation of Gaussian uncertainty through such 
     * function at such point.
     * @param evaluator interface to evaluate derivative of a function at a
     * certain point.
     * @param dist 1D Gaussian distribution to be propagated.
     * @param result instance where propagated Gaussian distribution will be
     * stored.
     * @see <a href="https://github.com/joansola/slamtb">propagateUncertainty.m at https://github.com/joansola/slamtb</a>
     */
    public static void propagate(DerivativeEvaluator evaluator, NormalDist dist, 
            NormalDist result) {
        propagate(evaluator, dist.getMean(), dist.getStandardDeviation(), 
                result);
    }
    
    /**
     * Evaluates the derivative and a 1D function at a certain mean point and 
     * computes the non-linear propagation of Gaussian uncertainty through such 
     * function at such point.
     * @param evaluator interface to evaluate derivative of a function at a
     * certain point.
     * @param dist 1D Gaussian distribution to be propagated.
     * @return a new propagated Gaussian distribution.
     * @see <a href="https://github.com/joansola/slamtb">propagateUncertainty.m at https://github.com/joansola/slamtb</a>
     */
    public static NormalDist propagate(DerivativeEvaluator evaluator, 
            NormalDist dist) {
        NormalDist result = new NormalDist();
        propagate(evaluator, dist, result);
        return result;
    }
    
    /**
     * Evaluates the derivative and a 1D function at the mean point of this 
     * normal distribution and computes the non-linear propagation of Gaussian
     * uncertainty through such function at such point.
     * @param evaluator interface to evaluate derivative of a function at the
     * mean point of this normal distribution.
     * @param result instance where propagated Gaussian distribution will be
     * stored.
     * @see <a href="https://github.com/joansola/slamtb">propagateUncertainty.m at https://github.com/joansola/slamtb</a>
     */
    public void propagateThisDistribution(DerivativeEvaluator evaluator, 
            NormalDist result) {
        propagate(evaluator, this, result);
    }
    
    /**
     * Evaluates the derivative and a 1D function at the mean point of this
     * normal distribution and computes the non-linear propagation of Gaussian
     * uncertainty through such function at such point.
     * @param evaluator interface to evaluate derivative of a function at the
     * mean point of this normal distribution.
     * @return a new propagated Gaussian distribution.
     * @see <a href="https://github.com/joansola/slamtb">propagateUncertainty.m at https://github.com/joansola/slamtb</a>
     */
    public NormalDist propagateThisDistribution(DerivativeEvaluator evaluator) {
        NormalDist result = new NormalDist();
        propagateThisDistribution(evaluator, result);
        return result;
    }
    
    /**
     * Interface to evaluate a one dimensional function at point x and to obtain
     * its derivative at such point.
     */
    public interface DerivativeEvaluator {
        
        /**
         * Evaluates function at point x.
         * @param x point x where derivative is evaluated.
         * @return evaluation of function at point x.
         */
        double evaluate(double x);
        
        /**
         * Evaluates derivative of a function at point x.
         * @param x point x where derivative is evaluated.
         * @return derivative of one dimenstional function at point x.
         */
        double evaluateDerivative(double x);
    }
}
