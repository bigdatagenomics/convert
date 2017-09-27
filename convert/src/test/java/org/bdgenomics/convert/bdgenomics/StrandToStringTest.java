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

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Strand;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for StrandToString.
 */
public final class StrandToStringTest {
    private final Logger logger = LoggerFactory.getLogger(StrandToStringTest.class);
    private Converter<Strand, String> strandConverter;

    @Before
    public void setUp() {
        strandConverter = new StrandToString();
    }

    @Test
    public void testConstructor() {
        assertNotNull(strandConverter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        strandConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(strandConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(strandConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        assertEquals("+", strandConverter.convert(Strand.FORWARD, ConversionStringency.STRICT, logger));
        assertEquals("-", strandConverter.convert(Strand.REVERSE, ConversionStringency.STRICT, logger));
        assertEquals(".", strandConverter.convert(Strand.INDEPENDENT, ConversionStringency.STRICT, logger));
        assertEquals("?", strandConverter.convert(Strand.UNKNOWN, ConversionStringency.STRICT, logger));
    }
}
