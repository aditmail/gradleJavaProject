package classes

class Calculator {
    int adding(int a, int b) {
        a + b
    }

    int subtract(int a, int b) {
        a - b
    }

    int multiply(int a, int b) {
        a * b
    }

    float divide(int a, int b) {
        if (b == 0) throw new RuntimeException("Cannot Divide by Zero")
        a / b
    }
}
