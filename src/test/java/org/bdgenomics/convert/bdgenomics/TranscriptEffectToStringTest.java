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

import org.junit.Before;
import org.junit.Test;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.TranscriptEffect;
import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for TranscriptEffectToString.
 */
public final class TranscriptEffectToStringTest {
    private Converter<TranscriptEffect, String> transcriptEffectConverter;
    private Converter<VariantAnnotationMessage, String> variantAnnotationMessageConverter;
    private final Logger logger = LoggerFactory.getLogger(TranscriptEffectToStringTest.class);

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

    @Test
    public void testConvert() {
        //assertEquals("+", transcriptEffectConverter.convert(TranscriptEffect.FORWARD, ConversionStringency.STRICT, logger));
    }
}
