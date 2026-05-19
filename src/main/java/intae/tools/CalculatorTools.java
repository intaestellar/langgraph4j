package intae.tools;

import dev.langchain4j.agent.tool.Tool;

public class CalculatorTools {

    @Tool("x에 y를 곱합니다.")
    private float multiply(float x, float y) {
        return x * y;
    }

    @Tool("x를 y 제곱합니다.")
    private double exponentiate(double x, double y) {
        return Math.pow(x, y);
    }

    @Tool("x와 y를 더합니다.")
    private float add(float x, float y) {
        return x + y;
    }
}
