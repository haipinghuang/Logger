package com.hai.logger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hai.mylog.AndroidLogAdapter;
import com.hai.mylog.DiskLogAdapter;
import com.hai.mylog.Logger;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new DiskLogAdapter(this));
    }
    public void clk(View v){
        Logger.d("标签", "this is a log for write to file\n\tthis is a log for write to file");
        Logger.d("这是一段 log");
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(null, "除法异常", e);
            Logger.e(e);
        }
        Logger.d("标签", "null异常", new NullPointerException("jjjjj"));

        Me me = new Me("黄还", "难", 12);


        String xmlStr = "<?xml version='1.0' encoding='UTF-8'?><ROOT><MAIN_BILL_ID>13905837718</MAIN_BILL_ID><PROD_ID>600000044996</PROD_ID></ROOT>";
        Logger.xml(xmlStr);
    }
}
