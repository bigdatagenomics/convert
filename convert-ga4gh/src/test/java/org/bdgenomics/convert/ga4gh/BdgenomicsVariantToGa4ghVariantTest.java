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
package org.bdgenomics.convert.ga4gh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;
import org.bdgenomics.convert.Converter;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for BdgenomicsVariantToGa4ghVariant.
 */
public final class BdgenomicsVariantToGa4ghVariantTest {
    private final Logger logger = LoggerFactory.getLogger(BdgenomicsVariantToGa4ghVariantTest.class);
    private Converter<org.bdgenomics.formats.avro.Variant, ga4gh.Variants.Variant> variantConverter;

    private org.bdgenomics.formats.avro.Variant bdgVariant;

    @Before
    public void setUp() {
        variantConverter = new BdgenomicsVariantToGa4ghVariant();

        bdgVariant = org.bdgenomics.formats.avro.Variant.newBuilder()
                .setNames(Arrays.asList("rs123"))
                .setContigName("1")
                .setStart(19190L)
                .setEnd(19191L)
                .setReferenceAllele("G")
                .setAlternateAllele("A").setFiltersApplied(Boolean.TRUE)
                .setFiltersPassed(Boolean.TRUE)
                .setFiltersFailed(Arrays.asList(""))
                .build();
    }

    @Test
    public void testConstructor() {
        assertNotNull(variantConverter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        variantConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(variantConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(variantConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        ga4gh.Variants.Variant ga4ghVariant = variantConverter.convert(bdgVariant, ConversionStringency.STRICT, logger);
        assertEquals("1", ga4ghVariant.getReferenceName());
        assertEquals(19190L, ga4ghVariant.getStart());
        assertEquals(19191L, ga4ghVariant.getEnd());
        assertEquals("G", ga4ghVariant.getReferenceBases());
        assertEquals("A", ga4ghVariant.getAlternateBases(0));

    }
}

