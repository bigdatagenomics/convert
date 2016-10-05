/**
 * Licensed to Big Data Genomics (BDG) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The BDG licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdgenomics.convert;

import org.slf4j.Logger;

/**
 * Convert an instance of a source type into an instance of a target type.
 *
 * @param S source type
 * @param T target type
 */
public interface Converter<S, T> {

    /**
     * Return the source class for this converter.
     *
     * @return the source class for this converter
     */
    Class<?> getSourceClass();

    /**
     * Return the target class for this converter.
     *
     * @return the target class for this converter
     */
    Class<?> getTargetClass();

    /**
     * Convert the specified source into the target type <code>T</code>.
     *
     * @param source source to convert
     * @param stringency conversion stringency, must not be null
     * @param logger logger, must not be null
     * @return the specified source converted into the target type <code>T</code>
     * @throws ConversionException if conversion fails and the specified conversion stringency is strict
     * @throws NullPointerException if either conversion stringency or logger are null
     */
    T convert(S source, ConversionStringency stringency, Logger logger) throws ConversionException;
}
