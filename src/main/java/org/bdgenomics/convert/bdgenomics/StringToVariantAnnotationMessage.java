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

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.slf4j.Logger;

/**
 * Convert String to VariantAnnotationMessage.
 */
final class StringToVariantAnnotationMessage extends AbstractConverter<String, VariantAnnotationMessage> {

    /** Map of variant annotation messages keyed by name and message code. */
    private static final Map<String, VariantAnnotationMessage> MESSAGES;

    static {
        Map<String, VariantAnnotationMessage> messages = new HashMap<String, VariantAnnotationMessage>();
        // add name --> message mapping
        for (VariantAnnotationMessage message : VariantAnnotationMessage.values()) {
            messages.put(message.name(), message);
        }
        // and message code --> message mapping
        messages.put("E1", VariantAnnotationMessage.ERROR_CHROMOSOME_NOT_FOUND);
        messages.put("E2", VariantAnnotationMessage.ERROR_OUT_OF_CHROMOSOME_RANGE);
        messages.put("W1", VariantAnnotationMessage.WARNING_REF_DOES_NOT_MATCH_GENOME);
        messages.put("W2", VariantAnnotationMessage.WARNING_SEQUENCE_NOT_AVAILABLE);
        messages.put("W3", VariantAnnotationMessage.WARNING_TRANSCRIPT_INCOMPLETE);
        messages.put("W4", VariantAnnotationMessage.WARNING_TRANSCRIPT_MULTIPLE_STOP_CODONS);
        messages.put("W5", VariantAnnotationMessage.WARNING_TRANSCRIPT_NO_START_CODON);
        messages.put("I1", VariantAnnotationMessage.INFO_REALIGN_3_PRIME);
        messages.put("I2", VariantAnnotationMessage.INFO_COMPOUND_ANNOTATION);
        messages.put("I3", VariantAnnotationMessage.INFO_NON_REFERENCE_ANNOTATION);

        MESSAGES = ImmutableMap.copyOf(messages);
    }


    /**
     * Package private constructor.
     */
    StringToVariantAnnotationMessage() {
        super(String.class, VariantAnnotationMessage.class);
    }


    @Override
    public VariantAnnotationMessage convert(final String value,
                                            final ConversionStringency stringency,
                                            final Logger logger) throws ConversionException {
        if (value == null) {
            warnOrThrow(value, "must not be null", null, stringency, logger);
            return null;
        }
        VariantAnnotationMessage message = MESSAGES.get(value);
        if (message == null) {
            warnOrThrow(value, "incorrectly formatted VariantAnnotationMessage", null, stringency, logger);
        }
        return message;
    }
}
