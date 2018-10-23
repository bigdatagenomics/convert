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
 * Unit test for ProcessingStepToProgram.
 */
public final class ProcessingStepToProgramTest {
    private final Logger logger = LoggerFactory.getLogger(ProcessingStepToProgramTest.class);
    private Converter<ProcessingStep, Program> processingStepConverter;

    @Before
    public void setUp() {
        processingStepConverter = new ProcessingStepToProgram();
    }

    @Test
    public void testConstructor() {
        assertNotNull(processingStepConverter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        processingStepConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(processingStepConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(processingStepConverter.convert(null, ConversionStringency.SILENT, logger));
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

        Program program = processingStepConverter.convert(processingStep, ConversionStringency.STRICT, logger);
        assertNotNull(program);
        assertEquals(program.getId(), processingStep.getId());
        assertEquals(program.getName(), processingStep.getProgramName());
        assertEquals(program.getCommandLine(), processingStep.getCommandLine());
        assertEquals(program.getPrevProgramId(), processingStep.getPreviousId());
        assertEquals(program.getVersion(), processingStep.getVersion());
    }
}
