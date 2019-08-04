import Exceptions.OperationException;
import Handler.Exceptions.MethodCallerWithException;
import Handler.Encapsulate;
import Handler.Process.MethodCaller;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        final Integer a = 5;
        final Integer b = 4;

        // Throws OperationException
        Encapsulate.of(() -> add(a, null))
                .handleException(Main::handleOperationException)
                .handleResult(Main::handleOperationError).evaluate().ifPresent(System.out::println);

        // Goes well
        Encapsulate.of(() -> add(a, b))
                .handleException(Main::handleOperationException)
                .handleResult(Main::handleOperationError).evaluate().ifPresent(System.out::println);

        // Handling result
        Encapsulate.of(() -> sub(a, null))
                .handleResult(Main::zeroIfResultLessThanZero).evaluate().ifPresent(System.out::println);

    }

    public static Double add(Integer a, Integer b) throws OperationException {
        return getDouble(a) + getDouble(b);
    }

    public static Double sub(Integer a, Integer b) {
        return Double.valueOf(a) - Double.valueOf(b);
    }

    public static Double getDouble(Integer i) throws OperationException {
        return Optional.ofNullable(i).map(Double::valueOf).orElseThrow(OperationException::new);
    }

    public static Double handleOperationException(MethodCallerWithException<Double, OperationException> method) {
        try {
            System.out.println("On handleOperationException");
            return method.call();
        } catch (OperationException e) {
            System.out.println("Handling OperationException...");
            return null;
        }
    }

    public static Double handleOperationError(MethodCaller<Double> method) {
        System.out.println("On handleOperationError");
        return method.call();
    }

    public static Double zeroIfResultLessThanZero(MethodCaller<Double> method) {
        Double result = method.call();
        return result < 0 ? 0 : result;
    }

}
