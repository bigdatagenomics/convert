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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import javax.annotation.concurrent.Immutable;

import ga4gh.Common.Program;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;
import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.slf4j.Logger;

/**
 * Convert bdg-formats ReadGroup to GA4GH ReadGroup.
 */
@Immutable
final class BdgenomicsReadGroupToGa4ghReadGroup extends AbstractConverter<org.bdgenomics.formats.avro.ReadGroup, ga4gh.Reads.ReadGroup> {
    /** Convert bdg-formats ProcessingStep to GA4GH Program. */
    private final Converter<ProcessingStep, Program> processingStepConverter;

    /**
     * Convert bdg-formats ReaGroup to GA4GH ReadGroup.
     *
     * @param processingStepConverter bdg-formats ProcessingStep to GA4GH Program
     *    converter, must not be null
     */
    BdgenomicsReadGroupToGa4ghReadGroup(final Converter<ProcessingStep, Program> processingStepConverter) {
        super(org.bdgenomics.formats.avro.ReadGroup.class, ga4gh.Reads.ReadGroup.class);
        checkNotNull(processingStepConverter);
        this.processingStepConverter = processingStepConverter;
    }


    @Override
    public ga4gh.Reads.ReadGroup convert(final org.bdgenomics.formats.avro.ReadGroup readGroup,
                                         final ConversionStringency stringency,
                                         final Logger logger) throws ConversionException {

        if (readGroup == null) {
            warnOrThrow(readGroup, "must not be null", null, stringency, logger);
            return null;
        }

        ga4gh.Reads.ReadGroup.Builder builder = ga4gh.Reads.ReadGroup.newBuilder()
            .setId(isNotEmpty(readGroup.getId()) ? readGroup.getId() : "1")
            .setName(isNotEmpty(readGroup.getId()) ? readGroup.getId() : "1");

        if (isNotEmpty(readGroup.getSampleId())) {
            builder.setSampleName(readGroup.getSampleId());
        }

        if (isNotEmpty(readGroup.getDescription())) {
            builder.setDescription(readGroup.getDescription());
        }

        if (readGroup.getPredictedMedianInsertSize() != null) {
            builder.setPredictedInsertSize(readGroup.getPredictedMedianInsertSize());
        }

        if (!readGroup.getProcessingSteps().isEmpty()) {
            for (ProcessingStep processingStep : readGroup.getProcessingSteps()) {
                builder.addPrograms(processingStepConverter.convert(processingStep, stringency, logger));
            }

        }
        return builder.build();
    }
}
