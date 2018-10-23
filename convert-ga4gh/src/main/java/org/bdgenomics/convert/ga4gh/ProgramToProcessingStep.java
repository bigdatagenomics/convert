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

import javax.annotation.concurrent.Immutable;

import ga4gh.Common.Program;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.slf4j.Logger;

/**
 * Convert GA4GH Program to bdg-formats ProcessingStep.
 */
@Immutable
final class ProgramToProcessingStep extends AbstractConverter<Program, ProcessingStep> {

    /**
     * Convert GA4GH Program to bdg-formats ProcessingStep.
     */
    ProgramToProcessingStep() {
        super(Program.class, ProcessingStep.class);
    }


    @Override
    public ProcessingStep convert(final Program program,
                                  final ConversionStringency stringency,
                                  final Logger logger) throws ConversionException {

        if (program == null) {
            warnOrThrow(program, "must not be null", null, stringency, logger);
            return null;
        }

        return ProcessingStep.newBuilder()
            .setId(program.getId())
            .setProgramName(program.getName())
            .setCommandLine(program.getCommandLine())
            .setPreviousId(program.getPrevProgramId())
            .setVersion(program.getVersion())
            .build();
    }
}
