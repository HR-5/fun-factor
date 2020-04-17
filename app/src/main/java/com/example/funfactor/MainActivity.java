package com.example.funfactor;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static android.os.VibrationEffect.*;


public class MainActivity extends AppCompatActivity {
    public int num;
    Button mButton;
    EditText mEdit;
    TextView bsc;
    TextView sc;
    TextView disp;
    RelativeLayout rl;
    ArrayList<Integer> no;
    int ans;
    int score;
    int bscore;
    CountDownTimer timer;
    TextView counttime;
    String dscore;
    String time;
    String str;
    int bcolor;
    Vibrator vibrator;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dscore = "Current Score:0";
        bcolor = R.color.back;

        str = " ";
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score");
            dscore = savedInstanceState.getString("dscore");

            str = savedInstanceState.getString("disp");
            bcolor = savedInstanceState.getInt("bcolor");

        }
        mButton = findViewById(R.id.go);
        mEdit = findViewById(R.id.editText);
        rl = (RelativeLayout) findViewById(R.id.mainlayout);
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
        timer = new CountDownTimer(11000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = "Time left: " + millisUntilFinished/1000;
                counttime.setText(time);
            }
            @Override
            public void onFinish() {
                resetOptions();
                counttime.setText("Time left: 0");
                disp = (TextView) findViewById(R.id.disp);
                str = "Timeout! \nCorrect answer: " + ans + "\nYour Score: " + score;
                disp.setText(str);
            }
        };
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score",score);
        outState.putString("dscore", String.valueOf(dscore));
        outState.putString("disp", String.valueOf(str));
        outState.putInt("bcolor",bcolor);
    }



    public void resetOptions() {
        for (int i = 0; i < 3; i++) {
            String bid = "op".concat(Integer.toString(i + 1));
            Button b = (Button) findViewById(getResources().getIdentifier(bid, "id", getPackageName()));
            b.setVisibility(View.INVISIBLE);
        }
        TextView t = (TextView) findViewById(R.id.textView);
        t.setVisibility(View.INVISIBLE);
        mEdit= (EditText) findViewById(R.id.editText);
        mEdit.setText(null);
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
        num = Integer.parseInt(text);

        if (num < 6) errorDisp("Enter a number greater than 5");
        else {
            final TextView counttime = findViewById(R.id.time);
            counttime.setVisibility(View.VISIBLE);

            timer.start();

            ans = find_nos();
            if (ans != 0) {
                for (int i = 0; i < 3; i++) {
                    String bid = "op".concat(Integer.toString(i + 1));
                    Button b = (Button) findViewById(getResources().getIdentifier(bid, "id", getPackageName()));
                    b.setText(Integer.toString(no.get(i)));
                    b.setVisibility(View.VISIBLE);
                }
                TextView t = (TextView) findViewById(R.id.textView);
                t.setVisibility(View.VISIBLE);
            }


        }
    }

    @SuppressLint("SetTextI18n")
    public void findAnswer(View view) {
        Button b = (Button) view;
        String text = b.getText().toString();
        int bnum = Integer.parseInt(text);
        timer.cancel();
        resetOptions();

        disp = (TextView) findViewById(R.id.disp);
        sc = (TextView) findViewById(R.id.score);
        rl = (RelativeLayout) findViewById(R.id.mainlayout);

        if (bnum == ans) {
            str = "Correct Answer :)";
            disp.setText(str);
            score++;
            dscore = "Current Score: ".concat(Integer.toString(score));
            sc.setText(dscore);
            bcolor = R.color.customGreen;

            rl.setBackgroundColor(getResources().getColor(bcolor));



        }
        else {
            str = "Wrong Answer :(\nCorrect answer: " + ans + "\nYour Score: " + score;
            disp.setText(str);
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            bcolor = R.color.customRed;
            rl.setBackgroundColor(getResources().getColor(bcolor));
            if (bscore < score) {
                SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("key", score);
                editor.apply();
                bsc.setText("Best Score: ".concat(Integer.toString(score)));
            }
            score = 0;
            sc.setText("Current Score: ".concat(Integer.toString(score)));

        }
    }

    public int find_nos() {
        no = new ArrayList<Integer>(num);
        Random rand = new Random();

        if (num == 6) {
            int n1 = 4, n2 = 5;
            int f = rand.nextInt(2) + 2;
            no.add(n1);
            no.add(n2);
            no.add(f);
            Collections.shuffle(no, new Random());
            return f;
        }

        int[] fact = new int[num];
        int[] notfact = new int[num];
        int k = 0, j = 0;
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                fact[j++] = i;
            } else {
                notfact[k++] = i;
            }
        }
        if (j == 0) {
            errorDisp("Don't enter a prime number");
            return 0;
        }
        int f = fact[rand.nextInt(j)];
        int n1, n2;
        if (k == 1 || k == 2) {
            int n = (int) (num / 2);
            n1 = notfact[rand.nextInt(k)];
            n2 = rand.nextInt(n - 1) + n + 1;
        } else {
            n1 = notfact[rand.nextInt(k)];
            n2 = 0;
            do {
                n2 = notfact[rand.nextInt(k)];
            } while (n2 == n1);
        }
        no.add(n1);
        no.add(n2);
        no.add(f);
        Collections.shuffle(no, new Random());
        return f;
    }

}