package Handler;

import Handler.Exceptions.ExceptionHandler;
import Handler.Exceptions.MethodCallerWithException;
import Handler.Process.MethodHandler;

import java.util.Optional;

public class HandlerBuilder<R, E extends Exception> {

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

    public HandlerBuilder<R, E> whenError(MethodHandler<R> errorHandler) {
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
