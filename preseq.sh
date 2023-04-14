#!/bin/bash

cd allSeqs
#load modules
module load StdEnv/2020
module load nixpkgs/16.09
module load gcc/9.3.0
module load gcc/7.3.0
module load blast+/2.10.1
#get the entry with the matching input transcript id
seq=$(echo | blastdbcmd -db allSeqs -entry $1 -outfmt %s)
echo $seq
echo "preseq.sh has run"
