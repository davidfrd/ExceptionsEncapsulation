package Handler.Exceptions;

@FunctionalInterface
public interface ExceptionHandler<R, E extends Exception> {
    R invoke(MethodCallerWithException<R, E> methodCaller);
}
