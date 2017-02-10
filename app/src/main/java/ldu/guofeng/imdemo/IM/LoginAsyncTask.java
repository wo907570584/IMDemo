package ldu.guofeng.imdemo.im;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

import ldu.guofeng.imdemo.activity.MainActivity;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.util.ToastUtils;
import ldu.guofeng.imdemo.view.CustomLoadingDialog;

/**
 * 登录-异步任务
 * <p>
 * 执行次序	执行时机	方法名称	调用方
 * 1.异步任务执行前	 onPreExecute       UI线程
 * 2.异步任务执行中	 doInBackground     后台线程
 * 3.异步任务执行中    publishProgress	    后台线程
 * 4.异步任务执行中    onProgressUpdate    UI线程
 * 5.异步任务执行后    onPostExecute	    UI线程
 */

public class LoginAsyncTask extends AsyncTask<String, String, Boolean> {
    private CustomLoadingDialog loadDialog;
    private Context mContext;

    public LoginAsyncTask(Context context) {
        mContext = context;
    }


    /**
     * 该方法运行在UI线程中,在执行耗时操作前被调用，主要用于UI控件初始化操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //显示登录提示条
        loadDialog = new CustomLoadingDialog(mContext);
        loadDialog.setTitle("正在登录...");
        loadDialog.show();
    }


    /**
     * 必须重写的方法
     * 该方法不运行在UI线程中,主要用于耗时的处理工作,
     * 可调用publishProgress()方法，触发onProgressUpdate对UI进行操作，更新任务进度。
     */
    @Override
    protected Boolean doInBackground(String... strings) {
        //获取执行异步任务时传入的参数
        String username = strings[0];
        String pwd = strings[1];
        //建立连接
        SmackUtils.getInstance().getXMPPConnection();
        //登录成功
        if (SmackUtils.getInstance().login(username, pwd)) {
            //保存密码到本地
            PreferencesUtils.getInstance().putString("username", username);
            PreferencesUtils.getInstance().putString("pwd", pwd);
            //初始化，监听包
            IMApplication.connection.addAsyncStanzaListener(
                    new PacketListener(),
                    new StanzaFilter() {
                        @Override
                        public boolean accept(Stanza stanza) {
                            return true;
                        }
                    }
            );
            return true;//传送给onPostExecute布尔值
        }
        return false;//传送给onPostExecute布尔值
    }

    /**
     * doInBackground执行完成后，此方法被UI线程调用，计算结果传递到UI线程。
     */
    @Override
    protected void onPostExecute(Boolean bool) {
        //关闭等待条
        if (loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
        //转到main页
        if (bool) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        } else {
            ToastUtils.showShortToast("请检查用户名和密码是否正确/网络是否可用");
        }
    }
}
