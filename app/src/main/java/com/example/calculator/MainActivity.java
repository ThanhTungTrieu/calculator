package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private TextView screen;
    private String display="";
    private EditText inputText;
    private TextView displayText;
    private String currentOperator="";
    private String result="";
    private String alphaBetExceptX = "abcdefghijklmnopqrstuvwz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton deletevar = (ImageButton) findViewById(R.id.butdelet);
        deletevar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNumber();
            }
        });


        screen = (TextView)findViewById(R.id.input_box);
        screen.setText(display);
        inputText = findViewById(R.id.input_box);
        displayText = findViewById(R.id.result_box);
    }

    private void appendToLast(String str) {
        this.inputText.getText().append(str);
    }

    public void onClickNumber(View v) {
        Button b = (Button) v;
        display += b.getText();
        appendToLast(display);
        display="";
    }

    public void onClickOperator(View v) {
        Button b = (Button) v;
        display += b.getText();
        if(endsWithOperator())
        {
            replace(display);
            currentOperator = b.getText().toString();
            display = "";
        }
        else {
            appendToLast(display);
            currentOperator = b.getText().toString();

            display = "";
        }

    }

    public void onClearButton(View v) {
        inputText.getText().clear();
        displayText.setText("");
    }

    public void deleteNumber() {
        if (inputText.getText() != null && inputText.getText().length() > 0) {
            this.inputText.getText().delete(getInput().length() - 1, getInput().length());
        }
    }

    private String getInput() {
        return this.inputText.getText().toString();
    }

    private boolean endsWithOperator() {
        return getInput().endsWith("+") || getInput().endsWith("-") || getInput().endsWith("\u00F7") || getInput().endsWith("x");
    }

    private boolean startsWithOperator() {
        return getInput().startsWith("+") || getInput().startsWith("-") || getInput().startsWith("\u00F7") || getInput().startsWith("x");
    }

    private boolean hasAlphaBet(String inp) {
        for (int i = 0; i < inp.length(); i++) {
            if (alphaBetExceptX.contains(String.valueOf(inp.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

    private void replace(String str) {
        inputText.getText().replace(getInput().length() - 1, getInput().length(), str);
    }

    private double operate(String a, String b, String cp) {
        switch(cp) {
            case "+": return Double.valueOf(a) + Double.valueOf(b);
            case "-": return Double.valueOf(a) - Double.valueOf(b);
            case "x": return Double.valueOf(a) * Double.valueOf(b);
            case "\u00F7": return Double.valueOf(a) / Double.valueOf(b);
            default: return -1;
        }
    }

    public void equalResult(View v) {
        String input = getInput();
        if (hasAlphaBet(input) || startsWithOperator()) {
            displayText.setText("ERROR");
            return;
        }

        if(!endsWithOperator()) {

            if (input.contains("x")) {
                input = input.replaceAll("x", "*");
            }

            if (input.contains("\u00F7")) {
                input = input.replaceAll("\u00F7", "/");
            }

            Expression expression = new ExpressionBuilder(input).build();
            try {
                double result = expression.evaluate();
                displayText.setText(String.valueOf(result));
            } catch (ArithmeticException e) {
                displayText.setText("ERROR");
            }

        } else {
            displayText.setText("");
        }

    }

}
