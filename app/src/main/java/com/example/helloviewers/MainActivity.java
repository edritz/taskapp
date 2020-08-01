package com.example.helloviewers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testactivity);
        TextView cpu = (TextView) findViewById(R.id.cutil);
        TextView breakdown = (TextView) findViewById(R.id.rproc);

        int [] tot = getCpuUsageStatistic();
        int userutil = tot[0];
        int sysutil = tot[1];
        int totalutil = 0;
        for( int i : tot)
        {
            totalutil += i;
        }
        cpu.setText(String.format("CPU Utilization: %s%%", totalutil));
        breakdown.setText(String.format("USER: %d%%  SYSTEM: %d%%", userutil, sysutil));


        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        ListAdapter adapter = new ListAdapter(this, runningProcesses);
        ListView lview = (ListView)findViewById(R.id.procs);
        lview.setAdapter(adapter);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        Thread.sleep(5000);
                        final int [] tot = getCpuUsageStatistic();
                        final int userutil = tot[0];
                        final int sysutil = tot[1];
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int totalutil = 0;
                                for( int i : tot)
                                {
                                    totalutil += i;
                                }
                                TextView ucpu = findViewById(R.id.cutil);
                                TextView ubreakdown = findViewById(R.id.rproc);
                                ucpu.setText(String.format("CPU Utilization: %s%%", totalutil));
                                ubreakdown.setText(String.format("USER: %d%%  SYSTEM: %d%%", userutil, sysutil));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    private int[] getCpuUsageStatistic() {

        String tempString = executeTop();

        tempString = tempString.replaceAll(",", "");
        tempString = tempString.replaceAll("User", "");
        tempString = tempString.replaceAll("System", "");
        tempString = tempString.replaceAll("IOW", "");
        tempString = tempString.replaceAll("IRQ", "");
        tempString = tempString.replaceAll("%", "");
        for (int i = 0; i < 10; i++) {
            tempString = tempString.replaceAll("  ", " ");
        }
        tempString = tempString.trim();
        String[] myString = tempString.split(" ");
        int[] cpuUsageAsInt = new int[myString.length];
        for (int i = 0; i < myString.length; i++) {
            myString[i] = myString[i].trim();
            cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
        }
        return cpuUsageAsInt;
    }

    private String executeTop() {
        java.lang.Process p = null;
        BufferedReader in = null;
        String returnString = null;
        try {
            p = Runtime.getRuntime().exec("top -n 1");
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (returnString == null || returnString.contentEquals("")) {
                returnString = in.readLine();
            }
        } catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                p.destroy();
            } catch (IOException e) {
                Log.e("executeTop",
                        "error in closing and destroying top process");
                e.printStackTrace();
            }
        }
        System.out.println(returnString);
        return returnString;
    }

}