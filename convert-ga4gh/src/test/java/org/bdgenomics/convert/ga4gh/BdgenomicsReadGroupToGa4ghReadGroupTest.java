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

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for BdgenomicsReadGroupToGa4ghReadGroup.
 */
public final class BdgenomicsReadGroupToGa4ghReadGroupTest {
    private final Logger logger = LoggerFactory.getLogger(BdgenomicsReadGroupToGa4ghReadGroup.class);
    private Converter<ProcessingStep, Program> processingStepConverter;
    private Converter<org.bdgenomics.formats.avro.ReadGroup, ga4gh.Reads.ReadGroup> readGroupConverter;

    @Before
    public void setUp() {
        processingStepConverter = new ProcessingStepToProgram();
        readGroupConverter = new BdgenomicsReadGroupToGa4ghReadGroup(processingStepConverter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(readGroupConverter);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullProcessingStepConverter() {
        new BdgenomicsReadGroupToGa4ghReadGroup(null);
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
        ProcessingStep processingStep = ProcessingStep.newBuilder()
            .setId("id")
            .setProgramName("name")
            .setCommandLine("command line")
            .setPreviousId("previous id")
            .setVersion("version")
            .build();

        List<ProcessingStep> processingSteps = new ArrayList<ProcessingStep>(1);
        processingSteps.add(processingStep);

        org.bdgenomics.formats.avro.ReadGroup bdgenomicsReadGroup = org.bdgenomics.formats.avro.ReadGroup.newBuilder()
            .setId("id")
            .setSampleId("sampleId")
            .setDescription("description")
            .setPredictedMedianInsertSize(42)
            .setProcessingSteps(processingSteps)
            .build();

        ga4gh.Reads.ReadGroup ga4ghReadGroup = readGroupConverter.convert(bdgenomicsReadGroup, ConversionStringency.STRICT, logger);
        assertNotNull(ga4ghReadGroup);
        assertEquals(ga4ghReadGroup.getId(), bdgenomicsReadGroup.getId());
        assertEquals(ga4ghReadGroup.getName(), bdgenomicsReadGroup.getId());
        assertEquals(ga4ghReadGroup.getSampleName(), bdgenomicsReadGroup.getSampleId());
        assertEquals(ga4ghReadGroup.getDescription(), bdgenomicsReadGroup.getDescription());
        assertEquals(ga4ghReadGroup.getPredictedInsertSize(), bdgenomicsReadGroup.getPredictedMedianInsertSize().intValue());
        assertFalse(ga4ghReadGroup.getProgramsList().isEmpty());
        assertEquals(ga4ghReadGroup.getProgramsCount(), 1);

        Program program = ga4ghReadGroup.getProgramsList().get(0);
        assertNotNull(program);
        assertEquals(program.getId(), processingStep.getId());
        assertEquals(program.getName(), processingStep.getProgramName());
        assertEquals(program.getCommandLine(), processingStep.getCommandLine());
        assertEquals(program.getPrevProgramId(), processingStep.getPreviousId());
        assertEquals(program.getVersion(), processingStep.getVersion());
    }
}
