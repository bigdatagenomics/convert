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

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for Ga4ghReadGroupToBdgenomicsReadGroup.
 */
public final class Ga4ghReadGroupToBdgenomicsReadGroupTest {
    private final Logger logger = LoggerFactory.getLogger(Ga4ghReadGroupToBdgenomicsReadGroupTest.class);
    private Converter<Program, ProcessingStep> programConverter;
    private Converter<ga4gh.Reads.ReadGroup, org.bdgenomics.formats.avro.ReadGroup> readGroupConverter;

    @Before
    public void setUp() {
        programConverter = new ProgramToProcessingStep();
        readGroupConverter = new Ga4ghReadGroupToBdgenomicsReadGroup(programConverter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(readGroupConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProgramConverter() {
        new Ga4ghReadGroupToBdgenomicsReadGroup(null);
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

        ga4gh.Reads.ReadGroup ga4ghReadGroup = ga4gh.Reads.ReadGroup.newBuilder()
            .setId("readGroupId")
            .setName("readGroupName")
            .setSampleName("readGroupSample")
            .setDescription("readGroupDescription")
            .setPredictedInsertSize(42)
            .addPrograms(program)
            .build();

        org.bdgenomics.formats.avro.ReadGroup bdgenomicsReadGroup = readGroupConverter.convert(ga4ghReadGroup, ConversionStringency.STRICT, logger);
        assertNotNull(bdgenomicsReadGroup);
        assertEquals(bdgenomicsReadGroup.getId(), ga4ghReadGroup.getId());
        assertEquals(bdgenomicsReadGroup.getSampleId(), ga4ghReadGroup.getSampleName());
        assertEquals(bdgenomicsReadGroup.getDescription(), ga4ghReadGroup.getDescription());
        assertEquals(bdgenomicsReadGroup.getPredictedMedianInsertSize().intValue(), ga4ghReadGroup.getPredictedInsertSize());
        assertFalse(bdgenomicsReadGroup.getProcessingSteps().isEmpty());

        ProcessingStep processingStep = bdgenomicsReadGroup.getProcessingSteps().get(0);
        assertNotNull(processingStep);
        assertEquals(processingStep.getId(), program.getId());
        assertEquals(processingStep.getProgramName(), program.getName());
        assertEquals(processingStep.getCommandLine(), program.getCommandLine());
        assertEquals(processingStep.getPreviousId(), program.getPrevProgramId());
        assertEquals(processingStep.getVersion(), program.getVersion());
    }
}
