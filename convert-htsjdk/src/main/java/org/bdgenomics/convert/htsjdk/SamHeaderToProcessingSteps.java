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

import java.util.ArrayList;
import java.util.List;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMProgramRecord;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ProcessingStep;

import org.slf4j.Logger;

/**
 * Convert a SAMFileHeader to a list of ProcessingSteps.
 */
public final class SamHeaderToProcessingSteps extends AbstractConverter<SAMFileHeader, List<ProcessingStep>> {

    /** Convert a SAMProgramRecord to a ProcessingStep. */
    private final Converter<SAMProgramRecord, ProcessingStep> processingStepConverter;


    /**
     * Create a new SAMFileHeader to a list of ProcessingSteps converter.
     */
    public SamHeaderToProcessingSteps() {
        this(new SamProgramRecordToProcessingStep());
    }

    /**
     * Create a new SAMFileHeader to a list of ProcessingSteps converter.
     *
     * @param processingStepConverter processing step converter, must not be null
     */
    SamHeaderToProcessingSteps(final Converter<SAMProgramRecord, ProcessingStep> processingStepConverter) {
        super(SAMProgramRecord.class, ProcessingStep.class);

        checkNotNull(processingStepConverter);
        this.processingStepConverter = processingStepConverter;
    }


    @Override
    public List<ProcessingStep> convert(final SAMFileHeader header,
                                        final ConversionStringency stringency,
                                        final Logger logger) throws ConversionException {

        if (header == null) {
            warnOrThrow(header, "must not be null", null, stringency, logger);
            return null;
        }

        List<ProcessingStep> processingSteps = new ArrayList<ProcessingStep>();
        if (header.getProgramRecords() != null) {
            for (SAMProgramRecord record : header.getProgramRecords()) {
                processingSteps.add(processingStepConverter.convert(record, stringency, logger));
            }
        }
        return processingSteps;
    }
}
