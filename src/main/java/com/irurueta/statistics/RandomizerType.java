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

/**
 * Indicates type of Randomizer.
 */
public enum RandomizerType {

    /**
     * Type used for Uniform randomizers where generated values are uniformly
     * distributed.
     */
    UNIFORM_RANDOMIZER,

    /**
     * Type used for Gaussian randomizers where generated values are distributed
     * following a Gaussian distribution.
     */
    GAUSSIAN_RANDOMIZER
}
