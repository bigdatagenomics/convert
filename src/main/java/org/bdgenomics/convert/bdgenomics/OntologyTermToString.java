package org.bdgenomics.convert.bdgenomics;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.OntologyTerm;

import org.slf4j.Logger;

/**
 * Convert OntologyTerm to String.
 */
public final class OntologyTermToString extends AbstractConverter<OntologyTerm, String> {

    /**
     * Package private constructor.
     */
    OntologyTermToString() {
        super(OntologyTerm.class, String.class);
    }


    @Override
    public String convert(final OntologyTerm ontologyTerm,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (ontologyTerm == null) {
            warnOrThrow(ontologyTerm, "must not be null", null, stringency, logger);
            return null;
        }
        return ontologyTerm.getDb() + ":" + ontologyTerm.getAccession();
    }
}
