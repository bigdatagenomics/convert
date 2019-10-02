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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import ga4gh.Common.Position;
import ga4gh.Common.Strand;

import ga4gh.Reads.CigarUnit;
import ga4gh.Reads.LinearAlignment;
import ga4gh.Reads.ReadAlignment;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.TextCigarCodec;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Alignment;

import org.slf4j.Logger;

/**
 * Convert bgd-formats Alignment to GA4GH ReadAlignment.
 */
@Immutable
final class AlignmentToReadAlignment extends AbstractConverter<Alignment, ReadAlignment> {
    /** Convert htsjdk Cigar to a list of GA4GH CigarUnits. */
    private final Converter<Cigar, List<CigarUnit>> cigarConverter;

    /**
     * Convert bgd-formats Alignment to GA4GH ReadAlignment.
     *
     * @param cigarConverter cigar converter, must not be null
     */
    AlignmentToReadAlignment(final Converter<Cigar, List<CigarUnit>> cigarConverter) {
        super(Alignment.class, ReadAlignment.class);
        checkNotNull(cigarConverter);
        this.cigarConverter = cigarConverter;
    }


    @Override
    public ReadAlignment convert(final Alignment alignment,
                                 final ConversionStringency stringency,
                                 final Logger logger) throws ConversionException {

        if (alignment == null) {
            warnOrThrow(alignment, "must not be null", null, stringency, logger);
            return null;
        }
        ReadAlignment.Builder builder = ReadAlignment.newBuilder()
            .setAlignedSequence(alignment.getSequence())
            .setDuplicateFragment(alignment.getDuplicateRead())
            .setFailedVendorQualityChecks(alignment.getFailedVendorQualityChecks())
            .setFragmentName(alignment.getReadName())
            .setImproperPlacement(!alignment.getProperPair())
            .setNumberReads(alignment.getReadPaired() ? 2 : 1)
            .setReadGroupId(isNotEmpty(alignment.getReadGroupId()) ? alignment.getReadGroupId() : "1")
            .setReadNumber(alignment.getReadInFragment())
            .setSecondaryAlignment(alignment.getSecondaryAlignment())
            .setSupplementaryAlignment(alignment.getSupplementaryAlignment());

        if (alignment.getInsertSize() != null) {
            builder.setFragmentLength(alignment.getInsertSize().intValue());
        }

        if (alignment.getMateReferenceName() != null) {
            Position matePosition = Position.newBuilder()
                .setReferenceName(alignment.getMateReferenceName())
                .setPosition(alignment.getMateAlignmentStart())
                .setStrand(alignment.getMateNegativeStrand() ? Strand.NEG_STRAND : Strand.POS_STRAND)
                .build();

            builder.setNextMatePosition(matePosition);
        }

        if (isNotEmpty(alignment.getQualityScores())) {
            List<Integer> alignedQuality = new ArrayList<Integer>(alignment.getQualityScores().length());
            for (char c : alignment.getQualityScores().toCharArray()) {
                alignedQuality.add(((int) c) - 33);
            }
            builder.addAllAlignedQuality(alignedQuality);
        }

        if (alignment.getReadMapped()) {
            Position position = Position.newBuilder()
                .setReferenceName(alignment.getReferenceName())
                .setPosition(alignment.getStart())
                .setStrand(alignment.getReadNegativeStrand() ? Strand.NEG_STRAND : Strand.POS_STRAND)
                .build();

            LinearAlignment.Builder alignmentBuilder = LinearAlignment.newBuilder()
                .setPosition(position);

            if (alignment.getMappingQuality() != null)
              alignmentBuilder.setMappingQuality(alignment.getMappingQuality());

            Cigar cigar = null;
            try {
                cigar = TextCigarCodec.decode(alignment.getCigar());
            }
            catch (RuntimeException e) { // sigh ...
                warnOrThrow(alignment, "could not decode cigar, caught " + e.getMessage(), e, stringency, logger);
            }
            if (cigar != null) {
                alignmentBuilder.addAllCigar(cigarConverter.convert(cigar, stringency, logger));
            }

            builder.setAlignment(alignmentBuilder.build());
        }
        return builder.build();
    }
}
