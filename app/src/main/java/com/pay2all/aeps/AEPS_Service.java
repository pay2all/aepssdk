package com.pay2all.aeps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pay2all.aeps.AEPSICICI.AadhaarPay;
import com.pay2all.aeps.AEPSICICI.BalaneEnquiry;
import com.pay2all.aeps.AEPSICICI.MiniStatement;
import com.pay2all.aeps.AEPSICICI.Withdrawal;
import com.pay2all.aeps.InsuranceDetails.Insurance;
import com.pay2all.aeps.aepsnew.AEPSNewService;
import com.pay2all.aeps.AgentVerifyDetail.VerifyAgent;
import com.pay2all.aeps.Reports.TransactionReports;

public class AEPS_Service extends AppCompatActivity {

    int INTENTCODE=1421;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_e_p_s__service);
        Bundle intent=getIntent().getExtras();

        if (getIntent().hasExtra("outlet_id"))
        {
            Constants.outlet_id=intent.getString("outlet_id");
        }

        if (intent.containsKey("mobile"))
        {
            Constants.mobile=intent.getString("mobile");
        }

        if (intent.containsKey("service"))
        {
            Constants.service_id=intent.getString("service");
        }

        if (intent.containsKey("isReceipt"))
        {
            Constants.isReceipt=intent.getBoolean("isReceipt");
        }

        if (intent.containsKey("aadhaar"))
        {
            Constants.aadhaar=intent.getString("aadhaar");
        }

        if (intent.containsKey("name"))
        {
            Constants.name=intent.getString("name");
        }

        if (Constants.service_id.equalsIgnoreCase("all"))
        {

        }
        else{
            if (Constants.mobile.equals("")|| /*Constants.outlet_id.equals("")||*/ Constants.service_id.equals("")|| Constants.name.equals(""))
            {
                Intent i=new Intent();
                i.putExtra("status","2");
                i.putExtra("message","invalid credential");
                setResult(RESULT_CANCELED,i);
            }
            mCallServices();
        }
    }

    private void mCallServices()
    {
        Intent intent = null;
        if (Constants.service_id.equalsIgnoreCase("cw")) {

            intent = new Intent(AEPS_Service.this, Withdrawal.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("be"))
        {
            intent=new Intent(AEPS_Service.this, BalaneEnquiry.class);
        }

        else if (Constants.service_id.equalsIgnoreCase("mst"))
        {
            intent=new Intent(AEPS_Service.this, MiniStatement.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("th"))
        {
            intent=new Intent(AEPS_Service.this, TransactionReports.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("ap"))
        {
            intent=new Intent(AEPS_Service.this, AadhaarPay.class);
        }

        else if (Constants.service_id.equalsIgnoreCase("mst2"))
        {
            intent=new Intent(AEPS_Service.this, com.pay2all.aeps.PaytmAEPS.MiniStatement.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("be2"))
        {
            intent=new Intent(AEPS_Service.this, com.pay2all.aeps.PaytmAEPS.BalaneEnquiry.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("cw2"))
        {
            intent=new Intent(AEPS_Service.this, com.pay2all.aeps.PaytmAEPS.Withdrawal.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("2f"))
        {
            intent=new Intent(AEPS_Service.this, VerifyAgent.class);
        }
        else if (Constants.service_id.equalsIgnoreCase("be3"))
        {
            intent= mCallNewAEPS("159","Balance Enquiry");
        }
        else if (Constants.service_id.equalsIgnoreCase("cw3"))
        {
            intent=  mCallNewAEPS("158","Cash Withdrawal");
        }
        else if (Constants.service_id.equalsIgnoreCase("mst3"))
        {
            intent= mCallNewAEPS("172","Mini Statement");
        }
        else if (Constants.service_id.equalsIgnoreCase("ap3"))
        {
           intent= mCallNewAEPS("175","Aadhaar Pay");
        }
        else if (Constants.service_id.equalsIgnoreCase("ins"))
        {
            intent=new Intent(AEPS_Service.this, Insurance.class);
        }

        startActivityForResult(intent,INTENTCODE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==INTENTCODE)
        {
            if (resultCode==RESULT_OK)
            {
                assert data != null;
                if (data.hasExtra("data")) {
                    Intent intent=new Intent();
                    intent.putExtra("alldata",Constants.all_data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent();
                    intent.putExtra("status","2");
                    intent.putExtra("message","something went wrong");
                    intent.putExtra("alldata","null");
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        }
    }

    protected Intent mCallNewAEPS(String provider_id,String type)
    {
        Intent intent=new Intent(AEPS_Service.this, AEPSNewService.class);
        intent.putExtra("provider_id",provider_id);
        intent.putExtra("payment_id","5");
        intent.putExtra("api_id","");
        intent.putExtra("type",type);

        return intent;
    }
}