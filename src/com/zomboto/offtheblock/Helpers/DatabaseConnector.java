package com.zomboto.offtheblock.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import com.zomboto.offtheblock.Providers.OffTheBlockProvider;

import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DATABASE_NAME = "OffTheBlock";
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    //Tables
    public static final String AREA_CODES = "blocked_codes";
    public static final String LOGS = "log_table";

    //Columns
    private static final String ID = "_id";

    public DatabaseConnector(Context context)
    {
        databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    }

    public void open() throws SQLException
    {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public Cursor query(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();
        qb.setTables(table);
        //Log.d(DB, String.format("Query is %s", qb.buildQuery(projection, selection, null, null, null, sortOrder)));
        return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public void close()
    {
        if (database != null) {
            database.close();
        }
    }

    public long insertAreaCode(ContentValues values) throws SQLException {
        open();
        long newId = database.insert(AREA_CODES, null, values);
        close();
        return newId;
    }

    public long insertLog(String description) throws SQLException
    {
        ContentValues values = new ContentValues();
        values.put(OffTheBlockProvider.Log.DESCRIPTION, description);
        return insertLog(values);
    }

    public long insertLog(ContentValues values) throws SQLException {
        open();
        long newId = database.insert(LOGS, null, values);
        close();
        return newId;
    }



    public Cursor getAllAreaCodes() {
        return database.query(AREA_CODES, new String[] {ID, OffTheBlockProvider.AllowedCodes.AREA_CODE}, null,
                null, null, null, OffTheBlockProvider.AllowedCodes.AREA_CODE);
    }

    public Cursor getAreaCode(String id) {
        return database.query(AREA_CODES, null, String.format("%s=%s", ID, id), null, null, null, null);
    }

    public void deleteAreaCode(String id) throws SQLException {
        open();
        database.delete(AREA_CODES, String.format("%s=%s", ID, id), null);
        close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("CREATE TABLE %s (%s integer primary key autoincrement,%s text);",
                    AREA_CODES, ID, OffTheBlockProvider.AllowedCodes.AREA_CODE));

            db.execSQL(String.format("CREATE TABLE %s (%s integer primary key autoincrement,%s text);",
                    LOGS, ID, OffTheBlockProvider.Log.DESCRIPTION));

            db.execSQL(String.format("INSERT INTO %s(%s) VALUES('%d');",
                    AREA_CODES, OffTheBlockProvider.AllowedCodes.AREA_CODE, 412));

            db.execSQL(String.format("INSERT INTO %s(%s) VALUES('%d');",
                    AREA_CODES, OffTheBlockProvider.AllowedCodes.AREA_CODE, 724));

            db.execSQL(String.format("INSERT INTO %s(%s) VALUES('%s');",
                    LOGS, OffTheBlockProvider.Log.DESCRIPTION, "This is just a test..."));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(String.format("DROP TABLE IF EXISTS '%s'", AREA_CODES));
            db.execSQL(String.format("DROP TABLE IF EXISTS '%s'", LOGS));
        }
    }
}
