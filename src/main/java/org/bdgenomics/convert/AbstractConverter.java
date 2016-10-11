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
 * Abstract converter.
 *
 * @param S source type
 * @param T target type
 */
public abstract class AbstractConverter<S, T> implements Converter<S, T> {
    /** Source class. */
    private final Class<?> sourceClass;

    /** Target class. */
    private final Class<?> targetClass;


    /**
     * Create a new abstract converter.
     *
     * @param sourceClass source class, must not be null
     * @param targetClass target class, must not be null
     */
    protected AbstractConverter(final Class<?> sourceClass, final Class<?> targetClass) {
        if (sourceClass == null) {
            throw new NullPointerException("sourceClass must not be null");
        }
        if (targetClass == null) {
            throw new NullPointerException("targetClass must not be null");
        }
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }


    @Override
    public final Class<?> getSourceClass() {
        return sourceClass;
    }

    @Override
    public final Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * Check the specified converter is not null.
     *
     * @param converter converter, must not b null
     * @throws NullPointerException if converter is null
     */
    protected final void checkNotNull(final Converter<?, ?> converter) {
        if (converter == null) {
            throw new NullPointerException("converter must not be null");
        }
    }

    /**
     * Check the specified conversion stringency and logger are not null.
     *
     * @param stringency conversion stringency, must not be null
     * @param logger logger, must not be null
     * @throws NullPointerException if either conversion stringency or logger are null
     */
    protected final void checkNotNull(final ConversionStringency stringency,
                                      final Logger logger) {
        if (stringency == null) {
            throw new NullPointerException("stringency must not be null");
        }
        if (logger == null) {
            throw new NullPointerException("logger must not be null");
        }
    }

    /**
     * If the conversion stringency is lenient, log a warning with the specified message,
     * or if the conversion stringency is strict, throw a ConversionException with the specified
     * message and cause.
     *
     * @param source source
     * @param message message
     * @param cause cause
     * @param stringency conversion stringency, must not be null
     * @param logger logger, must not be null
     * @throws ConversionException if the specified conversion stringency is strict
     * @throws NullPointerException if either conversion stringency or logger are null
     */
    protected final void warnOrThrow(final S source,
                                     final String message,
                                     final Throwable cause,
                                     final ConversionStringency stringency,
                                     final Logger logger) throws ConversionException {

        checkNotNull(stringency, logger);
        if (stringency.isLenient()) {
            logger.warn("could not convert {} to {}, {}", sourceClass.toString(), targetClass.toString(), message);
        }
        else if (stringency.isStrict()) {
            throw new ConversionException(String.format("could not convert %s to %s, %s", sourceClass.toString(), targetClass.toString(), message), cause, source, sourceClass, targetClass);
        }
    }
}
