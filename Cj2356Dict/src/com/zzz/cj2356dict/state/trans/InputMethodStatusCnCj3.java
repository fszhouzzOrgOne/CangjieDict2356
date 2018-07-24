package com.zzz.cj2356dict.state.trans;

import java.util.List;
import java.util.Map;

import com.zzz.cj2356dict.dto.Item;
import com.zzz.cj2356dict.mb.MbUtils;

import android.content.Context;

public class InputMethodStatusCnCj3 extends InputMethodStatusCnCj {

    public InputMethodStatusCnCj3(Context con) {
        super(con);
        this.setSubType(MbUtils.TYPE_CODE_CJGEN3);
        this.setSubTypeName("倉3");
    }
    
    @Override
    public Map<String, Object> getKeysNameMap() {
        Map<String, Object> mbTransMap = super.getKeysNameMap();
        // 依Arthurmcarthur建議，z鍵中文字母改爲片字：
        // 实际上官方的「z」一直是「片」用于片语，补完三代因为忠于官方，希望能在这方面上与官方保持一致
        mbTransMap.put("z", "片");
        return mbTransMap;
    }

    @Override
    public List<Item> getCandidatesInfo(String code, boolean extraResolve) {
        return MbUtils.selectDbByCode(new String[] { MbUtils.TYPE_CODE_CJINTERSECT, MbUtils.TYPE_CODE_CJGEN3 }, code,
                false, null, extraResolve);
    }

    @Override
    public List<Item> getCandidatesInfoByChar(String cha) {
        return MbUtils.selectDbByChar(new String[] { MbUtils.TYPE_CODE_CJINTERSECT, MbUtils.TYPE_CODE_CJGEN3 }, cha);
    }

    @Override
    public boolean couldContinueInputing(String code) {
        return MbUtils.existsDBLikeCode(new String[] { MbUtils.TYPE_CODE_CJINTERSECT, MbUtils.TYPE_CODE_CJGEN3 }, code);
    }

    @Override
    public String getInputMethodName() {
        return MbUtils.getInputMethodName(MbUtils.TYPE_CODE_CJGEN3);
    }
}
