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

import htsjdk.samtools.SAMProgramRecord;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.slf4j.Logger;

/**
 * Convert a SAMProgramRecord to a ProcessingStep.
 */
public final class SamProgramRecordToProcessingStep extends AbstractConverter<SAMProgramRecord, ProcessingStep> {

    /**
     * Create a new SAMProgramRecord to a ProcessingStep converter.
     */
    public SamProgramRecordToProcessingStep() {
        super(SAMProgramRecord.class, ProcessingStep.class);
    }


    @Override
    public ProcessingStep convert(final SAMProgramRecord record,
                                  final ConversionStringency stringency,
                                  final Logger logger) throws ConversionException {

        if (record == null) {
            warnOrThrow(record, "must not be null", null, stringency, logger);
            return null;
        }
        if (record.getId() == null) {
            warnOrThrow(record, "id must not be null", null, stringency, logger);
            return null;
        }

        ProcessingStep.Builder builder = ProcessingStep.newBuilder()
            .setId(record.getId());

        Optional.ofNullable(record.getCommandLine()).ifPresent(commandLine -> builder.setCommandLine(commandLine));
        Optional.ofNullable(record.getPreviousProgramGroupId()).ifPresent(previousId -> builder.setPreviousId(previousId));
        Optional.ofNullable(record.getProgramName()).ifPresent(programName -> builder.setProgramName(programName));
        Optional.ofNullable(record.getProgramVersion()).ifPresent(version -> builder.setVersion(version));

        return builder.build();
    }
}
