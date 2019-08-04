package Handler.Exceptions;

@FunctionalInterface
public interface MethodCallerWithException<R, E extends Exception> {
    R call() throws E;
}
