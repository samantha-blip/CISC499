#get the transcript id from human genome annotations using the gene name

echo "Executing transcriptid.sh"

transid=$(echo | grep $1 hg38.refGene.gtf | awk '{ print $12 }')
echo $transid
echo "transcriptid.sh has run"
