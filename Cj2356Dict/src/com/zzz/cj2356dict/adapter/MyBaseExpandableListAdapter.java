package com.zzz.cj2356dict.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.zzz.cj2356dict.R;
import com.zzz.cj2356dict.mb.MbUtils;
import com.zzz.cj2356dict.vo.Group;
import com.zzz.cj2356dict.vo.Item;

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Group> gData;
    private Context mContext;

    public MyBaseExpandableListAdapter(ArrayList<Group> gData, Context mContext) {
        this.gData = gData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return gData.get(groupPosition).getItems().size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        return gData.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return gData.get(groupPosition).getgId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return gData.get(groupPosition).getItems().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // 取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView
                    .findViewById(R.id.tv_group_name);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tv_group_name.setText(gData.get(groupPosition).getgName());
        return convertView;
    }

    // 取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tv_character = (TextView) convertView
                    .findViewById(R.id.tv_character);
            itemHolder.tv_encode = (TextView) convertView
                    .findViewById(R.id.tv_encode);

            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        itemHolder.tv_character.setText(gData.get(groupPosition).getItems()
                .get(childPosition).getCharacter());
        itemHolder.tv_encode.setText(generateCodeText(gData.get(groupPosition)
                .getItems().get(childPosition)));
        return convertView;
    }

    private String generateCodeText(Item item) {
        if (item.isEmpty()) {
            return "碼表太小，沒有找到。";
        }
        String text = MbUtils.translateEN2ZH(item.getEncode(),
                item.getGenCode());
        return text + "（" + item.getEncode() + "）";
    }

    // 设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolderGroup {
        private TextView tv_group_name;
    }

    private static class ViewHolderItem {
        private TextView tv_character;
        private TextView tv_encode;
    }

}