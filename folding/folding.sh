#This file creates postscript files of the folded structure of RNA sequences using RNAfold from ViennaRNA

#Input: 
	#1 - fasta file of sequences to be folded

module load viennarna
cd folded
RNAfold $1
echo "folding.sh has run"


#Through the use RNAplot you should be able to add annotations to the ps file
#if you get the location of miRNA and miRNA* from the alignment against the precursor
	#you should teoretically be able to use RNAplot to make these sections a different
	#color. The commented out code below is a rough example of how to 

