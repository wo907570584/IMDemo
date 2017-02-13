package ldu.guofeng.imdemo.view;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ldu.guofeng.imdemo.R;


/**
 * 自定义等待条
 *
 * @author 郭峰
 */

public class CustomLoadingDialog extends Dialog {

    private TextView tv;
    private boolean cancelable = true;

    public CustomLoadingDialog(Context context) {
        super(context, R.style.Dialog_style);
        init();
    }

    private void init() {
        View contentView = View.inflate(
                getContext(),
                R.layout.common_loading_dialog,
                null
        );
        setContentView(contentView);
        //点击关闭
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelable) {
                    dismiss();
                }
            }
        });
        tv = (TextView) findViewById(R.id.tv);
    }

//    @Override
//    public void show() {
//        super.show();
//    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setCancelable(boolean flag) {
        cancelable = flag;
        super.setCancelable(flag);
    }

    @Override
    public void setTitle(CharSequence title) {
        tv.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }
}
