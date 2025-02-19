package com.example.calculator;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText editText; // Поле ввода
    private String currentInput = ""; // Текущее значение в поле ввода
    private String operator = ""; // Оператор (+, -, *, /)
    private double firstNumber = 0; // Первое число для операции
    private boolean isOperatorClicked = false; // Флаг для отслеживания нажатия оператора

    // Добавьте DecimalFormat для форматирования чисел
    private DecimalFormat decimalFormat = new DecimalFormat("#.######");

    // Добавляем SharedPreferences для сохранения результата
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CalculatorPrefs";
    private static final String LAST_RESULT_KEY = "last_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Инициализация текстового поля
        editText = findViewById(R.id.editText);

        // Восстанавливаем последний результат при запуске
        currentInput = sharedPreferences.getString(LAST_RESULT_KEY, "");
        editText.setText(currentInput.isEmpty() ? "0" : currentInput);

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
        setupButtonWithAnimation(bu0,  () -> handleDigitInput("0"));
        setupButtonWithAnimation(bu1,  () -> handleDigitInput("1"));
        setupButtonWithAnimation(bu2,  () -> handleDigitInput("2"));
        setupButtonWithAnimation(bu3,  () -> handleDigitInput("3"));
        setupButtonWithAnimation(bu4,  () -> handleDigitInput("4"));
        setupButtonWithAnimation(bu5,  () -> handleDigitInput("5"));
        setupButtonWithAnimation(bu6,  () -> handleDigitInput("6"));
        setupButtonWithAnimation(bu7,  () -> handleDigitInput("7"));
        setupButtonWithAnimation(bu8,  () -> handleDigitInput("8"));
        setupButtonWithAnimation(bu9,  () -> handleDigitInput("9"));

        setupButtonWithAnimation(buClear, this::handleClear);
        setupButtonWithAnimation(buPlusMinus, this::handlePlusMinus);
        setupButtonWithAnimation(buPercent, this::handlePercent);
        setupButtonWithAnimation(buDelete, this::handleDelete);
        setupButtonWithAnimation(buEquals, this::handleEquals);
        setupButtonWithAnimation(buPlus, () -> handleOperator("+"));
        setupButtonWithAnimation(buMinus, () -> handleOperator("-"));
        setupButtonWithAnimation(buMul, () -> handleOperator("*"));
        setupButtonWithAnimation(buDev, () -> handleOperator("/"));
        setupButtonWithAnimation(buDot, this::handleDot);

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                handleKeyInput(keyCode, event);
                return true;
            }
            return false;
        });
    }

    // Обработка нажатий клавиш клавиатуры
    private void handleKeyInput(int keyCode, KeyEvent event) {
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            String number = String.valueOf(event.getNumber());
            handleDigitInput(number);
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    handleDot();
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    handleEquals();
                    break;
                case KeyEvent.KEYCODE_DEL:
                    handleDelete();
                    break;
                case KeyEvent.KEYCODE_PLUS:
                    handleOperator("+");
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    handleOperator("-");
                    break;
                case KeyEvent.KEYCODE_STAR:
                    handleOperator("*");
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    handleOperator("/");
                    break;
                case KeyEvent.KEYCODE_EQUALS:
                    handleEquals();
                    break;
            }
        }
    }

    private void handleDigitInput(String digit) {
        if (isOperatorClicked) {
            currentInput = "";
            isOperatorClicked = false;
        }
        currentInput += digit;
        editText.setText(currentInput);
    }

    private void setupButtonWithAnimation(Button button, Runnable action) {
        button.setOnClickListener(v -> {
            // Анимация цвета
            int normalColor = ContextCompat.getColor(this, R.color.gray);
            int pressedColor = ContextCompat.getColor(this, R.color.black);

            button.setBackgroundTintList(ColorStateList.valueOf(pressedColor));
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    button.setBackgroundTintList(ColorStateList.valueOf(normalColor)), 100);

            // Выполняем действие кнопки
            action.run();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_RESULT_KEY, currentInput);
        editor.apply();
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