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
import htsjdk.variant.vcf.VCFHeaderLine;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

/**
 * Convert a VCFHeader to a list of VCFHeaderLines.
 */
public final class VcfHeaderToVcfHeaderLines extends AbstractConverter<VCFHeader, List<VCFHeaderLine>> {

    /**
     * Create a new VCFHeader to a list of VCFHeaderLines converter.
     */
    public VcfHeaderToVcfHeaderLines() {
        super(VCFHeader.class, VCFHeaderLine.class);
    }


    @Override
    public List<VCFHeaderLine> convert(final VCFHeader header,
                                       final ConversionStringency stringency,
                                       final Logger logger) throws ConversionException {

        if (header == null) {
            warnOrThrow(header, "must not be null", null, stringency, logger);
            return null;
        }

        List<VCFHeaderLine> headerLines = new ArrayList<VCFHeaderLine>();
        headerLines.addAll(header.getFilterLines());
        headerLines.addAll(header.getFormatHeaderLines());
        headerLines.addAll(header.getInfoHeaderLines());
        headerLines.addAll(header.getOtherHeaderLines());
        return headerLines;
    }
}
