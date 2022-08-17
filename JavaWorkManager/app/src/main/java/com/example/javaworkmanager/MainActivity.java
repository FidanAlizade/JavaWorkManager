package com.example.javaworkmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Datalari bele qoyuruq
        Data data = new Data.Builder().putInt("intKey", 1).build();

        //Constraints - meselen, sadece wifiye bagli olduqda calissin, ve ya sadece sharja bagli olduqda calissin ve s.
        Constraints constraints = new Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

        /*
        //Bir defe calisan workRequest yaradiriq
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)
                //.setInitialDelay(5, TimeUnit.MINUTES)
                .setInputData(data)
                //.addTag("My Tag")
                .build();

        //WorkManagere yaratdigimiz workRequesti qoyuruq
        WorkManager.getInstance(this).enqueue(workRequest);

         */

        //Periodik olaraq caslismasini istediyimizde PeriodicWorkRequest'i istifade edirik
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class,15,TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);

        //Musahide- ID'sine ve ya verdiyimiz taglara gore workReuqest'leri Observe etmedir
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo.getState() == WorkInfo.State.RUNNING){
                    System.out.println("Running");
                }else if(workInfo.getState() == WorkInfo.State.SUCCEEDED){
                    System.out.println("Succeded");
                }else if(workInfo.getState() == WorkInfo.State.FAILED){
                    System.out.println("Failed");
                }
            }
        });

        //butun isleri ve ya her hansinisa ID'sine ve ya verdiyimiz taglara gore legv ede bilerik
        //WorkManager.getInstance(this).cancelAllWork();

        //Chaining - Zencirleme
//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
//                .setInputData(data)
//                        .setConstraints(constraints)
//                                .build();
//
//        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
//                .then(oneTimeWorkRequest)
//                .then(oneTimeWorkRequest)
//                .enqueue();
    }
}