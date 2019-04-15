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
import java.util.Optional;

import htsjdk.samtools.SAMSequenceRecord;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Reference;

import org.slf4j.Logger;

/**
 * Convert a SAMSequenceRecord to a Reference.
 */
public final class SamSequenceRecordToReference extends AbstractConverter<SAMSequenceRecord, Reference> {
    static final String REFSEQ_TAG = "REFSEQ";
    static final String GENBANK_TAG = "GENBANK";

    /**
     * Create a new SAMSequenceRecord to a Reference converter.
     */
    public SamSequenceRecordToReference() {
        super(SAMSequenceRecord.class, Reference.class);
    }


    @Override
    public Reference convert(final SAMSequenceRecord record,
                             final ConversionStringency stringency,
                             final Logger logger) throws ConversionException {

        if (record == null) {
            warnOrThrow(record, "must not be null", null, stringency, logger);
            return null;
        }

        Reference.Builder builder = Reference.newBuilder();

        if (record.getSequenceIndex() > -1) {
            builder.setIndex(record.getSequenceIndex());
        }
        if (record.getSequenceLength() > 0) {
            builder.setLength((long) record.getSequenceLength());
        }

        Optional.ofNullable(record.getSequenceName()).ifPresent(name -> builder.setName(name));
        Optional.ofNullable(record.getMd5()).ifPresent(md5 -> builder.setMd5(md5));
        Optional.ofNullable(record.getSpecies()).ifPresent(species -> builder.setSpecies(species));
        Optional.ofNullable(record.getAttribute(SAMSequenceRecord.URI_TAG)).ifPresent(uri -> builder.setSourceUri(uri));

        List<String> accessions = new ArrayList<String>(2);
        Optional.ofNullable(record.getAttribute(REFSEQ_TAG)).ifPresent(refseq -> accessions.add(refseq));
        Optional.ofNullable(record.getAttribute(GENBANK_TAG)).ifPresent(genbank -> accessions.add(genbank));
        if (!accessions.isEmpty()) {
            builder.setSourceAccessions(accessions);
        }

        return builder.build();
    }
}
