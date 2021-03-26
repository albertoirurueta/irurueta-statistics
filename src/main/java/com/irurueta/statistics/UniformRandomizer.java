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
 * Generates pseudo-random values following a uniform distribution.
 */
public class UniformRandomizer extends Randomizer {

    /**
     * Constructor.
     * Uses default {@link Random} implementation.
     */
    public UniformRandomizer() {
        super();
    }

    /**
     * Constructor.
     *
     * @param internalRandom Internal Random instance in charge of generating
     *                       pseudo-random values.
     * @throws NullPointerException thrown if provided internal random is null.
     */
    public UniformRandomizer(final Random internalRandom) {
        super(internalRandom);
    }

    /**
     * Returns next boolean value following a uniform distribution (e.g. the
     * probability of returning either true or false is 50%).
     *
     * @return Next boolean value following a uniform distribution.
     */
    @Override
    public boolean nextBoolean() {
        return getInternalRandom().nextBoolean();
    }

    /**
     * Returns next random integer value within the range of integer values and
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @return Next integer value following a uniform distribution.
     */
    @Override
    public int nextInt() {
        return getInternalRandom().nextInt();
    }

    /**
     * Returns next random integer value within 0 and provided value following
     * a uniform distribution (e.g. each possible value has approximately the
     * same probability to be picked).
     *
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next integer value following a uniform distribution.
     * @throws IllegalArgumentException if provided values is negative.
     */
    public int nextInt(final int maxValue) {
        return getInternalRandom().nextInt(maxValue);
    }

    /**
     * Fills provided array with uniform integer values within 0 and provided
     * maxValue following a uniform distribution.
     *
     * @param array    Array to be filled.
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if provided value is negative.
     */
    public void fill(final int[] array, final int maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextInt(maxValue);
        }
    }

    /**
     * Returns array of random uniform integer values within 0 and provided
     * maxValue.
     *
     * @param length   Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random uniform integers.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public int[] nextInts(final int length, final int maxValue) {
        if (length <= 0) {
            throw new IllegalArgumentException();
        }
        final int[] array = new int[length];
        fill(array, maxValue);
        return array;
    }

    /**
     * Returns next random integer value between the range of provided values
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next integer value following a uniform distribution.
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     *                                  or equal than minValue.
     */
    public int nextInt(final int minValue, final int maxValue) {

        if (maxValue <= minValue) {
            throw new IllegalArgumentException();
        }

        final int diff = maxValue - minValue;
        return getInternalRandom().nextInt(diff) + minValue;
    }

    /**
     * Fills provided array with uniform integer values within provided
     * minValue (inclusive) and maxValue (exclusive) following a uniform
     * distribution.
     *
     * @param array    Array to be filled.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     *                                  minValue.
     */
    public void fill(final int[] array, final int minValue, final int maxValue) {
        if (maxValue <= minValue) {
            throw new IllegalArgumentException();
        }
        final int diff = maxValue - minValue;

        for (int i = 0; i < array.length; i++) {
            array[i] = getInternalRandom().nextInt(diff) + minValue;
        }
    }

    /**
     * Returns array of random uniform integer values within provided minValue
     * and maxValue.
     *
     * @param length   Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random uniform integers.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public int[] nextInts(final int length, final int minValue, final int maxValue) {
        final int[] array = new int[length];
        fill(array, minValue, maxValue);
        return array;
    }

    /**
     * Returns next random long value within the range of long values and
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @return Next long value following a uniform distribution.
     */
    @Override
    public long nextLong() {
        return getInternalRandom().nextLong();
    }

    /**
     * Returns next random long value within 0 and provided value following
     * a uniform distribution (e.g. each possible value has approximately the
     * same probability to be picked).
     *
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next long value following a uniform distribution.
     * @throws IllegalArgumentException if provided value is negative.
     */
    public long nextLong(final long maxValue) {
        return nextLong(0, maxValue);
    }

    /**
     * Fills provided array with uniform long values within 0 and provided
     * maxValue following a uniform distribution.
     *
     * @param array    Array to be filled.
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if provided value is negative.
     */
    public void fill(final long[] array, final long maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextLong(maxValue);
        }
    }

    /**
     * Returns array of random uniform long values within 0 and provided
     * maxValue.
     *
     * @param length   Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random uniform long integers.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public long[] nextLongs(final int length, final long maxValue) {
        final long[] array = new long[length];
        fill(array, maxValue);
        return array;
    }

    /**
     * Returns next random long value between the range of provided values
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next long value following a uniform distribution.
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     *                                  or equal than minValue.
     */
    public long nextLong(final long minValue, final long maxValue) {

        if (maxValue <= minValue) {
            throw new IllegalArgumentException();
        }

        final long diff = maxValue - minValue;
        return (Math.abs(getInternalRandom().nextLong()) % diff) + minValue;
    }

    /**
     * Fills provided array with uniform long values within provided
     * minValue (inclusive) and maxValue (exclusive) following a uniform
     * distribution.
     *
     * @param array    Array to be filled.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     *                                  minValue.
     */
    public void fill(final long[] array, final long minValue, final long maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextLong(minValue, maxValue);
        }
    }

    /**
     * Returns array of random uniform long values within provided minValue
     * and maxValue.
     *
     * @param length   Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random uniform integers.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public long[] nextLongs(final int length, final long minValue, final long maxValue) {
        final long[] array = new long[length];
        fill(array, minValue, maxValue);
        return array;
    }

    /**
     * Returns next random floating-point value within the range 0.0 and 1.0
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @return Next float value following a uniform distribution between 0.0 and
     * 1.0.
     */
    @Override
    public float nextFloat() {
        return getInternalRandom().nextFloat();
    }

    /**
     * Returns next random floating-point value between 0.0 and provided value
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next floating-point value following a uniform distribution.
     * @throws IllegalArgumentException if provided value is negative.
     */
    public float nextFloat(final float maxValue) {
        return nextFloat(0.0f, maxValue);
    }

    /**
     * Fills provided array with floating point values within 0.0 and provided
     * maxValue following a uniform distribution.
     *
     * @param array    Array to be filled.
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if provided value is negative.
     */
    public void fill(final float[] array, final float maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextFloat(maxValue);
        }
    }

    /**
     * Returns array of random uniform floating point values within 0.0 and
     * provided maxValue.
     *
     * @param length   Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random uniform floating point.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public float[] nextFloats(final int length, final float maxValue) {
        final float[] array = new float[length];
        fill(array, maxValue);
        return array;
    }

    /**
     * Returns next random floating-point value between the range of provided
     * values following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next floating-point value following a uniform distribution.
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     *                                  or equal than minValue.
     */
    public float nextFloat(final float minValue, final float maxValue) {

        if (maxValue <= minValue) {
            throw new IllegalArgumentException();
        }

        final float diff = maxValue - minValue;
        return getInternalRandom().nextFloat() * diff + minValue;
    }

    /**
     * Fills provided array with uniform floating point values within provided
     * minValue (inclusive) and maxValue (exclusive) following a uniform
     * distribution.
     *
     * @param array    Array to be filled.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     *                                  minValue.
     */
    public void fill(final float[] array, final float minValue, final float maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextFloat(minValue, maxValue);
        }
    }

    /**
     * Returns array of random uniform floating point values within provided
     * minValue and maxValue.
     *
     * @param length   Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random floating point values.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public float[] nextFloats(final int length, final float minValue, final float maxValue) {
        final float[] array = new float[length];
        fill(array, minValue, maxValue);
        return array;
    }

    /**
     * Returns next random double precision floating-point value within the
     * range 0.0 and 1.0 following a uniform distribution (e.g. each possible
     * value has approximately the same probability to be picked).
     *
     * @return Next float value following a uniform distribution between 0.0 and
     * 1.0.
     */
    @Override
    public double nextDouble() {
        return getInternalRandom().nextDouble();
    }

    /**
     * Returns next random floating-point value between 0.0 and provided value
     * following a uniform distribution (e.g. each possible value has
     * approximately the same probability to be picked).
     *
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next floating-point value following a uniform distribution.
     * @throws IllegalArgumentException if provided value is negative.
     */
    public double nextDouble(final double maxValue) {
        return nextDouble(0.0, maxValue);
    }

    /**
     * Fills provided array with double precision floating point values within
     * 0.0 and provided maxValue following a uniform distribution.
     *
     * @param array    Array to be filled.
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if provided value is negative.
     */
    public void fill(final double[] array, final double maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextDouble(maxValue);
        }
    }

    /**
     * Returns array of random uniform double precision floating point values
     * within 0.0 and provided maxValue.
     *
     * @param length   Length of array to be returned.
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random uniform double precision floating point values.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public double[] nextDoubles(final int length, final double maxValue) {
        final double[] array = new double[length];
        fill(array, maxValue);
        return array;
    }

    /**
     * Returns next random double precision floating-point value between the
     * range of provided values following a uniform distribution (e.g. each
     * possible value has approximately the same probability to be picked).
     *
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Next floating-point value following a uniform distribution.
     * @throws IllegalArgumentException Exception thrown if maxValue is smaller
     *                                  or equal than minValue.
     */
    public double nextDouble(final double minValue, final double maxValue) {

        if (maxValue <= minValue) {
            throw new IllegalArgumentException();
        }

        final double diff = maxValue - minValue;
        return getInternalRandom().nextDouble() * diff + minValue;
    }

    /**
     * Fills provided array with uniform double precision floating point values
     * within provided minValue (inclusive) and maxValue (exclusive) following
     * a uniform distribution.
     *
     * @param array    Array to be filled.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @throws IllegalArgumentException if maxValue is smaller or equal than
     *                                  minValue.
     */
    public void fill(final double[] array, final double minValue, final double maxValue) {
        for (int i = 0; i < array.length; i++) {
            array[i] = nextDouble(minValue, maxValue);
        }
    }

    /**
     * Returns array of random uniform floating point values within provided
     * minValue and maxValue.
     *
     * @param length   Length of array to be returned.
     * @param minValue Minimum value to be returned (inclusive).
     * @param maxValue Maximum value to be returned (exclusive).
     * @return Array of random floating point values.
     * @throws IllegalArgumentException if provided values are zero or negative.
     */
    public double[] nextDoubles(final int length, final double minValue, final double maxValue) {
        final double[] array = new double[length];
        fill(array, minValue, maxValue);
        return array;
    }

    /**
     * Returns the randomizer type of this instance.
     *
     * @return Randomizer type.
     */
    @Override
    public RandomizerType getType() {
        return RandomizerType.UNIFORM_RANDOMIZER;
    }
}
