package com.example.demodialog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "JumbilinDb";


    private static final String TABLE_VIDEOS_IDENTIFIER = "table_videos_identifier";
    private static final String VIDEO_IDENTIFIER_COL_KEY = "videos_identifier_key";
    private static final String VIDEO_IDENTIFIER_COL_DOWNLOAD = "videos_identifier_download";


    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_VIDEOS_TABLE = "CREATE TABLE " + TABLE_VIDEOS_IDENTIFIER + "("
                + VIDEO_IDENTIFIER_COL_KEY + " TEXT PRIMARY KEY,"
                + VIDEO_IDENTIFIER_COL_DOWNLOAD + " INTEGER"
                + ")";


        db.execSQL(CREATE_VIDEOS_TABLE);
    }



    public void addVideosIdentifier(String identifier, int downloaded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEO_IDENTIFIER_COL_KEY, identifier);
        values.put(VIDEO_IDENTIFIER_COL_DOWNLOAD, downloaded);
        db.insert(TABLE_VIDEOS_IDENTIFIER, null, values);
        db.close();
    }

    public int updateVideosIdentifier(VideosIdentifierListDbModel videosIdentifierListDbModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEO_IDENTIFIER_COL_KEY, videosIdentifierListDbModel.getKey());
        values.put(VIDEO_IDENTIFIER_COL_DOWNLOAD, videosIdentifierListDbModel.getIsDownloaded());
        return db.update(TABLE_VIDEOS_IDENTIFIER, values, VIDEO_IDENTIFIER_COL_KEY + " = ?",
                new String[]{videosIdentifierListDbModel.getKey()});
    }

    public List<VideosIdentifierListDbModel> getVideosIdentifier() {
        List<VideosIdentifierListDbModel> videosIdentifierListDbModels = new ArrayList<VideosIdentifierListDbModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEOS_IDENTIFIER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VideosIdentifierListDbModel vd = new VideosIdentifierListDbModel();
                vd.setKeys(cursor.getString(0));
                vd.setIsDownloaded(Integer.parseInt(cursor.getString(1)));
                videosIdentifierListDbModels.add(vd);
            } while (cursor.moveToNext());
        }
        return videosIdentifierListDbModels;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
