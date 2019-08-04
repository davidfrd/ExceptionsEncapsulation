package Handler.Process;

@FunctionalInterface
public interface MethodHandler<R> {
    R invoke(TestFailurables.MethodCaller<R> methodCaller);
}
