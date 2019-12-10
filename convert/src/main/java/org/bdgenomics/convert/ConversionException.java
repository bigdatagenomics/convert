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

/**
 * Runtime exception thrown in the event of a failed conversion.
 */
public final class ConversionException extends RuntimeException {
    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Source whose failed conversion threw this conversion exception. */
    private final Object source;

    /** Source class of the failed conversion that threw this conversion exception. */
    private final Class<?> sourceClass;

    /** Target class of the failed conversion that threw this conversion exception. */
    private final Class<?> targetClass;


    /**
     * Create a new conversion exception with the specified source, source class, target class,
     * and message.
     *
     * @param message message
     * @param source source
     * @param sourceClass source class, must not be null
     * @param targetClass target class, must not be null
     */
    public ConversionException(final String message, final Object source, final Class<?> sourceClass, final Class<?> targetClass) {
        this(message, null, source, sourceClass, targetClass);
    }
 
    /**
     * Create a new conversion exception with the specified source, source class, target class,
     * and cause.
     *
     * @param cause cause
     * @param source source
     * @param sourceClass source class, must not be null
     * @param targetClass target class, must not be null
     */
    public ConversionException(final Throwable cause, final Object source, final Class<?> sourceClass, final Class<?> targetClass) {
        this(null, cause, source, sourceClass, targetClass);
    }

    /**
     * Create a new conversion exception with the specified source, source class, target class,
     * message, and cause.
     *
     * @param message message
     * @param cause cause
     * @param source source
     * @param sourceClass source class, must not be null
     * @param targetClass target class, must not be null
     */
    public ConversionException(final String message, final Throwable cause, final Object source, final Class<?> sourceClass, final Class<?> targetClass) {
        super(message, cause);
        if (sourceClass == null) {
            throw new NullPointerException("sourceClass must not be null");
        }
        if (targetClass == null) {
            throw new NullPointerException("targetClass must not be null");
        }
        this.source = source;
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }


    /**
     * Return the source whose failed conversion threw this conversion exception.
     *
     * @return the source whose failed conversion threw this conversion exception
     */
    public Object getSource() {
        return source;
    }

    /**
     * Return the source class of the failed conversion that threw this conversion exception.
     *
     * @return the source class of the failed conversion that threw this conversion exception
     */
    public Class<?> getSourceClass() {
        return sourceClass;
    }

    /**
     * Return the target class of the failed conversion that threw this conversion exception.
     *
     * @return the target class of the failed conversion that threw this conversion exception
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }
}
