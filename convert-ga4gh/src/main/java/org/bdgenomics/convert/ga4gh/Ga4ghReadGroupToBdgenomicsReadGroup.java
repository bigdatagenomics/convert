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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import ga4gh.Common.Program;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;
import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.slf4j.Logger;

/**
 * Convert GA4GH ReadGroup to bdg-formats ReadGroup.
 */
@Immutable
final class Ga4ghReadGroupToBdgenomicsReadGroup extends AbstractConverter<ga4gh.Reads.ReadGroup, org.bdgenomics.formats.avro.ReadGroup> {
    /** Convert GA4GH Program to bdg-formats ProcessingStep. */
    private final Converter<Program, ProcessingStep> programConverter;

    /**
     * Convert GA4GH ReadGroup to bdg-formats ReadGroup.
     *
     * @param programConverter GA4GH Program to bdg-formats ProcessingGroup converter,
     *    must not be null
     */
    Ga4ghReadGroupToBdgenomicsReadGroup(final Converter<Program, ProcessingStep> programConverter) {
        super(ga4gh.Reads.ReadGroup.class, org.bdgenomics.formats.avro.ReadGroup.class);
        checkNotNull(programConverter);
        this.programConverter = programConverter;
    }


    @Override
    public org.bdgenomics.formats.avro.ReadGroup convert(final ga4gh.Reads.ReadGroup readGroup,
                                                         final ConversionStringency stringency,
                                                         final Logger logger) throws ConversionException {

        if (readGroup == null) {
            warnOrThrow(readGroup, "must not be null", null, stringency, logger);
            return null;
        }

        org.bdgenomics.formats.avro.ReadGroup.Builder builder = org.bdgenomics.formats.avro.ReadGroup.newBuilder()
            .setId(readGroup.getId())
            .setSampleId(readGroup.getSampleName())
            .setDescription(readGroup.getDescription())
            .setPredictedMedianInsertSize(readGroup.getPredictedInsertSize());

        List<Program> programs = readGroup.getProgramsList();
        if (!programs.isEmpty()) {
            List<ProcessingStep> processingSteps = new ArrayList<ProcessingStep>(programs.size());
            for (Program program : programs) {
                processingSteps.add(programConverter.convert(program, stringency, logger));
            }
            builder.setProcessingSteps(processingSteps);
        }
        return builder.build();
    }
}
