package com.example.calculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText editText; // Поле ввода
    private String currentInput = ""; // Текущее значение в поле ввода
    private String operator = ""; // Оператор (+, -, *, /)
    private double firstNumber = 0; // Первое число для операции
    private boolean isOperatorClicked = false; // Флаг для отслеживания нажатия оператора

    // Добавьте DecimalFormat для форматирования чисел
    private DecimalFormat decimalFormat = new DecimalFormat("#.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация текстового поля
        editText = findViewById(R.id.editText);


        // Инициализация кнопок
        Button buClear = findViewById(R.id.buClear);
        Button buPlusMinus = findViewById(R.id.buPlusMinus);
        Button buPercent = findViewById(R.id.buPercent);
        Button buDelete = findViewById(R.id.buDelete);
        Button buEquals = findViewById(R.id.buEquals);
        Button buPlus = findViewById(R.id.buPlus);
        Button buMinus = findViewById(R.id.buMinus);
        Button buMul = findViewById(R.id.buMul);
        Button buDev = findViewById(R.id.buDev);
        Button buDot = findViewById(R.id.buDot);

        // Кнопки цифр
        Button bu0 = findViewById(R.id.bu0);
        Button bu1 = findViewById(R.id.bu1);
        Button bu2 = findViewById(R.id.bu2);
        Button bu3 = findViewById(R.id.bu3);
        Button bu4 = findViewById(R.id.bu4);
        Button bu5 = findViewById(R.id.bu5);
        Button bu6 = findViewById(R.id.bu6);
        Button bu7 = findViewById(R.id.bu7);
        Button bu8 = findViewById(R.id.bu8);
        Button bu9 = findViewById(R.id.bu9);

        // Установка обработчиков событий
        setButtonClickListeners(bu0, "0");
        setButtonClickListeners(bu1, "1");
        setButtonClickListeners(bu2, "2");
        setButtonClickListeners(bu3, "3");
        setButtonClickListeners(bu4, "4");
        setButtonClickListeners(bu5, "5");
        setButtonClickListeners(bu6, "6");
        setButtonClickListeners(bu7, "7");
        setButtonClickListeners(bu8, "8");
        setButtonClickListeners(bu9, "9");

        buDot.setOnClickListener(v -> handleDot());
        buClear.setOnClickListener(v -> handleClear());
        buDelete.setOnClickListener(v -> handleDelete());
        buPlusMinus.setOnClickListener(v -> handlePlusMinus());
        buPercent.setOnClickListener(v -> handlePercent());
        buPlus.setOnClickListener(v -> handleOperator("+"));
        buMinus.setOnClickListener(v -> handleOperator("-"));
        buMul.setOnClickListener(v -> handleOperator("*"));
        buDev.setOnClickListener(v -> handleOperator("/"));
        buEquals.setOnClickListener(v -> handleEquals());
    }

    private void setupKeyboardListener() {
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                handleKeyPress(keyCode); // Обрабатываем нажатие клавиши
                return true;
            }
            return false;
        });
    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_NUMPAD_0:
                handleNumberInput("0");
                break;
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_NUMPAD_1:
                handleNumberInput("1");
                break;
            // Добавьте обработку остальных цифр аналогично
            case KeyEvent.KEYCODE_PERIOD:
            case KeyEvent.KEYCODE_NUMPAD_DOT:
                handleDot();
                break;
            case KeyEvent.KEYCODE_ENTER:
                handleEquals();
                break;
            case KeyEvent.KEYCODE_DEL:
                handleDelete();
                break;
            // Добавьте обработку других клавиш (+, -, *, / и т.д.)
        }
    }
    private void handleNumberInput(String number) {
        if (isOperatorClicked) {
            currentInput = ""; // Сбрасываем текущий ввод, если был нажат оператор
            isOperatorClicked = false;
        }
        currentInput += number; // Добавляем цифру к текущему вводу
        editText.setText(currentInput); // Обновляем поле ввода
    }

    private void setButtonClickListeners(Button button, String number) {
        button.setOnClickListener(v -> {
            if (isOperatorClicked) {
                currentInput = "";
                isOperatorClicked = false;
            }
            currentInput += number;
            editText.setText(currentInput);
        });
    }

    private void handleDot() {
        if (!currentInput.contains(".")) {
            currentInput += ".";
            editText.setText(currentInput);
        }
    }

    private void handleClear() {
        currentInput = "";
        operator = "";
        firstNumber = 0;
        isOperatorClicked = false;
        editText.setText("0");
    }

    private void handleDelete() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            editText.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private void handlePlusMinus() {
        if (!currentInput.isEmpty()) {
            double number = Double.parseDouble(currentInput);
            number = -number;
            // Форматируем результат
            currentInput = decimalFormat.format(number);
            editText.setText(currentInput);
        }
    }

    private void handlePercent() {
        if (!currentInput.isEmpty()) {
            double number = Double.parseDouble(currentInput);
            number = number / 100;
            // Форматируем результат
            currentInput = decimalFormat.format(number);
            editText.setText(currentInput);
        }
    }

    private void handleOperator(String op) {
        if (!currentInput.isEmpty()) {
            firstNumber = Double.parseDouble(currentInput);
            operator = op;
            isOperatorClicked = true;
        }
    }

    private void handleEquals() {
        if (!operator.isEmpty() && !currentInput.isEmpty()) {
            double secondNumber = Double.parseDouble(currentInput);
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        editText.setText("Error");
                        currentInput = "";
                        operator = "";
                        firstNumber = 0;
                        isOperatorClicked = false;
                        return;
                    }
                    break;
            }

            // Форматируем результат с помощью DecimalFormat
            currentInput = decimalFormat.format(result);
            editText.setText(currentInput);
            operator = "";
            isOperatorClicked = false;
        }
    }
}