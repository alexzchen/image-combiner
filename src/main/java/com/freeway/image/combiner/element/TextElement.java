package com.freeway.image.combiner.element;

import java.awt.*;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description 图片合并元素
 **/

public class TextElement extends CombineElement<TextElement> {

    //基础属性
    private String text;                //文本
    private Font font;                  //字体
    private boolean strikeThrough;      //删除线
    private Color color = new Color(0, 0, 0);   //颜色，默认黑色

    //换行计算相关属性
    private boolean autoBreakLine = false;  //是否自动换行
    private int maxLineWidth = 600;         //最大行宽，超出则换行
    private int maxLineCount = 2;           //最大行数，超出则丢弃多余行
    private int lineHeight = 50;            //行高，根据字体大小酌情设置

    /**
     * @param text 文本内容
     * @param font Font对象
     * @param x    x坐标
     * @param y    y坐标
     */
    public TextElement(String text, Font font, int x, int y) {
        this.text = text;
        this.font = font;
        super.setX(x);
        super.setY(y);
    }

    /**
     * @param text     文本内容
     * @param fontSize 字号
     * @param x        x坐标
     * @param y        y坐标
     */
    public TextElement(String text, int fontSize, int x, int y) {
        this.text = text;
        this.font = new Font("阿里巴巴普惠体", Font.PLAIN, fontSize);
        super.setX(x);
        super.setY(y);
    }

    /**
     * @param text     文本内容
     * @param fontName 字体名称
     * @param fontSize 字号
     * @param x        x坐标
     * @param y        y坐标
     */
    public TextElement(String text, String fontName, int fontSize, int x, int y) {
        this.text = text;
        this.font = new Font(fontName, Font.PLAIN, fontSize);
        super.setX(x);
        super.setY(y);
    }


    public String getText() {
        return text;
    }

    public TextElement setText(String text) {
        this.text = text;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public TextElement setFont(Font font) {
        this.font = font;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public TextElement setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isStrikeThrough() {
        return strikeThrough;
    }


    public TextElement setStrikeThrough(boolean strikeThrough) {
        this.strikeThrough = strikeThrough;
        return this;
    }

    public boolean isAutoBreakLine() {
        return autoBreakLine;
    }

    /**
     * 设置自动换行
     *
     * @param maxLineWidth 最大行宽
     * @param maxLineCount
     * @param lineHeight
     * @return
     */
    public TextElement setAutoBreakLine(int maxLineWidth, int maxLineCount, int lineHeight) {
        this.autoBreakLine = true;
        this.maxLineWidth = maxLineWidth;
        this.maxLineCount = maxLineCount;
        this.lineHeight = lineHeight;
        return this;
    }

    public int getMaxLineWidth() {
        return maxLineWidth;
    }

    public int getMaxLineCount() {
        return maxLineCount;
    }

    public int getLineHeight() {
        return lineHeight;
    }
}
