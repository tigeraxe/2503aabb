package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SetActivity2 extends AppCompatActivity implements imgePassWord.OnPatternChangeListener {
    private imgePassWord lpv;
    String pat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set2);
        lpv = (imgePassWord) findViewById(R.id.lock_pattern_view);
        lpv.setOnPatternChangeListener(this);
    }
    @Override
    public void onPatternChange(String patternPassword) {
        if (patternPassword == null) {
            Toast.makeText(SetActivity2.this, "请至少3个点", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent2 = getIntent();
            pat = intent2.getStringExtra("pat");
            if (intent2.getIntExtra("copy", 0) == 0) {
                Toast.makeText(SetActivity2.this, "请重复一遍", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetActivity2.this, SetActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("copy", 1);
                intent.putExtra("pat",patternPassword);
                startActivity(intent);
                finish();
            } else {

                if (patternPassword.equals(pat)) {
                    Toast.makeText(SetActivity2.this, "ok", Toast.LENGTH_SHORT).show();
                    add(LoginActivity.username, patternPassword);
                    Intent intent = new Intent(SetActivity2.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("copy", 1);
                    startActivity(intent);
                    finish();
                }else Toast.makeText(SetActivity2.this, "与上次不一致", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onPatternStarted(boolean isStarted) {
    }
    public void add(String name,String text){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("DATATYPE", Activity.MODE_PRIVATE)
                .edit();

        editor.putString(name,text);
        editor.apply();
    }
    public String  get(String name){
        @SuppressLint("CommitPrefEdits") SharedPreferences editor = getSharedPreferences("DATATYPE",Activity.MODE_PRIVATE);

        return editor.getString(name,null);
    }
}