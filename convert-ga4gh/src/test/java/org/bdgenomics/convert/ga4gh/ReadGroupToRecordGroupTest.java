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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import ga4gh.Common.Program;

import ga4gh.Reads.ReadGroup;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ProcessingStep;
import org.bdgenomics.formats.avro.RecordGroup;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for ReadGroupToRecordGroup.
 */
public final class ReadGroupToRecordGroupTest {
    private final Logger logger = LoggerFactory.getLogger(ReadGroupToRecordGroupTest.class);
    private Converter<Program, ProcessingStep> programConverter;
    private Converter<ReadGroup, RecordGroup> readGroupConverter;

    @Before
    public void setUp() {
        programConverter = new ProgramToProcessingStep();
        readGroupConverter = new ReadGroupToRecordGroup(programConverter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(readGroupConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProgramConverter() {
        new ReadGroupToRecordGroup(null);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        readGroupConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(readGroupConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(readGroupConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        Program program = Program.newBuilder()
            .setId("id")
            .setName("name")
            .setCommandLine("command line")
            .setPrevProgramId("previous id")
            .setVersion("version")
            .build();

        ReadGroup readGroup = ReadGroup.newBuilder()
            .setName("readGroupName")
            .setSampleName("readGroupSample")
            .setDescription("readGroupDescription")
            .setPredictedInsertSize(42)
            .addPrograms(program)
            .build();

        RecordGroup recordGroup = readGroupConverter.convert(readGroup, ConversionStringency.STRICT, logger);
        assertNotNull(recordGroup);
        assertEquals(recordGroup.getName(), readGroup.getName());
        assertEquals(recordGroup.getSample(), readGroup.getSampleName());
        assertEquals(recordGroup.getDescription(), readGroup.getDescription());
        assertEquals(recordGroup.getPredictedMedianInsertSize().intValue(), readGroup.getPredictedInsertSize());
        assertFalse(recordGroup.getProcessingSteps().isEmpty());

        ProcessingStep processingStep = recordGroup.getProcessingSteps().get(0);
        assertNotNull(processingStep);
        assertEquals(processingStep.getId(), program.getId());
        assertEquals(processingStep.getProgramName(), program.getName());
        assertEquals(processingStep.getCommandLine(), program.getCommandLine());
        assertEquals(processingStep.getPreviousId(), program.getPrevProgramId());
        assertEquals(processingStep.getVersion(), program.getVersion());
    }
}
