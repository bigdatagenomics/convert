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
package org.bdgenomics.convert.htsjdk;

import java.util.Optional;

import htsjdk.samtools.SAMReadGroupRecord;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ReadGroup;

import org.slf4j.Logger;

/**
 * Convert a SAMReadGroup to a ReadGroup.
 */
public final class SamReadGroupRecordToReadGroup extends AbstractConverter<SAMReadGroupRecord, ReadGroup> {

    /**
     * Create a new SAMReadGroupRecord to a ReadGroup converter.
     */
    public SamReadGroupRecordToReadGroup() {
        super(SAMReadGroupRecord.class, ReadGroup.class);
    }


    @Override
    public ReadGroup convert(final SAMReadGroupRecord record,
                             final ConversionStringency stringency,
                             final Logger logger) throws ConversionException {

        if (record == null) {
            warnOrThrow(record, "must not be null", null, stringency, logger);
            return null;
        }
        if (record.getReadGroupId() == null) {
            warnOrThrow(record, "readGroupId must not be null", null, stringency, logger);
            return null;
        }
        if (record.getSample() == null) {
            warnOrThrow(record, "sample must not be null", null, stringency, logger);
            return null;
        }

        ReadGroup.Builder builder = ReadGroup.newBuilder()
            .setId(record.getReadGroupId())
            .setSampleId(record.getSample());

        Optional.ofNullable(record.getDescription()).ifPresent(description -> builder.setDescription(description));
        Optional.ofNullable(record.getFlowOrder()).ifPresent(flowOrder -> builder.setFlowOrder(flowOrder));
        Optional.ofNullable(record.getKeySequence()).ifPresent(keySequence -> builder.setKeySequence(keySequence));
        Optional.ofNullable(record.getLibrary()).ifPresent(library -> builder.setLibrary(library));
        Optional.ofNullable(record.getPlatform()).ifPresent(platform -> builder.setPlatform(platform));
        Optional.ofNullable(record.getPlatformModel()).ifPresent(platformModel -> builder.setPlatformModel(platformModel));
        Optional.ofNullable(record.getPlatformUnit()).ifPresent(platformUnit -> builder.setPlatformUnit(platformUnit));
        Optional.ofNullable(record.getPredictedMedianInsertSize()).ifPresent(insertSize -> builder.setPredictedMedianInsertSize(insertSize));
        Optional.ofNullable(record.getRunDate()).map(runDate -> builder.setRunDateEpoch(runDate.getTime()));
        Optional.ofNullable(record.getSequencingCenter()).ifPresent(sequencingCenter -> builder.setSequencingCenter(sequencingCenter));

        // processing steps?

        return builder.build();
    }
}
