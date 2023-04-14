Authors: Molly Fleming and Chloe Talman

This application is designed to classify human non-coding RNA sequences. 
We obtained our reference genome (Human Genome 38) in Gene-Transfer Format from the UCSC Genome 
Browser and in FASTA format from the NCBI (National Center for Biotechnology Information). 
This pipeline is run through the command line in a Linux environment through the Centre of Advanced 
Computing (CAC). 

Starting instructions:
Unzip the resources folder and make sure its contents are in the same folder as the 
other applications components (miRNA_curation)
Be sure the java classes are compiled using javac *.java for each class.
The pipeline takes a fasta (miRNAs/ncRNAs) and optionally a fastq (precursors) file as input.
There will be several prompts before the pipeline starts, please read and answer them carefully.


The following are brief explanations of each file in the application and its dependencies.

Java Classes:
curate.java
The main class that executes the pipeline. 
Candidate.java
A class to represent a ncRNA. This class is not currently in use.

Precursor.java
A class to represent a known precursor sequence.

getIDs.java
Gets the transcript id of each precursor sequence. Runs transcriptid.sh.

getSequences.java
Gets the full sequence of each precursor and writes it to the fasta file. Runs preseq.sh.

mapping.java
Reads in the precursors (from miRNA_master) and converts each precursor's name to its 
gene name to be used for getting the transcriptid.

miRNA.java
generates an output .csv file describing the composition of the given ncRNAs, showing percentages of
each ncRNA type found in the sample.


Bash Scripts:
alignment.sh
Runs HISAT alignment 

alignmentFile.sh
Creates files for alignment output

build.sh
creates the build files needed for alignment

downloadGenome.sh
if user answers yes when asked about downloading, this will download hg38.refGene.gtf and hg38.analysisSet.fa

folding.sh
Runs folding on precursors

loadModules.sh
A script for preloading all modules needed. Not currently in use.

preseq.sh
Gets the full sequence of a known precursor using the transcript id and allSeqs blast database.

ps2pdf.sh
Converts precursor folding output (postscript files) into pdfs for viewing.

transcriptid.sh
Gets the transcript id of a precursor sequence using the gene name and human genome 38 annotations.


Resources:
miRNA_master.tsv
Contains the known precursor sequences used. The precursor names are extracted from this file.

hg38.refGene.gtf
Human Genome 38 genome annotations. Obtained from UCSC Genome Browser.

refSeq.fa
Human Genome 38 sequences in fasta format. Obtained from NCBI.

artificialSeq.fastq
A file containing artificial sequences to be used as miRNAs. Used for testing.

artSeqShort.fastq
A smaller version of artificialSeq.fastq used for quick testing.

smalltest.fa
A small fasta file containing the first 15 (13) precursors, used for testing.

/allSeqs (several files)
files for a blastdb called allSeqs that contains hg38 transcript ids and sequences. Created from refSeq.fa.

/folding
contains folding.sh and ps2pdf.sh
also contains output from folding and ps2pdf, which is a postscript file and corresponding pdf for each
precursor's folding structure.

modules
All modules are loaded using ‘module load packageName/version’. 
blast+/2.10.1
hisat2/2.1.0
viennarna/2.4.9

custom modules in CAC:

gcc/9.3.0
gcc/7.3.0
StdEnv/2020
nixpkgs/16.09
