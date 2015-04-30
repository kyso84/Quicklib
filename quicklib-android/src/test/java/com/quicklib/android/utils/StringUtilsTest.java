/*
 * Copyright (C) 2015 Quicklib
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quicklib.android.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benoit Deschanel
 * @since 15-04-30
 * Copyright (C) 2015 Quicklib
 */
public class StringUtilsTest {

    private static final String TEST_STRING = "Go Habs Go!";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testFromStream_WithNullInputStream_ReturnsEmptyString() throws Exception {
        String result = StringUtils.fromStream(null);

        assertThat(result).isEqualTo(StringUtils.EMPTY_STRING);
    }

    @Test
    public void testFromStream_WithInputStream_ReturnsString() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(TEST_STRING.getBytes());

        String result = StringUtils.fromStream(inputStream);

        assertThat(result).isEqualTo(TEST_STRING);
    }

}
