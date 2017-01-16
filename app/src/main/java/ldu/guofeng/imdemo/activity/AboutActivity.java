package ldu.guofeng.imdemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.util.NormalUtils;
import ldu.guofeng.imdemo.util.ToastUtils;


/**
 * 关于作者
 *
 * @author GUOFENG
 */
public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout toolbarLayout;
    private Toolbar toolbar;
    private TextView about_version;
    private Button btnGitHub;
    private Button btnBlog;
    private Button btnAlipay;
    private Button btnShare;
    private Button btnUpdata;
    private Button btnBug;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findView();
        init();
    }

    private void findView() {
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        about_version = (TextView) findViewById(R.id.about_version);
        btnGitHub = (Button) findViewById(R.id.btn_github);
        btnBlog = (Button) findViewById(R.id.btn_csdn);
        btnAlipay = (Button) findViewById(R.id.btn_alipay);
        btnShare = (Button) findViewById(R.id.btn_shareApp);
        btnUpdata = (Button) findViewById(R.id.btn_updataApp);
        btnBug = (Button) findViewById(R.id.btn_bug);
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle(getString(R.string.app_name));
        about_version.setText(getText(R.string.app_version));
        btnGitHub.setOnClickListener(this);
        btnBlog.setOnClickListener(this);
        btnAlipay.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnUpdata.setOnClickListener(this);
        btnBug.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_github:
                goToHtml(getString(R.string.html_guofeng_github));
                break;
            case R.id.btn_csdn:
                goToHtml(getString(R.string.html_guofeng_csdn));
                break;
            case R.id.btn_alipay:
                NormalUtils.copyToClipboard(getString(R.string.guofeng_alipay), this);
                break;
            case R.id.btn_shareApp:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_txt));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)));
                break;
            case R.id.btn_bug:
                ToastUtils.showShortToast("反馈意见功能暂时未添加");
                break;
            case R.id.btn_updataApp:
                ToastUtils.showShortToast("更新软件功能暂时未添加");
                break;
        }
    }

    //跳转网页
    private void goToHtml(String url) {
        Uri uri = Uri.parse(url); //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW); //指定Action
        intent.setData(uri);   //设置Uri
        startActivity(intent);
    }
}
