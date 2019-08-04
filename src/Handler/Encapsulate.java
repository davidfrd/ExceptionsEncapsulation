package Handler;

import Handler.Exceptions.ExceptionHandler;
import Handler.Exceptions.MethodCallerWithException;
import Handler.Process.MethodHandler;

import java.util.Optional;

public class Encapsulate<R, E extends Exception> {

    MethodCallerWithException<R, E> methodCallerWithException;

    public Encapsulate(MethodCallerWithException<R, E> method) {
        this.setMethodWithException(method);
    }

    public static <R, E extends Exception> Encapsulate<R, E> of(MethodCallerWithException<R, E> method) {
        return new Encapsulate<>(method);
    }

    private void setMethodWithException(MethodCallerWithException<R, E> method) {
        methodCallerWithException = method;
    }

    public Encapsulate<R, E> handleException(ExceptionHandler<R, E> exceptionHandler) {
        return new Encapsulate<>(() -> exceptionHandler.invoke(methodCallerWithException));
    }

    public Encapsulate<R, E> handleResult(MethodHandler<R> errorHandler) {
        return new Encapsulate<>(() -> errorHandler.invoke(() -> {
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
