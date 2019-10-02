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

import org.bdgenomics.formats.avro.Alignment;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for AlignmentToSamRecord.
 */
public final class AlignmentToSamRecordTest {
    private final Logger logger = LoggerFactory.getLogger(AlignmentToSamRecordTest.class);

    SAMFileHeader header;
    Converter<Alignment, SAMRecord> converter;

    @Before
    public void setUp() {
        header = new SAMFileHeader();
        SAMSequenceRecord sequenceRecord = new SAMSequenceRecord("1", 3000000);
        header.getSequenceDictionary().addSequence(sequenceRecord);

        converter = new AlignmentToSamRecord(header);
    }

    @Test
    public void testConstructor() {
        assertNotNull(converter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHeader() {
        new AlignmentToSamRecord(null);
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
        Alignment alignment = Alignment.newBuilder()
            .setReadMapped(true)
            .setReferenceName(null)
            .build();

        converter.convert(alignment, STRICT, logger);
    }

    @Test
    public void testConvertNullReferenceNameLenient() {
        Alignment alignment = Alignment.newBuilder()
            .setReadMapped(true)
            .setReferenceName(null)
            .build();

        SAMRecord samRecord = converter.convert(alignment, LENIENT, logger);
        assertEquals("*", samRecord.getReferenceName());
    }

    @Test
    public void testConvertNullReferenceNameSilent() {
        Alignment alignment = Alignment.newBuilder()
            .setReadMapped(true)
            .setReferenceName(null)
            .build();

        SAMRecord samRecord = converter.convert(alignment, SILENT, logger);
        assertEquals("*", samRecord.getReferenceName());
    }

    @Test
    public void testConvert() {
        // todo
    }
}
