package com.freeway.image.combiner.painter;

import com.freeway.image.combiner.element.CombineElement;
import com.freeway.image.combiner.element.ImageElement;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description
 */
public class ImagePainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) throws Exception {

        //强制转成子类
        ImageElement imageElement = (ImageElement)element;

        //读取元素图
        BufferedImage image = imageElement.getImage();

        //计算宽高
        int width = 0;
        int height = 0;

        switch (imageElement.getZoomMode()) {
            case Origin:
                width = image.getWidth();
                height = image.getHeight();
                break;
            case Width:
                width = imageElement.getWidth();
                height = image.getHeight() * width / image.getWidth();
                break;
            case Height:
                height = imageElement.getHeight();
                width = image.getWidth() * height / image.getHeight();
                break;
            case WidthHeight:
                height = imageElement.getHeight();
                width = imageElement.getWidth();
                break;
        }

        //设置圆角
        if (imageElement.getRoundCorner() != null) {
            image = makeRoundCorner(image, width, height, imageElement.getRoundCorner());
        }

        //判断是否设置居中
        if (imageElement.isCenter()) {
            int centerX = (canvasWidth - width) / 2;
            imageElement.setX(centerX);
        }

        //设置透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, imageElement.getAlpha()));

        //将元素图绘制到画布
        g.drawImage(image, imageElement.getX(), imageElement.getY(), width, height, null);
    }

    private BufferedImage makeRoundCorner(BufferedImage srcImage, int width, int height, int radius) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRoundRect(0, 0, width, height, radius, radius);
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        return image;
    }
}

