import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.function.BiFunction;

public class UserFunction {

    private Expression expression;

    public UserFunction(String expression) {
        try {
            this.expression = new ExpressionBuilder(expression)
                    .variables("x", "y")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            this.expression = null;
        }
    }

    public BiFunction<Double, Double, Double> getBiFunction() {
        return (x, y) -> {
            try {
                if (expression != null) {
                    expression.setVariable("x", x);
                    expression.setVariable("y", y);
                    return expression.evaluate();
                } else {
                    return 0.0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
            }
        };
    }
}
