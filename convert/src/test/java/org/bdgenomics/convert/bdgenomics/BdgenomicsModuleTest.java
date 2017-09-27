/**
 * Licensed to Big Data Genomics (BDG) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The BDG licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdgenomics.convert.bdgenomics;

import static org.junit.Assert.assertNotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Guice;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Dbxref;
import org.bdgenomics.formats.avro.OntologyTerm;
import org.bdgenomics.formats.avro.Read;
import org.bdgenomics.formats.avro.Sequence;
import org.bdgenomics.formats.avro.Slice;
import org.bdgenomics.formats.avro.Strand;
import org.bdgenomics.formats.avro.TranscriptEffect;
import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.junit.Before;
import org.junit.Test;

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
        assertNotNull(target.getStringToTranscriptEffect());
        assertNotNull(target.getTranscriptEffectToString());
        assertNotNull(target.getStringToVariantAnnotationMessage());
        assertNotNull(target.getVariantAnnotationMessageToString());
        assertNotNull(target.getReadToSequence());
        assertNotNull(target.getReadToSlice());
        assertNotNull(target.getSequenceToRead());
        assertNotNull(target.getSequenceToSlice());
        assertNotNull(target.getSliceToRead());
        assertNotNull(target.getSliceToSequence());
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
        final Converter<String, TranscriptEffect> stringToTranscriptEffect;
        final Converter<TranscriptEffect, String> transcriptEffectToString;
        final Converter<String, VariantAnnotationMessage> stringToVariantAnnotationMessage;
        final Converter<VariantAnnotationMessage, String> variantAnnotationMessageToString;
        final Converter<Read, Sequence> readToSequence;
        final Converter<Read, Slice> readToSlice;
        final Converter<Sequence, Read> sequenceToRead;
        final Converter<Sequence, Slice> sequenceToSlice;
        final Converter<Slice, Read> sliceToRead;
        final Converter<Slice, Sequence> sliceToSequence;

        @Inject
        Target(final Converter<String, Dbxref> stringToDbxref,
               final Converter<Dbxref, String> dbxrefToString,
               final Converter<String, OntologyTerm> stringToOntologyTerm,
               final Converter<OntologyTerm, String> ontologyTermToString,
               final Converter<String, Strand> stringToStrand,
               final Converter<Strand, String> strandToString,
               final Converter<String, TranscriptEffect> stringToTranscriptEffect,
               final Converter<TranscriptEffect, String> transcriptEffectToString,
               final Converter<String, VariantAnnotationMessage> stringToVariantAnnotationMessage,
               final Converter<VariantAnnotationMessage, String> variantAnnotationMessageToString,
               final Converter<Read, Sequence> readToSequence,
               final Converter<Read, Slice> readToSlice,
               final Converter<Sequence, Read> sequenceToRead,
               final Converter<Sequence, Slice> sequenceToSlice,
               final Converter<Slice, Read> sliceToRead,
               final Converter<Slice, Sequence> sliceToSequence) {

            this.stringToDbxref = stringToDbxref;
            this.dbxrefToString = dbxrefToString;
            this.stringToOntologyTerm = stringToOntologyTerm;
            this.ontologyTermToString = ontologyTermToString;
            this.stringToStrand = stringToStrand;
            this.strandToString = strandToString;
            this.stringToTranscriptEffect = stringToTranscriptEffect;
            this.transcriptEffectToString = transcriptEffectToString;
            this.stringToVariantAnnotationMessage = stringToVariantAnnotationMessage;
            this.variantAnnotationMessageToString = variantAnnotationMessageToString;
            this.readToSequence = readToSequence;
            this.readToSlice = readToSlice;
            this.sequenceToRead = sequenceToRead;
            this.sequenceToSlice = sequenceToSlice;
            this.sliceToRead = sliceToRead;
            this.sliceToSequence = sliceToSequence;
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

        Converter<String, TranscriptEffect> getStringToTranscriptEffect() {
            return stringToTranscriptEffect;
        }

        Converter<TranscriptEffect, String> getTranscriptEffectToString() {
            return transcriptEffectToString;
        }

        Converter<String, VariantAnnotationMessage> getStringToVariantAnnotationMessage() {
            return stringToVariantAnnotationMessage;
        }

        Converter<VariantAnnotationMessage, String> getVariantAnnotationMessageToString() {
            return variantAnnotationMessageToString;
        }

        Converter<Read, Sequence> getReadToSequence() {
            return readToSequence;
        }

        Converter<Read, Slice> getReadToSlice() {
            return readToSlice;
        }

        Converter<Sequence, Read> getSequenceToRead() {
            return sequenceToRead;
        }

        Converter<Sequence, Slice> getSequenceToSlice() {
            return sequenceToSlice;
        }

        Converter<Slice, Read> getSliceToRead() {
            return sliceToRead;
        }

        Converter<Slice, Sequence> getSliceToSequence() {
            return sliceToSequence;
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
