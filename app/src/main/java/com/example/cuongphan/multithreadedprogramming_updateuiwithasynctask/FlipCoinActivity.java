package com.example.cuongphan.multithreadedprogramming_updateuiwithasynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FlipCoinActivity extends Activity {
    private static final int HEADER_TIMEOUT = 10;
    LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        mLinearLayout = (LinearLayout)findViewById(R.id.linear_layout);
    }

    public void flipCoin(View view){
        mLinearLayout.requestLayout();
        ExecutorService taskList = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 5; i++) {
            TextviewDisplay task = new TextviewDisplay(i);
            task.execute();
        }
        try {
            taskList.shutdown();
            taskList.awaitTermination(HEADER_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class TextviewDisplay extends AsyncTask<Void, Void, Integer>{
        int i;

        public TextviewDisplay(int i) {
            this.i = i;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int coin_sum = 0;
            int coin;
            Integer max = 0;
            Random rd = new Random();
            synchronized (this) {
                for (int i = 0; i < 1000; i++) {
                    coin = rd.nextInt(2);
                    if (coin_sum == (coin_sum + coin)) {
                        coin_sum = 0;
                    } else {
                        coin_sum += coin;
                    }
                    if (coin_sum > max) {
                        max = coin_sum;
                    }
                }
            }
            return max;
        }
        public void onPostExecute(Integer max){
            TextView textView = new TextView(FlipCoinActivity.this);
            textView.setText("Max coin "+this.i+": "+max);
            mLinearLayout.addView(textView);
        }
    }
}
