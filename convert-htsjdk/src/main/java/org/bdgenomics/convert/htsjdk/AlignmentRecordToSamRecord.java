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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMReadGroupRecord;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMUtils;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.AlignmentRecord;

import org.slf4j.Logger;

/**
 * Convert AlignmentRecord to htsjdk SAMRecord.
 */
public final class AlignmentRecordToSamRecord extends AbstractConverter<AlignmentRecord, SAMRecord> {

    /** Regex to capture attributes. */
    private static final Pattern ATTRIBUTE = Pattern.compile("([^:]{2,4}):([AifZHB]):(.*)");

    /** Regex to capture array attribute types. */
    private static final Pattern ARRAY_ATTRIBUTE = Pattern.compile("([cCiIsSf]{1},)(.*)");
    
    /** Header. */
    private final SAMFileHeader header;


    /**
     * Create a new AlignmentRecord to htsjdk SAMRecord converter with the specified header.
     *
     * @param header header, must not be null
     */
    @Inject
    public AlignmentRecordToSamRecord(@Assisted final SAMFileHeader header) {
        super(AlignmentRecord.class, SAMRecord.class);

        checkNotNull(header);
        this.header = header;
    }


    @Override
    public SAMRecord convert(final AlignmentRecord alignmentRecord,
                             final ConversionStringency stringency,
                             final Logger logger) throws ConversionException {

        if (alignmentRecord == null) {
            warnOrThrow(alignmentRecord, "must not be null", null, stringency, logger);
            return null;
        }

        SAMRecord builder = new SAMRecord(header);
        builder.setReadName(alignmentRecord.getReadName());
        builder.setReadString(alignmentRecord.getSequence());

        if (alignmentRecord.getQualityScores() == null) {
            builder.setBaseQualityString("*");
        }
        else {
            builder.setBaseQualityString(alignmentRecord.getQualityScores());
        }

        String readGroupId = alignmentRecord.getReadGroupId();
        if (readGroupId != null) {
            builder.setAttribute("RG", readGroupId);

            SAMReadGroupRecord readGroup = header.getReadGroup(readGroupId);
            if (readGroup != null && readGroup.getLibrary() != null) {
                builder.setAttribute("LB", readGroup.getLibrary());
            }
            if (readGroup != null && readGroup.getPlatformUnit() != null) {
                builder.setAttribute("PU", readGroup.getPlatformUnit());
            }
        }

        if (alignmentRecord.getMateReferenceName() != null) {
            builder.setMateReferenceName(alignmentRecord.getMateReferenceName());
        }
        if (alignmentRecord.getMateAlignmentStart() != null) {
            builder.setMateAlignmentStart(alignmentRecord.getMateAlignmentStart().intValue() + 1);
        }
        if (alignmentRecord.getInsertSize() != null) {
            builder.setInferredInsertSize(alignmentRecord.getInsertSize().intValue());
        }

        if (alignmentRecord.getReadPaired() != null) {
            boolean readPaired = alignmentRecord.getReadPaired();
            builder.setReadPairedFlag(readPaired);

            if (readPaired) {
                if (alignmentRecord.getMateNegativeStrand() != null) {
                    builder.setMateNegativeStrandFlag(alignmentRecord.getMateNegativeStrand());
                }
                if (alignmentRecord.getMateMapped() != null) {
                    builder.setMateUnmappedFlag(!alignmentRecord.getMateMapped());
                }
                if (alignmentRecord.getProperPair() != null) {
                    builder.setProperPairFlag(alignmentRecord.getProperPair());
                }
                if (alignmentRecord.getReadInFragment() != null) {
                    builder.setFirstOfPairFlag(alignmentRecord.getReadInFragment() == 0);
                    builder.setSecondOfPairFlag(alignmentRecord.getReadInFragment() == 1);
                }
            }
        }

        if (alignmentRecord.getDuplicateRead() != null) {
            builder.setDuplicateReadFlag(alignmentRecord.getDuplicateRead());
        }

        if (alignmentRecord.getReadMapped() != null) {
            boolean readMapped = alignmentRecord.getReadMapped();
            builder.setReadUnmappedFlag(!readMapped);

            if (alignmentRecord.getReadNegativeStrand() != null) {
                builder.setReadNegativeStrandFlag(alignmentRecord.getReadNegativeStrand());
            }

            if (readMapped) {
                if (alignmentRecord.getReferenceName() == null) {
                    warnOrThrow(alignmentRecord, "referenceName must not be null if read aligned", null, stringency, logger);
                }
                else {
                    builder.setReferenceName(alignmentRecord.getReferenceName());
                }

                if (alignmentRecord.getCigar() != null) {
                    builder.setCigarString(alignmentRecord.getCigar());
                }
                if (alignmentRecord.getPrimaryAlignment() != null) {
                    builder.setNotPrimaryAlignmentFlag(!alignmentRecord.getPrimaryAlignment());
                }
                if (alignmentRecord.getSupplementaryAlignment() != null) {
                    builder.setSupplementaryAlignmentFlag(alignmentRecord.getSupplementaryAlignment());
                }
                if (alignmentRecord.getStart() != null) {
                    builder.setAlignmentStart(alignmentRecord.getStart().intValue() + 1);
                }
                if (alignmentRecord.getMappingQuality() != null) {
                    builder.setMappingQuality(alignmentRecord.getMappingQuality());
                }
            }
            else {
                builder.setMappingQuality(0);
            }
        }

        if (alignmentRecord.getFailedVendorQualityChecks() != null) {
            builder.setReadFailsVendorQualityCheckFlag(alignmentRecord.getFailedVendorQualityChecks());
        }
        if (alignmentRecord.getMismatchingPositions() != null) {
            builder.setAttribute("MD", alignmentRecord.getMismatchingPositions());
        }
        if (alignmentRecord.getOriginalQualityScores() != null) {
            builder.setOriginalBaseQualities(SAMUtils.fastqToPhred(alignmentRecord.getOriginalQualityScores()));
        }
        if (alignmentRecord.getOriginalCigar() != null) {
            builder.setAttribute("OC", alignmentRecord.getOriginalCigar());
        }
        if (alignmentRecord.getOriginalStart() != null) {
            builder.setAttribute("OP", alignmentRecord.getOriginalStart().intValue() + 1);
        }

        if (alignmentRecord.getAttributes() != null) {
            String[] tokens = alignmentRecord.getAttributes().split("\t");
            for (String token : tokens) {
                Matcher m = ATTRIBUTE.matcher(token);
                if (m.matches()) {
                    String tagName = m.group(1);
                    String tagType = m.group(2);
                    String value = m.group(3);

                    if ("B".equals(tagType)) {
                        m = ARRAY_ATTRIBUTE.matcher(value);
                        if (m.matches()) {
                            tagType = tagType + ":" + m.group(1);
                            value = m.group(2);
                        }
                    }

                    switch (tagType) {
                    case "A":
                        builder.setAttribute(tagName, value.charAt(0));
                    case "i":
                        builder.setAttribute(tagName, Integer.valueOf(value));
                    case "f":
                        builder.setAttribute(tagName, Float.valueOf(value));
                    case "Z":
                        builder.setAttribute(tagName, value);
                    case "H":
                        builder.setAttribute(tagName, value.getBytes());
                    case "B:c,":
                    case "B:C,":
                        builder.setAttribute(tagName, splitToByteArray(value));
                    case "B:i,":
                    case "B:I,":
                        builder.setAttribute(tagName, splitToIntegerArray(value));
                    case "B:s,":
                    case "B:S,":
                        builder.setAttribute(tagName, splitToShortArray(value));
                    case "B:f,":
                        builder.setAttribute(tagName, splitToFloatArray(value));
                    default:
                        warnOrThrow(alignmentRecord, "invalid attribute type " + tagType, null, stringency, logger);
                    }
                }
            }
        }

        return builder;
    }

    /**
     * Split the specified value to a byte array.
     *
     * @param value value
     * @return the specified value split to a byte array
     */
    static byte[] splitToByteArray(final String value) {
        String[] tokens = value.split(",");
        byte[] bytes = new byte[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            bytes[i] = Byte.valueOf(tokens[i]);
        }
        return bytes;
    }

    /**
     * Split the specified value to an integer array.
     *
     * @param value value
     * @return the specified value split to an integer array
     */
    static int[] splitToIntegerArray(final String value) {
        String[] tokens = value.split(",");
        int[] ints = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            ints[i] = Integer.valueOf(tokens[i]);
        }
        return ints;
    }

    /**
     * Split the specified value to a short array.
     *
     * @param value value
     * @return the specified value split to a short array
     */
    static short[] splitToShortArray(final String value) {
        String[] tokens = value.split(",");
        short[] shorts = new short[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            shorts[i] = Short.valueOf(tokens[i]);
        }
        return shorts;
    }

    /**
     * Split the specified value to a float array.
     *
     * @param value value
     * @return the specified value split to a float array
     */
    static float[] splitToFloatArray(final String value) {
        String[] tokens = value.split(",");
        float[] floats = new float[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            floats[i] = Float.valueOf(tokens[i]);
        }
        return floats;
    }
}
