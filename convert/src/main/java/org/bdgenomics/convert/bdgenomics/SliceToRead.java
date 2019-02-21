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

import org.bdgenomics.formats.avro.Read;
import org.bdgenomics.formats.avro.Slice;

import org.slf4j.Logger;

/**
 * Convert Slice to Read.
 */
final class SliceToRead extends AbstractConverter<Slice, Read> {

    /**
     * Package private constructor.
     */
    SliceToRead() {
        super(Slice.class, Read.class);
    }


    @Override
    public Read convert(final Slice slice,
                        final ConversionStringency stringency,
                        final Logger logger) throws ConversionException {
        if (slice == null) {
            warnOrThrow(slice, "must not be null", null, stringency, logger);
            return null;
        }
        return Read.newBuilder()
            .setName(slice.getName())
            .setDescription(slice.getDescription())
            .setAlphabet(slice.getAlphabet())
            .setSequence(slice.getSequence())
            .setLength(slice.getLength())
            .setQualityScores(unknownQualityScores(slice.getLength() == null ? 0 : slice.getLength().intValue()))
            .setAttributes(slice.getAttributes())
            .build();
    }

    /**
     * Return unknown quality scores ("B") for a slice with the specified length.
     *
     * @param length slice length
     * @return unknown quality scores ("B") for a slice with the specified length
     */
    static String unknownQualityScores(final int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("B");
        }
        return sb.toString();
    }
}
