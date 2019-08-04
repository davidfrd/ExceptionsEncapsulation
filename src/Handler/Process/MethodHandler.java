package Handler.Process;

@FunctionalInterface
public interface MethodHandler<R> {
    R invoke(MethodCaller<R> methodCaller);
}
