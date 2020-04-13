#!/bin/bash
rm temp*
echo "Generating Random Data of ASCII values"
./gensort -a 640000000 generatedfile
echo "Sorting data"
javac SharedMemorySort.java
java SharedMemorySort generatedfile outfile
echo "Done Sorting, Validating data"
./valsort outfile
rm generatedfile                                                                                                     rm outfile
