package com.pay2all.aeps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TestAEPS extends AppCompatActivity {


    EditText outlet,mobile,service,name;
    Button call_service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_a_e_p_s);

        outlet=findViewById(R.id.outlet);
        mobile=findViewById(R.id.mobile);
        service=findViewById(R.id.service_name);
        name=findViewById(R.id.name);
        call_service=findViewById(R.id.button_call);
        call_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestAEPS.this,AEPS_Service.class);
                intent.putExtra("outlet_id",outlet.getText().toString());
                intent.putExtra("mobile",mobile.getText().toString());
                intent.putExtra("service",service.getText().toString());
                intent.putExtra("name",name.getText().toString());
                startActivity(intent);
            }
        });
    }
}
