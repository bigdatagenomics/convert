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

import java.util.ArrayList;
import java.util.List;

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
 * Unit test for TranscriptEffectToString.
 */
public final class TranscriptEffectToStringTest {
    private Converter<TranscriptEffect, String> transcriptEffectConverter;
    private Converter<VariantAnnotationMessage, String> variantAnnotationMessageConverter;
    private final Logger logger = LoggerFactory.getLogger(TranscriptEffectToStringTest.class);
    private static final String VALID = "T|upstream_gene_variant||TAS1R3|ENSG00000169962|transcript|ENST00000339381.5|protein_coding|1/2|c.-485C>T|||4|1/42|453|I3";

    @Before
    public void setUp() {
        variantAnnotationMessageConverter = new VariantAnnotationMessageToString();
        transcriptEffectConverter = new TranscriptEffectToString(variantAnnotationMessageConverter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(transcriptEffectConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullVariantAnnotationMessageConverter() {
        new TranscriptEffectToString(null);
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
    public void testConvertInvalidFractionStrict() {
        TranscriptEffect invalidFraction = TranscriptEffect.newBuilder()
            .setTotal(4)
            .build();
        transcriptEffectConverter.convert(invalidFraction, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertInvalidFractionLenient() {
        TranscriptEffect invalidFraction = TranscriptEffect.newBuilder()
            .setTotal(4)
            .build();
        assertNull(transcriptEffectConverter.convert(invalidFraction, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertInvalidFractionSilent() {
        TranscriptEffect invalidFraction = TranscriptEffect.newBuilder()
            .setTotal(4)
            .build();
        assertNull(transcriptEffectConverter.convert(invalidFraction, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        TranscriptEffect te = TranscriptEffect.newBuilder()
            .setAlternateAllele("T")
            .setEffects(listOf("upstream_gene_variant"))
            .setGeneName("TAS1R3")
            .setGeneId("ENSG00000169962")
            .setFeatureType("transcript")
            .setFeatureId("ENST00000339381.5")
            .setBiotype("protein_coding")
            .setTranscriptHgvs("c.-485C>T")
            .setRank(1)
            .setTotal(2)
            .setCodingSequencePosition(4)
            .setProteinPosition(1)
            .setProteinLength(42)
            .setDistance(453)
            .setMessages(listOf(VariantAnnotationMessage.INFO_NON_REFERENCE_ANNOTATION))
            .build();

        assertEquals(VALID, transcriptEffectConverter.convert(te, ConversionStringency.STRICT, logger));
    }

    private static <T> List<T> listOf(final T value) {
        List<T> list = new ArrayList<T>();
        list.add(value);
        return list;
    }
}
