package org.bdgenomics.convert.bdgenomics;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.OntologyTerm;

import org.slf4j.Logger;

/**
 * Convert String to OntologyTerm.
 */
public final class StringToOntologyTerm extends AbstractConverter<String, OntologyTerm> {

    /**
     * Package private constructor.
     */
    StringToOntologyTerm() {
        super(String.class, OntologyTerm.class);
    }


    @Override
    public OntologyTerm convert(final String value,
                                final ConversionStringency stringency,
                                final Logger logger) throws ConversionException {
        if (value == null) {
            warnOrThrow(value, "must not be null", null, stringency, logger);
            return null;
        }
        int i = value.indexOf(":");
        if (i < 0) {
            warnOrThrow(value, "incorrectly formatted OntologyTerm, should be db:accession", null, stringency, logger);
            return null;
        }
        return new OntologyTerm(value.substring(0, i), value.substring(i));
    }
}
