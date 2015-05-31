package il.ac.hit.android.smartmeters.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import il.ac.hit.android.smartmeters.client.Client;

import java.util.ArrayList;
import java.util.List;


public class DatabaseOperations extends SQLiteOpenHelper
{
    public static final int database_version = 2;
    public String CreateQueryClientTable = "CREATE TABLE " + Tables.ClientTable.TableName + "(" + Tables.ClientTable.UserId + " TEXT," + Tables.ClientTable.ClientName + " TEXT," + Tables.ClientTable.Password + " TEXT," + Tables.ClientTable.Address + " TEXT," + Tables.ClientTable.PhoneNumber + " TEXT);";
    public String CreateQueryMeterTable = "CREATE TABLE " + Tables.MeterTable.TableName + "(" + Tables.MeterTable.UserId + " TEXT," + Tables.MeterTable.MeterID + " TEXT," + Tables.MeterTable.Address + " TEXT," + Tables.MeterTable.kWh + " TEXT);";
    public String CreateQueryReadingTable = "CREATE TABLE " + Tables.ReadingTable.TableName + "(" + Tables.ReadingTable.ReadingID + " TEXT," + Tables.ReadingTable.MeterID + " TEXT," + Tables.ReadingTable.Date + " TEXT," + Tables.ReadingTable.Time + " TEXT," + Tables.ReadingTable.kWhRead + " TEXT);";
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


        putDemoInfoToDb(sdb);
    }

    private void putDemoInfoToDb(SQLiteDatabase database)
    {
        putDemoClientInfo(database);
        putDemoUserTypeInfo(database);
        putDemoMeterInfo(database);
        putDemoReadingInfo(database);
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

        SQ.close();
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

        long k = SQ.insert(Tables.UserTypeTable.TableName, null, cv);

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

    private void putDemoUserTypeInfo(SQLiteDatabase database)
    {
        this.setUserTypeDemo(database, "000", Tables.UserTypeTable.UserTypes.CLIENT);
        this.setUserTypeDemo(database, "123", Tables.UserTypeTable.UserTypes.CLIENT);
        this.setUserTypeDemo(database, "456", Tables.UserTypeTable.UserTypes.CLIENT);
        this.setUserTypeDemo(database, "654", Tables.UserTypeTable.UserTypes.CLIENT);
        this.setUserTypeDemo(database, "951", Tables.UserTypeTable.UserTypes.ADMIN);
        this.setUserTypeDemo(database, "753", Tables.UserTypeTable.UserTypes.SUPPORT);
    }

    private void putDemoClientInfo(SQLiteDatabase database)
    {
        this.setClientDemo(database, "951", "123", "Tom", "חולון הטכנולגי המכון", "");
        this.setClientDemo(database, "753", "123", "Koko", "חולון הטכנולגי המכון", "");
        this.setClientDemo(database, "000", "mendy", "Mendy", "ראשון 30 מוהליבר", "0549330469");
        this.setClientDemo(database, "123", "inga", "Inga", "22 הערבה חולון", "0524669721");
        this.setClientDemo(database, "456", "alon", "Alon", "לציון ראשון מערב", "");
        this.setClientDemo(database, "654", "idan", "Idan", "המלך יהואחז 5 אשדוד", "");
    }

    private void putDemoMeterInfo(SQLiteDatabase database)
    {
        this.setMeterDemo(database, "11111", "123", "Holon", "7800"); //המלך יהואחז 5 אשדוד
        this.setMeterDemo(database, "22222", "123", "Tel Aviv", "1400"); //הציונות 1 אשדוד
        this.setMeterDemo(database, "33333", "123", "Bat yam", "9000"); //הרב ניסים 13 אשדוד
        this.setMeterDemo(database, "01111", "000", "Lud", "500");
        this.setMeterDemo(database, "02222", "000", "Yafo", "10085");
    }

    private void putDemoReadingInfo(SQLiteDatabase database)
    {
        this.setReadDemo(database, "1", "11111", "01,01,2015", "00:00:00", "30");
        this.setReadDemo(database, "2", "11111", "02,01,2015", "00:00:00", "45");
        this.setReadDemo(database, "3", "11111", "01,02,2015", "00:00:00", "34");
        this.setReadDemo(database, "4", "11111", "02,02,2015", "00:00:00", "67");
        this.setReadDemo(database, "5", "11111", "01,01,2014", "00:00:00", "34");
        this.setReadDemo(database, "6", "11111", "01,02,2015", "00:00:00", "23");

        this.setReadDemo(database, "7", "22222", "01,01,2015", "00:00:00", "34");
        this.setReadDemo(database, "8", "22222", "02,01,2015", "00:00:00", "25");
        this.setReadDemo(database, "9", "22222", "01,02,2015", "00:00:00", "45");
        this.setReadDemo(database, "10", "22222", "02,02,2015", "00:00:00", "67");
        this.setReadDemo(database, "11", "22222", "01,01,2014", "00:00:00", "55");
        this.setReadDemo(database, "12", "22222", "01,02,2015", "00:00:00", "88");

        this.setReadDemo(database, "13", "33333", "01,01,2015", "00:00:00", "35");
        this.setReadDemo(database, "14", "33333", "02,01,2015", "00:00:00", "46");
        this.setReadDemo(database, "15", "33333", "01,02,2015", "00:00:00", "57");
        this.setReadDemo(database, "15", "33333", "02,02,2015", "00:00:00", "37");
        this.setReadDemo(database, "16", "33333", "01,01,2014", "00:00:00", "25");
        this.setReadDemo(database, "17", "33333", "01,02,2015", "00:00:00", "22");

        this.setReadDemo(database, "18", "01111", "01,01,2015", "00:00:00", "11");
        this.setReadDemo(database, "19", "01111", "02,01,2015", "00:00:00", "23");
        this.setReadDemo(database, "20", "01111", "01,02,2015", "00:00:00", "15");
        this.setReadDemo(database, "21", "01111", "02,02,2015", "00:00:00", "74");
        this.setReadDemo(database, "22", "01111", "01,01,2014", "00:00:00", "99");
        this.setReadDemo(database, "23", "01111", "01,02,2015", "00:00:00", "67");

        this.setReadDemo(database, "24", "02222", "01,01,2015", "00:00:00", "67");
        this.setReadDemo(database, "25", "02222", "02,01,2015", "00:00:00", "64");
        this.setReadDemo(database, "26", "02222", "01,02,2015", "00:00:00", "46");
        this.setReadDemo(database, "27", "02222", "02,02,2015", "00:00:00", "28");
        this.setReadDemo(database, "28", "02222", "01,01,2014", "00:00:00", "83");
        this.setReadDemo(database, "29", "02222", "01,02,2015", "00:00:00", "44");
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

            if (clientNameDb.equalsIgnoreCase(clientName))
            {
                Log.d("Dtabase operations", "Check if: " + password + ", from user, is equals to: " + passwordDb);
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

    public String getPasswordIfUserNameExits(String clientName)
    {
        Cursor cursor = getClientInfo(this);
        String clientNameDb;
        String clientPasswordDb;

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            clientNameDb = cursor.getString(2);

            Log.d("Dtabase operations", "Check if: " + clientName + ", from user, is equals to: " + clientNameDb);

            if (clientNameDb.equalsIgnoreCase(clientName))
            {
                clientPasswordDb = cursor.getString(1);
                cursor.close();

                Log.d("Dtabase operations", "Password found: " + clientPasswordDb);

                return clientPasswordDb;
            }

            cursor.moveToNext();
        }

        cursor.close();

        return null;
    }

    public String getAllTablesString(DatabaseOperations databaseOperations)
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
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(3));
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
            stringBuffer.append(" ");
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
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(" ");
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
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(1));
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(2));
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(3));
            stringBuffer.append(" ");
            stringBuffer.append(cursor.getString(4));
            stringBuffer.append(System.getProperty("line.separator"));
            cursor.moveToNext();
        }

        stringBuffer.append("---------------------------");
        stringBuffer.append(System.getProperty("line.separator"));
        cursor.close();


        return stringBuffer.toString();
    }


    private long setClientDemo(SQLiteDatabase SQ, String userId, String password, String clientName, String address, String phoneNumber)
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.ClientTable.UserId, userId);
        cv.put(Tables.ClientTable.Password, password);
        cv.put(Tables.ClientTable.ClientName, clientName);
        cv.put(Tables.ClientTable.Address, address);
        cv.put(Tables.ClientTable.PhoneNumber, phoneNumber);
        long k = SQ.insert(Tables.ClientTable.TableName, null, cv);


        Log.d("Dtabase operations", "One raw inserted to Client table");

        return k;
    }

    private void setMeterDemo(SQLiteDatabase SQ, String meterID, String userId, String address, String kWh)
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.MeterTable.MeterID, meterID);
        cv.put(Tables.MeterTable.UserId, userId);
        cv.put(Tables.MeterTable.Address, address);
        cv.put(Tables.MeterTable.kWh, kWh);
        long k = SQ.insert(Tables.MeterTable.TableName, null, cv);
        Log.d("Dtabase operations", "One raw inserted to Meter table");
    }

    private long setReadDemo(SQLiteDatabase SQ, String readingID, String meterID, String date, String time, String kWhRead)
    {
        ContentValues cv = new ContentValues();
        cv.put(Tables.ReadingTable.ReadingID, readingID);
        cv.put(Tables.ReadingTable.MeterID, meterID);
        cv.put(Tables.ReadingTable.Date, date);
        cv.put(Tables.ReadingTable.Time, time);
        cv.put(Tables.ReadingTable.kWhRead, kWhRead);
        long k = SQ.insert(Tables.ReadingTable.TableName, null, cv);


        Log.d("Dtabase operations", "One raw inserted to Reading table");

        return k;
    }

    private long setUserTypeDemo(SQLiteDatabase SQ, String userId, String userType)
    {
        ContentValues cv = new ContentValues();

        cv.put(Tables.UserTypeTable.UserId, userId);
        cv.put(Tables.UserTypeTable.UserType, userType);

        long k = SQ.insert(Tables.UserTypeTable.TableName, null, cv);


        Log.d("Dtabase operations", "One raw inserted to User Type table");

        return k;
    }

    public List<Meter> getAllMetersById(String clientId, DatabaseOperations databaseOperations)
    {
        List<Meter> meters = new ArrayList<>();
        Meter meter = null;

        Cursor cursor = getMeterInfo(databaseOperations);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            meter = cursorToMeter(cursor);
            Log.d("Dtabase operations", "Check if meterId from database: " + meter.getUserId() + " is equal to " + clientId);
            if (meter.getUserId().equalsIgnoreCase(clientId))
            {
                meters.add(meter);
            }
            cursor.moveToNext();
        }

        cursor.close();

        return meters;
    }

    private Meter cursorToMeter(Cursor cursor)
    {
        Meter meter = new Meter();

        meter.setUserId(cursor.getString(1));
        meter.setMeterId(cursor.getString(0));
        meter.setAddress(cursor.getString(2));
        meter.setkWh(cursor.getString(3));

        return meter;
    }

    private Client cursorToClient(Cursor cursor)
    {
        Client client = new Client();

        client.setUserId(cursor.getString(0));
        client.setPassword(cursor.getString(1));
        client.setName(cursor.getString(2));
        client.setAddress(cursor.getString(3));
        client.setPhoneNumber(cursor.getString(4));

        return client;
    }

    public Client getClientById(String clientId, DatabaseOperations databaseOperations)
    {
        Client client = null;

        List<Client> clients = new ArrayList<>();

        Cursor cursor = getClientInfo(databaseOperations);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            client = cursorToClient(cursor);
            if (client.getUserId().equalsIgnoreCase(clientId))
            {
                return client;
            }

            cursor.moveToNext();
        }

        return client;
    }
}

