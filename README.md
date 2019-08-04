# Exceptions Encapsulation
The intent of this project is create a library which allows create a clean and reusable exception management separated of the main code.

## Examples

### Basic Exception Handling
Adds method, throws an exception when a parameter is null. In this case this condition will be met and the OperationException will be thrown.
```java
Encapsulate.of(() -> add(5, null))
            .handleException(MyClass::handleOperationException)
            .evaluate()
            .ifPresent(System.out::println);
```
When the OperationException is throw, **handleOperationException** method will handle this exception.
```java
public static Double add(Integer a, Integer b) throws OperationException {
    return getDouble(a) + getDouble(b);
}

...

public static Double handleOperationException(MethodCallerWithException<Double, OperationException> method) {
        try {
            return method.call();
        } catch (OperationException e) {
            // Handling Operation Exception
            return // What you want
        }
    }
```

After the evaluation of the handling methods, you will received an **Optional** which you can keep chaining logic.  
```java
Encapsulate.of(() -> add(5, 2))
            .handleException(MyClass::handleOperationException)
            .evaluate()
            .ifPresent(System.out::println);

```

### Chaining exception handling with result handling
When you want to reuse certain result handling logic after controlling exceptions, you can chain method to do this:
```java
Encapsulate.of(() -> add(a, b))
                .handleException(Main::handleOperationException)
                .handleResult(Main::handleResultLessThanZero)
                .evaluate()
                .ifPresent(System.out::println);
```
```java
public static Double handleResultLessThanZero(MethodCaller<Double> method) {
        Double result = method.call();
        return result < 0 ? 0 : result;
    }
```
In this case, if the result is less than zero, the method method will be return zero.


## Authors

* **David Fernando Redondo Durand** - [GitHub](https://github.com/davidfrd/) - [Email](mailto:davidfrd2@gmail.com)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details