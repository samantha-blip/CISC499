module load hisat2
hisat2-build $1 $2
echo "build.sh has run"
#1 should be a fasta file
#2 should be a string/index name
