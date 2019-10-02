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

import org.bdgenomics.formats.avro.Alignment;

import org.slf4j.Logger;

/**
 * Convert Alignment to htsjdk SAMRecord.
 */
public final class AlignmentToSamRecord extends AbstractConverter<Alignment, SAMRecord> {

    /** Regex to capture attributes. */
    private static final Pattern ATTRIBUTE = Pattern.compile("([^:]{2,4}):([AifZHB]):(.*)");

    /** Regex to capture array attribute types. */
    private static final Pattern ARRAY_ATTRIBUTE = Pattern.compile("([cCiIsSf]{1},)(.*)");
    
    /** Header. */
    private final SAMFileHeader header;


    /**
     * Create a new Alignment to htsjdk SAMRecord converter with the specified header.
     *
     * @param header header, must not be null
     */
    @Inject
    public AlignmentToSamRecord(@Assisted final SAMFileHeader header) {
        super(Alignment.class, SAMRecord.class);

        checkNotNull(header);
        this.header = header;
    }


    @Override
    public SAMRecord convert(final Alignment alignment,
                             final ConversionStringency stringency,
                             final Logger logger) throws ConversionException {

        if (alignment == null) {
            warnOrThrow(alignment, "must not be null", null, stringency, logger);
            return null;
        }

        SAMRecord builder = new SAMRecord(header);
        builder.setReadName(alignment.getReadName());
        builder.setReadString(alignment.getSequence());

        if (alignment.getQualityScores() == null) {
            builder.setBaseQualityString("*");
        }
        else {
            builder.setBaseQualityString(alignment.getQualityScores());
        }

        String readGroupId = alignment.getReadGroupId();
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

        if (alignment.getMateReferenceName() != null) {
            builder.setMateReferenceName(alignment.getMateReferenceName());
        }
        if (alignment.getMateAlignmentStart() != null) {
            builder.setMateAlignmentStart(alignment.getMateAlignmentStart().intValue() + 1);
        }
        if (alignment.getInsertSize() != null) {
            builder.setInferredInsertSize(alignment.getInsertSize().intValue());
        }

        if (alignment.getReadPaired() != null) {
            boolean readPaired = alignment.getReadPaired();
            builder.setReadPairedFlag(readPaired);

            if (readPaired) {
                if (alignment.getMateNegativeStrand() != null) {
                    builder.setMateNegativeStrandFlag(alignment.getMateNegativeStrand());
                }
                if (alignment.getMateMapped() != null) {
                    builder.setMateUnmappedFlag(!alignment.getMateMapped());
                }
                if (alignment.getProperPair() != null) {
                    builder.setProperPairFlag(alignment.getProperPair());
                }
                if (alignment.getReadInFragment() != null) {
                    builder.setFirstOfPairFlag(alignment.getReadInFragment() == 0);
                    builder.setSecondOfPairFlag(alignment.getReadInFragment() == 1);
                }
            }
        }

        if (alignment.getDuplicateRead() != null) {
            builder.setDuplicateReadFlag(alignment.getDuplicateRead());
        }

        if (alignment.getReadMapped() != null) {
            boolean readMapped = alignment.getReadMapped();
            builder.setReadUnmappedFlag(!readMapped);

            if (alignment.getReadNegativeStrand() != null) {
                builder.setReadNegativeStrandFlag(alignment.getReadNegativeStrand());
            }

            if (readMapped) {
                if (alignment.getReferenceName() == null) {
                    warnOrThrow(alignment, "referenceName must not be null if read aligned", null, stringency, logger);
                }
                else {
                    builder.setReferenceName(alignment.getReferenceName());
                }

                if (alignment.getCigar() != null) {
                    builder.setCigarString(alignment.getCigar());
                }
                if (alignment.getPrimaryAlignment() != null) {
                    builder.setNotPrimaryAlignmentFlag(!alignment.getPrimaryAlignment());
                }
                if (alignment.getSupplementaryAlignment() != null) {
                    builder.setSupplementaryAlignmentFlag(alignment.getSupplementaryAlignment());
                }
                if (alignment.getStart() != null) {
                    builder.setAlignmentStart(alignment.getStart().intValue() + 1);
                }
                if (alignment.getMappingQuality() != null) {
                    builder.setMappingQuality(alignment.getMappingQuality());
                }
            }
            else {
                builder.setMappingQuality(0);
            }
        }

        if (alignment.getFailedVendorQualityChecks() != null) {
            builder.setReadFailsVendorQualityCheckFlag(alignment.getFailedVendorQualityChecks());
        }
        if (alignment.getMismatchingPositions() != null) {
            builder.setAttribute("MD", alignment.getMismatchingPositions());
        }
        if (alignment.getOriginalQualityScores() != null) {
            builder.setOriginalBaseQualities(SAMUtils.fastqToPhred(alignment.getOriginalQualityScores()));
        }
        if (alignment.getOriginalCigar() != null) {
            builder.setAttribute("OC", alignment.getOriginalCigar());
        }
        if (alignment.getOriginalStart() != null) {
            builder.setAttribute("OP", alignment.getOriginalStart().intValue() + 1);
        }

        if (alignment.getAttributes() != null) {
            String[] tokens = alignment.getAttributes().split("\t");
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
                        warnOrThrow(alignment, "invalid attribute type " + tagType, null, stringency, logger);
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
