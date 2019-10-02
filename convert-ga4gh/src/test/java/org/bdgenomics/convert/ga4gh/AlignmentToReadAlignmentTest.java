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
import java.util.List;

import com.google.protobuf.util.JsonFormat;

import ga4gh.Common.Strand;

import ga4gh.Reads.CigarUnit;
import ga4gh.Reads.CigarUnit.Operation;
import ga4gh.Reads.ReadAlignment;

import ga4gh.ReadServiceOuterClass.SearchReadsResponse;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarOperator;

import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;
import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.Alignment;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for AlignmentToReadAlignment.
 */
public final class AlignmentToReadAlignmentTest {
    private final Logger logger = LoggerFactory.getLogger(AlignmentToReadAlignmentTest.class);
    private Converter<CigarOperator, Operation> operatorConverter;
    private Converter<Cigar, List<CigarUnit>> cigarConverter;
    private Converter<Alignment, ReadAlignment> alignmentConverter;

    private Alignment alignment;

    @Before
    public void setUp() {
        operatorConverter = new CigarOperatorToOperation();
        cigarConverter = new CigarToCigarUnits(operatorConverter);
        alignmentConverter = new AlignmentToReadAlignment(cigarConverter);

        alignment = Alignment.newBuilder()
            .setReadName("read0")
            .setStart(10L)
            .setReadMapped(true)
            .setCigar("10M")
            .setSequence("AAAAAAAAAA")
            .setQualityScores("**********")
            .setReadNegativeStrand(false)
            .setMappingQuality(60)
            .setMismatchingPositions("10")
            .setOriginalStart(12L)
            .setOriginalCigar("2^AAA3")
            .setReadGroupId("rg1")
            .setReferenceName("myCtg")
            .setProperPair(true)
            .setDuplicateRead(false)
            .setFailedVendorQualityChecks(false)
            .setSecondaryAlignment(false)
            .setSupplementaryAlignment(false)
            .setMateReferenceName("myCtg")
            .setMateNegativeStrand(false)
            .setMateAlignmentStart(100L)
            .setMateMapped(true)
            .setReadPaired(true)
            .setInsertSize(200L)
            .build();
    }

    @Test
    public void testConstructor() {
        assertNotNull(alignmentConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullCigarConverter() {
        new AlignmentToReadAlignment(null);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        alignmentConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(alignmentConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(alignmentConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        ReadAlignment readAlignment = alignmentConverter.convert(alignment, ConversionStringency.STRICT, logger);
        assertEquals(10L, readAlignment.getAlignment().getPosition().getPosition());
        assertEquals( "myCtg", readAlignment.getAlignment().getPosition().getReferenceName());
        assertEquals(Strand.POS_STRAND, readAlignment.getAlignment().getPosition().getStrand());
        assertEquals("rg1", readAlignment.getReadGroupId());
        assertEquals("read0", readAlignment.getFragmentName());
        assertEquals(false, readAlignment.getImproperPlacement());
        assertEquals(false, readAlignment.getDuplicateFragment());
        assertEquals(false,readAlignment.getFailedVendorQualityChecks());
        assertEquals(false,readAlignment.getSecondaryAlignment());
        assertEquals(false, readAlignment.getSupplementaryAlignment());
        assertEquals("myCtg", readAlignment.getNextMatePosition().getReferenceName());
        assertEquals(2, readAlignment.getNumberReads());
        assertEquals(200L, readAlignment.getFragmentLength());
        assertEquals("AAAAAAAAAA", readAlignment.getAlignedSequence());
        assertEquals(Arrays.asList(9, 9, 9, 9, 9, 9, 9, 9, 9, 9), readAlignment.getAlignedQualityList());
    }

    @Test
    public void testConvertNoMapq() {
      Alignment alignmentNoMapq = Alignment.newBuilder()
          .setReadName("read0")
          .setStart(10L)
          .setReadMapped(true)
          .setCigar("10M")
          .setSequence("AAAAAAAAAA")
          .setQualityScores("**********")
          .setReadNegativeStrand(false)
          .setMismatchingPositions("10")
          .setOriginalStart(12L)
          .setOriginalCigar("2^AAA3")
          .setReadGroupId("rg1")
          .setReferenceName("myCtg")
          .setProperPair(true)
          .setDuplicateRead(false)
          .setFailedVendorQualityChecks(false)
          .setSecondaryAlignment(false)
          .setSupplementaryAlignment(false)
          .setMateReferenceName("myCtg")
          .setMateNegativeStrand(false)
          .setMateAlignmentStart(100L)
          .setMateMapped(true)
          .setReadPaired(true)
          .setInsertSize(200L)
          .build();

      ReadAlignment readAlignment = alignmentConverter.convert(alignmentNoMapq, ConversionStringency.STRICT, logger);

      assertEquals(0, readAlignment.getAlignment().getMappingQuality());
    }

    @Test
    public void testJson() throws Exception {
        ReadAlignment readAlignment = alignmentConverter.convert(alignment, ConversionStringency.STRICT, logger);

        SearchReadsResponse response = SearchReadsResponse.newBuilder()
            .addAllAlignments(Arrays.asList(readAlignment))
            .build();

        String json = JsonFormat.printer().print(response).replaceAll("\\s+","");
        assertEquals("{\"alignments\":[{\"readGroupId\":\"rg1\",\"fragmentName\":\"read0\",\"numberReads\":2,\"fragmentLength\":200,\"alignment\":{\"position\":{\"referenceName\":\"myCtg\",\"position\":\"10\",\"strand\":\"POS_STRAND\"},\"mappingQuality\":60,\"cigar\":[{\"operation\":\"ALIGNMENT_MATCH\",\"operationLength\":\"10\"}]},\"alignedSequence\":\"AAAAAAAAAA\",\"alignedQuality\":[9,9,9,9,9,9,9,9,9,9],\"nextMatePosition\":{\"referenceName\":\"myCtg\",\"position\":\"100\",\"strand\":\"POS_STRAND\"}}]}", json);
    }
}
