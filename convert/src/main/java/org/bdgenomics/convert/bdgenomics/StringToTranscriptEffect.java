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
package org.bdgenomics.convert.bdgenomics;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.google.common.base.Splitter;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Impact;
import org.bdgenomics.formats.avro.TranscriptEffect;
import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.slf4j.Logger;

/**
 * Convert String to TranscriptEffect.
 */
final class StringToTranscriptEffect extends AbstractConverter<String, TranscriptEffect> {

    /** Convert String to Impact. */
    private final Converter<String, Impact> impactConverter;

    /** Convert String to VariantAnnotationMessage. */
    private final Converter<String, VariantAnnotationMessage> variantAnnotationMessageConverter;


    /**
     * Convert String to TranscriptEffect.
     *
     * @param impactConverter convert String to Impact, must not be null
     * @param variantAnnotationMessageConverter convert String to VariantAnnotationMessage, must not be null
     */
    StringToTranscriptEffect(final Converter<String, Impact> impactConverter, final Converter<String, VariantAnnotationMessage> variantAnnotationMessageConverter) {
        super(String.class, TranscriptEffect.class);
        checkNotNull(impactConverter);
        checkNotNull(variantAnnotationMessageConverter);
        this.impactConverter = impactConverter;
        this.variantAnnotationMessageConverter = variantAnnotationMessageConverter;
    }


    @Override
    public TranscriptEffect convert(final String value,
                                    final ConversionStringency stringency,
                                    final Logger logger) throws ConversionException {
        if (value == null) {
            warnOrThrow(value, "must not be null", null, stringency, logger);
            return null;
        }

        List<String> tokens = Splitter.on("|").splitToList(value);
        if (tokens.size() != 16) {
            warnOrThrow(value,
                        "value must have sixteen fields ( Allele | Annotation | Annotation_Impact | "
                        + "Gene_Name | Gene_ID | Feature_Type | Feature_ID | Transcript_BioType | Rank / Total | HGVS.c | HGVS.p | "
                        + "cDNA.pos / cDNA.length | CDS.pos / CDS.length | AA.pos / AA.length | Distance | MESSAGES / WARNINGS / INFO)",
                        null,
                        stringency,
                        logger);
            return null;
        }

        TranscriptEffect transcriptEffect = null;
        try {
            String alternateAllele = emptyToNull(tokens.get(0));
            List<String> effects = splitEffects(tokens.get(1));
            String impact = emptyToNull(tokens.get(2));
            String geneName = emptyToNull(tokens.get(3));
            String geneId = emptyToNull(tokens.get(4));
            String featureType = emptyToNull(tokens.get(5));
            String featureId = emptyToNull(tokens.get(6));
            String biotype = emptyToNull(tokens.get(7));
            Integer rank = numerator(tokens.get(8));
            Integer total = denominator(tokens.get(8));
            String transcriptHgvs = emptyToNull(tokens.get(9));
            String proteinHgvs = emptyToNull(tokens.get(10));
            Integer cdnaPosition = numerator(tokens.get(11));
            Integer cdnaLength = denominator(tokens.get(11));
            Integer cdsPosition = numerator(tokens.get(12));
            Integer cdsLength = denominator(tokens.get(12));
            Integer proteinPosition = numerator(tokens.get(13));
            Integer proteinLength = denominator(tokens.get(13));
            Integer distance = emptyToNullInteger(tokens.get(14));
            List<VariantAnnotationMessage> messages = splitMessages(tokens.get(15), stringency, logger);

            transcriptEffect = TranscriptEffect.newBuilder()
                .setAlternateAllele(alternateAllele)
                .setEffects(effects)
                .setImpact(impact == null ? null : impactConverter.convert(impact, stringency, logger))
                .setGeneName(geneName)
                .setGeneId(geneId)
                .setFeatureType(featureType)
                .setFeatureId(featureId)
                .setBiotype(biotype)
                .setRank(rank)
                .setTotal(total)
                .setTranscriptHgvs(transcriptHgvs)
                .setProteinHgvs(proteinHgvs)
                .setCdnaPosition(cdnaPosition)
                .setCdnaLength(cdnaLength)
                .setCodingSequencePosition(cdsPosition)
                .setCodingSequenceLength(cdsLength)
                .setProteinPosition(proteinPosition)
                .setProteinLength(proteinLength)
                .setDistance(distance)
                .setMessages(messages)
                .build();
        }
        catch (NumberFormatException e) {
            warnOrThrow(value, "could not parse transcript effect", e, stringency, logger);
        }
        return transcriptEffect;
    }

    /**
     * Split the specified string into a list of effects.
     *
     * @param s string to split
     * @return the specified string split into a list of effects
     */
    List<String> splitEffects(final String s) {
        return Splitter.on("&").omitEmptyStrings().splitToList(s);
    }

    /**
     * Split the specified string into a list of variant annotation messages.
     *
     * @param s string to split
     * @param stringency conversion stringency, must not be null
     * @param logger logger, must not be null
     * @return the specified string split into a list of variant annotation messages
     * @throws ConversionException if conversion fails and the specified conversion stringency is strict
     * @throws NullPointerException if either conversion stringency or logger are null
     */
    List<VariantAnnotationMessage> splitMessages(final String s,
                                                 final ConversionStringency stringency,
                                                 final Logger logger) throws ConversionException {
        return Splitter
            .on("&")
            .omitEmptyStrings()
            .splitToList(s)
            .stream()
            .map(m -> variantAnnotationMessageConverter.convert(m, stringency, logger))
            .collect(toList());
    }

    /**
     * Return null if the specified string is empty.
     *
     * @param s string
     * @return null if the specified string is empty
     */
    static String emptyToNull(final String s) {
        return "".equals(s) ? null : s;
    }

    /**
     * Parse the specified string into an integer, returning null if the string is empty.
     *
     * @param s string to parse
     * @return the specified string parsed into an integer, or null if the string is empty
     */
    static Integer emptyToNullInteger(final String s) {
        return "".equals(s) ? null : Integer.parseInt(s);
    }

    /**
     * Parse the specified string as a fraction and return the numerator, if any.
     *
     * @param s string to parse
     * @return the numerator from the specified string parsed as a fraction, or null if the string is empty
     */
    static Integer numerator(final String s) {
        if ("".equals(s)) {
            return null;
        }
        String[] tokens = s.split("/");
        return emptyToNullInteger(tokens[0]);
    }

    /**
     * Parse the specified string as a fraction and return the denominator, if any.
     *
     * @param s string to parse
     * @return the denominator from the specified string parsed as a fraction,
     *    or null if the string is empty or if the fraction has no denominator
     */
    static Integer denominator(final String s) {
        if ("".equals(s)) {
            return null;
        }
        String[] tokens = s.split("/");
        return (tokens.length < 2) ? null : emptyToNullInteger(tokens[1]);
    }
}
