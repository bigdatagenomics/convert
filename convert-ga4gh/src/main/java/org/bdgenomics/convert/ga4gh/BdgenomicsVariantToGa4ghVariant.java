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
package org.bdgenomics.convert.ga4gh;

import java.util.Arrays;

import javax.annotation.concurrent.Immutable;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

/**
 * Convert bdg-formats Variant to GA4GH Variant.
 */
@Immutable
public class BdgenomicsVariantToGa4ghVariant extends AbstractConverter<org.bdgenomics.formats.avro.Variant, ga4gh.Variants.Variant> {

    /**
     * Convert bdg-formats Variant to GA4GH Variant.
     */
    BdgenomicsVariantToGa4ghVariant() {
        super(org.bdgenomics.formats.avro.Variant.class, ga4gh.Variants.Variant.class);
    }

    @Override
    public ga4gh.Variants.Variant convert(final org.bdgenomics.formats.avro.Variant variant,
                                          final ConversionStringency stringency,
                                          final Logger logger) throws ConversionException {
        if (variant == null) {
            warnOrThrow(variant, "must not be null", null, stringency, logger);
            return null;
        }

        ga4gh.Variants.Variant.Builder builder = ga4gh.Variants.Variant.newBuilder();

        if (variant.getNames() != null && !variant.getNames().isEmpty()) {
            builder.addAllNames(variant.getNames());
        }
        if (variant.getContigName() != null) {
            builder.setReferenceName(variant.getContigName());
        }
        if (variant.getStart() != null) {
            builder.setStart(variant.getStart());
        }
        if (variant.getEnd() != null) {
            builder.setEnd(variant.getEnd());
        }
        if (variant.getReferenceAllele() != null) {
            builder.setReferenceBases(variant.getReferenceAllele());
        }
        if (variant.getAlternateAllele() != null) {
            builder.addAllAlternateBases(Arrays.asList(variant.getAlternateAllele()));
        }
        if (variant.getFiltersFailed() != null && !variant.getFiltersFailed().isEmpty()) {
            builder.addAllFiltersFailed(variant.getFiltersFailed());
        }
        if (variant.getFiltersPassed() != null) {
            builder.setFiltersPassed(variant.getFiltersPassed());
        }
        if (variant.getFiltersApplied() != null) {
            builder.setFiltersPassed(variant.getFiltersApplied());
        }

        return builder.build();
    }
}
