# java

- pass-by-value language (instance updated when reference type passed as argument though)
- static/strong typing

---
## keywords (worth mentioning)

- instanceof = Checks whether an object is an instance of a specific class or an interface
- native
- public
- private
- protected
- (friendly)
- abstract = for classes and methods: An abstract class cannot be used to create objects (to access it, it must be inherited from another class). An abstract method can only be used in an abstract class, and it does not have a body. The body is provided by the subclass (inherited from)
- interface
- final = for classes, attributes and methods, which makes them non-changeable (impossible to inherit or override)
- module (v9) = declares a module
- strictfp
- static
- requires (v9) = required lib for module
- transient = specifies that attribute is not serialized (and therefore not persisted)
- synchronized
- volatile = any write to a volatile field happens before every subsequent read of the same field. This is the volatile variable rule of the Java Memory Model (JMM). (can help to avoid errors due to compiler optimizations like re-ordering)
- var (v10) = local variable type inference

---
## datatypes

- primitives
  - byte	1 byte	Stores whole numbers from -128 to 127
  - short	2 bytes	Stores whole numbers from -32,768 to 32,767
  - int	4 bytes	Stores whole numbers from -2,147,483,648 to 2,147,483,647
  - long	8 bytes	Stores whole numbers from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
  - float	4 bytes	Stores fractional numbers. Sufficient for storing 6 to 7 decimal digits
  - double	8 bytes	Stores fractional numbers. Sufficient for storing 15 decimal digits
  - boolean	1 bit	Stores true or false values
  - char	2 bytes	Stores a single character/letter or ASCII values

- reference (classes, store addresses):
  - class
  - interface
  - type variables
  - arrays

```
byte bNum = 2;
short sNum = 3;
int iNum = 0;  
long lNum = 6540654;

// underscores introduced in 7, for readability; no bearing on number
long ccNum = 4081_5464_6545_8444l;  
long hex = 0xFF_EC_DE_5E;
byte nible = 0b0110_1110;
long bytes = 0b0111_1101_0111_0000;
float fNum = 6540.5065f;
double dNum = 4.022E33;
char ch = 'a';
boolean bool = false; // can't be null
        
String str = "where it's at!";  // special reference type, immutable
```
---
## functional 
- lambda = (simple/one-liner) anonymous function that can have any number of args but one expression
- functional interface = 
- predicate = expression that returns a boolean ``` (t -> t.passesSomeTest() && t.isWhatever...) ```
- consumer = expression that performs op on arg and returns void (terminates); like a side effect ``` (t -> println(t)) ```
- supplier = provides an instance of T (such as a factory) 
```
  Supplier<LocalDateTime> s = () -> LocalDateTime.now();
  LocalDateTime time = s.get();
```
- function = transforms T to U
```
Function<Drummer,Beat> funk = t -> t.getBestBeat()
Drummer trilokGurtu = new Drummer()
println(funk.apply(trilokGurtu))       // output is "tabla batta, batta matta, ba boom.."
```

---
## stream
- immutable, can only be used once (else need to create new stream)
- may be lazy depending upon terminal op (forEach isn't lazy). 
computation on source data performed only when the terminal op is initiated
- intermediate => filter, map, peek
- terminal => forEach, count, sum, average, min, max, collect
- terminal short-circuit => findFirst, findAny, anyMatch, noneMatch, allMatch

```
strArray.stream().count()
strArray.stream().map(f -> f.contains("chip"))
strArray.stream().filter(f -> f.contains("chocolate"))
strArray.stream().skip(8).peek(flav -> flav)

List<String> list = Stream.of("foo", "bar").collect(Collectors.toList());			// stream to list
Set<T> copy = original.stream().collect(Collectors.toSet());                          // stream to set
Set<String> set = people.stream().map(Person::getName).collect(Collectors.toCollection(TreeSet::new));
```

---
## collections and iteration

#### arrays
```
int[] anArray;
int anOtherArray[];
int[] anArray = new int[10];
int[] anArray = new int[] {1, 2, 3, 4, 5};
anArray[0] = 10;
for(int i=0; i<array.length; ++i) {...}
for(int element : anArray) {...}

void varargsMethod(String... varargs) {}
String[] anArray = new String[] {"Milk", "Tomato", "Chips"};
varargsMethod(anArray);

List<Integer> aList = Arrays.asList(anArray);		// convert to List
Stream<String> aStream = Arrays.stream(anArray);	// convert to Stream

Arrays.sort(anArray);			// sorting
Arrays.sort(yetAnotherArray, 1, 3, Comparator.comparing(String::toString).reversed());

int index = Arrays.binarySearch(anArray, 4);			// for sorted arrays
System.arraycopy(anArray, 0, resultArray, 0, anArray.length);

```

#### lists
- ordered, sequence, control over where in the list each element is inserted; allows nulls and duplicates
- array list
- linked list 

```
List<String> list = List.of("foo", "bar", "baz");

```

#### sets
- uniquity, no duplicate elements, such that e1.equals(e2)
- at most 1 null
- general purpose sets = HashSet (performance), TreeSet (ordered, red-black), LinkedHashSet (insertion order)

``` 
Set<String> set = Set.of("foo", "bar", "baz");			// java 9, immuatable
Set<T> copy = new HashSet<>(original);                // set constructor returns a copy
```
 
#### maps 
- maps keys to values, cannot contain duplicate keys, each key can map to at most one value.
- hashmap = not synchronized, constant-time performance
- hashtable = synchronized, keys must implement hashCode , equals methods
- treemap = ordered by key
- concurrent hashmap

## generics

convention:
- T: Type
- E: Element
- K: Key
- V: Value
- S,U: second, third types or more

## i/o

```
randClass.getResourceAsStream("/fileTest.txt");     // loads resource from the src/main/resources folder
classLoader.getResourceAsStream("fileTest.txt");      // loads absolute path from classpath root (not resources folder)
File file = new File(classLoader.getResource("fileTest.txt").getFile())


```
## facts

- collection = a single object designed to manage a group of objects (which rely hevily on generics)
- primitives are not allowed in a collection
- collections lie in the java.util package (List, Set, Map)
- streams = in line with functional programming, it is a coding style to implement program logic by composing functions and executing them in a data flow


