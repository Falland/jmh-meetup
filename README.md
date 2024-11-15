# jmh-meetup

This repository is a demonstration of the path one can follow to transform the microbenchmark to have more clear results 
that can guide the decisions on taking one approach in place of another.

The microbenchmarks are covering very simple use case comparing ArrayList and Linked list iteration performance.

There's also a class that illustrates the environment in which we are using the particular iteration: [Problem](src/main/kotlin/org/falland/meetups/jmh/Problem.kt)

In order to run the benchmarks please build the project with following command (from the root of the project):
```zsh
./gradlew jmhJar
```

Then you can run the benchmarks like so (you need to use Java 21):
```zsh
java -jar ./build/libs/jmh-meetup-1.0-SNAPSHOT-jmh.jar B01Naive
```
