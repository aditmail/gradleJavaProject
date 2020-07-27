import classes.Calculator
import org.junit.Test

class HelloWorldTest {

    @Test
    void addNumber() {
        def a = 2
        def b = 3
        def calculator = new Calculator()

        assert 5 == calculator.adding(a, b)
    }

    @Test
    void subtractNumber() {
        def a = 5
        def b = 25
        def calculator = new Calculator()

        assert -20 == calculator.subtract(a, b)
    }

    @Test
    void multiplyNumber() {
        def a = 20
        def b = 2
        def calculator = new Calculator()

        assert 40 == calculator.multiply(a, b)
    }

    @Test
    void divideNumber(){
        def a = 6
        def b = 5
        def calculator = new Calculator()

        assert 1.2 =~ calculator.divide(a, b)
    }
}
