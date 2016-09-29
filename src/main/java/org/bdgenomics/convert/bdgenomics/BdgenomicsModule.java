package org.bdgenomics.convert.bdgenomics;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.Dbxref;
import org.bdgenomics.formats.avro.OntologyTerm;
import org.bdgenomics.formats.avro.Strand;

/**
 * Guice module for the org.bdgenomics.convert.bdgenomics package.
 */
public final class BdgenomicsModule extends AbstractModule {
    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    Converter<String, Dbxref> createStringToDbxrefConverter() {
        return new StringToDbxref();
    }

    @Provides @Singleton
    Converter<Dbxref, String> createDbxrefToStringConverter() {
        return new DbxrefToString();
    }

    @Provides @Singleton
    Converter<String, OntologyTerm> createStringToOntologyTermConverter() {
        return new StringToOntologyTerm();
    }

    @Provides @Singleton
    Converter<OntologyTerm, String> createOntologyTermToStringConverter() {
        return new OntologyTermToString();
    }

    @Provides @Singleton
    Converter<String, Strand> createStringToStrandConverter() {
        return new StringToStrand();
    }

    @Provides @Singleton
    Converter<Strand, String> createStrandToStringConverter() {
        return new StrandToString();
    }
}
