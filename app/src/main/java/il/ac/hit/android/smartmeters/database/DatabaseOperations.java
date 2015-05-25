package il.ac.hit.android.smartmeters.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DatabaseOperations extends SQLiteOpenHelper
{
    public static final int database_version = 1;
    public String CreateQueryClientTable = "CREATE TABLE " + Tables.ClientTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";
    public String CreateQueryMeterTable = "CREATE TABLE " + Tables.MeterTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";
    public String CreateQueryReadingTable = "CREATE TABLE " + Tables.ReadingTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";
    public String CreateUserTypeTable = "CREATE TABLE " + Tables.UserTypeTable.TableName + "(" + Tables.UserTypeTable.UserId + " TEXT," + Tables.UserTypeTable.UserType + " TEXT);";

    public DatabaseOperations(Context context)
    {
        super(context, Tables.DataBaseName, null, database_version);
        Log.d("Dtabase operations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb)
    {
        sdb.execSQL(CreateQueryClientTable);
        Log.d("Dtabase operations", "Client table created");
        sdb.execSQL(CreateQueryMeterTable);
        Log.d("Dtabase operations", "Meter table created");
        sdb.execSQL(CreateQueryReadingTable);
        Log.d("Dtabase operations", "Reading table created");
        sdb.execSQL(CreateUserTypeTable);
        Log.d("Dtabase operations", "User Type table created");


        putDemoClientInfo();
        putDemoUserTypeInfo();
        putDemoMeterInfo();
        putDemoReadingInfo();

        Log.d("Dtabase operations", "All the database: \n\n" + getAllTablesString(this));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2)
    {
        Log.i("Dtabase operations", "onUpgrade the table: " + Tables.ClientTable.TableName);
        Log.i("Dtabase operations", "onUpgrade the table: " + Tables.MeterTable.TableName);
        Log.i("Dtabase operations", "onUpgrade the table: " + Tables.ReadingTable.TableName);
        Log.i("Dtabase operations", "onUpgrade the table: " + Tables.UserTypeTable.TableName);

        db.execSQL("DROP TABLE IF EXISTS " + Tables.ClientTable.TableName);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.MeterTable.TableName);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ReadingTable.TableName);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.UserTypeTable.TableName);
        onCreate(db);
    }

    public long setClient(DatabaseOperations dop, String userId, String password, String clientName, String address, String phoneNumber)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tables.ClientTable.UserId, userId);
        cv.put(Tables.ClientTable.Password, password);
        cv.put(Tables.ClientTable.ClientName, clientName);
        cv.put(Tables.ClientTable.Address, address);
        cv.put(Tables.ClientTable.PhoneNumber, phoneNumber);
        long k = SQ.insert(Tables.ClientTable.TableName, null, cv);

        SQ.close();
        Log.d("Dtabase operations", "One raw inserted to Client table");

        return k;
    }

    public void setMeter(DatabaseOperations dop, String meterID, String userId, String address, String kWh)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tables.MeterTable.MeterID, meterID);
        cv.put(Tables.MeterTable.UserId, userId);
        cv.put(Tables.MeterTable.Address, address);
        cv.put(Tables.MeterTable.kWh, kWh);
        long k = SQ.insert(Tables.MeterTable.TableName, null, cv);
        Log.d("Dtabase operations", "One raw inserted to Meter table");
    }

    public long setRead(DatabaseOperations dop, String readingID, String meterID, String date, String time, String kWhRead)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tables.ReadingTable.ReadingID, readingID);
        cv.put(Tables.ReadingTable.MeterID, meterID);
        cv.put(Tables.ReadingTable.Date, date);
        cv.put(Tables.ReadingTable.Time, time);
        cv.put(Tables.ReadingTable.kWhRead, kWhRead);
        long k = SQ.insert(Tables.ReadingTable.TableName, null, cv);

        SQ.close();
        Log.d("Dtabase operations", "One raw inserted to Reading table");

        return k;
    }

    public long setUserType(DatabaseOperations dop, String userId, String userType)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Tables.UserTypeTable.UserId, userId);
        cv.put(Tables.UserTypeTable.UserType, userType);

        long k = SQ.insert(Tables.ClientTable.TableName, null, cv);

        SQ.close();
        Log.d("Dtabase operations", "One raw inserted to User Type table");

        return k;
    }


    public Cursor getClientInfo(DatabaseOperations dop)
    {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloumns = {Tables.ClientTable.UserId, Tables.ClientTable.Password, Tables.ClientTable.ClientName, Tables.ClientTable.Address, Tables.ClientTable.PhoneNumber};
        Cursor CR = SQ.query(Tables.ClientTable.TableName, coloumns, null, null, null, null, null);
        return CR;
    }

    public Cursor getMeterInfo(DatabaseOperations dop)
    {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloumns = {Tables.MeterTable.MeterID, Tables.MeterTable.UserId, Tables.MeterTable.Address, Tables.MeterTable.kWh};
        Cursor CR = SQ.query(Tables.MeterTable.TableName, coloumns, null, null, null, null, null);
        return CR;
    }

    public Cursor getReadingInfo(DatabaseOperations dop)
    {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloumns = {Tables.ReadingTable.ReadingID, Tables.ReadingTable.MeterID, Tables.ReadingTable.Date, Tables.ReadingTable.Time, Tables.ReadingTable.kWhRead};
        Cursor CR = SQ.query(Tables.ReadingTable.TableName, coloumns, null, null, null, null, null);
        return CR;
    }

    public Cursor getUserTypeInfo(DatabaseOperations dop)
    {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloumns = {Tables.UserTypeTable.UserId, Tables.UserTypeTable.UserType};
        Cursor CR = SQ.query(Tables.UserTypeTable.TableName, coloumns, null, null, null, null, null);
        return CR;
    }

    private void putDemoUserTypeInfo()
    {
        this.setUserType(this, "000", Tables.UserTypeTable.UserTypes.CLINET);
        this.setUserType(this, "123", Tables.UserTypeTable.UserTypes.CLINET);
        this.setUserType(this, "456", Tables.UserTypeTable.UserTypes.CLINET);
        this.setUserType(this, "654", Tables.UserTypeTable.UserTypes.CLINET);
        this.setUserType(this, "951", Tables.UserTypeTable.UserTypes.ADMIN);
        this.setUserType(this, "753", Tables.UserTypeTable.UserTypes.SUPPORT);
    }

    private void putDemoClientInfo()
    {
        this.setClient(this, "951", "123", "Tom", "חולון הטכנולגי המכון", "");
        this.setClient(this, "753", "123", "Koko", "חולון הטכנולגי המכון", "");
        this.setClient(this, "000", "mendy", "Mendy", "ראשון 30 מוהליבר", "0549330469");
        this.setClient(this, "123", "inga", "Inga", "22 הערבה חולון", "0524669721");
        this.setClient(this, "456", "alon", "Alon", "לציון ראשון מערב", "");
        this.setClient(this, "654", "idan", "Idan", "המלך יהואחז 5 אשדוד", "");
    }

    private void putDemoMeterInfo()
    {
        this.setMeter(this, "11111", "123", "המלך יהואחז 5 אשדוד", "7800");
        this.setMeter(this, "22222", "123", "הציונות 1 אשדוד", "1400");
        this.setMeter(this, "33333", "123", "הרב ניסים 13 אשדוד", "9000");
        this.setMeter(this, "01111", "000", "בלפור 14 תל אביב", "500");
        this.setMeter(this, "02222", "000", "רוטשילד 4 תל אביב", "10085");
    }

    private void putDemoReadingInfo()
    {
        this.setRead(this, "1", "11111", "01,01,2015", "00:00:00", "30");
        this.setRead(this, "2", "11111", "02,01,2015", "00:00:00", "45");
        this.setRead(this, "3", "11111", "01,02,2015", "00:00:00", "34");
        this.setRead(this, "4", "11111", "02,02,2015", "00:00:00", "67");
        this.setRead(this, "5", "11111", "01,01,2014", "00:00:00", "34");
        this.setRead(this, "6", "11111", "01,02,2015", "00:00:00", "23");

        this.setRead(this, "7", "22222", "01,01,2015", "00:00:00", "34");
        this.setRead(this, "8", "22222", "02,01,2015", "00:00:00", "25");
        this.setRead(this, "9", "22222", "01,02,2015", "00:00:00", "45");
        this.setRead(this, "10", "22222", "02,02,2015", "00:00:00", "67");
        this.setRead(this, "11", "22222", "01,01,2014", "00:00:00", "55");
        this.setRead(this, "12", "22222", "01,02,2015", "00:00:00", "88");

        this.setRead(this, "13", "33333", "01,01,2015", "00:00:00", "35");
        this.setRead(this, "14", "33333", "02,01,2015", "00:00:00", "46");
        this.setRead(this, "15", "33333", "01,02,2015", "00:00:00", "57");
        this.setRead(this, "15", "33333", "02,02,2015", "00:00:00", "37");
        this.setRead(this, "16", "33333", "01,01,2014", "00:00:00", "25");
        this.setRead(this, "17", "33333", "01,02,2015", "00:00:00", "22");

        this.setRead(this, "18", "01111", "01,01,2015", "00:00:00", "11");
        this.setRead(this, "19", "01111", "02,01,2015", "00:00:00", "23");
        this.setRead(this, "20", "01111", "01,02,2015", "00:00:00", "15");
        this.setRead(this, "21", "01111", "02,02,2015", "00:00:00", "74");
        this.setRead(this, "22", "01111", "01,01,2014", "00:00:00", "99");
        this.setRead(this, "23", "01111", "01,02,2015", "00:00:00", "67");

        this.setRead(this, "24", "02222", "01,01,2015", "00:00:00", "67");
        this.setRead(this, "25", "02222", "02,01,2015", "00:00:00", "64");
        this.setRead(this, "26", "02222", "01,02,2015", "00:00:00", "46");
        this.setRead(this, "27", "02222", "02,02,2015", "00:00:00", "28");
        this.setRead(this, "28", "02222", "01,01,2014", "00:00:00", "83");
        this.setRead(this, "29", "02222", "01,02,2015", "00:00:00", "44");
    }

    public boolean checkIfUserNameExists(String clientNameFromUser)
    {

        Cursor cursor = getClientInfo(this);
        String clientNameDb;

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            clientNameDb = cursor.getString(2);
            Log.d("Dtabase operations", "Check if: " + clientNameFromUser + ", from user, is equals to: " + clientNameDb);
            if (clientNameDb.equalsIgnoreCase(clientNameFromUser))
            {
                cursor.close();
                return true;
            }

            cursor.moveToNext();
        }

        cursor.close();
        return false;
    }

    public String isUserNameAndPasswordAreCorrect(String clientName, String password)
    {
        Cursor cursor = getClientInfo(this);
        String clientNameDb;
        String passwordDb;
        String clientId;

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            clientNameDb = cursor.getString(2);
            passwordDb = cursor.getString(1);

            Log.d("Dtabase operations", "Check if: " + clientName + ", from user, is equals to: " + clientNameDb);
            Log.d("Dtabase operations", "Check if: " + password + ", from user, is equals to: " + passwordDb);

            if (clientNameDb.equalsIgnoreCase(clientName))
            {
                if (passwordDb.equalsIgnoreCase(password))
                {
                    clientId = cursor.getString(0);
                    cursor.close();

                    return clientId;
                }
            }

            cursor.moveToNext();
        }

        cursor.close();

        return null;
    }

    public String getUserTypeById(String userId)
    {
        Cursor cursor = getUserTypeInfo(this);
        String userIdDb;
        String userTypeDb;

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            userIdDb = cursor.getString(0);

            Log.d("Dtabase operations", "Check if: " + userId + ", from user, is equals to: " + userIdDb);

            if (userIdDb.equalsIgnoreCase(userId))
            {
                userTypeDb = cursor.getString(1);
                cursor.close();

                return userTypeDb;
            }

            cursor.moveToNext();
        }

        cursor.close();

        return null;
    }

    private String getAllTablesString(DatabaseOperations databaseOperations)
    {
        Log.i("Dtabase operations", "Get all the tables");
        StringBuffer stringBuffer = new StringBuffer();

        Cursor cursor = getClientInfo(databaseOperations);

        stringBuffer.append("---Client Table--");
        stringBuffer.append(System.getProperty("line.separator"));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            stringBuffer.append(cursor.getString(0));
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(cursor.getString(3));
            stringBuffer.append(cursor.getString(5));
            stringBuffer.append(System.getProperty("line.separator"));
            cursor.moveToNext();
        }

        stringBuffer.append("---------------------------");
        stringBuffer.append(System.getProperty("line.separator"));
        cursor.close();

        ////////////////////////////////////////////////////
        cursor = getUserTypeInfo(databaseOperations);

        stringBuffer.append("---User Type Table--");
        stringBuffer.append(System.getProperty("line.separator"));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            stringBuffer.append(cursor.getString(0));
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(System.getProperty("line.separator"));
            cursor.moveToNext();
        }

        stringBuffer.append("---------------------------");
        stringBuffer.append(System.getProperty("line.separator"));
        cursor.close();

        ////////////////////////////////////////////////////
        cursor = getMeterInfo(databaseOperations);

        stringBuffer.append("---Meter Table--");
        stringBuffer.append(System.getProperty("line.separator"));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            stringBuffer.append(cursor.getString(0));
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(cursor.getString(3));
            stringBuffer.append(System.getProperty("line.separator"));
            cursor.moveToNext();
        }

        stringBuffer.append("---------------------------");
        stringBuffer.append(System.getProperty("line.separator"));
        cursor.close();

        ////////////////////////////////////////////////////
        cursor = getMeterInfo(databaseOperations);

        stringBuffer.append("---Meter Table--");
        stringBuffer.append(System.getProperty("line.separator"));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            stringBuffer.append(cursor.getString(0));
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(cursor.getString(3));
            stringBuffer.append(System.getProperty("line.separator"));
            cursor.moveToNext();
        }

        stringBuffer.append("---------------------------");
        stringBuffer.append(System.getProperty("line.separator"));
        cursor.close();

        ////////////////////////////////////////////////////
        cursor = getReadingInfo(databaseOperations);

        stringBuffer.append("---Reading Table--");
        stringBuffer.append(System.getProperty("line.separator"));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            stringBuffer.append(cursor.getString(0));
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(cursor.getString(3));
            stringBuffer.append(cursor.getString(4));
            stringBuffer.append(System.getProperty("line.separator"));
            cursor.moveToNext();
        }

        stringBuffer.append("---------------------------");
        stringBuffer.append(System.getProperty("line.separator"));
        cursor.close();


        return stringBuffer.toString();
    }
}

