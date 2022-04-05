# convert

[![Maven Central](https://img.shields.io/maven-central/v/org.bdgenomics.convert/convert.svg?maxAge=600)](http://search.maven.org/#search%7Cga%7C1%7Corg.bdgenomics.convert)
[![API Documentation](http://javadoc.io/badge/org.bdgenomics.convert/convert.svg?color=brightgreen&label=javadoc)](http://javadoc.io/doc/org.bdgenomics.convert/convert)

### Hacking convert

Install

 * JDK 1.8 or later, http://openjdk.java.net
 * Apache Maven 3.8.5 or later, http://maven.apache.org

To build

    $ mvn install


### About convert

The [`Converter`](https://github.com/bigdatagenomics/convert/blob/master/convert/src/main/java/org/bdgenomics/convert/Converter.java)
interface, inspired by Apache [Commons Convert](https://commons.apache.org/sandbox/commons-convert/)
(sandbox component, never released), provides for converting from a source type `S` to a target type `T`, with a
[conversion stringency](https://github.com/bigdatagenomics/convert/blob/master/convert/src/main/java/org/bdgenomics/convert/ConversionStringency.java)
and [SLF4J logger](http://www.slf4j.org/) given as context.

```java
public interface Converter<S, T> {
  T convert(S source, ConversionStringency stringency, Logger logger) throws ConversionException;
  // ...
}
```

Rather than implement a custom converter registry, `convert` relies on dependency injection via Google [Guice](https://github.com/google/guice).

Use the `@Inject` annotation to inject converters into class constructors:

```java
final class MyClass {
  private final Converter<String, OntologyTerm ontologyTermConverter;
  private final ConversionStringency stringency = ConversionStringency.STRICT;
  private static final logger = LoggerFactory.getLogger(MyClass.class);

  @Inject
  MyClass(final Converter<String, OntologyTerm> ontologyTermConverter) {
    this.ontologyTermConverter = ontologyTermConverter;
  }

  void doIt() {
    OntologyTerm ontologyTerm = ontologyTermConverter.convert("SO:0000110", stringency, logger);
  }
}
```

The Guice injector handles construction of the converter instances, managing nested converter dependencies
as necessary (if say, a `Gff3Record` to `Feature` converter depends on a `String` to `Strand` converter).

Some converters may require late bindings; for those a converter factory is available via injection:
```java
final class MyClass {
  private final AlignmentRecordToSamRecordFactory alignmentRecordToSamRecordFactory;
  private final ConversionStringency stringency = ConversionStringency.STRICT;
  private static final logger = LoggerFactory.getLogger(MyClass.class);

  @Inject
  MyClass(final AlignmentRecordToSamRecordFactory alignmentRecordToSamRecordFactory) {
    this.alignmentRecordToSamRecordFactory = alignmentRecordToSamRecordFactory;
  }

  void doIt() {
    AlignmentRecord alignmentRecord = ...;
    SAMFileHeader header = ...;
    Converter<AlignmentRecord, SAMRecord> converter = alignmentRecordToSamRecordFactory.create(header);
    SAMRecord record = converter.convert(alignmentRecord, stringency, logger);
  }
}
```
