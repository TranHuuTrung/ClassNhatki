package com.huutrung.nhatki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huutrung.nhatki.bean.NhatKi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11/20/2017.
 */

public class NhatKiDatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "SQLite";
    // Phiên bản
    private static final int DATABASE_VERSION = 1;
    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "NhatKi_Manager";
    // Tên bảng: Note.
    private static final String TABLE_NHATKI = "NhatKi";
    private static final String COLUMN_NHATKI_ID ="NhatKi_Id";
    private static final String COLUMN_NHATKI_TITLE ="NhatKi_Title";
    private static final String COLUMN_NHATKI_CONTENT = "NhatKi_Content";

    public NhatKiDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //taọ bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "NhatKiDatabaseHelper.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_NHATKI + "("
                + COLUMN_NHATKI_ID + " INTEGER PRIMARY KEY," + COLUMN_NHATKI_TITLE + " TEXT,"
                + COLUMN_NHATKI_CONTENT + " TEXT" + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, "NhatKiDatabaseHelper.onUpgrade ... ");
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHATKI);
        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultNotesIfNeed()  {
        int count = this.getNhatKiCount();
        if(count ==0 ) {
            NhatKi nhatKi1 = new NhatKi("Welcome to app!",
                    "Hello every body");
            this.addNhatKi(nhatKi1);
        }
    }

    public void addNhatKi(NhatKi nki) {
        Log.i(TAG, "NhatKiDatabaseHelper.addNote ... " + nki.getNhatKiTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NHATKI_TITLE, nki.getNhatKiTitle());
        values.put(COLUMN_NHATKI_TITLE, nki.getNhatKiContent());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_NHATKI, null, values);

        // Đóng kết nối database.
        db.close();
    }

    public NhatKi getNhatki(int id) {
        Log.i(TAG, "NhatKiDatabaseHelper.getNhatki ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NHATKI, new String[] { COLUMN_NHATKI_ID,
                        COLUMN_NHATKI_TITLE, COLUMN_NHATKI_CONTENT }, COLUMN_NHATKI_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        NhatKi nhatki = new NhatKi(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return note
        return nhatki;
    }

    public List<NhatKi> getAllNhatKi() {
        Log.i(TAG, "NhatKiDatabaseHelper.getAllNhatKi ... " );

        List<NhatKi> nhatkiList = new ArrayList<NhatKi>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NHATKI;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                NhatKi nhatki = new NhatKi();
                nhatki.setNhatKiId(Integer.parseInt(cursor.getString(0)));
                nhatki.setNhatKiTitle(cursor.getString(1));
                nhatki.setNhatKiContent(cursor.getString(2));
                // Thêm vào danh sách.
                nhatkiList.add(nhatki);
            } while (cursor.moveToNext());
        }
        // return note list
        return nhatkiList;
    }

    public int getNhatKiCount() {
        Log.i(TAG, "NhatKiDatabaseHelper.getNhatKiCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_NHATKI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        // return count
        return count;
    }

    public int updateNhatKi(NhatKi nhatki) {
//        Log.i(TAG, "NhatKiDatabaseHelper.updateNhatKi ... "  + nhatki.getNhatKiTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NHATKI_TITLE, nhatki.getNhatKiTitle());
        values.put(COLUMN_NHATKI_CONTENT, nhatki.getNhatKiContent());

        // updating row
        return db.update(TABLE_NHATKI, values, COLUMN_NHATKI_ID + " = ?",
                new String[]{String.valueOf(nhatki.getNhatKiId())});
    }

    public void deleteNhatKi(NhatKi nhatki) {
        Log.i(TAG, "NhatKiDatabaseHelper.deleteNhatKi ... " + nhatki.getNhatKiTitle() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NHATKI, COLUMN_NHATKI_ID + " = ?",
                new String[] { String.valueOf(nhatki.getNhatKiId()) });
        db.close();
    }
}
