
#geneNames=()
#i=0

echo "This file is running"

echo > geneNames.txt

while IFS= read -r line; do
    arr=($line);
    if [[  ${arr[0]} != "@SQ" && ${arr[0]} != "@PG" && ${arr[0]} != "@HD" ]]; then
        #let i=i+1
        chr=${arr[2]}

        nt=${arr[3]}
        geneName=""
        while [[ $geneName == "" ]]; do
            geneName=($(awk -v c=$chr -v n=$((nt)) '{if(n > $4 && n < $5 && c == $1)print$18}' $2))
            if [[ $geneName != "" ]]; then 
                geneName=`cut -b 2- <<< $geneName `
                geneName=$(echo $geneName | rev)
                geneName=`cut -b 3- <<< $geneName `
                geneName=$(echo $geneName | rev)
                #geneNames+=$geneName
            fi

            if [[ $geneName == "" ]]; then
                geneName="other"
            fi
        done   
        #echo $i
        #echo $geneName

        echo $geneName >> geneNames.txt
    fi
done < $1
echo "getGeneNames.sh has run"
