package org.bdgenomics.convert.bdgenomics;

import static org.junit.Assert.assertNotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Guice;

import org.junit.Before;
import org.junit.Test;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Dbxref;
import org.bdgenomics.formats.avro.OntologyTerm;
import org.bdgenomics.formats.avro.Strand;

/**
 * Unit test for BdgenomicsModule.
 */
public final class BdgenomicsModuleTest {
    private BdgenomicsModule module;

    @Before
    public void setUp() {
        module = new BdgenomicsModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(module);
    }

    @Test
    public void testBdgenomicsModule() {
        Injector injector = Guice.createInjector(module, new TestModule());
        Target target = injector.getInstance(Target.class);
        assertNotNull(target.getStringToDbxref());
        assertNotNull(target.getDbxrefToString());
        assertNotNull(target.getStringToOntologyTerm());
        assertNotNull(target.getOntologyTermToString());
        assertNotNull(target.getStringToStrand());
        assertNotNull(target.getStrandToString());

    }

    /**
     * Injection target.
     */
    static class Target {
        final Converter<String, Dbxref> stringToDbxref;
        final Converter<Dbxref, String> dbxrefToString;
        final Converter<String, OntologyTerm> stringToOntologyTerm;
        final Converter<OntologyTerm, String> ontologyTermToString;
        final Converter<String, Strand> stringToStrand;
        final Converter<Strand, String> strandToString;

        @Inject
        Target(final Converter<String, Dbxref> stringToDbxref,
               final Converter<Dbxref, String> dbxrefToString,
               final Converter<String, OntologyTerm> stringToOntologyTerm,
               final Converter<OntologyTerm, String> ontologyTermToString,
               final Converter<String, Strand> stringToStrand,
               final Converter<Strand, String> strandToString) {

            this.stringToDbxref = stringToDbxref;
            this.dbxrefToString = dbxrefToString;
            this.stringToOntologyTerm = stringToOntologyTerm;
            this.ontologyTermToString = ontologyTermToString;
            this.stringToStrand = stringToStrand;
            this.strandToString = strandToString;
        }

        Converter<String, Dbxref> getStringToDbxref() {
            return stringToDbxref;
        }

        Converter<Dbxref, String> getDbxrefToString() {
            return dbxrefToString;
        }

        Converter<String, OntologyTerm> getStringToOntologyTerm() {
            return stringToOntologyTerm;
        }

        Converter<OntologyTerm, String> getOntologyTermToString() {
            return ontologyTermToString;
        }

        Converter<String, Strand> getStringToStrand() {
            return stringToStrand;
        }

        Converter<Strand, String> getStrandToString() {
            return strandToString;
        }
    }

    /**
     * Test module.
     */
    class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Target.class);
        }
    }
}
