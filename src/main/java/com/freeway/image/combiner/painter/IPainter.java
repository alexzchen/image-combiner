package com.freeway.image.combiner.painter;

import com.freeway.image.combiner.element.CombineElement;

import java.awt.*;
import java.io.IOException;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description
 */
public interface IPainter {
    void draw(Graphics2D g, CombineElement element, int canvasWidth) throws IOException, Exception;
}
