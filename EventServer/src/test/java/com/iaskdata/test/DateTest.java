package com.iaskdata.test;

import java.text.SimpleDateFormat;

import org.junit.Test;

public class DateTest {

    @Test
    public void test() throws Exception {
        String when = "٢٠١٥-٠١-٢٣ ٢٠:٠٩:١٥";
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println(sdf.format(sdf.parse(when)));
    }
}
