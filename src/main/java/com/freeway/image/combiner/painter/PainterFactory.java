package com.freeway.image.combiner.painter;

import com.freeway.image.combiner.element.CombineElement;
import com.freeway.image.combiner.element.ImageElement;
import com.freeway.image.combiner.element.TextElement;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description
 */
public class PainterFactory {
    private static ImagePainter imagePainter;
    private static TextPainter textPainter;

    public static IPainter createInstance(CombineElement element) throws Exception {

        //考虑到性能，这里用单件，先不lock了
        if (element instanceof ImageElement) {
            if (imagePainter == null) {
                imagePainter = new ImagePainter();
            }
            return imagePainter;
        } else if (element instanceof TextElement) {
            if (textPainter == null) {
                textPainter = new TextPainter();
            }
            return textPainter;
        } else {
            throw new Exception("不支持的Painter类型");
        }
    }
}
