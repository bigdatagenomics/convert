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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import ga4gh.Common;
import org.junit.Before;
import org.junit.Test;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Unit test for MapToGa4ghAttributes.
 */
public final class MapToGa4ghAttributesTest {
    private final Logger logger = LoggerFactory.getLogger(MapToGa4ghAttributesTest.class);
    private Converter<java.util.Map<String, String>, ga4gh.Common.Attributes> attributesConverter;


    @Before
    public void setUp() {
        attributesConverter = new MapToGa4ghAttributes();
    }

    @Test
    public void testConstructor() {
        assertNotNull(attributesConverter);
    }

    @Test(expected=ConversionException.class)
    public void testConvertNullStrict() {
        attributesConverter.convert(null, ConversionStringency.STRICT, logger);
    }

    @Test
    public void testConvertNullLenient() {
        assertNull(attributesConverter.convert(null, ConversionStringency.LENIENT, logger));
    }

    @Test
    public void testConvertNullSilent() {
        assertNull(attributesConverter.convert(null, ConversionStringency.SILENT, logger));
    }

    @Test
    public void testConvert() {
        Map<String, String> attributes =  new java.util.HashMap<String, String>();
        attributes.put("thickStrand", "10");
        attributes.put("blockCount", "1");
        attributes.put("blockSizes", "10,100,2,4,");

        Map<String, Common.AttributeValueList> map = new HashMap<String, Common.AttributeValueList>();

        Iterator<Map.Entry<String, String>> entries = attributes.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();

            Common.AttributeValueList attributeValue = Common.AttributeValueList.newBuilder()
                    .addValues(Common.AttributeValue.newBuilder().setStringValue(entry.getValue())).build();

            map.put(entry.getKey(), attributeValue);

        }

        Common.Attributes expected = ga4gh.Common.Attributes.newBuilder().putAllAttr(map).build();

        assertEquals(expected, attributesConverter.convert(attributes, ConversionStringency.STRICT, logger));
    }
}
