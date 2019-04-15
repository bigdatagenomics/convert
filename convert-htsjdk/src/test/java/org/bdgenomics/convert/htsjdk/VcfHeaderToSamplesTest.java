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
package org.bdgenomics.convert.htsjdk;

import static org.bdgenomics.convert.ConversionStringency.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;

import org.bdgenomics.formats.avro.Sample;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for VcfHeaderToSamples.
 */
public final class VcfHeaderToSamplesTest {
    private final Logger logger = LoggerFactory.getLogger(VcfHeaderToSamplesTest.class);

    VCFHeader header;
    Converter<VCFHeader, List<Sample>> converter;

    @Before
    public void setUp() {
        header = new VCFHeader(Collections.<VCFHeaderLine>emptySet(), Collections.singleton("sampleId"));
        converter = new VcfHeaderToSamples();
    }

    @Test
    public void testConstructor() {
        assertNotNull(converter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullSourceStrict() {
        converter.convert(null, STRICT, logger);
    }

    @Test
    public void testConvertNullSourceLenient() {
        assertEquals(null, converter.convert(null, LENIENT, logger));
    }

    @Test
    public void testConvertNullSourceSilent() {
        assertEquals(null, converter.convert(null, SILENT, logger));
    }

    @Test
    public void testConvert() {
        List<Sample> samples = converter.convert(header, SILENT, logger);
        assertNotNull(samples);
        assertEquals(1, samples.size());
        assertEquals("sampleId", samples.get(0).getId());
    }

    @Test
    public void testConvertEmpty() {
        List<Sample> samples = converter.convert(new VCFHeader(), SILENT, logger);
        assertNotNull(samples);
        assertTrue(samples.isEmpty());
    }
}
