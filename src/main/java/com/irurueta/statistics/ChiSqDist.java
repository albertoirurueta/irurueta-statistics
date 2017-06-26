/**
 * @file
 * This file contains implementation of
 * com.irurueta.statistics.ChiSqDist
 * 
 * @author Alberto Irurueta (alberto@irurueta.com)
 * @date December 28, 2015
 */
package com.irurueta.statistics;

/**
 * Contains methods to work with Chi squared distributions.
 * Methods of this class use methods of the Gamma class.
 * This class is based in code of Numerical Recipes 3rd ed. section 6.14.8.
 */
public class ChiSqDist {
    
    /**
     * Nu parameter of chi square distribution.
     * Typically this parameter is provided as an integer value indicating the
     * number of degrees of freedom.
     */
    private double mNu;
    
    /**
     * Constant factor to multiply p.d.f of chi squared by. This factor depends
     * on nu parameter.
     */
    private double mFac;
    
    /**
     * A gamma function instance to be reused for memory efficiency.
     */
    private Gamma mGamma;
    
    /**
     * Constructor.
     * @param nu parameter of chi square distribution.
     * @throws IllegalArgumentException if provided nu parameter is negative or 
     * zero.
     */
    public ChiSqDist(double nu) throws IllegalArgumentException {
        mGamma = new Gamma();
        setNu(nu);
    }
    
    /**
     * Returns nu parameter of chi square distribution.
     * Typically this parameter is an integer value indicating the number of 
     * degrees of freedom.
     * @return nu parameter of chi square distribution.
     */
    public double getNu(){
        return mNu;
    }
    
    /**
     * Sets nu parameter of chi square distribution.
     * Typically this parameter is an integer value indicating the number of 
     * degrees of freedom.
     * @param nu nu parameter of chi square distribution.
     * @throws IllegalArgumentException if provided nu parameter is negative or
     * zero.
     */
    public final void setNu(double nu) throws IllegalArgumentException{
	if (nu <= 0.0){
            throw new IllegalArgumentException("nu must be greater than 0.0");
        }
        
        mNu = nu;
	mFac = fac(nu);        
    }
    
    /**
     * Evaluates the probability density function (p.d.f.) of a Chi square 
     * distribution.
     * @param x2 chi square value where p.d.f. is evaluated. Must be greater
     * than 0.0.
     * @param nu nu parameter of chi square distribution. Typically this is an
     * integer value indicating the number of degrees of freedom. Must be 
     * greater than 0.0.
     * @return evaluation of p.d.f.
     * @throws IllegalArgumentException if either x2 or nu are negative or zero.
     */
    public static double p(double x2, double nu) 
            throws IllegalArgumentException{
        if(nu <= 0.0){
            throw new IllegalArgumentException("nu must be greater than 0.0");
        }
        
        return internalP(x2, nu, fac(nu));
    }
    
    /**
     * Evaluates the probability density function (p.d.f.) of this Chi square
     * distribution.
     * @param x2 chi square value where p.d.f. is evaluated. Must be greater 
     * than 0.0.
     * @return evaluation of p.d.f.
     * @throws IllegalArgumentException if x2 is negative or zero.
     */
    public double p(double x2) throws IllegalArgumentException{
        return internalP(x2, mNu, mFac);
    }
    
    /**
     * Evaluates the cumulative distribution function (c.d.f.) of a Chi-squared
     * distribution having parameter nu.
     * @param x2 chi square value where c.d.f. is evaluated. Must be positive or
     * zero.
     * @param nu nu parameter of chi square distribution. Typically this is an
     * integer value indicating the number of degrees of freedom. Must be 
     * greater than 0.0.
     * @return evaluation of c.d.f.
     * @throws IllegalArgumentException if provided chi square value is negative
     * or if provided nu parameter is negative or zero.
     * @throws MaxIterationsExceededException if convergence of incomplete
     * gamma function cannot be reached. This is rarely thrown and happens 
     * usually for numerically unstable input values.
     */
    public static double cdf(double x2, double nu)
            throws IllegalArgumentException, MaxIterationsExceededException{
	if (nu <= 0.0){
            throw new IllegalArgumentException("nu must be greater than 0.0");
        }
        
        return internalCdf(x2, nu, new Gamma());
    }
    
    /**
     * Evaluates the cumulative distribution function (c.d.f.) of this Chi-square
     * distribution.
     * @param x2 chi square value where c.d.f. is evaluated. Must be positive or
     * zero.
     * @return evaluation of c.d.f.
     * @throws IllegalArgumentException if provided chi square value is 
     * negative.
     * @throws MaxIterationsExceededException if convergence of incomplete gamma
     * funciton cannot be reached. This is rarely thrown and happens usually for
     * numerically unstable input values.
     */
    public double cdf(double x2) throws IllegalArgumentException, 
            MaxIterationsExceededException{
        return internalCdf(x2, mNu, mGamma);
    }
    
    /**
     * Evaluates the inverse cumulative distribution function of a Chi squared
     * distribution having parameter nu.
     * Because the c.d.f is a monotonically increasing function with values
     * between 0.0 and 1.0, its inverse is uniquely defined between such range 
     * of values.
     * @param p value to evaluate the inverse c.d.f. at. This value is
     * equivalent to a probability and must be between 0.0 and 1.0.
     * @param nu nu parameter of chi square distribution. Typically this is an
     * integer value indicating the number of degrees of freedom. Must be 
     * greater than 0.0.
     * @return the value x2 for which the c.d.f. has value p.
     * @throws IllegalArgumentException if provided probability value is not 
     * between 0.0 and 1.0 of if provided nu parameter is negative or zero.
     * @throws MaxIterationsExceededException if convergence of inverse 
     * incomplete gamma function cannot be reached. This is rarely thrown and
     * happens usually for numerically unstable values.
     */
    public static double invcdf(double p, double nu) 
            throws IllegalArgumentException, MaxIterationsExceededException{
	if (nu <= 0.0){
            throw new IllegalArgumentException("nu must be greater than 0.0");
        }
        
        return internalInvcdf(p, nu, new Gamma());
    }
    
    /**
     * Evaluates the inverse cumulative distribution function of this Chi squared
     * distribution.
     * Because the c.d.f is a monotonically increasing function with values
     * between 0.0 and 1.0, its inverse is uniquely defined between such range 
     * of values.
     * @param p value to evaluate the inverse c.d.f. at. This value is
     * equivalent to a probability and must be between 0.0 and 1.0.
     * @return the value x2 for which the c.d.f. has value p.
     * @throws IllegalArgumentException if provided probability value is not
     * between 0.0 and 1.0.
     * @throws MaxIterationsExceededException if convergence of inverse 
     * incomplete gamma function cannot be reached. This is rarely thrown and
     * happens usually for numerically unstable values.
     */
    public double invcdf(double p) throws IllegalArgumentException, 
            MaxIterationsExceededException{
        return internalInvcdf(p, mNu, mGamma);
    }
        
    /**
     * Evaluates the probability density function (p.d.f.) of a Chi square 
     * distribution.
     * This method is used internally.
     * @param x2 chi square value where p.d.f. is evaluated. Must be greater
     * than 0.0.
     * @param nu nu parameter of chi square distribution. Typically this is an
     * integer value indicating the number of degrees of freedom. Must be 
     * greater than 0.0.
     * @return evaluation of p.d.f.
     * @throws IllegalArgumentException if x2 is negative or zero.
     */
    private static double internalP(double x2, double nu, double fac) 
            throws IllegalArgumentException{
	if (x2 <= 0.0){
            throw new IllegalArgumentException(
                    "chi square must be greater than zero");
        }
	
        return Math.exp(-0.5 * (x2 - (nu - 2.0) * Math.log(x2)) - fac);        
    }
    
    /**
     * Evaluates the cumulative distribution function (c.d.f.) of a Chi-squared
     * distribution having parameter nu.
     * This method is used internally.
     * @param x2 chi square value where c.d.f. is evaluated. Must be positive 
     * or zero.
     * @param nu nu parameter of chi square distribution. Typically this is an
     * integer value indicating the number of degrees of freedom. Must be 
     * greater than 0.0.
     * @param gamma a gamma instance to evaluate the incomplete gamma function.
     * @return evaluation of c.d.f.
     * @throws IllegalArgumentException if provided chi square value is 
     * negative.
     * @throws MaxIterationsExceededException if convergence of incomplete gamma
     * function cannot be reached. This is rarely thrown and happens usually for
     * numerically unstable values.
     */
    private static double internalCdf(double x2, double nu, Gamma gamma) 
            throws IllegalArgumentException, MaxIterationsExceededException{
	if (x2 < 0.0) throw new IllegalArgumentException(
                "chi square must be positive or zero");
	
        return gamma.gammp(0.5 * nu, 0.5 * x2);        
    }
    
    /**
     * Evaluates the inverse cumulative distribution function of a Chi squared
     * distribution having parameter nu.
     * Because the c.d.f is a monotonically increasing function with values
     * between 0.0 and 1.0, its inverse is uniquely defined between such range 
     * of values.
     * This method is used internally.
     * @param p value to evaluate the inverse c.d.f. at. This value is
     * equivalent to a probability and must be between 0.0 and 1.0.
     * @param nu nu parameter of chi square distribution. Typically this is an
     * integer value indicating the number of degrees of freedom. Must be 
     * greater than 0.0.
     * @param gamma a gamma instance to evaluate the inverse incomplete gamma 
     * function.
     * @return the value x2 for which the c.d.f. has value p.
     * @throws IllegalArgumentException if provided probability value is not 
     * between 0.0 and 1.0.
     * @throws MaxIterationsExceededException if convergence of inverse 
     * incomplete gamma function cannot be reached. This is rarely thrown and
     * happens usually for numerically unstable values.
     */
    private static double internalInvcdf(double p, double nu, Gamma gamma)
            throws IllegalArgumentException, MaxIterationsExceededException{
	
        if (p < 0.0 || p >= 1.0){
            throw new IllegalArgumentException(
                "probability value must be between 0.0 and 1.0");
        }
        
	return 2.0 * gamma.invgammp(p, 0.5 * nu);        
    }
    
    /**
     * Computes constant factor to multiply p.d.f. of chi squared by.
     * @param nu nu parameter of chi square distribution. Typically this 
     * parameter is provided as an integer value indicating the number of 
     * degrees of freedom.
     * @return constant factor to multiply p.d.f. of chi squared.
     */
    private static double fac(double nu){
        return 0.693147180559945309*(0.5*nu)+Gamma.gammln(0.5*nu);
    }
}
