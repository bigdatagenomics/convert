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
import htsjdk.samtools.SAMReadGroupRecord;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.ReadGroup;

import org.slf4j.Logger;

/**
 * Convert a SAMFileHeader to a list of ReadGroups.
 */
public final class SamHeaderToReadGroups extends AbstractConverter<SAMFileHeader, List<ReadGroup>> {

    /** Convert a SAMReadGroupRecord to a ReadGroup. */
    private final Converter<SAMReadGroupRecord, ReadGroup> readGroupConverter;


    /**
     * Create a new SAMFileHeader to a list of ReadGroups converter.
     */
    public SamHeaderToReadGroups() {
        this(new SamReadGroupRecordToReadGroup());
    }

    /**
     * Create a new SAMFileHeader to a list of ReadGroups converter.
     *
     * @param readGroupConverter read group converter, must not be null
     */
    public SamHeaderToReadGroups(final Converter<SAMReadGroupRecord, ReadGroup> readGroupConverter) {
        super(SAMFileHeader.class, List.class);

        checkNotNull(readGroupConverter);
        this.readGroupConverter = readGroupConverter;
    }


    @Override
    public List<ReadGroup> convert(final SAMFileHeader header,
                                   final ConversionStringency stringency,
                                   final Logger logger) throws ConversionException {

        if (header == null) {
            warnOrThrow(header, "must not be null", null, stringency, logger);
            return null;
        }

        List<ReadGroup> readGroups = new ArrayList<ReadGroup>();
        if (header.getReadGroups() != null) {
            for (SAMReadGroupRecord record : header.getReadGroups()) {
                readGroups.add(readGroupConverter.convert(record, stringency, logger));
            }
        }
        return readGroups;
    }
}
