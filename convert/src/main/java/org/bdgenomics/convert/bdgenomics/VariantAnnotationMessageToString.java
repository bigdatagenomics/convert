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

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.VariantAnnotationMessage;

import org.slf4j.Logger;

/**
 * Convert VariantAnnotationMessage to String.
 */
final class VariantAnnotationMessageToString extends AbstractConverter<VariantAnnotationMessage, String> {

    /**
     * Package private constructor.
     */
    VariantAnnotationMessageToString() {
        super(VariantAnnotationMessage.class, String.class);
    }


    @Override
    public String convert(final VariantAnnotationMessage variantAnnotationMessage,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (variantAnnotationMessage == null) {
            warnOrThrow(variantAnnotationMessage, "must not be null", null, stringency, logger);
            return null;
        }
        // use message code if available
        switch (variantAnnotationMessage) {
            case ERROR_CHROMOSOME_NOT_FOUND:
                return "E1";
            case ERROR_OUT_OF_CHROMOSOME_RANGE:
                return "E2";
            case WARNING_REF_DOES_NOT_MATCH_GENOME:
                return "W1";
            case WARNING_SEQUENCE_NOT_AVAILABLE:
                return "W2";
            case WARNING_TRANSCRIPT_INCOMPLETE:
                return "W3";
            case WARNING_TRANSCRIPT_MULTIPLE_STOP_CODONS:
                return "W4";
            case WARNING_TRANSCRIPT_NO_START_CODON:
                return "W5";
            case INFO_REALIGN_3_PRIME:
                return "I1";
            case INFO_COMPOUND_ANNOTATION:
                return "I2";
            case INFO_NON_REFERENCE_ANNOTATION:
                return "I3";
            default:
                return variantAnnotationMessage.toString();
        }
    }
}
