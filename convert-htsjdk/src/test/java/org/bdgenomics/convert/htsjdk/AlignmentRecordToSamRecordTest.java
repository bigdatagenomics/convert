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

import org.bdgenomics.formats.avro.AlignmentRecord;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for AlignmentRecordToSamRecord.
 */
public final class AlignmentRecordToSamRecordTest {
    private final Logger logger = LoggerFactory.getLogger(AlignmentRecordToSamRecordTest.class);

    SAMFileHeader header;
    Converter<AlignmentRecord, SAMRecord> converter;

    @Before
    public void setUp() {
        header = new SAMFileHeader();
        SAMSequenceRecord sequenceRecord = new SAMSequenceRecord("1", 3000000);
        header.getSequenceDictionary().addSequence(sequenceRecord);

        converter = new AlignmentRecordToSamRecord(header);
    }

    @Test
    public void testConstructor() {
        assertNotNull(converter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHeader() {
        new AlignmentRecordToSamRecord(null);
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
    public void testConvertNullReferenceNameStrict() {
        AlignmentRecord alignmentRecord = AlignmentRecord.newBuilder()
            .setReadMapped(true)
            .setReferenceName(null)
            .build();

        converter.convert(alignmentRecord, STRICT, logger);
    }

    @Test
    public void testConvertNullReferenceNameLenient() {
        AlignmentRecord alignmentRecord = AlignmentRecord.newBuilder()
            .setReadMapped(true)
            .setReferenceName(null)
            .build();

        SAMRecord samRecord = converter.convert(alignmentRecord, LENIENT, logger);
        assertEquals("*", samRecord.getReferenceName());
    }

    @Test
    public void testConvertNullReferenceNameSilent() {
        AlignmentRecord alignmentRecord = AlignmentRecord.newBuilder()
            .setReadMapped(true)
            .setReferenceName(null)
            .build();

        SAMRecord samRecord = converter.convert(alignmentRecord, SILENT, logger);
        assertEquals("*", samRecord.getReferenceName());
    }

    @Test
    public void testConvert() {
        // todo
    }
}
