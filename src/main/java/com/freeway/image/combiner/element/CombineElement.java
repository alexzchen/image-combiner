package com.freeway.image.combiner.element;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description 合并元素父类
 */
public abstract class CombineElement<T extends CombineElement> {
    private int x;                  //起始坐标x，相对左上角
    private int y;                  //起始坐标y，相对左上角
    private boolean center;         //是否居中
    private float alpha = 1.0f;     //透明度

    public int getX() {
        return x;
    }

    public T setX(int x) {
        this.x = x;
        return (T) this;
    }

    public int getY() {
        return y;
    }

    public T setY(int y) {
        this.y = y;
        return (T) this;
    }

    public boolean isCenter() {
        return center;
    }

    public T setCenter(boolean center) {
        this.center = center;
        return (T) this;
    }

    public float getAlpha() {
        return alpha;
    }

    public T setAlpha(float alpha) {
        this.alpha = alpha;
        return (T) this;
    }
}
