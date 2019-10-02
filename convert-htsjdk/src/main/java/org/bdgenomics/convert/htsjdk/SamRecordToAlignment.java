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

import htsjdk.samtools.SAMBinaryTagAndValue;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMReadGroupRecord;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMTag;
import htsjdk.samtools.SAMUtils;
import htsjdk.samtools.TextTagCodec;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Alignment;

import org.slf4j.Logger;

/**
 * Convert htsjdk SAMRecord to Alignment.
 */
public final class SamRecordToAlignment extends AbstractConverter<SAMRecord, Alignment> {

    /** Regex to capture the first cigar operation. */
    private static final Pattern FIRST_CIGAR_OPERATION = Pattern.compile("^([0-9]*)([A-Z]).*$");

    /** Regex to capture the last cigar operation. */
    private static final Pattern LAST_CIGAR_OPERATION = Pattern.compile("^.*([0-9]*)([A-Z])$");

    /** SAM text tag codec. */
    private static final TextTagCodec TAG_CODEC = new TextTagCodec();


    /**
     * Create a new htsjdk SAMRecord to Alignment converter.
     */
    public SamRecordToAlignment() {
        super(SAMRecord.class, Alignment.class);
    }


    @Override
    public Alignment convert(final SAMRecord samRecord,
                                   final ConversionStringency stringency,
                                   final Logger logger) throws ConversionException {

        if (samRecord == null) {
            warnOrThrow(samRecord, "must not be null", null, stringency, logger);
            return null;
        }

        Alignment.Builder builder = Alignment.newBuilder()
            .setReadName(samRecord.getReadName())
            .setSequence(samRecord.getReadString());

        String cigar = samRecord.getCigarString();
        if (cigar != null && !"*".equals(cigar)) {
            builder.setCigar(cigar);
            builder.setBasesTrimmedFromStart(startTrim(cigar));
            builder.setBasesTrimmedFromEnd(endTrim(cigar));
        }

        if (samRecord.getBaseQualityString() != "*") {
            builder.setQualityScores(samRecord.getBaseQualityString());
        }
        if (samRecord.getOriginalBaseQualities() != null) {
            builder.setOriginalQualityScores(SAMUtils.phredToFastq(samRecord.getOriginalBaseQualities()));
        }

        int readReference = samRecord.getReferenceIndex();
        if (readReference != SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX) {
            builder.setReferenceName(samRecord.getReferenceName());

            long start = (long) samRecord.getAlignmentStart();
            if (start < 1L) {
                warnOrThrow(samRecord, "alignment start must be greater than zero if read aligned", null, stringency, logger);
            }
            else {
                builder.setStart(start - 1L);

                long end = start - 1L + samRecord.getCigar().getReferenceLength();
                builder.setEnd(end);
            }

            int mappingQuality = samRecord.getMappingQuality();
            if (mappingQuality != SAMRecord.UNKNOWN_MAPPING_QUALITY) {
                builder.setMappingQuality(mappingQuality);
            }

            if (samRecord.getAttribute("OP") != null) {
                builder.setOriginalStart(samRecord.getIntegerAttribute("OP") - 1L);
                builder.setOriginalCigar(samRecord.getStringAttribute("OC"));
            }
        }

        builder.setReadMapped(!samRecord.getReadUnmappedFlag());
        builder.setReadNegativeStrand(samRecord.getReadNegativeStrandFlag());
        builder.setPrimaryAlignment(!samRecord.getNotPrimaryAlignmentFlag());
        builder.setSupplementaryAlignment(samRecord.getSupplementaryAlignmentFlag());

        int mateReferenceIndex = samRecord.getMateReferenceIndex();
        if (mateReferenceIndex != SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX) {
            builder.setMateReferenceName(samRecord.getMateReferenceName());

            long mateStart = (long) samRecord.getMateAlignmentStart();
            if (mateStart < 1L) {
                warnOrThrow(samRecord, "mate start must be greater than zero if mate aligned", null, stringency, logger);
            }
            else {
                builder.setMateAlignmentStart(mateStart - 1L);
            }
        }

        if (samRecord.getFlags() != 0) {
            if (samRecord.getReadPairedFlag()) {
                builder.setReadPaired(true);
                if (samRecord.getMateNegativeStrandFlag()) {
                    builder.setMateNegativeStrand(true);
                }
                if (!samRecord.getMateUnmappedFlag()) {
                    builder.setMateMapped(true);
                }
                if (samRecord.getProperPairFlag()) {
                    builder.setProperPair(true);
                }
                if (samRecord.getFirstOfPairFlag()) {
                    builder.setReadInFragment(0);
                }
                if (samRecord.getSecondOfPairFlag()) {
                    builder.setReadInFragment(1);
                }
            }
            if (samRecord.getDuplicateReadFlag()) {
                builder.setDuplicateRead(true);
            }
            if (samRecord.getReadFailsVendorQualityCheckFlag()) {
                builder.setFailedVendorQualityChecks(true);
            }
        }

        long insertSize = (long) samRecord.getInferredInsertSize();
        if (insertSize != 0L) {
            builder.setInsertSize(insertSize);
        }

        SAMReadGroupRecord readGroup = samRecord.getReadGroup();
        if (readGroup != null) {
            builder.setReadGroupId(readGroup.getReadGroupId());
            builder.setReadGroupSampleId(readGroup.getSample());
        }

        String md = samRecord.getStringAttribute("MD");
        if (md != null) {
            builder.setMismatchingPositions(md);
        }

        String attributes = encodeAttributes(getBinaryAttributes(samRecord));
        if (!attributes.isEmpty()) {
            builder.setAttributes(attributes);
        }

        return builder.build();
    }

    /**
     * Calculate the hard clipped trim in bases from the start of the specified cigar string.
     *
     * @param cigar cigar string
     * @return the hard clipped trim in bases from the start of the specified cigar string
     */
    static int startTrim(final String cigar) {
        int startTrim = 0;
        Matcher m = FIRST_CIGAR_OPERATION.matcher(cigar);
        if (m.matches()) {
            String operator = m.group(2);
            if ("H".equals(operator)) {
                startTrim = Integer.parseInt(m.group(1));
            }
        }
        return startTrim;
    }

    /**
     * Calculate the hard clipped trim in bases from the end of the specified cigar string.
     *
     * @param cigar cigar string
     * @return the hard clipped trim in bases from the end of the specified cigar string
     */
    static int endTrim(final String cigar) {
        int endTrim = 0;
        Matcher m = LAST_CIGAR_OPERATION.matcher(cigar);
        if (m.matches()) {
            String operator = m.group(2);
            if ("H".equals(operator)) {
                endTrim = Integer.parseInt(m.group(1));
            }
        }
        return endTrim;
    }

    /**
     * Return true if the specified attribute tag should be skipped.
     *
     * @param tag attribute tag
     * @return true if the specified attribute tag should be skipped
     */
    static boolean skipTag(final short tag) {
        return tag == SAMTag.MD.getBinaryTag() || tag == SAMTag.OC.getBinaryTag() || tag == SAMTag.OP.getBinaryTag() || tag == SAMTag.OQ.getBinaryTag();
    }

    /**
     * Return <code>SAMRecord.getBinaryAttributes()</code> via reflection.
     *
     * @param samRecord SAM record
     * @return <code>SAMRecord.getBinaryAttributes()</code> via reflection
     */
    static SAMBinaryTagAndValue getBinaryAttributes(final SAMRecord samRecord) {
        try {
            java.lang.reflect.Field f = samRecord.getClass().getDeclaredField("mAttributes");
            f.setAccessible(true);
            return (SAMBinaryTagAndValue) f.get(samRecord);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Return <code>TextTagCodec.encodeUnsignedArray(String, Object)</code> via reflection.
     *
     * @param tag tag
     * @param value value
     * @return <code>TextTagCodec.encodeUnsignedArray(String, Object)</code> via reflection
     */
    static String encodeUnsignedArray(final String tag, final Object value) {
        try {
            java.lang.reflect.Method m = TAG_CODEC.getClass().getDeclaredMethod("encodeUnsignedArray");
            m.setAccessible(true);
            return (String) m.invoke(TAG_CODEC, new Object[] { tag, value }, new Class<?>[] { String.class, Object.class });
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Encode the specified attribute and its child attributes to a string.
     *
     * @param attribute attribute
     * @return the specified attribute and its child attributes to a string
     */
    static String encodeAttributes(SAMBinaryTagAndValue attribute) {
        StringBuilder sb = new StringBuilder();
        while (attribute != null) {
            if (!skipTag(attribute.tag)) {
                final String encodedTag;
                if (attribute.isUnsignedArray()) {
                    encodedTag = encodeUnsignedArray(SAMTag.makeStringTag(attribute.tag), attribute.value);
                }
                else {
                    encodedTag = TAG_CODEC.encode(SAMTag.makeStringTag(attribute.tag), attribute.value);
                }
                sb.append(encodedTag);
                sb.append("\t");
            }
            attribute = attribute.getNext();
        }
        return sb.toString().trim();
    }
}
