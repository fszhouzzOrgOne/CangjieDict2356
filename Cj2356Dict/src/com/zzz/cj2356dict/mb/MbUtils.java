package com.zzz.cj2356dict.mb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zzz.cj2356dict.vo.Group;
import com.zzz.cj2356dict.vo.Item;

/**
 * 加载碼表數據
 */
public class MbUtils {

    private static Context context;
    private static SQLiteDatabase mbdb = null;
    private static String dbName = "cjmbdb.db";
    private static String genTbName = "t_mb_type"; // 碼表名表
    private static String genClNameId = "_id";
    private static String genClNameGen = "type_code";
    private static String genClNameName = "type_name";
    private static String mbTbName = "t_mb_content"; // 碼表
    private static String mbClNameId = "_id";
    private static String mbClNameGen = "type_code";
    private static String mbClNameCod = "mb_code";
    private static String mbClNameVal = "mb_char";

    private static Map<String, String> mbTransMap = new HashMap<String, String>();

    /**
     * 初始化碼表數據
     */
    public static void init(Context mContext) {
        context = mContext;
        initMbdb();
        initMbTransMap();
    }

    private static void initMbdb() {
        String dest = context.getFilesDir().toString() + File.separator
                + dbName;
        try {
            IOUtils.copyFile(dbName, dest);
        } catch (Exception e) {
        }
        try {
            mbdb = SQLiteDatabase.openOrCreateDatabase(dest, null);
        } catch (SQLException e) {
        }
    }

    private static void initMbTransMap() {
        ArrayList<Group> mbTypes = initGroupDatas();
        String typeCode = null;
        for (Group g : mbTypes) {
            typeCode = g.getgCode();
            mbTransMap.put("a" + typeCode, "日");
            mbTransMap.put("b" + typeCode, "月");
            mbTransMap.put("c" + typeCode, "金");
            mbTransMap.put("d" + typeCode, "木");
            mbTransMap.put("e" + typeCode, "水");
            mbTransMap.put("f" + typeCode, "火");
            mbTransMap.put("g" + typeCode, "土");
            mbTransMap.put("h" + typeCode, "竹");
            mbTransMap.put("i" + typeCode, "戈");
            mbTransMap.put("j" + typeCode, "十");
            mbTransMap.put("k" + typeCode, "大");
            mbTransMap.put("l" + typeCode, "中");
            mbTransMap.put("m" + typeCode, "一");
            mbTransMap.put("n" + typeCode, "弓");
            mbTransMap.put("o" + typeCode, "人");
            mbTransMap.put("p" + typeCode, "心");
            mbTransMap.put("q" + typeCode, "手");
            mbTransMap.put("r" + typeCode, "口");
            mbTransMap.put("s" + typeCode, "尸");
            mbTransMap.put("t" + typeCode, "廿");
            mbTransMap.put("u" + typeCode, "山");
            mbTransMap.put("v" + typeCode, "女");
            mbTransMap.put("w" + typeCode, "田");
            mbTransMap.put("x" + typeCode, "難");
            mbTransMap.put("y" + typeCode, "卜");
            mbTransMap.put("z" + typeCode, "符");
        }
        typeCode = "cj6";
        mbTransMap.put("h" + typeCode, "的");
        mbTransMap.put("x" + typeCode, "止");
        mbTransMap.put("z" + typeCode, "片");
    }

    /**
     * 英文編碼翻譯成中文編碼
     * 
     * @author t
     * @time 2016-12-18下午3:19:17
     */
    public static String translateEN2ZH(String en, String genCode) {
        String zh = "";
        for (int i = 0; i < en.length(); i++) {
            String ent = ((Character) en.charAt(i)).toString();
            zh += mbTransMap.get(ent + genCode);
        }
        return zh;
    }

    /**
     * 按字符查詢
     * 
     * @author t
     * @time 2016-12-18下午1:03:12
     */
    public static ArrayList<Group> selectDbByChar(String cha) {
        if (null == mbdb || null == cha || cha.trim().length() == 0) {
            return null;
        }
        cha = cha.trim();

        ArrayList<Group> gData = initGroupDatas();
        Cursor cursor = mbdb.query(mbTbName, null, mbClNameVal + " = ?",
                new String[] { cha }, null, null, null);
        handleSelectResultCursor(gData, cursor);
        return gData;
    }

    /**
     * 按編碼查詢
     * 
     * @author t
     * @time 2016-12-18下午1:03:35
     */
    public static ArrayList<Group> selectDbByCode(String code) {
        if (null == mbdb || null == code || code.trim().length() == 0) {
            return null;
        }
        code = code.trim();

        ArrayList<Group> gData = initGroupDatas();
        Cursor cursor = mbdb.query(mbTbName, null, mbClNameCod + " like ? ",
                new String[] { code + "%" }, null, null, mbClNameCod + " asc ");
        handleSelectResultCursor(gData, cursor);
        return gData;
    }

    /** 游標的動作 */
    private static void handleSelectResultCursor(ArrayList<Group> gData,
            Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (true) {
                int idVal = cursor.getInt(cursor.getColumnIndex(mbClNameId));
                String genVal = cursor.getString(cursor
                        .getColumnIndex(mbClNameGen));
                String codeVal = cursor.getString(cursor
                        .getColumnIndex(mbClNameCod));
                String charVal = cursor.getString(cursor
                        .getColumnIndex(mbClNameVal));
                Item item = new Item(idVal, genVal, codeVal, charVal);
                for (Group g : gData) {
                    if (g.getgCode().equals(item.getGenCode())) {
                        if (g.getItems().size() == 1
                                && g.getItems().get(0).isEmpty()) {
                            g.getItems().clear();
                        }
                        g.getItems().add(item);
                    }
                }

                if (!cursor.moveToNext()) {
                    break;
                }
            }
        }
        cursor.close();
    }

    /**
     * 初始化分組列表
     * 
     * @author t
     * @time 2016-12-18下午1:02:18
     */
    public static ArrayList<Group> initGroupDatas() {
        ArrayList<Group> gData = null;
        Cursor cursor = mbdb.rawQuery("select * from " + genTbName
                + " order by " + genClNameId + " desc ", null);
        if (cursor.moveToFirst()) {
            gData = new ArrayList<Group>();
            while (true) {
                int idVal = cursor.getInt(cursor.getColumnIndex(genClNameId));
                String typeVal = cursor.getString(cursor
                        .getColumnIndex(genClNameGen));
                String nameVal = cursor.getString(cursor
                        .getColumnIndex(genClNameName));
                gData.add(new Group(idVal, typeVal, nameVal));

                if (!cursor.moveToNext()) {
                    break;
                }
            }
        }
        cursor.close();
        return gData;
    }

}
