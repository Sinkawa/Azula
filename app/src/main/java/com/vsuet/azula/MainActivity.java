package com.vsuet.azula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private String first;
    private String second;
    private boolean isFirst;
    private byte id;
    TextView display;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        animation.setRepeatCount(0);

        display = findViewById(R.id.display);
        doReset();
    }

    public void onDigitClick(View view) {
        display.startAnimation(animation);
        Button button = (Button)view;

        if (isFirst) {
            if (first.equals("0")) {
                first = "";
            }

            first += button.getText();
            display.setText(first);
        } else {
            if (second.equals("0")) {
                second = "";
            }

            second += button.getText();
            display.setText(second);
        }

    }

    public void onOperationClick(View view) {
        display.startAnimation(animation);
        if (isFirst) {
            isFirst = false;
            if (first.length() == 0) {
                first = "0";
            }
            switch (view.getId()){
                case R.id.btn_plus:
                    id = 0;
                    break;
                case R.id.btn_minus:
                    id = 1;
                    break;
                case R.id.btn_multi:
                    id = 2;
                    break;
                case R.id.btn_div:
                    id = 3;
                    break;
            }

        } else if (second.length() > 0){
            try {
                first = doOperation(first, second, id);
                second = "";
                displayResult(first);
            } catch (DivisionByNull e) {
                display.setText(R.string.NullException);
                doReset();
            }
        }
    }

    public void onEqualClick(View view) {
        display.startAnimation(animation);
        if (!isFirst) {
            if (second.length() > 0) {
                try {
                    first = doOperation(first, second, id);
                    displayResult(first);
                } catch (DivisionByNull e) {
                    display.setText(R.string.NullException);
                }
            } else {
                displayResult(first);
            }
            doReset();
        }
    }

    public void onPercentClick(View view) {
        display.startAnimation(animation);
        if (isFirst) {
            if(!first.equals("")) {
                displayResult(String.valueOf(Double.parseDouble(first) * 0.01));
            }
        } else {
            if(!second.equals("")) {
                displayResult(String.valueOf(Double.parseDouble(first) * 0.01));
            }
        }

    }

    public void onRickRollClick(View view) {
        doRickRoll();
    }

    private void doRickRoll(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://youtu.be/dQw4w9WgXcQ"));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    public void onDelClick(View view) {
        display.setText("");
        if (isFirst){
            if (first.length() > 0) {
                first = first.substring(0, first.length() - 1);
            }
            display.setText(nullizer(first));
        } else {
            if (second.length() > 0) {
                second = second.substring(0, second.length() - 1);
            }
            display.setText(nullizer(second));
        }
    }

    public void onClearClick(View view) {
        doReset();
        display.setText("0");
    }

    public void onDotClick(View view) {
        display.startAnimation(animation);
        if (isFirst){
            first = nullizer(first);
            if (!first.contains(".")) {
                first += ".";
            }
            display.setText(first);
        } else {
            second = nullizer(second);
            if (!second.contains(".")) {
                second += ".";
            }
            display.setText(second);
        }
    }

    private String doOperation(String _first, String _second, byte id) throws DivisionByNull {
        double first = Double.parseDouble(_first);
        double second = Double.parseDouble(_second);
        double result = 0;
        switch (id) {
            case 0:
                result = first + second;
                break;
            case 1:
                result = first - second;
                break;
            case 2:
                result = first * second;
                break;
            case 3:
                result = first / second;
                if(Math.abs(result) == Double.POSITIVE_INFINITY) {
                    throw new DivisionByNull();
                }
        }

        return String.valueOf(result);
    }

    private void doReset(){
        first = "";
        second = "";
        isFirst = true;
    }

    private String nullizer(String string){
        if (string.equals("")) {
            return "0";
        } else return string;
    }

    private void displayResult(String string){
        DecimalFormat df = new DecimalFormat("###.###");
        String result = df.format(Double.parseDouble(string));
        display.setText(result);
    }

    static class DivisionByNull extends Exception {

    }

}