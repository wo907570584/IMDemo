package ldu.guofeng.imdemo.util;

import android.widget.Toast;

import ldu.guofeng.imdemo.base.IMApplication;

/**
 * Toast 封装类
 *
 * @author 郭峰
 */
public class ToastUtils {

    public static void showShortToast(String msg) {
        Toast.makeText(IMApplication.getMyAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(IMApplication.getMyAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
