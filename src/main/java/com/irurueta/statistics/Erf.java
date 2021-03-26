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
 * Defines the error function and methods related to it.
 * The error function (or Gaussian function) is a special function typically
 * used in statistics and probability or to solve partial differential
 * equations.
 * The error function is defined as the integral between a interval of an
 * exponential with a negative squared exponent (like gaussian functions).
 * Such integral can be used for purposes like obtaining the probability of
 * gaussian distributions, because such probability is the integral of a pdf
 * having a similar expression as the error function.
 * The error function (a.k.a erf) has the following properties:
 * - is an odd function erf(-x) = -erf(x)
 * - erf(0) = 0
 * - erf(infinity) = 1
 * <p>
 * Typically also the complementary error function (a.k.a erfc) is also used,
 * and it is defined as erfc(x) = 1 - erf(x)
 * The complementary error function has the following properties:
 * - erfc(0) = 1
 * - erfc(infinity) = 0
 * - erfc(-x) = 2 - erfc(x).
 * <p>
 * Both functions (erf and erfc) are special cases of the incomplete gamma
 * function.
 * <p>
 * This class is based in code of Numerical Recipes 3rd ed. section 6.2.2.
 */
public class Erf {
    /**
     * Number of coefficients to approximate the error function using a
     * Chebychev approximation.
     */
    private static final int N_COF = 28;

    /**
     * Coefficients of Chebychev polynomial to approximate the error function.
     */
    private static final double[] COF = {-1.3026537197817094,
            6.4196979235649026e-1, 1.9476473204185836e-2, -9.561514786808631e-3,
            -9.46595344482036e-4, 3.66839497852761e-4, 4.2523324806907e-5,
            -2.0278578112534e-5, -1.624290004647e-6, 1.303655835580e-6,
            1.5626441722e-8, -8.5238095915e-8, 6.529054439e-9, 5.059343495e-9,
            -9.91364156e-10, -2.27365122e-10, 9.6467911e-11, 2.394038e-12,
            -6.886027e-12, 8.94487e-13, 3.13092e-13, -1.12708e-13, 3.81e-16,
            7.106e-15, -1.523e-15, -9.4e-17, 1.21e-16, -2.8e-17};

    /**
     * Empty constructor.
     */
    private Erf() {
    }

    /**
     * Evaluates the error function at x.
     *
     * @param x value to evualte the error function at.
     * @return value of the error function.
     */
    public static double erf(final double x) {
        if (x >= 0.0) {
            return 1.0 - erfccheb(x);
        } else {
            return erfccheb(-x) - 1.0;
        }
    }

    /**
     * Evaluates the complementary error function at x.
     *
     * @param x value to evaluate the complementary error function at.
     * @return value of the complementary error function.
     */
    public static double erfc(final double x) {
        if (x >= 0.0) {
            return erfccheb(x);
        } else {
            return 2.0 - erfccheb(-x);
        }
    }

    /**
     * Evaluates the inverse of the complementary error function at p.
     * Then:
     * p = erfc(x) &lt;--&gt; x = inverfc(p)
     *
     * @param p value to evaluate the inverse erfc function at.
     * @return result of the evaluation.
     */
    public static double inverfc(final double p) {
        double x;
        double err;
        final double t;
        final double pp;
        if (p >= 2.0) {
            return -100.0;
        }
        if (p <= 0.0) {
            return 100.0;
        }
        pp = p < 1.0 ? p : 2.0 - p;
        t = Math.sqrt(-2.0 * Math.log(pp / 2.0));
        x = -0.70711 * ((2.30753 + t * 0.27061) / (1.0 + t * (0.99229 + t * 0.04481)) - t);
        for (int j = 0; j < 2; j++) {
            err = erfc(x) - pp;
            x += err / (1.12837916709551257 * Math.exp(-x * x) - x * err);
        }
        return (p < 1.0 ? x : -x);
    }

    /**
     * Evaluates the inverse of the error function at p.
     * Then:
     * p = erf(x) &lt;--&gt; x = inverf(p)
     *
     * @param p value to evaluate the inverse erf function at.
     * @return result of the evaluation.
     */
    public static double inverf(final double p) {
        return inverfc(1.0 - p);
    }

    /**
     * Computes the complementary error function by using the Chebychev method
     * approximation.
     *
     * @param z value to evaluate the function at.
     * @return evaluation of the erfc at provided value.
     * @throws IllegalArgumentException if provided value is negative.
     */
    private static double erfccheb(final double z) {
        int j;
        final double t;
        final double ty;
        double tmp;
        double d = 0.0;
        double dd = 0.0;
        if (z < 0.0) {
            throw new IllegalArgumentException(
                    "erfccheb requires nonnegative argument");
        }

        t = 2.0 / (2.0 + z);
        ty = 4.0 * t - 2.;
        for (j = N_COF - 1; j > 0; j--) {
            tmp = d;
            d = ty * d - dd + COF[j];
            dd = tmp;
        }
        return t * Math.exp(-z * z + 0.5 * (COF[0] + ty * d) - dd);
    }
}
