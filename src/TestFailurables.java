import java.util.Optional;

public class TestFailurables {

    public static void main(String[] args) {
        final Integer a = 5;
        final Integer b = null;

        Optional<Double> sumResult =
                HandlerBuilder.of(() -> suma(a, b))
                        .whenException(TestFailurables::handleOperationException)
                        .whenError(TestFailurables::handleOperationError).evaluate();

    }

    public static Double suma(Integer a, Integer b) throws OperationException {
        return getDouble(a) + getDouble(b);
    }

    public static Double resta(Integer a, Integer b) throws OperationException {
        return getDouble(a) - getDouble(b);
    }

    public static Double division(Integer a, Integer b) throws OperationException {
        return getDouble(a) / getDouble(b);
    }

    public static Double multiplicacion(Integer a, Integer b) throws OperationException {
        return getDouble(a) * getDouble(b);
    }

    public static Double getDouble(Integer i) throws OperationException {
        return Optional.ofNullable(i).map(Double::valueOf).orElseThrow(OperationException::new);
    }

    public static Double handleOperationException(MethodCallerWithException<Double, OperationException> method) {
        try {
            System.out.println("Pasando por handleOperationException");
            return method.call();
        } catch (OperationException e) {
            System.out.println("Ha ocurrido un error OperationException");
            return null;
        }
    }

    public static Double handleOperationError(MethodCaller<Double> method) {
        System.out.println("Pasando por handleOperationError");
        return method.call();
    }

    public static class OperationException extends Exception {
    }

    @FunctionalInterface
    public interface ExceptionHandler<R, E extends Exception> {
        R invoke(MethodCallerWithException<R, E> methodCaller);
    }

    @FunctionalInterface
    public interface MethodCallerWithException<R, E extends Exception> {
        R call() throws E;
    }

    @FunctionalInterface
    public interface ErrorHandler<R> {
        R invoke(MethodCaller<R> methodCaller);
    }

    @FunctionalInterface
    public interface MethodCaller<R> {
        R call();
    }

    public static class HandlerBuilder<R, E extends Exception> {

        MethodCallerWithException<R, E> methodCallerWithException;

        public HandlerBuilder(MethodCallerWithException<R, E> method) {
            this.setMethodWithException(method);
        }

        public static <R, E extends Exception> HandlerBuilder<R, E> of(MethodCallerWithException<R, E> method) {
            return new HandlerBuilder<>(method);
        }

        private void setMethodWithException(MethodCallerWithException<R, E> method) {
            methodCallerWithException = method;
        }

        public HandlerBuilder<R, E> whenException(ExceptionHandler<R, E> exceptionHandler) {
            return new HandlerBuilder<>(() -> exceptionHandler.invoke(methodCallerWithException));
        }

        public HandlerBuilder<R, E> whenError(ErrorHandler<R> errorHandler) {
            return new HandlerBuilder<>(() -> errorHandler.invoke(() -> {
                try {
                    return methodCallerWithException.call();
                } catch (Exception e) {
                    return null;
                }
            }));
        }

        public Optional<R> evaluate() {
            try {
                return Optional.of(methodCallerWithException.call());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

}
