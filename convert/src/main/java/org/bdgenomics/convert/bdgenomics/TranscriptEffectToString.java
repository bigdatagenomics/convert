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

import com.google.common.base.Joiner;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.TranscriptEffect;
import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.slf4j.Logger;

/**
 * Convert TranscriptEffect to String.
 */
final class TranscriptEffectToString extends AbstractConverter<TranscriptEffect, String> {

    /** Convert VariantAnnotationMessage to String. */
    private final Converter<VariantAnnotationMessage, String> variantAnnotationMessageConverter;


    /**
     * Convert TranscriptEffect to String.
     *
     * @param variantAnnotationMessageConverter convert VariantAnnotationMessage to String, must not be null
     */
    TranscriptEffectToString(final Converter<VariantAnnotationMessage, String> variantAnnotationMessageConverter) {
        super(TranscriptEffect.class, String.class);
        checkNotNull(variantAnnotationMessageConverter);
        this.variantAnnotationMessageConverter = variantAnnotationMessageConverter;
    }


    @Override
    public String convert(final TranscriptEffect transcriptEffect,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {

        if (transcriptEffect == null) {
            warnOrThrow(transcriptEffect, "must not be null", null, stringency, logger);
            return null;
        }

        List<String> messages = transcriptEffect.getMessages().stream().map(m -> variantAnnotationMessageConverter.convert(m, stringency, logger)).collect(toList());

        try {
            return Joiner.on("|").join(nullToEmpty(transcriptEffect.getAlternateAllele()),
                                       Joiner.on("&").join(transcriptEffect.getEffects()),
                                       "", //nullToEmpty(transcriptEffect.getImpact()),
                                       nullToEmpty(transcriptEffect.getGeneName()),
                                       nullToEmpty(transcriptEffect.getGeneId()),
                                       nullToEmpty(transcriptEffect.getFeatureType()),
                                       nullToEmpty(transcriptEffect.getFeatureId()),
                                       nullToEmpty(transcriptEffect.getBiotype()),
                                       nullToEmpty(transcriptEffect.getRank(), transcriptEffect.getTotal()),
                                       nullToEmpty(transcriptEffect.getTranscriptHgvs()),
                                       nullToEmpty(transcriptEffect.getProteinHgvs()),
                                       nullToEmpty(transcriptEffect.getCdnaPosition(), transcriptEffect.getCdnaLength()),
                                       nullToEmpty(transcriptEffect.getCodingSequencePosition(), transcriptEffect.getCodingSequenceLength()),
                                       nullToEmpty(transcriptEffect.getProteinPosition(), transcriptEffect.getProteinLength()),
                                       nullToEmpty(transcriptEffect.getDistance()),
                                       Joiner.on("&").join(messages));
        }
        catch (NumberFormatException e) {
            warnOrThrow(transcriptEffect, e.getMessage(), e, stringency, logger);
        }
        return null;
    }

    private static String nullToEmpty(final String s) {
        return s == null ? "" : s;
    }

    private static String nullToEmpty(final Integer i) {
        return i == null ? "" : i.toString();
    }

    private static String nullToEmpty(final Integer a, final Integer b) {
        if (a == null && b != null) {
            throw new NumberFormatException(String.format("invalid fraction ?/%d, missing numerator", b));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(nullToEmpty(a));
        if (b != null) {
            sb.append("/");
            sb.append(b.toString());
        }
        return sb.toString();
    }
}
