package com.sir.app.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.sir.app.base.common.BaseApplication;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivityV4 extends FragmentActivity implements
        IBaseActivity {

    /***
     * 整个应用Applicaiton
     **/
    private BaseApplication mApplication = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    /**
     * 当前Activity渲染的视图View
     **/
    // private View mContextView = null;
    /**
     * 共通操作
     **/
    private Operation mBaseOperation = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置渲染视图View
        // mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(bindLayout());

        // mContextView = getWindow().getDecorView();

        //初始化
        ButterKnife.bind(this);

        // 获取应用Application
        mApplication = (BaseApplication) getApplicationContext();

        // 将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);

        // 实例化共通操作
        mBaseOperation = new Operation(this);

        // 初始化控件
        initView(getWindow().getDecorView());

        // 业务操作
        doBusiness(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
        mApplication.removeTask(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    protected Activity getContext() {
        if (null != context) {
            return context.get();
        } else {
            return null;
        }
    }

    /**
     * 获取共通操作机能
     */
    protected Operation getOperation() {
        return this.mBaseOperation;
    }

    /**
     * Actionbar点击返回键关闭事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //关闭窗体动画显示
                overridePendingTransition(0, R.anim.base_slide_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭窗体动画显示
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
