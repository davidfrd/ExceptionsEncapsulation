package Handler.Process;

@FunctionalInterface
public interface MethodCaller<R> {
    R call();
}
