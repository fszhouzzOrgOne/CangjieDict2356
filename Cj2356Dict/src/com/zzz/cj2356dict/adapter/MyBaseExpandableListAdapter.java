package com.zzz.cj2356dict.adapter;

import java.util.List;

import com.zzz.cj2356dict.R;
import com.zzz.cj2356dict.dto.Group;
import com.zzz.cj2356dict.dto.Item;
import com.zzz.cj2356dict.font.FontManager;
import com.zzz.cj2356dict.utils.UnicodeConvertUtil;
import com.zzz.cj2356dict.utils.UnicodeHanziUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private List<Group> gData;
    private Context mContext;

    public MyBaseExpandableListAdapter(List<Group> gData, Context mContext) {
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_dict_item_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tv_group_name.setTextColor(Color.LTGRAY);
        groupHolder.tv_group_name.setText(gData.get(groupPosition).getgName());
        return convertView;
    }

    // 取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        ViewHolderItem itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_dict_item_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tv_character = (TextView) convertView.findViewById(R.id.tv_character);
            itemHolder.tv_unicodeRangeName = (TextView) convertView.findViewById(R.id.tv_unicodeRangeName);
            itemHolder.tv_encode = (TextView) convertView.findViewById(R.id.tv_encode);

            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        // 字體自定義
        itemHolder.tv_character.setTypeface(FontManager.getTypeface(mContext));
        Item item = gData.get(groupPosition).getItems().get(childPosition);
        String character = item.getCharacter();
        itemHolder.tv_character.setText(character);

        String unicodeRangeName = UnicodeHanziUtil.getRangeNameByChar(character);
        if (null == unicodeRangeName) {
            unicodeRangeName = "";
        }
        // 統一碼碼位
        if (!item.isUnicodeItem()) {
            List<String> codes = UnicodeConvertUtil.getUnicodeStr4ListFromStr(character);
            if (null != codes && codes.size() == 1) {
                unicodeRangeName += "(" + codes.get(0) + ")";
            }
        }
        itemHolder.tv_unicodeRangeName.setText(unicodeRangeName);

        itemHolder.tv_encode.setText(generateCodeText(item));
        return convertView;
    }

    private String generateCodeText(Item item) {
        if (item.isEmpty()) {
            return "無結果。";
        }
        if (item.isUnicodeItem()) {
            return item.getEncode();
        }
        return item.getEncodeName() + "(" + item.getEncode() + ")";
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
        private TextView tv_unicodeRangeName;
        private TextView tv_encode;
    }

}
