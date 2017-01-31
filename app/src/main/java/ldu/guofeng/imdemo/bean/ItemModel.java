package ldu.guofeng.imdemo.bean;

import java.io.Serializable;


public class ItemModel implements Serializable {

    public static final int LEFT_TEXT = 1001;//左文字
    public static final int LEFT_IMAGE = 1002;//左图片
    public static final int LEFT_LOCATION = 1003;//左位置

    public static final int RIGHT_TEXT = 2001;//右文字
    public static final int RIGHT_IMAGE = 2002;//右图片
    public static final int RIGHT_LOCTION = 2003;//右位置

    public int leftOrRight;//左还是右
    public Object object;

    public ItemModel(int leftOrRight, Object object) {
        this.leftOrRight = leftOrRight;
        this.object = object;
    }
}
