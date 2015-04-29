package com.quicklib.android.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by benoit on 15-04-25.
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
