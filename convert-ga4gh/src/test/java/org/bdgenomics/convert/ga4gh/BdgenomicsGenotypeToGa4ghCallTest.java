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

import org.bdgenomics.formats.avro.GenotypeAllele;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for BdgenomicsGenotypeToGa4ghCall.
 */
public final class BdgenomicsGenotypeToGa4ghCallTest {
    private final Logger logger = LoggerFactory.getLogger(BdgenomicsGenotypeToGa4ghCallTest.class);
    private Converter<org.bdgenomics.formats.avro.Genotype, ga4gh.Variants.Call> callConverter;
    private Converter<org.bdgenomics.formats.avro.GenotypeAllele, String> genotypeAlleleConverter;

    private org.bdgenomics.formats.avro.Genotype genotype;

    @Before
    public void setUp() {
        genotypeAlleleConverter = new GenotypeAlleleToString();
        callConverter = new BdgenomicsGenotypeToGa4ghCall(genotypeAlleleConverter);

        genotype = org.bdgenomics.formats.avro.Genotype.newBuilder()
                .setAlleles(Arrays.asList(GenotypeAllele.REF, GenotypeAllele.ALT))
                .setReferenceName("1")
                .setStart(14522L)
                .setEnd(14522L)
                .setPhased(false)
                .setPhaseSetId(1)
                .setGenotypeLikelihoods(Arrays.asList())
                .setSampleId("testSample")
                .build();
    }

    @Test
    public void testConstructor() {
        assertNotNull(callConverter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        callConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(callConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(callConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        ga4gh.Variants.Call call = callConverter.convert(genotype, ConversionStringency.STRICT, logger);
        assertEquals("0", call.getGenotype().getValues(0).getStringValue());
        assertEquals("1", call.getGenotype().getValues(1).getStringValue());
    }
}

