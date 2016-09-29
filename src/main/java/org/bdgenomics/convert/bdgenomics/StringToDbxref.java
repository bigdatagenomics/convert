package org.bdgenomics.convert.bdgenomics;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Dbxref;

import org.slf4j.Logger;

/**
 * Convert String to Dbxref.
 */
public final class StringToDbxref extends AbstractConverter<String, Dbxref> {

    /**
     * Package private constructor.
     */
    StringToDbxref() {
        super(String.class, Dbxref.class);
    }


    @Override
    public Dbxref convert(final String value,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (value == null) {
            warnOrThrow(value, "must not be null", null, stringency, logger);
            return null;
        }
        int i = value.indexOf(":");
        if (i < 0) {
            warnOrThrow(value, "incorrectly formatted Dbxref, should be db:accession", null, stringency, logger);
            return null;
        }
        return new Dbxref(value.substring(0, i), value.substring(i));
    }
}
