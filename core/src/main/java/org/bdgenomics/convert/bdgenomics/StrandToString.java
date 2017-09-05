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

import org.bdgenomics.formats.avro.Strand;

import org.slf4j.Logger;

/**
 * Convert Strand to String.
 */
final class StrandToString extends AbstractConverter<Strand, String> {

    /**
     * Package private constructor.
     */
    StrandToString() {
        super(Strand.class, String.class);
    }


    @Override
    public String convert(final Strand strand,
                          final ConversionStringency stringency,
                          final Logger logger) throws ConversionException {
        if (strand == null) {
            warnOrThrow(strand, "must not be null", null, stringency, logger);
            return null;
        }
        switch (strand) {
            case FORWARD:
                return "+";
            case REVERSE:
                return "-";
            case INDEPENDENT:
                return ".";
            case UNKNOWN:
                return "?";
            default:
                return "";
        }
    }
}
