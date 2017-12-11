package com.zzz.cj2356dict;

import com.zzz.cj2356dict.view.SettingLoayoutTabIniter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Cj2356DictActivity extends Activity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 爲了隱藏輸入法之一
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.main);
        mContext = Cj2356DictActivity.this;

        SettingLoayoutTabIniter.initSettingLoayoutTab(mContext);

        // 爲了隱藏輸入法之二
        // 之三還要在xml中設置focusable和focusableInTouchMode為真
        InputMethodManager imm1 = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(getContentView().getWindowToken(), 0);
    }

    /**
     * 上面有setContentView，模擬get
     * 
     * @author fsz
     * @time 2017年9月27日上午9:29:43
     * @return
     */
    private View getContentView() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        ViewGroup content = (ViewGroup) view.getChildAt(0);
        return content.getChildAt(0);
    }

}