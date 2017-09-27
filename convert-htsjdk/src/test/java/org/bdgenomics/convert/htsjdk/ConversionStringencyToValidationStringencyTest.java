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

import htsjdk.samtools.ValidationStringency;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for ConversionStringencyToValidationStringency.
 */
public final class ConversionStringencyToValidationStringencyTest {
    private final Logger logger = LoggerFactory.getLogger(ConversionStringencyToValidationStringencyTest.class);

    Converter<ConversionStringency, ValidationStringency> converter;

    @Before
    public void setUp() {
        converter = new ConversionStringencyToValidationStringency();
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
        // should see warning, or use mocks
        assertEquals(null, converter.convert(null, LENIENT, logger));
    }

    @Test
    public void testConvertNullSourceSilent() {
        assertEquals(null, converter.convert(null, SILENT, logger));
    }

    @Test
    public void testConvert() {
        assertEquals(ValidationStringency.STRICT, converter.convert(STRICT, SILENT, logger));
        assertEquals(ValidationStringency.LENIENT, converter.convert(LENIENT, SILENT, logger));
        assertEquals(ValidationStringency.SILENT, converter.convert(SILENT, SILENT, logger));
    }
}
