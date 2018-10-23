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

import java.util.ArrayList;
import java.util.List;

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
 * Unit test for RecordGroupToReadGroup.
 */
public final class RecordGroupToReadGroupTest {
    private final Logger logger = LoggerFactory.getLogger(RecordGroupToReadGroup.class);
    private Converter<ProcessingStep, Program> processingStepConverter;
    private Converter<RecordGroup, ReadGroup> recordGroupConverter;

    @Before
    public void setUp() {
        processingStepConverter = new ProcessingStepToProgram();
        recordGroupConverter = new RecordGroupToReadGroup(processingStepConverter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(recordGroupConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProcessingStepConverter() {
        new RecordGroupToReadGroup(null);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        recordGroupConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(recordGroupConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(recordGroupConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        ProcessingStep processingStep = ProcessingStep.newBuilder()
            .setId("id")
            .setProgramName("name")
            .setCommandLine("command line")
            .setPreviousId("previous id")
            .setVersion("version")
            .build();

        List<ProcessingStep> processingSteps = new ArrayList<ProcessingStep>(1);
        processingSteps.add(processingStep);

        RecordGroup recordGroup = RecordGroup.newBuilder()
            .setName("recordGroupName")
            .setSample("recordGroupSample")
            .setDescription("recordGroupDescription")
            .setPredictedMedianInsertSize(42)
            .setProcessingSteps(processingSteps)
            .build();

        ReadGroup readGroup = recordGroupConverter.convert(recordGroup, ConversionStringency.STRICT, logger);
        assertNotNull(readGroup);
        assertEquals(readGroup.getId(), recordGroup.getName());
        assertEquals(readGroup.getName(), recordGroup.getName());
        assertEquals(readGroup.getSampleName(), recordGroup.getSample());
        assertEquals(readGroup.getDescription(), recordGroup.getDescription());
        assertEquals(readGroup.getPredictedInsertSize(), recordGroup.getPredictedMedianInsertSize().intValue());
        assertFalse(readGroup.getProgramsList().isEmpty());
        assertEquals(readGroup.getProgramsCount(), 1);

        Program program = readGroup.getProgramsList().get(0);
        assertNotNull(program);
        assertEquals(program.getId(), processingStep.getId());
        assertEquals(program.getName(), processingStep.getProgramName());
        assertEquals(program.getCommandLine(), processingStep.getCommandLine());
        assertEquals(program.getPrevProgramId(), processingStep.getPreviousId());
        assertEquals(program.getVersion(), processingStep.getVersion());
    }
}
