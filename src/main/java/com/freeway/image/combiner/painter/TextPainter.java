package com.freeway.image.combiner.painter;

import com.freeway.image.combiner.element.CombineElement;
import com.freeway.image.combiner.element.TextElement;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description
 */
public class TextPainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) {

        //强制转成子类
        TextElement textElement = (TextElement) element;

        //首先计算是否要换行（由于拆行计算比较耗资源，不设置换行则直接用原始对象绘制）
        List<TextElement> textLineElements = new ArrayList<>();
        textLineElements.add(textElement);

        if (textElement.isAutoBreakLine()) {
            textLineElements = this.getBreakLineElements(textElement);
        }

        for (TextElement textLineElement : textLineElements) {

            int textWidth = 0;
            //设置字体、颜色
            g.setFont(textLineElement.getFont());
            g.setColor(textLineElement.getColor());

            //设置居中
            if (textLineElement.isCenter()) {
                textWidth = this.getFrontWidth(textLineElement.getText(), textLineElement.getFont());
                int centerX = (canvasWidth - textWidth) / 2;
                textLineElement.setX(centerX);
            }

            //旋转
            if (textLineElement.getRotate() != null) {
                if (textWidth == 0) {
                    textWidth = this.getFrontWidth(textLineElement.getText(), textLineElement.getFont());
                }
                g.rotate(Math.toRadians(textLineElement.getRotate()), textLineElement.getX() + textWidth / 2, textLineElement.getY());
            }

            //设置透明度
            if (textLineElement.getAlpha() != 1.0f) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, textLineElement.getAlpha()));
            }

            //带删除线样式的文字要特殊处理
            if (textLineElement.isStrikeThrough() == true) {
                AttributedString as = new AttributedString(textLineElement.getText());
                as.addAttribute(TextAttribute.FONT, textLineElement.getFont());
                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, textLineElement.getText().length());
                g.drawString(as.getIterator(), textLineElement.getX(), textLineElement.getY());
            } else {
                g.drawString(textLineElement.getText(), textLineElement.getX(), textLineElement.getY());
            }

            //绘制完后反向旋转，以免影响后续元素
            if (textLineElement.getRotate() != null) {
                g.rotate(-Math.toRadians(textLineElement.getRotate()), textLineElement.getX() + textWidth / 2, textLineElement.getY());
            }
        }
    }

    public int getFrontWidth(String text, Font font) {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += metrics.charWidth(text.charAt(i));
        }
        return width;
    }

    private boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    private List<String> computeLines(String text, Font font, int maxLineWidth) {

        List<String> computedLines = new ArrayList<>();     //最终要返回的多行文本（不超限定宽度）
        String strToComputer = "";
        String word = "";             //一个完整单词
        boolean hasWord = false;      //是否获得一个完整单词
        char[] chars = text.toCharArray();
        int count = 0;

        //遍历每个字符，拆解单词（一个中文算一个单词，其他字符直到碰到空格算一个单词）
        for (int i = 0; i < chars.length; i++) {
            if (count++ > 500) {
                break;      //防止意外情况进入死循环
            }
            char c = chars[i];          //当前字符
            if (isChineseChar(c) || c == ' ' || i == (chars.length - 1)) {
                word += c;             //如果是中文或空格或最后一个字符，一个中文算一个单词, 其他字符遇到空格认为单词结束
                hasWord = true;
            } else {
                word += c;             //英文或其他字符，加入word，待组成单词
            }

            //获得了一个完整单词，加入当前行，并计算限宽
            if (hasWord) {

                //计算现有文字宽度
                int originWidth = getFrontWidth(strToComputer, font);

                //计算单个单词宽度（防止一个单词就超限宽的情况）
                int wordWidth = getFrontWidth(word, font);

                //单词加入待计算字符串
                strToComputer += word;

                //加入了新单词之后的宽度
                int newWidth = originWidth + wordWidth;

                //一个单词就超限，要暴力换行
                if (wordWidth > maxLineWidth) {
                    //按比例计算要取几个字符（不是特别精准）
                    int fetch = (int) ((float) (maxLineWidth - originWidth) / (float) wordWidth * word.length());   //本行剩余宽度所占word宽度比例，乘以字符长度（字符不等宽的时候不太准）
                    strToComputer = strToComputer.substring(0, strToComputer.length() - word.length() + fetch);     //去除最后的word的后半截
                    computedLines.add(strToComputer);                      //加入计算结果列表
                    strToComputer = "";
                    i -= (word.length() - fetch);                          //遍历计数器回退word.length()-fetch个
                }
                //行宽度超出限宽，则去除最后word，加入计算结果列表
                else if (newWidth > maxLineWidth) {
                    strToComputer = strToComputer.substring(0, strToComputer.length() - word.length());      //去除最后word
                    computedLines.add(strToComputer);                       //加入计算结果列表
                    strToComputer = "";
                    i -= word.length();                                     //遍历计数器回退word.length()个
                }

                word = "";
                hasWord = false;        //重置标记
            }
        }

        if (strToComputer != "") {
            computedLines.add(strToComputer);                               //加入计算结果列表
        }

        return computedLines;
    }

    public List<TextElement> getBreakLineElements(TextElement textElement) {
        List<TextElement> breakLineElements = new ArrayList<>();
        List<String> breakLineTexts = computeLines(textElement.getText(), textElement.getFont(), textElement.getMaxLineWidth());
        int y = textElement.getY();
        for (int i = 0; i < breakLineTexts.size(); i++) {
            if (i < textElement.getMaxLineCount()) {
                String text = breakLineTexts.get(i);
                //如果计该行是要取的最后一行，但不是整体最后一行，则加...
                if (i == textElement.getMaxLineCount() - 1 && i < breakLineTexts.size() - 1) {
                    text = text.substring(0, text.length() - 1) + "...";
                }
                TextElement combineTextLine = new TextElement(text, textElement.getFont(), textElement.getX(), y);
                combineTextLine.setColor(textElement.getColor());
                combineTextLine.setStrikeThrough(textElement.isStrikeThrough());
                combineTextLine.setCenter(textElement.isCenter());
                combineTextLine.setAlpha(textElement.getAlpha());
                combineTextLine.setRotate(textElement.getRotate());
                breakLineElements.add(combineTextLine);

                //累加高度
                y += textElement.getLineHeight();
            } else {
                break;
            }
        }
        return breakLineElements;
    }
}
