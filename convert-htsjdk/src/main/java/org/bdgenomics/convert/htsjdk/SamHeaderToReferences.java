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
import htsjdk.samtools.SAMSequenceRecord;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Reference;

import org.slf4j.Logger;

/**
 * Convert a SAMFileHeader to a list of References.
 */
public final class SamHeaderToReferences extends AbstractConverter<SAMFileHeader, List<Reference>> {

    /** Convert SAMSequenceRecord to Reference. */
    private final Converter<SAMSequenceRecord, Reference> referenceConverter;


    /**
     * Create a new SAMFileHeader to a list of References converter.
     */
    public SamHeaderToReferences() {
        this(new SamSequenceRecordToReference());
    }

    /**
     * Create a new SAMFileHeader to a list of References converter.
     *
     * @param referenceConverter reference converter, must not be null
     */
    SamHeaderToReferences(final Converter<SAMSequenceRecord, Reference> referenceConverter) {
        super(SAMFileHeader.class, List.class);

        checkNotNull(referenceConverter);
        this.referenceConverter = referenceConverter;
    }


    @Override
    public List<Reference> convert(final SAMFileHeader header,
                                   final ConversionStringency stringency,
                                   final Logger logger) throws ConversionException {

        if (header == null) {
            warnOrThrow(header, "must not be null", null, stringency, logger);
            return null;
        }

        List<Reference> references = new ArrayList<Reference>();
        if (header.getSequenceDictionary() != null && header.getSequenceDictionary().getSequences() != null) {
            for (SAMSequenceRecord record: header.getSequenceDictionary().getSequences()) {
                references.add(referenceConverter.convert(record, stringency, logger));
            }
        }
        return references;
    }
}
