package Handler.Exceptions;

import Handler.HandlerBuilder;
import Handler.Process.MethodHandler;

import java.util.Optional;

public class HandleMethodException<R, E extends Exception> {

    MethodCallerWithException<R, E> methodCallerWithException;

    public HandleMethodException(MethodCallerWithException<R, E> method) {
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


    public Optional<R> evaluate() {
        try {
            return Optional.of(methodCallerWithException.call());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}