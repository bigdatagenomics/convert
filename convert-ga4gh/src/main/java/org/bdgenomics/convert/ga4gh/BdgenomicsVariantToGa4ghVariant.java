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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import java.util.Arrays;
import javax.annotation.concurrent.Immutable;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

@Immutable
public class BdgenomicsVariantToGa4ghVariant extends AbstractConverter<org.bdgenomics.formats.avro.Variant, ga4gh.Variants.Variant> {

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

        return ga4gh.Variants.Variant.newBuilder()
                .addAllNames(variant.getNames())
                .setReferenceName(variant.getContigName())
                .setStart(variant.getStart())
                .setEnd(variant.getEnd())
                .setReferenceBases(variant.getReferenceAllele())
                .addAllAlternateBases(Arrays.asList(variant.getAlternateAllele() ))
                .addAllFiltersFailed(variant.getFiltersFailed())
                .setFiltersPassed(variant.getFiltersPassed())
                .setFiltersApplied(variant.getFiltersApplied())
                .build();
    }
}
