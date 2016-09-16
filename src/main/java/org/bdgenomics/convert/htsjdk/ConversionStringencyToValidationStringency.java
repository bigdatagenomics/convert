package org.bdgenomics.convert.htsjdk;

import htsjdk.samtools.ValidationStringency;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

/**
 * Convert ConversionStrategy to ValidationStrategy.
 */
public final class ConversionStringencyToValidationStringency extends AbstractConverter<ConversionStringency, ValidationStringency> {

    /**
     * Package private constructor.
     */
    ConversionStringencyToValidationStringency() {
        super(ConversionStringency.class, ValidationStringency.class);
    }

    @Override
    public ValidationStringency convert(final ConversionStringency conversionStringency,
                                        final ConversionStringency stringency,
                                        final Logger logger) throws ConversionException {
        if (conversionStringency == null) {
            warnOrThrow(conversionStringency, "must not be null", null, stringency, logger);
            return null;
        }
        if (conversionStringency.isSilent()) {
            return ValidationStringency.SILENT;
        }
        else if (conversionStringency.isLenient()) {
            return ValidationStringency.LENIENT;
        }
        return ValidationStringency.STRICT;
    }
}
