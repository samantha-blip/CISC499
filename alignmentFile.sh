#this will create the alignment files

#input:
#1 - precursors.fa
#2 - sam file

#write precursor sequences to a file each
    #name file after precursor

#cd /global/home/hpc4982/submissionFiles/alignedFiles

while IFS= read -r line; do
    start=${line:0:1}
    if [[ $start == ">" ]]; then
        next=${line:1:2}
        if [[ $next == "NR" ]]; then
            file="${line:1:9}.txt"
            echo > $file

        fi
    elif [[ $start == "N" ]]; then
        file="$line.txt"
        echo > $file
    else
        echo $line > $file
    fi


done < $1


#go through sam file and for each alignment get precursor name
while IFS= read -r line; do

    arr=($line)

    if [[  ${arr[0]} != "@SQ" && ${arr[0]} != "@PG" && ${arr[0]} != "@HD" ]]; then
        if [[ ${arr[2]:0:2} == "NR" ]]; then
            seq=${arr[-11]}
            precName=${arr[2]}
            startPt=${arr[3]}
            let startPt=$startPt-1

            str=$(printf "%*s%s" $startPt ' ' "$seq")
            file="$precName.txt"

            echo "$str" >> $file

        fi


    fi

done < $2

echo "alignmentFile.sh has run" 
