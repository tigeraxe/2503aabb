package com.hanyuzhou.accountingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetActivity extends AppCompatActivity {
    private EditText et_psw,et_psw_again;
    private String username,psw,psw_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        init();
    }
    private void init(){
        Button btn_register = (Button)findViewById(R.id.btn_r);
        et_psw = (EditText)findViewById(R.id.psw1);
        et_psw_again = (EditText)findViewById(R.id.psw2);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = "guigui";
                psw = et_psw.getText().toString().trim();
                psw_again = et_psw_again.getText().toString().trim();

                if(TextUtils.isEmpty(psw)){
                    Toast.makeText(SetActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(psw_again)){
                    Toast.makeText(SetActivity.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                }
                else if(!psw.equals(psw_again)){
                    Toast.makeText(SetActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SetActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                    saveRegisterInfo(username, psw);
                    Intent data = new Intent();
                    data.putExtra("userName",username);
                    setResult(RESULT_OK,data);
                    SetActivity.this.finish();
                }
            }
        });
    }
    private void saveRegisterInfo(String userName, String psw){
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userName, psw);
        editor.apply();
    }
}