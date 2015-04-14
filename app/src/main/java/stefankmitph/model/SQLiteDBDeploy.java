/*
 * Copyright (C) 2012 Ingenious Camel http://ingenious-camel.blogspot.com 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @version 1.1
 * @created 2/22/2012
 */
package stefankmitph.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteDBDeploy {
    private static final String TAG = "SQLiteDBDeploy";
    private static List<String> ignoreSQLs;

    static {
        ignoreSQLs = new LinkedList<String>();
        ignoreSQLs.add("--");
        ignoreSQLs.add("begin transaction;");
        ignoreSQLs.add("commit;");
        ignoreSQLs.add("create table android_metadata (locale text);");
        ignoreSQLs.add("create table \"android_metadata\" (locale text);");
        ignoreSQLs.add("insert into android_metadata values('en_us');");
        ignoreSQLs.add("insert into \"android_metadata\" values('en_us');");
    }

    /**
     * Deploys given zip file in SQLiteDatabase
     *
     * @param sqlLiteDb
     *            the database
     * @param context
     *            to use to open or create the database
     * @param dbName
     *            dump zip file
     * @throws IOException
     */
    public static void deploy(SQLiteDatabase sqlLiteDb, Context context, String dbName) throws IOException {
        Log.i(TAG, "reading zip file: " + dbName);
        InputStream dbStream = context.getAssets().open(dbName);

        deploy(sqlLiteDb, dbStream);

        dbStream.close();
    }

    /**
     * Deploys given zip file url in SQLiteDatabase
     *
     * @param sqlLiteDb
     *            the database
     * @param dbUrl
     *            dump zip file url
     * @throws IOException
     */
    public static void deploy(SQLiteDatabase sqlLiteDb, String dbUrl) throws IOException {
        Log.i(TAG, "reading url: " + dbUrl);

        HttpURLConnection c = (HttpURLConnection) new URL(dbUrl).openConnection();
        c.setRequestMethod("GET");
        c.setDoOutput(true);
        c.connect();
        InputStream dbStream = c.getInputStream();

        deploy(sqlLiteDb, dbStream);

        dbStream.close();
        c.disconnect();
    }

    /**
     * Deploys given dump  file stream in SQLiteDatabase
     *
     * @param sqlLiteDb the database
     * @param dbStream stream to read dump data
     * @throws IOException
     */
    private static void deploy(SQLiteDatabase sqlLiteDb, InputStream dbStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(dbStream));
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            Log.i(TAG, "deploying zip entry: " + entry);
            InputStreamReader dbReader = new InputStreamReader(zis);
            deploy(sqlLiteDb, dbReader);
        }
    }

    /**
     * Deploys given stream in SQLiteDatabase
     *
     * @param sqlLiteDb
     *            the database
     * @param dbReader
     *            stream to read dump SQL statements
     * @throws IOException
     * @throws SQLException
     */
    private static void deploy(SQLiteDatabase sqlLiteDb, InputStreamReader dbReader) throws IOException {
        String sqlLine = null;
        StringBuffer sqlBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(dbReader);

        sqlLiteDb.beginTransaction();
        try {
            while ((sqlLine = bufferedReader.readLine()) != null) {
                String sql = sqlLine.trim();
                if (!isIgnoreSQL(sql)) {
                    if (sql.endsWith(";")) {
                        sqlBuffer.append(sql);
                        String execSQL = sqlBuffer.toString();
                        Log.d(TAG, "running sql=>" + execSQL);
                        sqlLiteDb.execSQL(execSQL);
                        sqlBuffer.delete(0, sqlBuffer.length());
                    } else {
                        if (sqlBuffer.length() > 0) {
                            sqlBuffer.append(' ');
                        }
                        sqlBuffer.append(sql);
                    }
                }
            }
            sqlLiteDb.setTransactionSuccessful();
        } finally {
            sqlLiteDb.endTransaction();
        }
    }

    /**
     * Returns true if the given SQL statement is to be ignored
     * @param sql SQL statement
     * @return
     */
    private static boolean isIgnoreSQL(String sql) {
        if (sql.length() == 0) {
            return true;
        }

        String lowerSQL = sql.toLowerCase();
        for (String ignoreSQL : ignoreSQLs) {
            if (lowerSQL.startsWith(ignoreSQL)) {
                return true;
            }
        }
        return false;
    }
}