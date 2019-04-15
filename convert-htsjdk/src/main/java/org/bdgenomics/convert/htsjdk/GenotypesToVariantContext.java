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

import java.util.List;

import com.google.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import htsjdk.variant.variantcontext.VariantContext;

import htsjdk.variant.vcf.VCFHeader;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.bdgenomics.formats.avro.Genotype;

import org.slf4j.Logger;

/**
 * Convert a list of Genotypes to a VariantContext.
 */
public final class GenotypesToVariantContext extends AbstractConverter<List<Genotype>, VariantContext> {

    /** Header. */
    private final VCFHeader header;


    /**
     * Create a new list of Genotypes to VariantContext converter with the specified header.
     *
     * @param header header, must not be null
     */
    @Inject
    public GenotypesToVariantContext(@Assisted final VCFHeader header) {
        super(List.class, VariantContext.class);

        checkNotNull(header);
        this.header = header;
    }


    @Override
    public VariantContext convert(final List<Genotype> genotypes,
                                  final ConversionStringency stringency,
                                  final Logger logger) throws ConversionException {

        if (genotypes == null) {
            warnOrThrow(genotypes, "must not be null", null, stringency, logger);
            return null;
        }
        if (genotypes.isEmpty()) {
            warnOrThrow(genotypes, "must not be empty", null, stringency, logger);
            return null;
        }

        return null;
    }
}
