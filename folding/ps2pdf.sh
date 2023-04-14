#This file takes a ps file and converts it to a pdf

#Input:
    #1 - a ps file

cd folded
ps2pdf $1
echo "ps2pdf.sh has run"
