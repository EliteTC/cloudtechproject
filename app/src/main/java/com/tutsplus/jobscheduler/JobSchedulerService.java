package com.tutsplus.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by paulruiz on 3/7/15.
 */
public class JobSchedulerService extends JobService {

    private Handler mJobHandler = new Handler( new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg ) {
            List<String> stats = getStats();
            Toast.makeText( getApplicationContext(), "JobService task running", Toast.LENGTH_LONG ).show();
            jobFinished( (JobParameters) msg.obj, false );
            return true;
        }
    } );

    @Override
    public boolean onStartJob(JobParameters params ) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob( JobParameters params ) {
        mJobHandler.removeMessages(1);
        return false;
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
