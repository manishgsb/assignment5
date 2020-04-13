#!/bin/bash
echo "Generating Random Data of ASCII values"
./gensort -a 640000000 generatedfile
echo "Done Creating data, Sorting"
LC_ALL=C sort -S 8G --parallel=4 -o outfile generatedfile
echo "Done Sorting. Validating data"
./valsort outfile
rm generatedfile
rm outfile
