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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import ga4gh.Common;

import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

/**
 * Convert GA4GH attributes to map of attributes.
 */
@Immutable
final class Ga4ghAttributesToMap extends AbstractConverter<ga4gh.Common.Attributes, Map<String, String>> {

    /**
     * Convert bdg-formats Strand to GA4GH Strand.
     */
    Ga4ghAttributesToMap() {
        super(ga4gh.Common.Attributes.class, Map.class);
    }


    @Override
    public Map<String, String> convert(final ga4gh.Common.Attributes attributes,
                                       final ConversionStringency stringency,
                                       final Logger logger) throws ConversionException {

        if (attributes == null) {
            warnOrThrow(attributes, "must not be null", null, stringency, logger);
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        Iterator<Map.Entry<String, Common.AttributeValueList>> entries = attributes.getAttr().entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, Common.AttributeValueList> entry = entries.next();
            String attributeValue = entry.getValue().getValues(0).getStringValue();
            map.put(entry.getKey(), attributeValue);
        }
        return map;
    }
}
