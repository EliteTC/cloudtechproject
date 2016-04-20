package com.tutsplus.jobscheduler;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

    private JobScheduler mJobScheduler;
  //  private Button mScheduleJobButton;
    private Button mCancelAllJobsButton;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
       // mScheduleJobButton = (Button) findViewById(R.id.schedule_job);
        mCancelAllJobsButton = (Button) findViewById(R.id.cancel_all);
        JobInfo job = new JobInfo.Builder( 1,new ComponentName( getPackageName(), JobSchedulerService.class.getName() )).setPeriodic(5000).build();
        int a = mJobScheduler.schedule(job);
        mJobScheduler.schedule(job);
        if( mJobScheduler.schedule(job)==1){
           // List<String> stats = getStats();
           // Toast.makeText(getApplicationContext(),"Data collected",Toast.LENGTH_LONG).show();
        }
      // if( mJobScheduler.schedule(job)<=0){
        //   Toast.makeText(getApplicationContext(),a+" ", Toast.LENGTH_SHORT).show();
      // }

        mCancelAllJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJobScheduler.cancelAll();
            }
        });
    }

    List<String> getStats(){
        UsageStatsManager mUsageStatsManager =(UsageStatsManager) getSystemService("usagestats");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        Collections.sort(queryUsageStats, new LastTimeLaunchedComparatorDesc());


        List<String> statistics = new ArrayList<String>();
        for (UsageStats lUsageStats:queryUsageStats){
            StringBuilder lStringBuilder = new StringBuilder();
            lStringBuilder.append(lUsageStats.getPackageName());
            lStringBuilder.append(" - ");
            long lastTimeUsed = lUsageStats.getLastTimeUsed();
            lStringBuilder.append((new SimpleDateFormat().format(new Date(lastTimeUsed))).toString());
            //lStringBuilder.append("\r\n");
            statistics.add(lStringBuilder.toString());

        }
        return statistics;
    }

    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
        }
    }
}
