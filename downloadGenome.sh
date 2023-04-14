
wget -q -t 1 --timeout=180 https://hgdownload.soe.ucsc.edu/goldenPath/hg38/bigZips/analysisSet/hg38.analysisSet.fa.gz
echo "Downloaded file"
gunzip hg38.analysisSet.fa.gz
echo "Unzipped file"
