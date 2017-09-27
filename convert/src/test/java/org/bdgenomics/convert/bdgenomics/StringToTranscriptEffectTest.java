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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.TranscriptEffect;
import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for StringToTranscriptEffect.
 */
public final class StringToTranscriptEffectTest {
    private Converter<String, TranscriptEffect> transcriptEffectConverter;
    private Converter<String, VariantAnnotationMessage> variantAnnotationMessageConverter;
    private static final Logger logger = LoggerFactory.getLogger(StringToTranscriptEffectTest.class);
    private static final String EMPTY = "";
    private static final String INVALID = "T|upstream_gene_variant||TAS1R3|ENSG00000169962|transcript|ENST00000339381.5|protein_coding|1/2|c.-485C>T|||4|1/42|453";
    private static final String INVALID_NUMBER = "T|upstream_gene_variant||TAS1R3|ENSG00000169962|transcript|ENST00000339381.5|protein_coding|1/2|c.-485C>T|||4|1/42|not a number|";
    private static final String INVALID_FRACTION = "T|upstream_gene_variant||TAS1R3|ENSG00000169962|transcript|ENST00000339381.5|protein_coding|not a number/2|c.-485C>T|||4|1/42|453|";
    private static final String VALID = "T|upstream_gene_variant||TAS1R3|ENSG00000169962|transcript|ENST00000339381.5|protein_coding|1/2|c.-485C>T|||4|1/42|453|I3";

    @Before
    public void setUp() {
        variantAnnotationMessageConverter = new StringToVariantAnnotationMessage();
        transcriptEffectConverter = new StringToTranscriptEffect(variantAnnotationMessageConverter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(transcriptEffectConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullVariantAnnotationMessageConverter() {
        new StringToTranscriptEffect(null);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        transcriptEffectConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(transcriptEffectConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(transcriptEffectConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test(expected=ConversionException.class)
    public void testConvertEmptyStrict() {
        transcriptEffectConverter.convert(EMPTY, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertEmptyLenient() {
        assertNull(transcriptEffectConverter.convert(EMPTY, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertEmptySilent() {
        assertNull(transcriptEffectConverter.convert(EMPTY, ConversionStringency.SILENT, logger));
    }

    @Test(expected=ConversionException.class)
    public void testConvertInvalidStrict() {
        transcriptEffectConverter.convert(INVALID, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertInvalidLenient() {
        assertNull(transcriptEffectConverter.convert(INVALID, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertInvalidSilent() {
        assertNull(transcriptEffectConverter.convert(INVALID, ConversionStringency.SILENT, logger));
    }

    @Test(expected=ConversionException.class)
    public void testConvertInvalidNumberStrict() {
        transcriptEffectConverter.convert(INVALID_NUMBER, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertInvalidNumberLenient() {
        assertNull(transcriptEffectConverter.convert(INVALID_NUMBER, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertInvalidNumberSilent() {
        assertNull(transcriptEffectConverter.convert(INVALID_NUMBER, ConversionStringency.SILENT, logger));
    }

    @Test(expected=ConversionException.class)
    public void testConvertInvalidFractionStrict() {
        transcriptEffectConverter.convert(INVALID_FRACTION, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertInvalidFractionLenient() {
        assertNull(transcriptEffectConverter.convert(INVALID_FRACTION, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertInvalidFractionSilent() {
        assertNull(transcriptEffectConverter.convert(INVALID_FRACTION, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        TranscriptEffect te = transcriptEffectConverter.convert(VALID, ConversionStringency.STRICT, logger);
        assertEquals("T", te.getAlternateAllele());
        assertTrue(te.getEffects().contains("upstream_gene_variant"));
        assertEquals("TAS1R3", te.getGeneName());
        assertEquals("ENSG00000169962", te.getGeneId());
        assertEquals("transcript", te.getFeatureType());
        assertEquals("ENST00000339381.5", te.getFeatureId());
        assertEquals("protein_coding", te.getBiotype());
        assertEquals(Integer.valueOf(1), te.getRank());
        assertEquals(Integer.valueOf(2), te.getTotal());
        assertEquals("c.-485C>T", te.getTranscriptHgvs());
        assertNull(te.getProteinHgvs());
        assertNull(te.getCdnaPosition());
        assertNull(te.getCdnaLength());
        assertEquals(Integer.valueOf(4), te.getCdsPosition());
        assertNull(te.getCdsLength());
        assertEquals(Integer.valueOf(1), te.getProteinPosition());
        assertEquals(Integer.valueOf(42), te.getProteinLength());
        assertEquals(Integer.valueOf(453), te.getDistance());
        assertTrue(te.getMessages().contains(VariantAnnotationMessage.INFO_NON_REFERENCE_ANNOTATION));
    }
}
