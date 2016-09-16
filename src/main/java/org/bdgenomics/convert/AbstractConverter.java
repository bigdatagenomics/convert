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
            throw new ConversionException("could not convert %s to %s, %s".format(sourceClass.toString(), targetClass.toString(), message), cause, source, sourceClass, targetClass);
        }
    }
}
