package com.freeway.image.combiner;

import com.freeway.image.combiner.element.CombineElement;
import com.freeway.image.combiner.element.ImageElement;
import com.freeway.image.combiner.element.TextElement;
import com.freeway.image.combiner.enums.OutputFormat;
import com.freeway.image.combiner.enums.ZoomMode;
import com.freeway.image.combiner.painter.IPainter;
import com.freeway.image.combiner.painter.PainterFactory;
import com.freeway.image.combiner.painter.TextPainter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description
 */
public class ImageCombiner {
    private List<CombineElement> combineElements = new ArrayList<>();   //待绘制的元素集合
    private BufferedImage combinedImage;                                //合成后的图片对象
    private int canvasWidth;                                            //画布宽度
    private int canvasHeight;                                           //画布高度
    private OutputFormat outputFormat;                                  //输出图片格式

    /**
     * @param bgImageUrl   背景图片地址（画布以背景图宽高为基准）
     * @param outputFormat 输出图片格式
     */
    public ImageCombiner(String bgImageUrl, OutputFormat outputFormat) throws Exception {
        ImageElement bgImageElement = new ImageElement(bgImageUrl, 0, 0);
        this.combineElements.add(bgImageElement);
        this.canvasWidth = bgImageElement.getImage().getWidth();
        this.canvasHeight = bgImageElement.getImage().getHeight();
        this.outputFormat = outputFormat;
    }

    /**
     * @param bgImage      背景图片对象（画布以背景图宽高为基准）
     * @param outputFormat 输出图片格式
     */
    public ImageCombiner(BufferedImage bgImage, OutputFormat outputFormat) throws Exception {
        ImageElement bgImageElement = new ImageElement(bgImage, 0, 0);
        this.combineElements.add(bgImageElement);
        this.canvasWidth = bgImage.getWidth();
        this.canvasHeight = bgImage.getHeight();
        this.outputFormat = outputFormat;
    }


    /**
     * 合成图片，返回图片对象
     *
     * @throws Exception
     */
    public BufferedImage combine() throws Exception {
        combinedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combinedImage.createGraphics();

        //PNG要做透明度处理，否则背景图透明部分会变黑
        if(outputFormat == OutputFormat.PNG) {
            combinedImage = g.getDeviceConfiguration().createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
            g = combinedImage.createGraphics();
        }
        //抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //循环绘制
        for (CombineElement element : combineElements) {
            IPainter painter = PainterFactory.createInstance(element);
            painter.draw(g, element, canvasWidth);
        }
        g.dispose();

        return combinedImage;
    }

    /**
     * 计算多行文本高度（常用于确定后续元素的y坐标，如跟在商品描述下方的价格，商品描述行数不定，需动态计算多行总高度）
     *
     * @param textElement
     * @return
     */
    public int computeTextLineHeight(TextElement textElement) {
        TextPainter textPainter = new TextPainter();
        List<TextElement> textElements = textPainter.getBreakLineElements(textElement);
        return textElement.getLineHeight() * textElements.size();
    }

    /**
     * 计算文本宽度（常用于确定后续元素的x坐标，如跟在原价后面的打折价，原价位数不定，需动态计算总宽度）
     * @param textElement
     * @return
     */
    public int computeTextWidth(TextElement textElement) {
        TextPainter textPainter = new TextPainter();
        return textPainter.getFrontWidth(textElement.getText(), textElement.getFont());
    }


    /*****************对象输出相关方法*******************/

    /**
     * 获取合成后的图片对象
     *
     * @return
     */
    public BufferedImage getCombinedImage() {
        return combinedImage;
    }

    /**
     * 获取合成后的图片流
     *
     * @return
     * @throws Exception
     */
    public InputStream getCombinedImageStream() throws Exception {
        if (combinedImage != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(combinedImage, outputFormat.getName(), os);
            return new ByteArrayInputStream(os.toByteArray());
        } else {
            throw new Exception("尚未执行图片合成，无法输出文件流");
        }
    }

    /**
     * 保存合成后的图片
     *
     * @param filePath 完整保存路径，如 “d://123.jpg”
     * @throws IOException
     */
    public void save(String filePath) throws Exception {
        if (combinedImage != null) {
            ImageIO.write(combinedImage, outputFormat.getName(), new File(filePath));
        } else {
            throw new Exception("尚未执行图片合成，无法保存文件");
        }
    }


    /*****************创建和添加元素的辅助方法*******************/

    /**
     * 设置背景高斯模糊（毛玻璃效果）
     * @return
     */
    public void setBackgroundBlur(int blur){
        ImageElement bgElement = (ImageElement)combineElements.get(0);
        bgElement.setBlur(blur);
    }

    /**
     * 添加元素（图片或文本）
     *
     * @param element 图片或文本元素
     */
    public void addElement(CombineElement element) {
        this.combineElements.add(element);
    }

    /**
     * 添加图片元素
     *
     * @param imgUrl 图片url
     * @param x      x坐标
     * @param y      y坐标
     * @return
     */
    public ImageElement addImageElement(String imgUrl, int x, int y) throws Exception {
        ImageElement imageElement = new ImageElement(imgUrl, x, y);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加图片元素
     *
     * @param image 图片对象
     * @param x     x坐标
     * @param y     y坐标
     * @return
     */
    public ImageElement addImageElement(BufferedImage image, int x, int y) throws Exception {
        ImageElement imageElement = new ImageElement(image, x, y);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加图片元素
     *
     * @param imgUrl   图片rul
     * @param x        x坐标
     * @param y        y坐标
     * @param width    宽度
     * @param height   高度
     * @param zoomMode 缩放模式
     * @return
     */
    public ImageElement addImageElement(String imgUrl, int x, int y, int width, int height, ZoomMode zoomMode) {
        ImageElement imageElement = new ImageElement(imgUrl, x, y, width, height, zoomMode);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加图片元素
     *
     * @param image    图片对象
     * @param x        x坐标
     * @param y        y坐标
     * @param width    宽度
     * @param height   高度
     * @param zoomMode 缩放模式
     * @return
     */
    public ImageElement addImageElement(BufferedImage image, int x, int y, int width, int height, ZoomMode zoomMode) {
        ImageElement imageElement = new ImageElement(image, x, y, width, height, zoomMode);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加文本元素
     *
     * @param text 文本
     * @param font Font对象
     * @param x    x坐标
     * @param y    y坐标
     * @return
     */
    public TextElement addTextElement(String text, Font font, int x, int y) {
        TextElement textElement = new TextElement(text, font, x, y);
        this.combineElements.add(textElement);
        return textElement;
    }

    /**
     * 添加文本元素
     *
     * @param text     文本
     * @param fontSize 字体大小
     * @param x        x坐标
     * @param y        y坐标
     * @return
     */
    public TextElement addTextElement(String text, int fontSize, int x, int y) {
        TextElement textElement = new TextElement(text, fontSize, x, y);
        this.combineElements.add(textElement);
        return textElement;
    }

    /**
     * 添加文本元素
     *
     * @param text     文本
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param x        x坐标
     * @param y        y坐标
     * @return
     */
    public TextElement addTextElement(String text, String fontName, int fontSize, int x, int y) {
        TextElement textElement = new TextElement(text, fontName, fontSize, x, y);
        this.combineElements.add(textElement);
        return textElement;
    }

}
