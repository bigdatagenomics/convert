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

import ga4gh.Reads.ReadGroup;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;
import org.bdgenomics.convert.Converter;

import org.bdgenomics.formats.avro.ProcessingStep;
import org.bdgenomics.formats.avro.RecordGroup;

import org.slf4j.Logger;

/**
 * Convert bdg-formats RecordGroup to GA4GH ReadGroup.
 */
@Immutable
final class RecordGroupToReadGroup extends AbstractConverter<RecordGroup, ReadGroup> {
    /** Convert bdg-formats ProcessingStep to GA4GH Program. */
    private final Converter<ProcessingStep, Program> processingStepConverter;

    /**
     * Convert bdg-formats RecordGroup to GA4GH ReadGroup.
     *
     * @param processingStepConverter bdg-formats ProcessingStep to GA4GH Program
     *    converter, must not be null
     */
    RecordGroupToReadGroup(final Converter<ProcessingStep, Program> processingStepConverter) {
        super(RecordGroup.class, ReadGroup.class);
        checkNotNull(processingStepConverter);
        this.processingStepConverter = processingStepConverter;
    }


    @Override
    public ReadGroup convert(final RecordGroup recordGroup,
                             final ConversionStringency stringency,
                             final Logger logger) throws ConversionException {

        if (recordGroup == null) {
            warnOrThrow(recordGroup, "must not be null", null, stringency, logger);
            return null;
        }

        ReadGroup.Builder builder = ReadGroup.newBuilder()
            .setId(isNotEmpty(recordGroup.getName()) ? recordGroup.getName() : "1")
            .setName(isNotEmpty(recordGroup.getName()) ? recordGroup.getName() : "1");

        if (isNotEmpty(recordGroup.getSample())) {
            builder.setSampleName(recordGroup.getSample());
        }

        if (isNotEmpty(recordGroup.getDescription())) {
            builder.setDescription(recordGroup.getDescription());
        }

        if (recordGroup.getPredictedMedianInsertSize() != null) {
            builder.setPredictedInsertSize(recordGroup.getPredictedMedianInsertSize());
        }

        if (!recordGroup.getProcessingSteps().isEmpty()) {
            for (ProcessingStep processingStep : recordGroup.getProcessingSteps()) {
                builder.addPrograms(processingStepConverter.convert(processingStep, stringency, logger));
            }

        }
        return builder.build();
    }
}
