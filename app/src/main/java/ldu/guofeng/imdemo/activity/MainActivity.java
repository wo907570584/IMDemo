package ldu.guofeng.imdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.fragment.ContactsFragment;
import ldu.guofeng.imdemo.fragment.SessionFragment;
import ldu.guofeng.imdemo.fragment.SettingFragment;

/**
 * 主页
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private SessionFragment sessionFragment;//会话页
    private ContactsFragment contactsFragment;//联系人页
    private SettingFragment settingFragment;//设置页

    private ImageButton mSession;//会话按钮
    private ImageButton mContacts;//联系人按钮
    private ImageButton mSetting;//设置按钮
    private View currentBtn;//标记 当前按钮view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        init();
    }

    private void findView() {
        sessionFragment = (SessionFragment) getFragmentManager().findFragmentById(R.id.fragment_session);
        contactsFragment = (ContactsFragment) getFragmentManager().findFragmentById(R.id.fragment_constact);
        settingFragment = (SettingFragment) getFragmentManager().findFragmentById(R.id.fragment_setting);
        mSession = (ImageButton) findViewById(R.id.buttom_session);//会话
        mContacts = (ImageButton) findViewById(R.id.buttom_contacts);//联系人
        mSetting = (ImageButton) findViewById(R.id.buttom_setting);//设置
    }

    private void init() {
        mSession.setOnClickListener(this);
        mContacts.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSession.performClick();//默认点击会话页
    }

    //-----------------------------------------------------
    @Override
    public void onClick(View view) {
        // 切换Fragment
        switch (view.getId()) {
            case R.id.buttom_session://消息
                getFragmentManager().beginTransaction()
                        .show(sessionFragment)
                        .hide(contactsFragment)
                        .hide(settingFragment)
                        .commit();
                setButton(view);
                break;
            case R.id.buttom_contacts://联系人
                getFragmentManager().beginTransaction()
                        .hide(sessionFragment)
                        .show(contactsFragment)
                        .hide(settingFragment)
                        .commit();
                setButton(view);
                break;
            case R.id.buttom_setting://设置
                getFragmentManager().beginTransaction()
                        .hide(sessionFragment)
                        .hide(contactsFragment)
                        .show(settingFragment)
                        .commit();
                setButton(view);
                break;
            default:
                break;
        }
    }

    private void setButton(View v) {
        if (currentBtn != null && currentBtn.getId() != v.getId()) {
            currentBtn.setEnabled(true);
        }
        v.setEnabled(false);
        currentBtn = v;
    }
}
