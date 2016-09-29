package org.bdgenomics.convert.bdgenomics;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Strand;

import org.slf4j.Logger;

/**
 * Convert String to Strand.
 */
public final class StringToStrand extends AbstractConverter<String, Strand> {

    /**
     * Package private constructor.
     */
    StringToStrand() {
        super(String.class, Strand.class);
    }


    @Override
    public Strand convert(final String value,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (value == null) {
            warnOrThrow(value, "must not be null", null, stringency, logger);
            return null;
        }
        if ("+".equals(value)) {
            return Strand.FORWARD;
        }
        else if ("-".equals(value)) {
            return Strand.REVERSE;
        }
        else if (".".equals(value)) {
            return Strand.INDEPENDENT;
        }
        else if ("?".equals(value)) {
            return Strand.UNKNOWN;
        }
        warnOrThrow(value, "incorrectly formatted Strand, should be one of { +, -, ., ? }", null, stringency, logger);
        return null;
    }
}
