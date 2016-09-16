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
