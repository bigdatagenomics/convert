package org.bdgenomics.convert.bdgenomics;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Dbxref;

import org.slf4j.Logger;

/**
 * Convert Dbxref to String.
 */
public final class DbxrefToString extends AbstractConverter<Dbxref, String> {

    /**
     * Package private constructor.
     */
    DbxrefToString() {
        super(Dbxref.class, String.class);
    }


    @Override
    public String convert(final Dbxref dbxref,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (dbxref == null) {
            warnOrThrow(dbxref, "must not be null", null, stringency, logger);
            return null;
        }
        return dbxref.getDb() + ":" + dbxref.getAccession();
    }
}
