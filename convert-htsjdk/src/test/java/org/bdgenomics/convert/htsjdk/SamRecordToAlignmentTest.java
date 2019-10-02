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

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMSequenceRecord;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Alignment;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for SamRecordToAlignment.
 */
public final class SamRecordToAlignmentTest {
    private final Logger logger = LoggerFactory.getLogger(SamRecordToAlignmentTest.class);

    Converter<SAMRecord, Alignment> converter;
    SAMFileHeader header;

    @Before
    public void setUp() {
        header = new SAMFileHeader();
        SAMSequenceRecord sequenceRecord = new SAMSequenceRecord("1", 3000000);
        header.getSequenceDictionary().addSequence(sequenceRecord);

        converter = new SamRecordToAlignment();
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

    @Test(expected=ConversionException.class)
    public void testConvertInvalidAlignmentStartStrict() {
        SAMRecord samRecord = new SAMRecord(header);
        samRecord.setReferenceIndex(0);
        samRecord.setReferenceName("1");
        samRecord.setAlignmentStart(0);

        converter.convert(samRecord, STRICT, logger);
    }

    @Test
    public void testConvertInvalidAlignmentStartLenient() {
        SAMRecord samRecord = new SAMRecord(header);
        samRecord.setReferenceIndex(0);
        samRecord.setReferenceName("1");
        samRecord.setAlignmentStart(0);

        Alignment alignment = converter.convert(samRecord, LENIENT, logger);
        assertEquals(samRecord.getReferenceName(), alignment.getReferenceName());
        assertNull(alignment.getStart());
        assertNull(alignment.getEnd());
    }

    @Test
    public void testConvertInvalidAlignmentStartSilent() {
        SAMRecord samRecord = new SAMRecord(header);
        samRecord.setReferenceIndex(0);
        samRecord.setReferenceName("1");
        samRecord.setAlignmentStart(0);

        Alignment alignment = converter.convert(samRecord, SILENT, logger);
        assertEquals(samRecord.getReferenceName(), alignment.getReferenceName());
        assertNull(alignment.getStart());
        assertNull(alignment.getEnd());
    }

    @Test(expected=ConversionException.class)
    public void testConvertInvalidMateAlignmentStartStrict() {
        SAMRecord samRecord = new SAMRecord(header);
        samRecord.setMateReferenceIndex(0);
        samRecord.setMateReferenceName("1");
        samRecord.setMateAlignmentStart(0);

        converter.convert(samRecord, STRICT, logger);
    }

    @Test
    public void testConvertInvalidMateAlignmentStartLenient() {
        SAMRecord samRecord = new SAMRecord(header);
        samRecord.setMateReferenceIndex(0);
        samRecord.setMateReferenceName("1");
        samRecord.setMateAlignmentStart(0);

        Alignment alignment = converter.convert(samRecord, LENIENT, logger);
        assertEquals(samRecord.getMateReferenceName(), alignment.getMateReferenceName());
        assertNull(alignment.getMateAlignmentStart());
    }

    @Test
    public void testConvertInvalidMateAlignmentStartSilent() {
        SAMRecord samRecord = new SAMRecord(header);
        samRecord.setMateReferenceIndex(0);
        samRecord.setMateReferenceName("1");
        samRecord.setMateAlignmentStart(0);

        Alignment alignment = converter.convert(samRecord, SILENT, logger);
        assertEquals(samRecord.getMateReferenceName(), alignment.getMateReferenceName());
        assertNull(alignment.getMateAlignmentStart());
    }

    @Test
    public void testConvert() {
        // todo
    }
}
