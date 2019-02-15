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

import javax.annotation.concurrent.Immutable;

import com.google.protobuf.Descriptors;
import ga4gh.Common;
import org.bdgenomics.convert.AbstractConverter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;

import java.util.*;

/**
 * Convert map of attributes to GA4GH attributes.
 */
@Immutable
final class MapToGa4ghAttributes extends AbstractConverter<java.util.Map<String, String>, ga4gh.Common.Attributes> {

    /**
     * Convert bdg-formats Strand to GA4GH Strand.
     */
    MapToGa4ghAttributes() {
        super(java.util.Map.class, ga4gh.Common.Attributes.class);
    }


    @Override
    public ga4gh.Common.Attributes convert(final java.util.Map<String, String> attributes,
                                       final ConversionStringency stringency,
                                       final Logger logger) throws ConversionException {

        if (attributes == null) {
            warnOrThrow(attributes, "must not be null", null, stringency, logger);
            return null;
        }

        Map<String, Common.AttributeValueList> map = new HashMap<String, Common.AttributeValueList>();
        Iterator<Map.Entry<String, String>> entries = attributes.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();

            Common.AttributeValueList attributeValue = Common.AttributeValueList.newBuilder()
                    .addValues(Common.AttributeValue.newBuilder().setStringValue(entry.getValue())).build();

            map.put(entry.getKey(), attributeValue);

        }

        return ga4gh.Common.Attributes.newBuilder().putAllAttr(map).build();
    }
}
