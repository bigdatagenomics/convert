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

import java.util.ArrayList;
import java.util.List;

import htsjdk.variant.vcf.VCFHeader;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Sample;

import org.slf4j.Logger;

/**
 * Convert a VCFHeader to a list of Samples.
 */
public final class VcfHeaderToSamples extends AbstractConverter<VCFHeader, List<Sample>> {

    /**
     * Create a new VCFHeader to a list of Samples converter.
     */
    public VcfHeaderToSamples() {
        super(VCFHeader.class, List.class);
    }


    @Override
    public List<Sample> convert(final VCFHeader header,
                                final ConversionStringency stringency,
                                final Logger logger) throws ConversionException {

        if (header == null) {
            warnOrThrow(header, "must not be null", null, stringency, logger);
            return null;
        }

        List<Sample> samples = new ArrayList<Sample>();
        if (header.getGenotypeSamples() != null) {
            Sample.Builder builder = Sample.newBuilder();
            for (String sampleId : header.getGenotypeSamples()) {
                samples.add(builder.setId(sampleId).build());
            }
        }
        return samples;
    }
}
