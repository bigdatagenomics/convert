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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ga4gh.Common;

import org.bdgenomics.convert.Converter;
import org.bdgenomics.convert.ConversionException;
import org.bdgenomics.convert.ConversionStringency;

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for Ga4ghAttributesToMap.
 */
public final class Ga4ghAttributesToMapTest {
    private final Logger logger = LoggerFactory.getLogger(Ga4ghAttributesToMapTest.class);
    private Converter<ga4gh.Common.Attributes, Map<String, String>> attributesConverter;

    @Before
    public void setUp() {
        attributesConverter = new Ga4ghAttributesToMap();
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

        Map<String, String> expected =  new java.util.HashMap<String, String>();
        expected.put("thickStrand", "10");
        expected.put("blockCount", "1");
        expected.put("blockSizes", "10,100,2,4,");

        Map<String, Common.AttributeValueList> map = new HashMap<String, Common.AttributeValueList>();
        Iterator<Map.Entry<String, String>> entries = expected.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();

            Common.AttributeValueList attributeValue = Common.AttributeValueList.newBuilder()
                    .addValues(Common.AttributeValue.newBuilder().setStringValue(entry.getValue())).build();

            map.put(entry.getKey(), attributeValue);
        }
        Common.Attributes attributes = ga4gh.Common.Attributes.newBuilder().putAllAttr(map).build();
        assertEquals(expected, attributesConverter.convert(attributes, ConversionStringency.STRICT, logger));
    }
}
