# yield4j

*yield4j* adds generator methods to Java through annotation processing magic. 

Currently, yield4j must be both a compile-time dependency and a runtime dependency. That may change in the future.

Here's an example of a generator method that generates numbers from the Fibonacci sequence:

```Java
public class Fibonacci {
    @Generator
    public Iterable<Integer> fib() {
        yield_return(0);
        yield_return(1);
        int i1 = 0, i2 = 1;
        while (true) {
            int next = i1 + i2;
            yield_return(next);
            i1 = i2;
            i2 = next;
        }
    }
}
```

## Return a value from a generator

Use `yield_return(X)` to generate value `X`. The `yield_return` method is a static method of `org.yield4j.YieldSupport`.

## Stop generating values

To stop/quit the generator, use `yield_break()`.

## Rules

* A generator method cannot contain a normal `return` statement.
* No yielding within a `synchronized` statement (generator methods can be synchronized, though).
* `yield_break` cannot be used in a `finally` statement.
* `yield_return` cannot be used in a `catch` or `finally` statement.

In general, the `yield_*` statements behave much as their C# counterparts.

## A note on finally

Code in a `finally` statement may not be executed if a generator is stopped before execution reaches that point.

## Supported Java versions

`yield4j` works with Java 6, 7 and 8. It was a while ago I tested with Java 8, so it may be shaky there.

To compile and test with all currently installed Java versions on Windows, run `buildall.bat`. Maven is required.

## Compilation

Just include the `yield4j` JAR in the class path. Make sure to not turn off annotation processing.

## TODO

See the TODO file.

A big TODO is to add some sort of editor/IDE support...

## License

MIT license. See LICENSE.md.
