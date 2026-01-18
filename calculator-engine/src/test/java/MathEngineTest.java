import com.example.calceng.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MathEngineTest {

    private MathEngine engine;
    private ExpressionManager manager;

    @BeforeEach
    void setUp() {
        engine = new MathEngine();
        manager = new ExpressionManager();
    }


    @Test
    @DisplayName("Базовая арифметика и форматирование")
    void testBasicArithmetic() {
        assertEquals(10.0, engine.calculate("5+5"));
        assertEquals(4.0, engine.calculate("8:2"));
        assertEquals("5", engine.formatResult(5.0));
        assertEquals("5.5", engine.formatResult(5.5));
        assertEquals("Error", engine.formatResult(Double.NaN));
    }

    @ParameterizedTest
    @CsvSource({
            "2+3*4, 14",
            "(2+3)*4, 20",
            "2^3^2, 512",
            "10-2-3, 5",
            "√9+16, 19",
            "√(9+16), 5"
    })
    void testPrecedence(String expression, double expected) {
        assertEquals(expected, engine.calculate(expression), 0.001);
    }

    @Test
    @DisplayName("Сложный случай: 2^-sqrt(4)")
    void testUnaryInPower() {
        assertEquals(0.25, engine.calculate("2^(-√(4))"));
    }

    @Test
    @DisplayName("Исключения: деление на 0 и корень из отрицательного")
    void testMathExceptions() {
        assertThrows(ArithmeticException.class, () -> engine.calculate("5:0"));
        assertThrows(ArithmeticException.class, () -> engine.calculate("√(-4)"));
    }




    @Test
    @DisplayName("Логика ввода цифр и точек")
    void testInputLogic() {
        manager.processInput("5");
        manager.processInput(".");
        manager.processInput("5");
        assertEquals("5.5", manager.getExpression());

        manager.reset();
        manager.processInput(".");
        assertEquals("0.", manager.getExpression());

        manager.processInput("+");
        manager.processInput(".");
        assertEquals("0+0.", manager.getExpression());
    }

    @Test
    @DisplayName("Автоматическое умножение и скобки")
    void testSmartBrackets() {
        manager.processInput("5");
        manager.processInput("(");
        assertEquals("5*(", manager.getExpression());

        manager.processInput("5");
        manager.processInput(")");
        manager.processInput("(");
        assertEquals("5*(5)*(", manager.getExpression());
    }

    @Test
    @DisplayName("Баланс скобок")
    void testBracketBalance() {
        manager.processInput(")"); // Не должна добавиться в пустой список
        assertEquals("0", manager.getExpression());

        manager.processInput("(");
        manager.processInput("5");
        manager.processInput(")");
        manager.processInput(")"); // Вторая не должна добавиться
        assertEquals("(5)", manager.getExpression());
    }

    @Test
    @DisplayName("Удаление символов (BackSpace)")
    void testBackSpace() {
        manager.processInput("1");
        manager.processInput("2");
        manager.processInput("CE");
        assertEquals("1", manager.getExpression());

        manager.processInput("CE");
        assertEquals("0", manager.getExpression());
    }

    @Test
    @DisplayName("Сброс после ошибки")
    void testErrorReset() {
        manager.setExpression("Any Error Message");
        manager.processInput("7");
        assertEquals("7", manager.getExpression());
    }


    @Test
    @DisplayName("Смена знака числа и выражений")
    void testSignToggle() {
        manager.processInput("9");
        manager.processInput("+/-");
        assertEquals("-9", manager.getExpression());

        manager.processInput("+/-");
        assertEquals("9", manager.getExpression());

        manager.reset();
        manager.processInput("(");
        manager.processInput("5");
        manager.processInput(")");
        manager.processInput("+/-");
        assertEquals("-(5)", manager.getExpression());
    }


    @Test
    @DisplayName("Утилиты токенов")
    void testUtils() {
        assertTrue(TokenUtils.isNumber("123.45"));
        assertTrue(TokenUtils.isNumber("-123.45"));
        assertFalse(TokenUtils.isNumber("abc"));

        assertTrue(TokenUtils.isOperator("√"));
        assertTrue(TokenUtils.isDigit("7"));
    }
}