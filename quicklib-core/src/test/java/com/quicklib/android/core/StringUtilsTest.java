package com.quicklib.android.core;

import com.quicklib.android.core.helper.StringUtils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class StringUtilsTest {
    @Test
    public void testFromStream() throws Exception {
        String input = "random stream content";
        InputStream is = new ByteArrayInputStream(input.getBytes());

        String output = StringUtils.fromStream(is);

        assertThat(output).isEqualTo(input);
    }


}