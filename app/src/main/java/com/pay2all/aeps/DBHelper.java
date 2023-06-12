package com.pay2all.aeps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import com.pay2all.aeps.PaytmAEPS.UTLsData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static com.pay2all.aeps.PaytmAEPS.UTLsData.generateKey;


public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME="d_sss";
    public static String TABLE_NAME="tbss_dat";
    public String id="id";
    public String mobile_number="mobile_number";
    public String pan_number="pan_number";
    public String first_name="first_name";
    public String last_name="last_name";
    public String email="email";
    public String pin_code="pin_code";
    public String status_id="status_id";
    public String user_id="user_id";
    public String aadhar_number="aadhar_number";
    public String company="company";
    public String address="address";
    public String created_at="created_at";
    public String updated_at="updated_at";
    public String access_token="access_token";
    public String balance="balance";
    public String aeps_balance="aeps_balance";
    public String bank_account_number="bank_account_number";
    public String ifsc="ifsc";
    public String agent_id_code="agent_id_code";
    public String agent_id_code_matm="agent_id_code_matm";

    Context mContext;

    private String tendency ="";

    public DBHelper(Context context)
    {


        super(context,DATABASE_NAME,null,1);
        mContext=context;
        tendency=mDecoded(mContext.getResources().getString(R.string.sjgcjsgfjhdgfh));

//        tendency=mDecoded(context.getResources().getString(R.string.unknownfile)+context.getResources().getString(R.string.unknownfile1)).substring(31,47);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+id+" INTEGER PRIMARY KEY AUTOINCREMENT,"+access_token+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }


    public void mSave(DBItems items)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        JSONObject jsonObject=new JSONObject();
        try
        {
            jsonObject.put(id,items.getId());
            jsonObject.put(mobile_number,items.getMobile_number());
            jsonObject.put(pan_number,items.getPan_number());
            jsonObject.put(first_name,items.getFirst_name());
            jsonObject.put(last_name,items.getLast_name());
            jsonObject.put(email,items.getEmail());
            jsonObject.put(pin_code,items.getPin_code());
            jsonObject.put(status_id,items.getStatus_id());
            jsonObject.put(user_id,items.getUser_id());
            jsonObject.put(aadhar_number,items.getAadhar_number());
            jsonObject.put(company,items.getCompany());
            jsonObject.put(address,items.getAddress());
            jsonObject.put(created_at,items.getCreated_at());
            jsonObject.put(updated_at,items.getUpdated_at());
            jsonObject.put(balance,items.getBalance());
            jsonObject.put(aeps_balance,items.getAeps_balance());
            jsonObject.put(bank_account_number,items.getBank_account_number());
            jsonObject.put(ifsc,items.getIfsc());
            jsonObject.put(agent_id_code,items.getAgent_id_code());
            jsonObject.put(agent_id_code_matm,items.getAgent_id_code_matm());


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SecretKey secretKey=null;
        try
        {
             secretKey=generateKey(tendency);
        }
        catch (NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
           e.printStackTrace();
        }
        try {
            cv.put(access_token, mEncodByteToStringBase64(com.pay2all.aeps.PaytmAEPS.UTLsData.encryptMsg(jsonObject.toString(), secretKey)));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (InvalidParameterSpecException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

//        Log.e("data",mDecoded(mContext.getResources().getString(R.string.unknownfile)).substring(31,47));

        db.insert(TABLE_NAME,null,cv);

    }

    public void mUpdate(DBItems items)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        JSONObject jsonObject=new JSONObject();
        try
        {
            jsonObject.put(id,items.getId());
            jsonObject.put(mobile_number,items.getMobile_number());
            jsonObject.put(pan_number,items.getPan_number());
            jsonObject.put(first_name,items.getFirst_name());
            jsonObject.put(last_name,items.getLast_name());
            jsonObject.put(email,items.getEmail());
            jsonObject.put(pin_code,items.getPin_code());
            jsonObject.put(status_id,items.getStatus_id());
            jsonObject.put(user_id,items.getUser_id());
            jsonObject.put(aadhar_number,items.getAadhar_number());
            jsonObject.put(company,items.getCompany());
            jsonObject.put(address,items.getAddress());
            jsonObject.put(created_at,items.getCreated_at());
            jsonObject.put(updated_at,items.getUpdated_at());
            jsonObject.put(balance,items.getBalance());
            jsonObject.put(aeps_balance,items.getAeps_balance());
            jsonObject.put(bank_account_number,items.getBank_account_number());
            jsonObject.put(ifsc,items.getIfsc());
            jsonObject.put(agent_id_code,items.getAgent_id_code());
            jsonObject.put(agent_id_code_matm,items.getAgent_id_code_matm());


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SecretKey secretKey=null;
        try
        {
            secretKey=generateKey(tendency);
        }
        catch (NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }
        try {
            cv.put(access_token, mEncodByteToStringBase64(com.pay2all.aeps.PaytmAEPS.UTLsData.encryptMsg(jsonObject.toString(), secretKey)));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (InvalidParameterSpecException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        db.update(TABLE_NAME, cv, id+"="+mGetId(), null);
        

    }


    public String mGetId()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery(selectdata,null);

        String id="";

        if (cursor.moveToFirst())
        {
            do
            {
                id=cursor.getString(0);

            }
            while (cursor.moveToNext());
        }

        return id;
    }


    //    select item from table by item_id
    public String mGetData()
    {
//        String selectdata = "SELECT  * FROM " + TABLE_NAME +" WHERE "+item_id +" = "+id;
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String item_quantity="";

        if (cursor.moveToFirst())
        {
            do
            {
                item_quantity=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }

        

        return item_quantity;
    }
    public String mGet()
    {

        return mDecoded(mContext.getResources().getString(R.string.sjgcjsgfjhdgfh));
    }

//    select item from table by item_id
    public String mGetSingleItem()
    {
//        String selectdata = "SELECT  * FROM " + TABLE_NAME +" WHERE "+item_id +" = "+id;
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String item_quantity="";

        if (cursor.moveToFirst())
        {
            do
            {
                item_quantity=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }

        

        item_quantity=mGetDetail(tendency,item_quantity,access_token);

        return item_quantity;
    }

//    to check where user is loged in or not
    public boolean mCheckLogin()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,first_name);

        

        if (!first.equals(""))
            return true;
        else
        return false;
    }


//    to check where user is loged in or not
    public String mGetOutLetId()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,id);

        


        return first;
    }
//    to check where user is loged in or not
    public String mGetBalance()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,balance);

        


        return first;
    }

//    to get aeps balance
    public String mGetAEPSBalance()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,aeps_balance);




        return first;
    }
//    to get aeps balance
    public String mGetBankAccountNumber()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,bank_account_number);




        return first;
    }
//    to get aeps balance
    public String mGetIFSC()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,ifsc);




        return first;
    }

//    to check where user is loged in or not
    public String mGetPanNumber()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,pan_number);

        


        return first;
    }
//    to check where user is loged in or not
    public String mGetStatusid()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,status_id);

        


        return first;
    }
//    to check where user is loged in or not
    public String mGetUserid()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,user_id);

        


        return first;
    }

//    to check where user is loged in or not
    public String mGetAadhaarNumber()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,aadhar_number);

        


        return first;
    }

//    to get name
    public String mGetName()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,first_name);

        
        return first;
    }

    public String mGetAgenetIdCode()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,agent_id_code);


        return first;
    }

    public String mGetAgenetCodemAtm()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }


        first=mGetDetail(tendency,first,agent_id_code_matm);


        return first;
    }
//    to get name
    public String mGetFirstName()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }

        
        first=mGetDetail(tendency,first,first_name);

        return first;
    }
//    to get name
    public String mGetLastName()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String first="";

        if (cursor.moveToFirst())
        {
            do
            {
                first=cursor.getString(1);

            }
            while (cursor.moveToNext());
        }

        

        first=mGetDetail(tendency,first,last_name);

        return first;
    }


//    to get profilepic
    public String mGetMobile()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String data="";

        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }

        

        data=mGetDetail(tendency,data,mobile_number);

        return data;
    }


//    to get profilepic
    public String mGetCompany()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String data="";

        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }

        

        data=mGetDetail(tendency,data,company);

        return data;
    }



//    to get profilepic
    public String mGetEmail()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String data="";

        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }

        


        data=mGetDetail(tendency,data,email);
        return data;
    }


//    to get profilepic
    public String mGetAddress()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String data="";

        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }

        

        data=mGetDetail(tendency,data,address);
        return data;
    }

//    to get profilepic
    public String mGetPincode()
    {
        String selectdata = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectdata,null);

        String data="";

        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(1);
            }
            while (cursor.moveToNext());
        }
        

        data=mGetDetail(tendency,data,pin_code);

        return data;
    }


    public void mDeleteData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE From "+TABLE_NAME);
        
    }


    //    to get profilepic
    public void mSetCvv(String status,String condition)
    {

//        SQLiteDatabase db=this.getWritableDatabase();
//        String strFilter = mobile+" = " + condition;
//        ContentValues args = new ContentValues();
//        args.put(cvv, status);
//        db.update(TABLE_NAME, args, strFilter, null);

//        

    }

    protected String mEncodByteToStringBase64(byte[] value)
    {
        String base64 = Base64.encodeToString(value, Base64.DEFAULT);

        return base64;
    }
    protected byte[] mEncodStringToByteBase64(String value)
    {
        byte[] data1 = Base64.decode(value, Base64.DEFAULT);

        return data1;
    }

    protected String mEncoded(String value)
    {
        byte[] data = null;
        try {
            data = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return base64;
    }

    protected String mDecoded(String data)
    {
        byte[] data1 = Base64.decode(data, Base64.DEFAULT);
        String text1 = null;
        try {
            text1 = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return text1;
    }

    public String mBaseURL()
    {
       String baseurl="https://api.pay2all.in/outlet/";

        return baseurl;
    }


    public String mGetDetail(String secret_key,String data,String getvalue)
    {

        SecretKey secretKey=null;

        String value="";
        try
        {
            secretKey=generateKey(secret_key);
        }
        catch (NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (!data.equals(""))
            {
                JSONObject jsonObject=new JSONObject(UTLsData.decryptMsg(mEncodStringToByteBase64(data),secretKey));
                value=jsonObject.getString(getvalue);
            }
            else
            {
                value="";
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (InvalidParameterSpecException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return value;
    }
}