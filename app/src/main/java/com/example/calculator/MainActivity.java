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


        this.screen = (TextView)findViewById(R.id.input_box);
        this.screen.setText(display);
        this.inputText = findViewById(R.id.input_box);
        this.displayText = findViewById(R.id.result_box);
    }

    private void appendToLast(String str) {
        this.inputText.getText().append(str);
    }

    public void onClickNumber(View v) {
        Button b = (Button) v;
        this.display += b.getText();
        this.appendToLast(display);
        this.display="";
    }

    public void onClickOperator(View v) {
        Button b = (Button) v;
        this.display += b.getText();
        if (endsWithOperator()) {
            this.replace(display);
        } else {
            appendToLast(display);
        }
        this.currentOperator = b.getText().toString();
        this.display = "";

    }

    public void onClearButton(View v) {
        this.inputText.getText().clear();
        this.displayText.setText("");
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

    // continious dots, starts with dot, ends with dot, dot  -> return true
    private boolean checkDotCases(String inp) {
        if (inp.contains("..")) {
            return true;
        }
        if (inp.startsWith(".")) {
            return true;
        }
        if (inp.endsWith(".")) {
            return true;
        }
        if (inp.contains(".+") || inp.contains("+.") || inp.contains(".-") || inp.contains("-.")
                || inp.contains("x.") || inp.contains(".x") || inp.contains("./") || inp.contains("/.")) {
            return true;
        }
        return false;
    }

    private void replace(String str) {
        inputText.getText().replace(getInput().length() - 1, getInput().length(), str);
    }

    public void equalResult(View v) {
        String input = getInput();
        if (hasAlphaBet(input) || startsWithOperator()) {
            displayText.setText("ERROR");
            return;
        }
        if (checkDotCases(input)) {
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

            try {
                Expression expression = new ExpressionBuilder(input).build();
                double result = expression.evaluate();
                displayText.setText(String.valueOf(result));
            } catch (ArithmeticException e) {
                displayText.setText("ERROR");
            } catch (Exception e1) {
                displayText.setText("ERROR");
            }

        } else {
            displayText.setText("");
        }

    }

}
