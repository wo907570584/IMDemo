package ldu.guofeng.imdemo.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 小工具类
 * Created by GUOFENG on 2016/12/17.
 */

public class NormalUtils {
    //复制剪切板
    public static void copyToClipboard(String info, Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        ToastUtils.showShortToast(String.format("已经复制作者的支付宝账号啦", info));
    }
}
