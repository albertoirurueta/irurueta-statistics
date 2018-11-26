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
 * Defines the gamma function, which is a function that extends the concept of
 * factorials from natural to real and complex numbers (except cero and negative
 * integer values).
 * If argument of gamma function is natural and positive, then it relates to
 factorials as: Gamma(n) = (n - 1)!
 */
public class Gamma extends GaussLegendreQuadrature {

    /**
     * Maximum number of logarithm of factorials cached.
     */
    protected static final int MAX_CACHED_LOG_FACTORIALS = 2000;

    /**
     * Defines when to switch to quadrature method.
     */
    private static final int ASWITCH = 100;

    /**
     * Epsilon for double. It is related to machine precision.
     */
    private static final double EPS = Math.ulp(1.0);
    
    /**
     * Constant related to machine precision.
     */
    private static final double FPMIN = Double.MIN_VALUE / EPS;
    
    /**
     * Maximum value supported to estimate factorials for.
     */
    private static final int MAX_FACTORIALS = 170;

    /**
     * Default number of maximum iterations to compute incomplete gamma functions.
     */
    private static final int DEFAULT_MAX_ITERATIONS = 100;

    /**
     * Coefficients for computation of logarithm of gamma function.
     */
    private static final double[] COF = {57.1562356658629235,-59.5979603554754912,
            14.1360979747417471,-0.491913816097620199,.339946499848118887e-4,
            .465236289270485756e-4,-.983744753048795646e-4,.158088703224912494e-3,
            -.210264441724104883e-3,.217439618115212643e-3,-.164318106536763890e-3,
            .844182239838527433e-4,-.261908384015814087e-4,.368991826595316234e-5};
    
    /**
     * Indicates whether factorials have been initialized and stored in a table 
     * for future faster access.
     */
    private static boolean factorialsInitialied;
    
    /**
     * Table where factorials are cached for faster future access.
     */
    private static double[] factorialsTable;
    
    /**
     * Indicates whether logarithm of factorials have been initialized and 
     * stored in a table for future faster access.
     */
    private static boolean logarithmOfFactorialsInitialized;
    
    /**
     * Table where logarithms of factorials are cached for faster future access.
     */
    private static double[] logarithmOfFactorialsTable;

    /**
     * Logarithm of gamma function.
     */
    private double gln;

    /**
     * Returns logarithm of gamma function.
     * @return logarithm of gamma function.
     */
    public double getGln() {
        return gln;
    }
    
    /**
     * Returns the value ln(gamma(xx)) for xx &gt; 0.
     * @param xx a value.
     * @return the logarithm of gamma function.
     * @throws IllegalArgumentException if value is negative.
     */
    public static double gammln(double xx) {
        if (xx <= 0.0) {
            throw new IllegalArgumentException("bad arg in gammln");
        }

        int j;
        double x;
        double tmp;
        double y;
        double ser;

        y = x = xx;
        tmp = x + 5.24218750000000000;
        tmp = (x + 0.5) * Math.log(tmp) - tmp;
        ser = 0.999999999999997092;
        for (j = 0; j < 14; j++) {
            ser += COF[j] / ++y;
        }
        return tmp + Math.log(2.5066282746310005 * ser / x);
    }
    
    /**
     * Returns the factorial of n (n!) as a floating-point number.
     * Factorials up to 22! have exact double precision representations.
     * Factorials from 23! to 170! are approximate due to IEEE double 
     * representation.
     * Factorials equal or greater than 170! are out of range.
     * @param n value to compute factorial for.
     * @return factorial of n, (i.e. n!).
     * @throws IllegalArgumentException if provided value generates a factorial
     * that cannot be represented using double precision.
     */
    public static double factrl(int n) {
        if (n < 0 || n > MAX_FACTORIALS) {
            throw new IllegalArgumentException("factrl out of range");
        }
        
        if (!factorialsInitialied) {
            factorialsInitialied = true;
            factorialsTable = new double[171];
            factorialsTable[0] = 1.;
            for (int i = 1; i < (MAX_FACTORIALS + 1); i++) {
                factorialsTable[i] = i * factorialsTable[i - 1];
            }
        }
        
        return factorialsTable[n];            
    }
    
    /**
     * Returns logarithm of n!
     * @param n value to compute logarithm of factorial for.
     * @return logarithm of factorial.
     * @throws IllegalArgumentException if provided value is negative.
     */
    public static double factln(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("negative arg in factln");
        }
        
        if (!logarithmOfFactorialsInitialized) {
            logarithmOfFactorialsInitialized = true;
            logarithmOfFactorialsTable = new double[MAX_CACHED_LOG_FACTORIALS];
            for (int i = 0; i < MAX_CACHED_LOG_FACTORIALS; i++) {
                logarithmOfFactorialsTable[i] = gammln(i + 1.);
            }
        }
        if (n < MAX_CACHED_LOG_FACTORIALS) {
            return logarithmOfFactorialsTable[n];
        }
        return gammln(n + 1.);
    }
    
    /**
     * Returns the binomial coefficient (n k) as a floating-point number, which
     * indicates the discrete probability distribution of getting exactly k 
     * successes in n trials.
     * @param n number of trials.
     * @param k number of successes.
     * @return binomial value.
     * @throws IllegalArgumentException if either n or k are negative or if k is greater than n.
     */
    public static double bico(int n, int k) {
        if (k < 0 || k > n) {
            throw new IllegalArgumentException("bad args in bico");
        }
        
        if (n < MAX_FACTORIALS + 1) {
            return Math.floor(0.5 + factrl(n) / (factrl(k) * factrl(n - k)));
        }
        
        //The floor function cleans up roundoff error for smaller values of n 
        //and k.
        return Math.floor(0.5 + Math.exp(factln(n) - factln(k) - factln(n - k)));
    }
    
    /**
     * Returns the value of the beta function B(z, w) (aka Euler integral).
     * @param z a parameter.
     * @param w a parameter.
     * @return value of beta function.
     * @throws IllegalArgumentException if either z or w are negative.
     */
    public static double beta(double z, double w) {
        return Math.exp(gammln(z) + gammln(w) - gammln(z + w));
    }    

    /**
     * Returns the incomplete gamma function P(a,x).
     * @param a a parameter.
     * @param x x parameter.
     * @return value of incomplete gamma function.
     * @throws IllegalArgumentException if provided values are invalid.
     * @throws MaxIterationsExceededException if convergence cannot be reached.
     */
    public double gammp(double a, double x) 
            throws MaxIterationsExceededException {
        if (x < 0.0 || a <= 0.0) {
            throw new IllegalArgumentException("bad args in gammp");
        }

        if (x == 0.0) {
            return 0.0;
        } else if ((int)a >= ASWITCH) {
            return gammpapprox(a,x,1);
        } else if (x < a + 1.0) {
            return gser(a,x);
        } else {
            return 1.0 - gcf(a,x);
        }
    }

    /**
     * Returns the incomplete gamma function Q(a, x) = 1 - P(a, x).
     * @param a a parameter.
     * @param x x parameter.
     * @return value of incoplete gamma function.
     * @throws IllegalArgumentException if provided values are invalid.
     * @throws MaxIterationsExceededException if convergence cannot be reached.
     */
    public double gammq(double a, double x) 
            throws MaxIterationsExceededException {
        if (x < 0.0 || a <= 0.0) {
            throw new IllegalArgumentException("bad args in gammq");
        }

        if (x == 0.0) {
            return 1.0;
        } else if ((int)a >= ASWITCH) {
            return gammpapprox(a, x,0);
        } else if (x < a + 1.0) {
            return 1.0 - gser(a, x);
        } else {
            return gcf(a, x);
        }
    }

    /**
     * Returns the incomplete gamma function P(a, x) evaluated by its series
     * representation.
     * @param a a parameter to obtain its gamma logarithm.
     * @param x x parameter.
     * @return incomplete gamma function.
     * @throws MaxIterationsExceededException if maximum number of iterations is
     * exceeded.
     */
    private double gser(double a, double x) 
            throws MaxIterationsExceededException {
        return gser(a, x, DEFAULT_MAX_ITERATIONS);
    }
    
    /**
     * Returns the incomplete gamma function P(a, x) evaluated by its series
     * representation.
     * @param a a parameter to obtain its gamma logarithm.
     * @param x x parameter.
     * @param maxIterations maximum number of iterations.
     * @return incomplete gamma function.
     * @throws MaxIterationsExceededException if maximum number of iterations is
     * exceeded.
     */
    @SuppressWarnings("all")
    private double gser(double a, double x, int maxIterations) 
            throws MaxIterationsExceededException {
        double sum,del,ap;

        gln = gammln(a);
        ap = a;
        del = sum = 1.0 / a;
        for (;;) {
            ++ap;
            del *= x / ap;
            sum += del;
            if (Math.abs(del) < Math.abs(sum) * EPS) {
                return sum * Math.exp(-x + a * Math.log(x) - gln);
            }
            
            if (ap >= maxIterations) {
                throw new MaxIterationsExceededException();
            }
        }
    }
    
    /**
     * Returns the incomplete gamma function Q(a, x) evaluated by its continued
     * fraction representation.
     * @param a a parameter to obtain its gamma logarithm.
     * @param x x parameter.
     * @return incomplete gamma function.
     * @throws MaxIterationsExceededException if maximum number of iterations is
     * exceeded.
     */
    private double gcf(double a, double x) 
            throws MaxIterationsExceededException {
        return gcf(a, x, DEFAULT_MAX_ITERATIONS);
    }    

    /**
     * Returns the incomplete gamma function Q(a, x) evaluated by its continued
     * fraction representation.
     * @param a a parameter to obtain its gamma logarithm.
     * @param x x parameter.
     * @param maxIterations maximum number of iterations.
     * @return incomplete gamma function.
     * @throws MaxIterationsExceededException if maximum number of iterations is
     * exceeded.
     */
    @SuppressWarnings("all")
    private double gcf(double a, double x, int maxIterations) 
            throws MaxIterationsExceededException {
        int i;
        double an;
        double b;
        double c;
        double d;
        double del;
        double h;
        gln = gammln(a);
        b = x + 1.0 - a;
        c = 1.0 / FPMIN;
        d = 1.0 / b;
        h = d;
        for (i = 1;; i++) {
            an = -i * (i - a);
            b += 2.0;
            d = an * d + b;
            if (Math.abs(d) < FPMIN) {
                d = FPMIN;
            }
            c = b + an / c;
            if (Math.abs(c) < FPMIN) {
                c = FPMIN;
            }
            d = 1.0 / d;
            del = d * c;
            h *= del;
            if (Math.abs(del - 1.0) <= EPS) {
                break;
            }
            if (i >= maxIterations) {
                throw new MaxIterationsExceededException();
            }
        }
        return Math.exp(-x + a * Math.log(x) - gln) * h;
    }

    /**
     * Incomplete gamma by quadrature. Returns P(a, x) or Q(a, x), when psig is
     * 1 or 0 respectively.
     * @param a a parameter.
     * @param x x parameter.
     * @param psig a flag.
     * @return incomplete gamma by quadrature.
     */
    private double gammpapprox(double a, double x, int psig) {
        int j;
        double xu;
        double t;
        double sum;
        double ans;
        double a1 = a - 1.0;
        double lna1 = Math.log(a1);
        double sqrta1 = Math.sqrt(a1);
        gln = gammln(a);
        if (x > a1) {
            xu = Math.max(a1 + 11.5 * sqrta1, x + 6.0 * sqrta1);
        } else {
            xu = Math.max(0., Math.min(a1 - 7.5 * sqrta1, x - 5.0 * sqrta1));
        }
        sum = 0;
        for (j = 0; j < N_GAU; j++) {
            t = x + (xu - x) * Y[j];
            sum += W[j] * Math.exp(-(t - a1) + a1 * (Math.log(t) - lna1));
        }
        ans = sum * (xu - x) * Math.exp(a1 * (lna1 - 1.) - gln);

        if (psig != 0 ) {
            return ans > 0.0 ? 1.0 - ans : -ans;
        } else {
            return ans >= 0.0 ? ans : 1.0 + ans;
        }
    }

    /**
     * Inverse function on x of P(a, x).
     * Returns x such that P(a,x) = p for an argument p between 0 and 1.
     * @param p argument p.
     * @param a a parameter.
     * @return inverse value.
     * @throws IllegalArgumentException if arguments are invalid.
     * @throws MaxIterationsExceededException if maximum number of iterations is
     * exceeded.
     */
    @SuppressWarnings("Duplicates")
    public double invgammp(double p, double a) throws MaxIterationsExceededException {
        int j;
        double x;
        double err;
        double t;
        double u;
        double pp;
        double lna1 = 0.0;
        double afac = 0.0;
        double a1 = a - 1.0;
        gln = gammln(a);
        if (a <= 0.) {
            throw new IllegalArgumentException("a must be pos in invgammap");
        }
        if (p >= 1.) {
            return Math.max(100.,a + 100. * Math.sqrt(a));
        }
        if (p <= 0.) {
            return 0.0;
        }
        if (a > 1.) {
            lna1 = Math.log(a1);
            afac = Math.exp(a1 * (lna1 - 1.) - gln);
            pp = p < 0.5 ? p : 1. - p;
            t = Math.sqrt(-2. * Math.log(pp));
            x = (2.30753 + t * 0.27061) / (1. + t * (0.99229 + t * 0.04481)) - t;
            if (p < 0.5) {
                x = -x;
            }
            x = Math.max(1.e-3, a * Math.pow(1. - 1. / (9. * a) - x / (3. * Math.sqrt(a)), 3));
        } else {
            t = 1.0 - a * (0.253 + a * 0.12);
            if (p < t) {
                x = Math.pow(p / t, 1. / a);
            } else {
                x = 1. - Math.log(1. - (p - t) / (1. - t));
            }
        }
        for (j = 0; j < 12; j++) {
            if (x <= 0.0) {
                return 0.0;
            }
            err = gammp(a,x) - p;
            if (a > 1.) {
                t = afac * Math.exp(-(x - a1) + a1 * (Math.log(x) - lna1));
            } else {
                t = Math.exp(-x + a1 * Math.log(x) - gln);
            }
            u = err / t;
            x -= (t = u / (1. - 0.5 * Math.min(1., u * ((a - 1.) / x - 1))));
            if (x <= 0.) {
                x = 0.5 * (x + t);
            }
            if (Math.abs(t) < EPS * x) {
                break;
            }
        }
        return x;
    }
}
