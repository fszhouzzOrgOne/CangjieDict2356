package com.zzz.cj2356dict;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.zzz.cj2356dict.adapter.MyBaseExpandableListAdapter;
import com.zzz.cj2356dict.mb.MbUtils;
import com.zzz.cj2356dict.mb.StringUtils;
import com.zzz.cj2356dict.vo.Group;
import com.zzz.cj2356dict.vo.Item;

public class Cj2356DictActivity extends Activity {

    private ArrayList<Group> gData = null;
    private Context mContext;
    private SearchView searView;
    private ExpandableListView expandableListView;
    private MyBaseExpandableListAdapter myAdapter = null;

    /**
     * 爲默認字體
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 默認字體
        CalligraphyConfig.initDefault("fonts/kaisinsungthi.ttf", R.attr.fontPath);

        setContentView(R.layout.main);
        mContext = Cj2356DictActivity.this;
        MbUtils.init(mContext);

        searView = (SearchView) findViewById(R.id.searchView);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        searView.setIconifiedByDefault(false);
        searView.setSubmitButtonEnabled(false);
        searView.setQueryHint("請輸入漢字或編碼...");
        // 查詢框事件
        searView.setOnQueryTextListener(new MySearchViewOnQueryTextListener(
                mContext));

        // 数据准备
        gData = MbUtils.initGroupDatas();

        myAdapter = new MyBaseExpandableListAdapter(gData, mContext);
        expandableListView.setAdapter(myAdapter);
        // 为列表设置点击事件
        expandableListView
                .setOnChildClickListener(new MyExpandableListViewOnChildClickListener(
                        mContext));
    }

    public ArrayList<Group> getgData() {
        return gData;
    }

    public void setgData(ArrayList<Group> gData) {
        this.gData = gData;
        myAdapter = new MyBaseExpandableListAdapter(gData, mContext);
        expandableListView.setAdapter(myAdapter);
        expandableListView.requestFocus();
        // 默認展開
        for (int i = 0; i < myAdapter.getGroupCount(); i++) {
            if (this.gData.get(i).getItems().get(0).isEmpty() == false) {
                expandableListView.expandGroup(i);
            }
        }
    }

    public void searchSth(String cont) {
        searView.setQuery(cont, true);
    }
}

/**
 * 提交查詢
 * 
 * @author t
 * @time 2016-12-18下午12:17:49
 */
class MySearchViewOnQueryTextListener implements SearchView.OnQueryTextListener {

    private Context mContext;

    public MySearchViewOnQueryTextListener(Context c) {
        this.mContext = c;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ArrayList<Group> gData = null;

        String pattern = "[a-z]{1,}";
        if (StringUtils.hasText(query) && StringUtils.hasText(query.trim())) {
            query = query.trim();
            Toast.makeText(mContext, "查詢“" + query + "”", Toast.LENGTH_SHORT)
                    .show();

            if (query.matches(pattern)) {
                gData = MbUtils.selectDbByCode(query);
            } else {
                gData = MbUtils.selectDbByChar(query);
            }
        } else {
            Toast.makeText(mContext, "請輸入查詢", Toast.LENGTH_SHORT).show();
        }
        ((Cj2356DictActivity) mContext).setgData(gData);
        return false;
    }

}

/**
 * 點擊了列表項
 * 
 * @author t
 * @time 2016-12-18上午10:11:24
 */
class MyExpandableListViewOnChildClickListener implements
        ExpandableListView.OnChildClickListener {

    private Context mContext;

    public MyExpandableListViewOnChildClickListener(Context c) {
        this.mContext = c;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
        final Item item = ((Cj2356DictActivity) mContext).getgData()
                .get(groupPosition).getItems().get(childPosition);
        if (item.isEmpty()) {
            return false;
        }
        final String item1 = "查詢“" + item.getCharacter() + "”";
        final String item2 = "查詢“" + item.getEncode() + "”";
        String item3 = "取消";
        AlertDialog.Builder builder = new Builder(mContext).setTitle("繼續查詢？");
        builder.setItems(new String[] { item1, item2, item3 },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int index) {
                        if (index == 0) {
                            ((Cj2356DictActivity) mContext).searchSth(item
                                    .getCharacter());
                        } else if (index == 1) {
                            ((Cj2356DictActivity) mContext).searchSth(item
                                    .getEncode());
                        } else {

                        }
                        arg0.dismiss();
                    }
                });
        builder.show();
        return true;
    }

}