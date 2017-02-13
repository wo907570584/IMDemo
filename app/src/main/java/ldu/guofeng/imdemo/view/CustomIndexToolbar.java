package ldu.guofeng.imdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ldu.guofeng.imdemo.R;

/**
 * 自定义标题栏（左按钮、标签、右按钮、标签、中间标签 和 点击事件）
 *
 * @author GUOFENG
 */

public class CustomIndexToolbar extends RelativeLayout {

    //左btn
    private Boolean isLeftBtnVisible;
    private int leftResId;
    //左tv
    private Boolean isLeftTvVisible;
    private String leftTvText;
    //右btn
    private Boolean isRightBtnVisible;
    private int rightResId;
    //右tv
    private Boolean isRightTvVisible;
    private String rightTvText;
    //中间标题
    private Boolean isTitleVisible;
    private String titleText;
    //背景色
    private int backgroundResId;

    private Button leftBtn;
    private TextView leftTv;
    private Button rightBtn;
    private TextView rightTv;

    public CustomIndexToolbar(Context context) {
        this(context, null);
    }

    public CustomIndexToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomIndexToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }


    /**
     * 初始化属性
     */
    public void initView(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomToolBar);
        /**-------------获取左边按钮属性------------*/
        isLeftBtnVisible = typedArray.getBoolean(R.styleable.CustomToolBar_left_btn_visible, false);
        leftResId = typedArray.getResourceId(R.styleable.CustomToolBar_left_btn_src, -1);
        /**-------------获取左边文本属性------------*/
        isLeftTvVisible = typedArray.getBoolean(R.styleable.CustomToolBar_left_tv_visible, false);
        if (typedArray.hasValue(R.styleable.CustomToolBar_left_tv_text)) {
            leftTvText = typedArray.getString(R.styleable.CustomToolBar_left_tv_text);
        }
        /**-------------获取右边按钮属性------------*/
        isRightBtnVisible = typedArray.getBoolean(R.styleable.CustomToolBar_right_btn_visible, false);
        rightResId = typedArray.getResourceId(R.styleable.CustomToolBar_right_btn_src, -1);
        /**-------------获取右边文本属性------------*/
        isRightTvVisible = typedArray.getBoolean(R.styleable.CustomToolBar_right_tv_visible, false);
        if (typedArray.hasValue(R.styleable.CustomToolBar_right_tv_text)) {
            rightTvText = typedArray.getString(R.styleable.CustomToolBar_right_tv_text);
        }
        /**-------------获取标题属性------------*/
        isTitleVisible = typedArray.getBoolean(R.styleable.CustomToolBar_title_visible, false);
        if (typedArray.hasValue(R.styleable.CustomToolBar_title_text)) {
            titleText = typedArray.getString(R.styleable.CustomToolBar_title_text);
        }
        /**-------------背景颜色------------*/
        backgroundResId = typedArray.getResourceId(R.styleable.CustomToolBar_barBackground, -1);
        typedArray.recycle();
        /**-------------设置内容------------*/
        View barLayoutView = View.inflate(getContext(), R.layout.common_index_toolbar, null);
        leftBtn = (Button) barLayoutView.findViewById(R.id.toolbar_left_btn);
        leftTv = (TextView) barLayoutView.findViewById(R.id.toolbar_left_tv);
        TextView titleTv = (TextView) barLayoutView.findViewById(R.id.toolbar_title_tv);
        rightBtn = (Button) barLayoutView.findViewById(R.id.toolbar_right_btn);
        rightTv = (TextView) barLayoutView.findViewById(R.id.toolbar_right_tv);
        RelativeLayout barRlyt = (RelativeLayout) barLayoutView.findViewById(R.id.toolbar_content_rlyt);

        if (isLeftBtnVisible) {
            leftBtn.setVisibility(VISIBLE);
        }
        if (isLeftTvVisible) {
            leftTv.setVisibility(VISIBLE);
        }
        if (isRightBtnVisible) {
            rightBtn.setVisibility(VISIBLE);
        }
        if (isRightTvVisible) {
            rightTv.setVisibility(VISIBLE);
        }
        if (isTitleVisible) {
            titleTv.setVisibility(VISIBLE);
        }
        leftTv.setText(leftTvText);
        rightTv.setText(rightTvText);
        titleTv.setText(titleText);
        if (leftResId != -1) {
            leftBtn.setBackgroundResource(leftResId);
        }
        if (rightResId != -1) {
            rightBtn.setBackgroundResource(rightResId);
        }
        if (backgroundResId != -1) {
            barRlyt.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_title));
        }
        //将设置完成之后的View添加到此LinearLayout中
        addView(barLayoutView, 0);

    }

    // 为左侧按钮添加点击事件
    public void setLeftButtonListener(OnClickListener listener) {
        leftBtn.setOnClickListener(listener);
    }

    // 为左侧标签添加点击事件
    public void setLeftLabelListener(OnClickListener listener) {
        leftTv.setOnClickListener(listener);
    }

    // 为右侧按钮添加点击事件
    public void setRightButtonListener(OnClickListener listener) {
        rightBtn.setOnClickListener(listener);
    }

    // 为右侧标签添加点击事件
    public void setRightLabelListener(OnClickListener listener) {
        rightTv.setOnClickListener(listener);
    }
}
