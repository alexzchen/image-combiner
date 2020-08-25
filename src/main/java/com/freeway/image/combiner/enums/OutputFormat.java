package com.freeway.image.combiner.enums;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/24
 * @Description
 */
public enum OutputFormat {
    JPG("jpg"),
    PNG("png"),
    BMP("bmp");

    public final String name;

    OutputFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
