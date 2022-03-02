# scala
- static/strong typed but provides type inference
- support both pass by value and reference (or value and 'name')

## terms

- higher-order functions = those which either receive other functions as parameters or return functions as a result (i.e. filter, map, flatMap..)
- anonymous function (lambda) = 
- curried function = chaining function calls, as in curriedAdder(42)(42) = 84 (where curriedAdder returns a function)

## keywords

- access
  - protected
  - private
  - public (default/None)
  - sealed
  - final

## string

- concatenation = str1 + str2

- val sInterpolation = s"$name is learning about scala $action at the age of $age")

```
val angle = 3.1415f
val fInterpolation = f"Pi is somewhere aroung $angle%2.2f"
```

- val rawInterp = raw"This has a \n newline"

## method notation (all operators are methods)

infix or operator notation of the form "{object} {function} {argument}" => only works with single-arg methods
```
class Person(val name: String, val favMovie: String) {
    def likes(movie: String): Boolean = movie == favMovie
    def +(otherPerson: Person): String = s"$this.name is friends with $otherPerson.name" 
}
val gina = new Person("Gina", "Caddyshack")
val tony = new Person("Tony", "What About Bob")

println(gina likes "Hellraiser") ==> False
println(gina likes "Caddyshack") ==> True
println(gina + tony) ==> "Gina is friends with Tony"
```

- val prefixNotation = 1.unary_-
- val postfixNotation = someObject isActive ==> only works on no-argument methods


## recursion

## functions

Simple
- def saySomething() = println("In your eyes...")

With pass-by-value args
- def funct(x: String, y: Int, z: Int = 2): Int = {}

With pass-by-name or pass-by-reference args
- def lazyArgEval(a: => String, y: => Int, z: => Int) = {}

With default args (don't have to be in end only, then just name them)
- def defaultArgs(a: String = "Hello", b: Int = 99, c: Float = 3.1415) = {
println(s"$a $b $c")
}

- defaultArgs(c = 42.99, b = 31415, a = "Defined well")

```
val adder = new ((Int, Int) => Int) {   // sugar for Function2[....]
    override def apply(v1: Int, v2: Int): Int = v1 + v2
}
```
Following is a higher-order function that allows currying, such that thay may
be called w/multi parameter lists
```
  val fancyAdder: (Int) => Function1[Int, Int] = new Function1[Int, Function1[Int, Int]] {
    override def apply(x: Int): Int => Int = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }

  println(s"A curried function ${fancyAdder(99)(99)}")
```

## object oriented programming

- class ClassSample()
- object ObjectSample() {  }
  - singleton, a single instance that may have different handles
  - can define class-level defs and access them


Companions (allowing class-level functionality/members as well as instance-level)
```
object Programmer {

    val IQ = 88   // class level, called similar to a static var in java
    def isLazy: Boolean = true
    // factory method
    def apply(mother: Programmer, father: Programmer): Programmer = ...
    
    class Programmer(val name: String) {
      
    }
    
    // commonly used as a builder/factory of objects similar to constructor signature
    val anthony = Programmer(Cordelia, Tom)
}
```

## generics

`Use the same code on several (potentially unrelated) types`

- class MyClass[T]
- trait MyTrait[T]
- def empty[A]: MyClass[A]
