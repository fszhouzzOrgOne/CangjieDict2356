package com.zzz.cj2356dict.mb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zzz.cj2356dict.dto.Group;
import com.zzz.cj2356dict.dto.Item;
import com.zzz.cj2356dict.state.InputMethodStatus;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCn;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCj2;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCj3;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCj35;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCj5;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCj6;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCjMacOsX105;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCjMs;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnCjYhqm;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnElseKarina;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnElseKorea;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnElseManju;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnElsePy;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnElseSghm;
import com.zzz.cj2356dict.state.trans.InputMethodStatusCnElseZyfh;
import com.zzz.cj2356dict.utils.Cangjie2356ConfigUtils;
import com.zzz.cj2356dict.utils.Cangjie2356IMsUtils;
import com.zzz.cj2356dict.utils.StringUtils;

import android.content.Context;

/**
 * 倉頡字典的相關查詢
 * 
 * 
 * @author 日月遞炤
 * @time 2017年9月26日 下午10:43:48
 */
public class SettingDictMbUtils {

    private static Context context;

    /**
     * 字典查询的輸入法
     */
    private static final List<InputMethodStatusCn> dictIms = new ArrayList<InputMethodStatusCn>();

    public static void init(Context con) {
        context = con;
        if (dictIms.isEmpty()) {
            try {
                Cangjie2356ConfigUtils.init(context);
                String cjConfig = Cangjie2356ConfigUtils.getConfig(Cangjie2356IMsUtils.ORDER_CJ_KEY);
                String[] cjConfigArr = cjConfig.split(",");
                List<String> cjConfigList = new ArrayList<String>();
                for (String conf : cjConfigArr) {
                    cjConfigList.add(conf);
                }

                Map<String, Object> allCjIMsMap = new LinkedHashMap<String, Object>();
                InputMethodStatus im = new InputMethodStatusCnCj6(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCj5(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCjMacOsX105(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCj35(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCj3(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCjMs(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCjYhqm(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnCj2(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnElseSghm(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnElsePy(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnElseKarina(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnElseManju(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnElseKorea(context);
                allCjIMsMap.put(im.getSubType(), im);
                im = new InputMethodStatusCnElseZyfh(context);
                allCjIMsMap.put(im.getSubType(), im);

                for (String key : allCjIMsMap.keySet()) {
                    if (cjConfigList.contains(key)) {
                        dictIms.add((InputMethodStatusCn) allCjIMsMap.get(key));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 初始化字典查詢展示
     * 
     * @author 日月遞炤
     * @time 2017年9月26日 下午10:44:46
     * @return ArrayList<Group> 輸入法分組結果
     */
    public static List<Group> initGroupDatas() {
        List<Group> gData = new ArrayList<Group>();
        for (int i = 0; i < dictIms.size(); i++) {
            gData.add(new Group(i, dictIms.get(i).getSubType(), dictIms.get(i).getInputMethodName()));
        }
        return gData;
    }

    /**
     * 按編碼查詢
     * 
     * @author fsz
     * @time 2017年9月27日上午9:42:40
     * @param query
     * @return
     */
    public static List<Group> selectDbByCode(String query) {
        List<Group> gData = new ArrayList<Group>();
        for (int i = 0; i < dictIms.size(); i++) {
            Group g = new Group(i, dictIms.get(i).getSubType(), dictIms.get(i).getInputMethodName());
            List<Item> items = dictIms.get(i).getCandidatesInfo(query, false);
            // List<Item> items = null;
            // if (MbUtils.TYPE_CODE_CJGEN6.equals(dictIms.get(i).getSubType()))
            // {
            // items = dictIms.get(i).getCandidatesInfo(query, false);
            // }
            if (null != items && !items.isEmpty()) {
                for (Item it : items) {
                    if (StringUtils.hasText(it.getEncode())) {
                        it.setEncodeName(dictIms.get(i).translateCode2Name(it.getEncode()));
                    }
                }
                g.setItems(items);
            }
            gData.add(g);
        }
        return gData;
    }

    /**
     * 按字查詢
     * 
     * @author fsz
     * @time 2017年9月27日上午9:42:40
     * @param query
     * @return
     */
    public static List<Group> selectDbByChar(String query) {
        List<Group> gData = new ArrayList<Group>();
        for (int i = 0; i < dictIms.size(); i++) {
            Group g = new Group(i, dictIms.get(i).getSubType(), dictIms.get(i).getInputMethodName());
            List<Item> items = dictIms.get(i).getCandidatesInfoByChar(query);
            // List<Item> items = null;
            // if (MbUtils.TYPE_CODE_CJGEN6.equals(dictIms.get(i).getSubType()))
            // {
            // items = dictIms.get(i).getCandidatesInfoByChar(query);
            // }
            if (null != items && !items.isEmpty()) {
                for (Item it : items) {
                    if (StringUtils.hasText(it.getEncode())) {
                        it.setEncodeName(dictIms.get(i).translateCode2Name(it.getEncode()));
                    }
                }
                g.setItems(items);
            }
            gData.add(g);
        }
        return gData;
    }

}
