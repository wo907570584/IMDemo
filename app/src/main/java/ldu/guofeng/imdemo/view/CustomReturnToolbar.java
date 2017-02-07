package ldu.guofeng.imdemo.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ldu.guofeng.imdemo.R;


/**
 * Activity 的通用 Toolbar
 * Created by GUOFENG on 2016/11/2.
 */

public abstract class CustomReturnToolbar extends AppCompatActivity {

    Toolbar myToolbar;
    AppBarLayout myAppBar;

    abstract protected int provideContentViewId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        myAppBar = (AppBarLayout) findViewById(R.id.appbar_normal);
        myToolbar = (Toolbar) findViewById(R.id.toolbar_normal);

        if (myToolbar == null || myAppBar == null) {
            throw new IllegalStateException("find Toolbar failed");
        }
        myToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolbarClick();
            }
        });
        setSupportActionBar(myToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                //给左上角加一个返回的图标
                actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean canBack() {
        return true;
    }


    public Toolbar getToolbar() {
        return myToolbar;
    }

    public void onToolbarClick() {
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
}
