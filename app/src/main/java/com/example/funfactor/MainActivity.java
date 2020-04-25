package com.example.funfactor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.content.res.Configuration;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//import static android.os.VibrationEffect.*;


public class MainActivity extends AppCompatActivity {
    public long num;
    public int flag;
    Button mButton;
    EditText mEdit;
    TextView bsc;
    TextView sc;
    TextView disp;
    ConstraintLayout rl;
    long[] no;
    long ans;
    int score;
    int bscore;
    CountDownTimer timer;
    CountDownTimer timer2;
    CountDownTimer timer3;
    TextView counttime;
    String dscore;
    String time;
    String str;
    int bcolor;
    Vibrator vibrator;
    long fin;
    int f;
    int fs;
    long[] millisInFuture;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flag = 0;
        fin = 11000;
        dscore = "Current Score:0";
        bcolor = R.color.back;
        str = " ";
        millisInFuture = new long[]{0};
        if (savedInstanceState != null) {
            setContentView(R.layout.activity_main);
            fs = 1;
            score = savedInstanceState.getInt("score");
            dscore = savedInstanceState.getString("dscore");
            str = savedInstanceState.getString("disp");
            bcolor = savedInstanceState.getInt("bcolor");
            flag = savedInstanceState.getInt("flag");
            time = savedInstanceState.getString("time");
            no = savedInstanceState.getLongArray("no");
            ans = savedInstanceState.getLong("ans");
            f = savedInstanceState.getInt("f");
            millisInFuture[0] = savedInstanceState.getLong("fin");
            millisInFuture[0] = millisInFuture[0] - 1000;
            if (flag == 1) {
                if (millisInFuture[0] != 0) {
                    Button myButton = (Button) findViewById(R.id.go);
                    myButton.setEnabled(false);
                    timer2 = new CountDownTimer(millisInFuture[0], 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (flag != 1) timer2.cancel();
                            time = "Time left: " + millisUntilFinished / 1000;
                            counttime.setText(time);
                            fin = millisUntilFinished;
                        }

                        @Override
                        public void onFinish() {
                            resetOptions();
                            fin = 11000;
                            Button myButton = (Button) findViewById(R.id.go);
                            myButton.setEnabled(true);
                            flag = 3;
                            counttime.setText("Time left: 0");
                            disp = (TextView) findViewById(R.id.disp);
                            str = "Timeout! \nCorrect answer: " + ans + "\nYour Score: " + score;
                            if (f == 1) {
                                share();
                            }
                            disp.setText(str);
                            score = 0;
                            dscore = "Current Score: ".concat(Integer.toString(score));
                            sc.setText(dscore);
                            bcolor = R.color.customRed;
                            rl = (ConstraintLayout) findViewById(R.id.mainlayout);
                            rl.setBackgroundColor(getResources().getColor(bcolor));
                            millisInFuture[0] = 0;
                            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= 26) {
                                assert vibrator != null;
                                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                            }
                        }
                    }.start();


                    final TextView counttime = findViewById(R.id.time);
                    counttime.setVisibility(View.VISIBLE);
                    counttime.setText(time);
                    TextView t = (TextView) findViewById(R.id.textView);
                    t.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 3; i++) {
                        String bid = "op".concat(Integer.toString(i + 1));
                        Button b = (Button) findViewById(getResources().getIdentifier(bid, "id", getPackageName()));
                        b.setText(Long.toString(no[i]));
                        b.setVisibility(View.VISIBLE);
                    }

                }
            }

        }
        mButton = findViewById(R.id.go);
        mEdit = findViewById(R.id.editText);
        rl = (ConstraintLayout) findViewById(R.id.mainlayout);
        rl.setBackgroundColor(getResources().getColor(bcolor));

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        bscore = prefs.getInt("key", 0);
        bsc = (TextView) findViewById(R.id.bscore);
        sc = (TextView) findViewById(R.id.score);
        disp = (TextView) findViewById(R.id.disp);
        disp.setText(str);

        sc.setText(dscore);
        bsc.setText("Best Score: ".concat(Integer.toString(bscore)));
        counttime = findViewById(R.id.time);

        timer = new CountDownTimer(fin, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (flag == 3) timer.cancel();
                time = "Time left: " + millisUntilFinished / 1000;
                counttime.setText(time);
                fin = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                resetOptions();
                Button myButton = (Button) findViewById(R.id.go);
                myButton.setEnabled(true);
                flag=3;
                fs = 0;
                counttime.setText("Time left: 0");
                disp = (TextView) findViewById(R.id.disp);
                str = "Timeout! \nCorrect answer: " + ans + "\nYour Score: " + score;
                if (f == 1) {
                    share();
                }
                disp.setText(str);
                score = 0;
                dscore = "Current Score: ".concat(Integer.toString(score));
                sc.setText(dscore);
                bcolor = R.color.customRed;
                rl = (ConstraintLayout) findViewById(R.id.mainlayout);
                rl.setBackgroundColor(getResources().getColor(bcolor));
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    assert vibrator != null;
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }
        };

    }

    @SuppressLint("SetTextI18n")
    public void onResume() {
        super.onResume();
        if (flag == 1 && fs!=1) {
            fs =0;
            if (fin != 0) {
                Button myButton = (Button) findViewById(R.id.go);
                myButton.setEnabled(false);

                timer3 = new CountDownTimer(fin, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (flag != 1) timer3.cancel();
                        time = "Time left: " + millisUntilFinished / 1000;
                        counttime.setText(time);
                        fin = millisUntilFinished;
                    }

                    @Override
                    public void onFinish() {
                        resetOptions();
                        Button myButton = (Button) findViewById(R.id.go);
                        myButton.setEnabled(true);
                        flag = 3;
                        counttime.setText("Time left: 0");
                        disp = (TextView) findViewById(R.id.disp);
                        str = "Timeout! \nCorrect answer: " + ans + "\nYour Score: " + score;
                        if (f == 1) {
                            share();
                        }
                        disp.setText(str);
                        score = 0;
                        dscore = "Current Score: ".concat(Integer.toString(score));
                        sc.setText(dscore);
                        bcolor = R.color.customRed;
                        rl = (ConstraintLayout) findViewById(R.id.mainlayout);
                        rl.setBackgroundColor(getResources().getColor(bcolor));
                        millisInFuture[0] = 0;
                        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= 26) {
                            assert vibrator != null;
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                    }
                }.start();


                final TextView counttime = findViewById(R.id.time);
                counttime.setVisibility(View.VISIBLE);
                counttime.setText(time);
                TextView t = (TextView) findViewById(R.id.textView);
                t.setVisibility(View.VISIBLE);
                for (int i = 0; i < 3; i++) {
                    String bid = "op".concat(Integer.toString(i + 1));
                    Button b = (Button) findViewById(getResources().getIdentifier(bid, "id", getPackageName()));
                    b.setText(Long.toString(no[i]));
                    b.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    public void onPause() {
        super.onPause();
        millisInFuture[0] = fin;
        if(fs==1) {
            timer2.cancel();
            fs = 0;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score",score);
        outState.putString("dscore", String.valueOf(dscore));
        outState.putString("disp", String.valueOf(str));
        outState.putInt("bcolor",bcolor);
        outState.putInt("flag",flag);
        outState.putString("time",time);
        outState.putLong("fin",fin);
        outState.putLongArray("no",no);
        outState.putLong("ans",ans);
        outState.putInt("f",f);
        if(fin!=0)
            timer.cancel();
        if(timer3 != null)
            timer3.cancel();

    }





    public void resetOptions() {
        for (int i = 0; i < 3; i++) {
            String bid = "op".concat(Integer.toString(i + 1));
            Button b = (Button) findViewById(getResources().getIdentifier(bid, "id", getPackageName()));
            b.setVisibility(View.INVISIBLE);
        }
        TextView t = (TextView) findViewById(R.id.textView);
        t.setVisibility(View.INVISIBLE);
        counttime.setVisibility(View.INVISIBLE);
        mEdit= (EditText) findViewById(R.id.editText);
        mEdit.setText(null);

    }

    public void share(){
        if (f == 1) {
            SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("key", score);
            editor.apply();
            bsc.setText("Best Score: ".concat(Integer.toString(score)));
            str = str + "\n Congratulations! You have achieved new Best Score!";
            f = 0;
        }
    }

    public void errorDisp(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressLint("SetTextI18n")
    public void dispOptions(View view) {
        disp = (TextView) findViewById(R.id.disp);
        str =" ";
        disp.setText(str);

        String text = mEdit.getText().toString();
        num = Long.parseLong(text);

        if (num < 6) errorDisp("Enter a number greater than 5");
        else {
            ans = find_nos();
            if (ans != 0) {
                Button myButton = (Button) findViewById(R.id.go);
                myButton.setEnabled(false);
                flag=1;
                timer.start();
                final TextView counttime = findViewById(R.id.time);
                counttime.setVisibility(View.VISIBLE);
                int orientation = getResources().getConfiguration().orientation;
                for (int i = 0; i < 3; i++) {
                    String bid = "op".concat(Integer.toString(i + 1));
                    Button b = (Button) findViewById(getResources().getIdentifier(bid, "id", getPackageName()));
                    b.setText(Long.toString(no[i]));
                    b.setVisibility(View.VISIBLE);
                }
                TextView t = (TextView) findViewById(R.id.textView);
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    t.setVisibility(View.VISIBLE);
                } else {
                    t.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void findAnswer(View view) {
        timer.cancel();
        flag=2;
        Button b = (Button) view;
        String text = b.getText().toString();
        long bnum = Long.parseLong(text);
        resetOptions();

        disp = (TextView) findViewById(R.id.disp);
        sc = (TextView) findViewById(R.id.score);
        rl = (ConstraintLayout) findViewById(R.id.mainlayout);
        if (bnum == ans) {
            str = "Correct Answer :)";
            disp.setText(str);
            score++;
            dscore = "Current Score: ".concat(Integer.toString(score));
            sc.setText(dscore);
            bcolor = R.color.customGreen;
            rl.setBackgroundColor(getResources().getColor(bcolor));
            if (bscore < score) {
                SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("key", score);
                editor.apply();
                bsc.setText("Best Score: ".concat(Integer.toString(score)));
                bscore = score;
                f = 1;
            }
        }
        else {
            str = "Wrong Answer :(\nCorrect answer: " + ans + "\nYour Score: " + score;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                assert vibrator != null;
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            bcolor = R.color.customRed;
            rl.setBackgroundColor(getResources().getColor(bcolor));
            if (f==1) {
                str = str + "\n Congratulations! You have achieved new Best Score!";
                f = 0;
            }
            disp.setText(str);
            score = 0;
            dscore = "Current Score: ".concat(Integer.toString(score));
            sc.setText(dscore);
        }
        Button myButton = (Button) findViewById(R.id.go);
        myButton.setEnabled(true);
    }

    public void shuffle(long ar[]) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            long a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public long find_nos() {
        no = new long[3];
        Random rand = new Random();

        if (num == 6) {
            long n1 = 4, n2 = 5;
            long f = rand.nextInt(2) + 2;
            no[0] = n1;
            no[1] = n2;
            no[2] = f;
            shuffle(no);
            return f;
        }

        int n = Integer.MAX_VALUE;

        if (num<n) {
            n = (int) num;
        }
        long[] fact = new long[10000];
        int j = 0;
        for (long i = 2; (i <= n / 2) && (j < 10000); i++) {
            long r = num % i;
            if (r == 0) {
                fact[j++] = i;
            }

        }
        if (j == 0) {
            errorDisp("Don't enter a prime number");
            return 0;
        }
        long f = fact[rand.nextInt(j)];
        long n1, n2;

        do {
            n1 = rand.nextInt(n)+1;
        }while (num % n1 == 0);
        do {
            n2 = rand.nextInt(n)+1;
            if (n1<num/2) n2 += (int)(num/2);
        } while ((n2 == n1) || (num % n2 == 0));
        no[0] = n1;
        no[1] = n2;
        no[2] = f;
        shuffle(no);
        return f;
    }

}