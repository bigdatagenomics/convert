package org.bdgenomics.convert.htsjdk;

import htsjdk.samtools.ValidationStringency;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

/**
 * Convert ValidationStrategy to ConversionStrategy.
 */
public final class ValidationStringencyToConversionStringency extends AbstractConverter<ValidationStringency, ConversionStringency> {

    /**
     * Package private constructor.
     */
    ValidationStringencyToConversionStringency() {
        super(ValidationStringency.class, ConversionStringency.class);
    }

    @Override
    public ConversionStringency convert(final ValidationStringency validationStringency,
                                        final ConversionStringency stringency,
                                        final Logger logger) throws ConversionException {
        if (validationStringency == null) {
            warnOrThrow(validationStringency, "must not be null", null, stringency, logger);
            return null;
        }
        if (validationStringency == ValidationStringency.SILENT) {
            return ConversionStringency.SILENT;
        }
        else if (validationStringency == ValidationStringency.LENIENT) {
            return ConversionStringency.LENIENT;
        }
        return ConversionStringency.STRICT;
    }
}
