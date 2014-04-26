package com.zomboto.offtheblock.Providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.zomboto.offtheblock.Helpers.DatabaseConnector;

import java.sql.SQLException;

public class OffTheBlockProvider extends ContentProvider {
    public static final String AUTHORITY = "com.zomboto.offtheblock.Providers.OffTheBlockProvider";

    public static final class AllowedCodes implements BaseColumns {
        public static final String AREA_CODE_URI = "AreaCode";
        public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, AREA_CODE_URI));

        public static final String AREA_CODE = "AREA_CODE";
    }

    public static final class Log implements BaseColumns {
        public static final String LOG_URI = "Log";
        public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY, LOG_URI));
        public static final String DESCRIPTION = "DESCRIPTION";
    }

    private DatabaseConnector databaseConnector;

    @Override
    public boolean onCreate() {
        databaseConnector = new DatabaseConnector(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String uriSelection = uri.getLastPathSegment();
        if(uriSelection.equals(AllowedCodes.AREA_CODE_URI))
        {
            Cursor c = databaseConnector.query(DatabaseConnector.AREA_CODES, projection, selection, selectionArgs, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
        else if (uriSelection.equals(Log.LOG_URI))
        {
            Cursor c = databaseConnector.query(DatabaseConnector.LOGS, projection, selection, selectionArgs, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long result = databaseConnector.insertAreaCode(values);
            if (result > 0) {
                Uri rowUri = ContentUris.appendId(
                        AllowedCodes.CONTENT_URI.buildUpon(), result).build();
                getContext().getContentResolver().notifyChange(rowUri, null);
                return rowUri;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
