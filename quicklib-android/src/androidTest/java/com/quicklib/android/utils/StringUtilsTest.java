package com.quicklib.android.utils;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by bdescha1 on 15-04-25.
 */
public class StringUtilsTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.Test
    public void testFromStream_WithNullInputStream_ReturnsEmptyString() throws Exception {
        String result = StringUtils.fromStream(null);

        assertThat(result).isEqualTo(StringUtils.EMPTY_STRING);
    }

    @org.junit.Test
    public void testFromStream_WithInputStream_ReturnsString() throws Exception {
        InputStream inputStream = mock(InputStream.class);

        String result = StringUtils.fromStream(inputStream);

        assertThat(result).isEqualTo(StringUtils.EMPTY_STRING);
    }

}