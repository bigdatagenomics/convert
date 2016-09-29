package org.bdgenomics.convert.bdgenomics;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Strand;

import org.slf4j.Logger;

/**
 * Convert Strand to String.
 */
public final class StrandToString extends AbstractConverter<Strand, String> {

    /**
     * Package private constructor.
     */
    StrandToString() {
        super(Strand.class, String.class);
    }


    @Override
    public String convert(final Strand strand,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (strand == null) {
            warnOrThrow(strand, "must not be null", null, stringency, logger);
            return null;
        }
        switch (strand) {
            case FORWARD:
                return "+";
            case REVERSE:
                return "-";
            case INDEPENDENT:
                return ".";
            case UNKNOWN:
                return "?";
            default:
                return "";
        }
    }
}
