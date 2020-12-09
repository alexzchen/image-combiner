package com.freeway;

import static org.junit.Assert.assertTrue;

import com.freeway.image.combiner.ImageCombiner;
import com.freeway.image.combiner.element.ImageElement;
import com.freeway.image.combiner.element.TextElement;
import com.freeway.image.combiner.enums.OutputFormat;
import com.freeway.image.combiner.enums.ZoomMode;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testCombineImage() throws Exception {

        ImageCombiner combiner = new ImageCombiner("https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png", OutputFormat.JPG);

        //话题图片
        ImageElement topicImage = combiner.addImageElement("https://beast.oss-cn-hangzhou.aliyuncs.com/combine_image/funny_topic/resource/product3x4.png", 0, 160);
        topicImage.setRoundCorner(40);
        topicImage.setCenter(true);

        //二维码
        combiner.addImageElement("http://imgtest.thebeastshop.com/file/combine_image/qrcodef3d132b46b474fe7a9cc6e76a511dfd5.jpg", 138, 1707, 186, 186, ZoomMode.WidthHeight);

        combiner.combine();
        combiner.save("d://aaaaaaaaa.jpg");
        System.out.println("Hello World!");
    }

    @Test
    public void testCombineTopicImage() throws Exception {

        String qrCodeUrl = "http://imgtest.thebeastshop.com/file/combine_image/qrcodef3d132b46b474fe7a9cc6e76a511dfd5.jpg";
        String topicTitle = "# 最爱的家居";
        String topicContent = "井柏然说：“如果没有那个桌子，可能就没有那个水壶”，但是没有桌子，水壶还是那个水壶，只不过是个没有桌子的水壶";
        String topicImage = "https://beast.oss-cn-hangzhou.aliyuncs.com/combine_image/funny_topic/resource/product_3x4.png";

        //背景图
        String bgImage = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
        ImageCombiner imageCombiner = new ImageCombiner(bgImage, OutputFormat.JPG);
        //设置背景虚化
        imageCombiner.setBackgroundBlur(30);

        //话题图
        imageCombiner.addImageElement(topicImage, 0, 160, 837, 0, ZoomMode.Width)
                .setRoundCorner(500)
                .setCenter(true);

        //话题标题
        imageCombiner.addTextElement(topicTitle, 55, 150, 1400);

        //话题内容
        imageCombiner.addTextElement(topicContent, 40, 150, 1480)
                .setAutoBreakLine(837, 3, 60);

        //水印（虚化30）
        String waterMark = "https://img.thebeastshop.com/combine_image/funny_topic/resource/water_mark.png";
        imageCombiner.addImageElement(waterMark, 630, 1200)
                .setBlur(30);

        //二维码
        imageCombiner.addImageElement(qrCodeUrl, 138, 1707, 186, 186, ZoomMode.WidthHeight);

        //合成图片
        imageCombiner.combine();

        //保存
        imageCombiner.save("d://topic.png");
    }

    @Test
    public void demo() throws Exception {
        String bgImageUrl = "http://xxx.com/image/bg.jpg";                  //背景图
        String qrCodeUrl = "http://xxx.com/image/qrCode.png";               //二维码
        String productImageUrl = "http://xxx.com/image/product.jpg";        //商品图
        BufferedImage waterMark = ImageIO.read(new URL("https://xxx.com/image/waterMark.jpg")); //水印图
        BufferedImage avatar = ImageIO.read(new URL("https://xxx.com/image/avatar.jpg"));       //头像
        String title = "# 最爱的家居";                                       //标题文本
        String content = "苏格拉底说：“如果没有那个桌子，可能就没有那个水壶”";  //内容文本

        //背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner(bgImageUrl, OutputFormat.JPG);

        //商品图（设置坐标、宽高和缩放模式，若按宽度缩放，则高度按比例自动计算）
        combiner.addImageElement(productImageUrl, 0, 160, 837, 0, ZoomMode.Width)
                .setRoundCorner(46)     //设置圆角
                .setCenter(true);       //居中绘制，会忽略x坐标参数，改为自动计算

        //标题（默认字体为阿里普惠、黑色，也可以自己指定Font对象）
        combiner.addTextElement(title, 55, 150, 1400);

        //内容（设置文本自动换行，需要指定最大宽度（超出则换行）、最大行数（超出则丢弃）、行高）
        combiner.addTextElement(content, "微软雅黑", 40, 150, 1480)
                .setAutoBreakLine(837, 2, 60);

        //头像（圆角设置一定的大小，可以把头像变成圆的）
        combiner.addImageElement(avatar, 200, 1200).setRoundCorner(200);

        //水印（设置透明度，0.0~1.0）
        combiner.addImageElement(waterMark, 630, 1200).setAlpha(.8f);

        //二维码（强制按指定宽度、高度缩放）
        combiner.addImageElement(qrCodeUrl, 138, 1707, 186, 186, ZoomMode.WidthHeight);

        //元素对象也可以直接new，然后手动加入待绘制列表
        TextElement textPrice = new TextElement("￥1290", 60, 230, 1300);
        textPrice.setColor(Color.red);          //红色
        textPrice.setStrikeThrough(true);       //删除线
        combiner.addElement(textPrice);         //加入待绘制集合


        //执行图片合并
        combiner.combine();
        //获取流（并上传oss等）
        InputStream is = combiner.getCombinedImageStream();
        //保存文件
        combiner.save("d://topic.png");
    }

    public void simpleDemo() throws Exception {

        //背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner("http://xxx.com/image/bg.jpg", OutputFormat.JPG);

        //加图片元素（居中绘制，圆角，半透明）
        combiner.addImageElement("http://xxx.com/image/product.png", 0, 300)
                .setCenter(true)
                .setRoundCorner(60)
                .setAlpha(.8f);

        //加文本元素
        combiner.addTextElement("周末大放送", 60, 100, 960)
                .setColor(Color.red);
        //合成图片
        combiner.combine();
        //保存
        combiner.save("d://123.jpg");
    }

    @Test
    public void rotateTest() throws Exception {
        String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
        ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

        combiner.addTextElement("测试一下多行文本换行加旋转的动作，不知道是否能正常显示", 80, 300, 300)
                .setStrikeThrough(true)
                .setAutoBreakLine(600, 2, 80);
        combiner.addTextElement("测试一下多行文本换行加旋转的动作，不知道是否能正常显示", 80, 300, 300).setColor(Color.red)
                .setStrikeThrough(true)
                .setAutoBreakLine(600, 2, 80)
                .setRotate(40);

        combiner.addImageElement("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg", 300, 600)
                .setRotate(45);

        combiner.combine();
        combiner.save("d://rotateTest.jpg");
    }

    @Test
    public void rotateTest2() throws Exception {
        String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
        ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

        combiner.addTextElement("我觉得应该可以正常显示", 80, 300, 300)
                .setCenter(true);
        combiner.addTextElement("我觉得应该可以正常显示", 80, 300, 300).setColor(Color.red)
                .setCenter(true)
                .setRotate(40);

        combiner.addImageElement("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg", 300, 600)
                .setRotate(45);

        combiner.combine();
        combiner.save("d://rotateTest.jpg");
    }

    @Test
    public void dynamicWidthDemoTest() throws Exception {
        String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
        ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

        String str1 = "您出征";
        String str2 = "成都";     //内容不定，宽度也不定
        String str3 = "，共在前线战斗了";
        String str4 = "365";     //内容不定，宽度也不定
        String str5 = "天！";
        int fontSize = 60;
        int xxxFontSize = 80;

        int offsetX = 20;
        int y = 300;

        TextElement element1 = combiner.addTextElement(str1, fontSize, offsetX, y);
        offsetX += combiner.computeTextWidth(element1);

        TextElement element2 = combiner.addTextElement(str2, xxxFontSize, offsetX, y)
                .setColor(Color.red);
        offsetX += combiner.computeTextWidth(element2);

        TextElement element3 = combiner.addTextElement(str3, fontSize, offsetX, y);
        offsetX += combiner.computeTextWidth(element3);

        TextElement element4 = combiner.addTextElement(str4, xxxFontSize, offsetX, y)
                .setColor(Color.red);
        offsetX += combiner.computeTextWidth(element4);

        combiner.addTextElement(str5, fontSize, offsetX, y);

        combiner.combine();
        combiner.save("d://demo.jpg");
    }
}
