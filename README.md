# Calcscript interpreter

Calcscript is a stack based language designed with the purpose of executing complex calculations using minimum number of keystrokes.

## Building

Gradle will assemble the library that contains a small interactive shell:

    ./gradlew

Then it can be run with:

    java -jar build/libs/*.jar

## Script execution

Script is executed from left to right. When a number is found it is pushed onto the stack and appears on the right side of result. Each operator takes arguments from the top of the stack (right side) and leaves result on the stack.

Open the interactive shell and type: `2 3 8+*`

It is also possible to define functions in the interpreter. Running `{.sum\,/}:avg` will create function `avg` that calculates averages. After that `[1 2 4 7]avg` will calculate the average of given array.

For more in depth tutorial see https://calc.metacode.biz/tutorial
