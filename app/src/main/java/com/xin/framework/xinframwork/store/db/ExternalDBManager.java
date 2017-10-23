package com.xin.framework.xinframwork.store.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description :  使用外部的.db文件，扩展示例
 * Created by 王照鑫 on 2016/9/21 0021.
 */

public class ExternalDBManager {

    private static final String DB_NAME = " 数据库名称.db";
    private Context mContext;

    public ExternalDBManager(Context mContext) {
        this.mContext = mContext;
    }

    // 把assets目录下的db文件复制到dbPath下
    public SQLiteDatabase setDBManager(String packName) {
        @SuppressLint("SdCardPath") String dbPath = "/data/data/" + packName + "/databases/" + DB_NAME;
        if (!new File(dbPath).exists()) {
            try {
                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = mContext.getAssets()
                        .open(DB_NAME);
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = in.read(buffer)) != -1)
                    out.write(buffer,
                            0,
                            readBytes);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();


            }
        }


        return SQLiteDatabase.openOrCreateDatabase(dbPath,
                null);
    }

//    // 查询
//    public List<AreaFstLevelModel> queryFirst(SQLiteDatabase sqliteDB,
//                                              String[] columns,
//                                              String selection,
//                                              String[] selectionArgs) {
//        ArrayList<AreaFstLevelModel> areaFstLevelModels = new ArrayList<>();
//        ArrayList<AreaSecondLevelModel> areaSecondLevelModels;
//        AreaFstLevelModel areaFstLevelModel;
//        Cursor cursor = null;
//        try {
//            String table = "area_first_level_model";
//            cursor = sqliteDB.query(table,
//                                    columns,
//                                    selection,
//                                    selectionArgs,
//                                    null,
//                                    null,
//                                    "code");
//            while (cursor.moveToNext()) {
//                String code = cursor.getString(cursor.getColumnIndex("code"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                areaFstLevelModel = new AreaFstLevelModel();
//                areaFstLevelModel.setCode(code);
//                areaFstLevelModel.setName(name);
//                if (!(areaSecondLevelModels = querySecond(sqliteDB,
//                                                          null,
//                                                          "bind_code=?",
//                                                          new String[] { code })).isEmpty()) {
//                    areaFstLevelModel.setSecondLevelList(areaSecondLevelModels);
//
//                }
//
//                areaFstLevelModels.add(areaFstLevelModel);
//            }
//            return areaFstLevelModels;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return areaFstLevelModels;
//    }
//
//    // 查询
//    public ArrayList<AreaSecondLevelModel> querySecond(SQLiteDatabase sqliteDB,
//                                                       String[] columns,
//                                                       String selection,
//                                                       String[] selectionArgs) {
//        ArrayList<AreaSecondLevelModel> areaSecondLevelModels = new ArrayList<>();
//        AreaSecondLevelModel areaSecondLevelModel;
//        ArrayList<AreaThirdLevelModel> areaThirdLevelModels;
//        Cursor cursor = null;
//        try {
//            String table = "area_second_level_model";
//            cursor = sqliteDB.query(table,
//                                    columns,
//                                    selection,
//                                    selectionArgs,
//                                    null,
//                                    null,
//                                    "code");
//            while (cursor.moveToNext()) {
//                String code = cursor.getString(cursor.getColumnIndex("code"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String bindCode = cursor.getString(cursor.getColumnIndex("bind_code"));
//                areaSecondLevelModel = new AreaSecondLevelModel();
//                areaSecondLevelModel.setCode(code);
//                areaSecondLevelModel.setName(name);
//                areaSecondLevelModel.setBindCode(bindCode);
//
//                if (!(areaThirdLevelModels = queryThird(sqliteDB,
//                                                         null,
//                                                         "bind_code=?",
//                                                         new String[] { code })).isEmpty()) {
//                    areaSecondLevelModel.setThirdLevelList(areaThirdLevelModels);
//                }
//
//                areaSecondLevelModels.add(areaSecondLevelModel);
//            }
//            return areaSecondLevelModels;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return areaSecondLevelModels;
//    }
//
//    // 查询
//    public ArrayList<AreaThirdLevelModel> queryThird(SQLiteDatabase sqliteDB,
//                                                      String[] columns,
//                                                      String selection,
//                                                      String[] selectionArgs) {
//        ArrayList<AreaThirdLevelModel> areaThirdLevelModels = new ArrayList<>();
//        AreaThirdLevelModel areaThirdLevelModel;
//        Cursor cursor = null;
//        try {
//            String table = "area_third_level_model";
//            cursor = sqliteDB.query(table,
//                                    columns,
//                                    selection,
//                                    selectionArgs,
//                                    null,
//                                    null,
//                                    "code");
//            while (cursor.moveToNext()) {
//                String code = cursor.getString(cursor.getColumnIndex("code"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String bindCode = cursor.getString(cursor.getColumnIndex("bind_code"));
//                areaThirdLevelModel = new AreaThirdLevelModel();
//                areaThirdLevelModel.setCode(code);
//                areaThirdLevelModel.setName(name);
//                areaThirdLevelModel.setBindCode(bindCode);
//
//                areaThirdLevelModels.add(areaThirdLevelModel);
//            }
//
//            return areaThirdLevelModels;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return areaThirdLevelModels;
//    }
}
