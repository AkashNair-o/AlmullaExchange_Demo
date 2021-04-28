package com.example.almullaexchange_demo.toDo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;



    //constructor
    public DatabaseHelper(@Nullable Context context)
    {
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + Database.TABLE_NAME + "("
                        + Database.COLUMN_ID + " TEXT ,"
                        + Database.COLUMN_TITLE + " TEXT ,"
                        + Database.COLUMN_TIME+ " TEXT)";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + Database.TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //CRUD OPERATIONS
    public void addData(model_List dataModelFolders)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        final String title = String.valueOf(dataModelFolders.getTitle());
        final String time = String.valueOf(dataModelFolders.getTime());
        final String id = String.valueOf(dataModelFolders.getId());

        cv.put(Database.COLUMN_ID, id);
        cv.put(Database.COLUMN_TITLE, title);
        cv.put(Database.COLUMN_TIME, time);

        db.insert(Database.TABLE_NAME, null, cv);
        db.close();
    }



    public List<model_List> getdata()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<model_List> folderList = new ArrayList<>();

        String getAll = "SELECT * FROM " + Database.TABLE_NAME;
        Cursor cursor = db.rawQuery(getAll, null);

        if (cursor.moveToFirst())
        {
            do
                {
                    String title = cursor.getString(cursor.getColumnIndex(Database.COLUMN_TITLE));
                    String time = cursor.getString(cursor.getColumnIndex(Database.COLUMN_TIME));
                    String id = cursor.getString(cursor.getColumnIndex(Database.COLUMN_ID));

                    folderList.add(new model_List(id, title, String.valueOf(time)));
                }
            while (cursor.moveToNext());
        }

        return folderList;
    }



    private void updateData(String cid, String folder_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.COLUMN_ID, 1);
        String selection = Database.COLUMN_ID+" LIKE ?";
        String[] selection_args = {String.valueOf(cid)};
        db.update(Database.TABLE_NAME, contentValues, selection, selection_args);
        db.close();
    }


    public void deleteData(int position)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            StringBuilder query = new StringBuilder("DELETE FROM " + Database.TABLE_NAME +" WHERE id = ").append(position);
            Cursor cursor = db.rawQuery(query.toString(), null);
            if (cursor.moveToFirst())
            {

            }
            cursor.close();
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Deleting Failed", Toast.LENGTH_SHORT).show();
        }
    }

}
