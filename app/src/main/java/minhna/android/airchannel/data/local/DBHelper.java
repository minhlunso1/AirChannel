package minhna.android.airchannel.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.injection.annotation.ApplicationContext;
import minhna.android.airchannel.injection.annotation.DatabaseInfo;

/**
 * Created by Minh on 11/9/2017.
 */

@Singleton
public class DBHelper extends SQLiteOpenHelper {

    public static final String FAV_CHANNEL_TABLE_NAME = "channels";
    public static final String FAV_COLUMN_CHANNEL_ID = "channe_id";
    public static final String FAV_COLUMN_CHANNEL_TITLE = "channel_title";

    @Inject
    public DBHelper(@ApplicationContext Context context, @DatabaseInfo String dbName, @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_CHANNEL_TABLE_NAME);
        onCreate(db);
    }

    private void tableCreateStatements(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + FAV_CHANNEL_TABLE_NAME + "("
                            + FAV_COLUMN_CHANNEL_ID + " INTEGER PRIMARY KEY, "
                            + FAV_COLUMN_CHANNEL_TITLE + " VARCHAR(20)" + ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Channel getChannel(Long channelId) throws Resources.NotFoundException, NullPointerException {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + FAV_CHANNEL_TABLE_NAME
                            + " WHERE "
                            + FAV_COLUMN_CHANNEL_ID
                            + " = ? ",
                    new String[]{channelId + ""});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Channel channel = new Channel();
                channel.setChannelId(cursor.getInt(cursor.getColumnIndex(FAV_COLUMN_CHANNEL_ID)));
                channel.setChannelTitle(cursor.getString(cursor.getColumnIndex(FAV_COLUMN_CHANNEL_TITLE)));
                return channel;
            } else {
                throw new Resources.NotFoundException("channel with id " + channelId + " does not exists");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    protected List<Channel> getAllChannel() throws NullPointerException {
        Cursor cursor = null;
        List<Channel> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + FAV_CHANNEL_TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Channel channel = new Channel();
                    channel.setChannelId(cursor.getInt(cursor.getColumnIndex(FAV_COLUMN_CHANNEL_ID)));
                    channel.setChannelTitle(cursor.getString(cursor.getColumnIndex(FAV_COLUMN_CHANNEL_TITLE)));

                    list.add(channel);
                    cursor.moveToNext();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
            return list;
        }
    }

    protected Long insertChannel(Channel channel) throws Exception {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAV_COLUMN_CHANNEL_ID, channel.getChannelId());
            contentValues.put(FAV_COLUMN_CHANNEL_TITLE, channel.getChannelTitle());
            return db.insert(FAV_CHANNEL_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean deleteChannel(int id) throws Exception
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(FAV_CHANNEL_TABLE_NAME, FAV_COLUMN_CHANNEL_ID + "=" + id, null) > 0;
    }
}