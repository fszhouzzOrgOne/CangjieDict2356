package com.zzz.cj2356dict.state.trans;

import android.content.Context;

/**
 * 其他輸入法
 * 
 * @author t
 * @time 2017-2-9下午9:36:55
 */
public abstract class InputMethodStatusCnElse extends InputMethodStatusCn {

    InputMethodStatusCnElse(Context con) {
        super(con);
        this.setType("else");
        this.setTypeName("牠");
    }

}
