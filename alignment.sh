#This file performs folding using hisat2

#Input:
    #1 - index name
    #2 - fastq file
    #3 - output file name

module load hisat2
hisat2 -x $1 -U $2 -S $3
echo "alignment.sh has run"
