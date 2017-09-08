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

import org.bdgenomics.formats.avro.Alphabet;
import org.bdgenomics.formats.avro.Sequence;
import org.bdgenomics.formats.avro.Slice;
import org.bdgenomics.formats.avro.Strand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for SequenceToSlice.
 */
public final class SequenceToSliceTest {
    private final Logger logger = LoggerFactory.getLogger(SequenceToSliceTest.class);
    private Converter<Sequence, Slice> sequenceToSliceConverter;

    @Before
    public void setUp() {
        sequenceToSliceConverter = new SequenceToSlice();
    }

    @Test
    public void testConstructor() {
        assertNotNull(sequenceToSliceConverter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        sequenceToSliceConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(sequenceToSliceConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(sequenceToSliceConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        Sequence sequence = Sequence.newBuilder()
            .setName("name")
            .setDescription("description")
            .setAlphabet(Alphabet.DNA)
            .setSequence("actg")
            .setLength(4L)
            .build();

        Slice slice = Slice.newBuilder()
            .setName("name")
            .setDescription("description")
            .setAlphabet(Alphabet.DNA)
            .setSequence("actg")
            .setLength(4L)
            .setStart(0L)
            .setEnd(4L)
            .setStrand(Strand.INDEPENDENT)
            .build();

        assertEquals(slice, sequenceToSliceConverter.convert(sequence, ConversionStringency.STRICT, logger));
    }
}
