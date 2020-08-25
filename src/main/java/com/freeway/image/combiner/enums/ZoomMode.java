package com.freeway.image.combiner.enums;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description 缩放模式枚举
 **/

public enum ZoomMode {
    /**
     * 原始比例，不缩放
     */
    Origin,
    /**
     * 指定宽度，高度按比例
     */
    Width,
    /**
     * 指定高度，宽度按比例
     */
    Height,
    /**
     * 指定高度和宽度，强制缩放
     */
    WidthHeight;
}
