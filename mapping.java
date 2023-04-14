import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


//This contains getCandidates, which can be used to read in the miRNAs
//getPrecursors, which reads the precursor names in from a .tsv file and creates
//a new precursor instance for each precursor
//and format name, which formats the precursor name into the gene name
public class mapping {
    public static ArrayList<Candidate> getCandidates(String fastq){
        //read in miRNAs from fastq file
        //add them to an arraylist for alignment with each precursor sequence
        ArrayList<Candidate> mi_RNAs = new ArrayList<>();
        try(BufferedReader read_seqs = new BufferedReader(new FileReader(fastq))){
            String line = null;
            int lineCount = 1;
            while ((line = read_seqs.readLine()) != null){
                if (lineCount == 2){
                    Candidate c = new Candidate(line);
                    mi_RNAs.add(c);
                } 
                if (lineCount == 4){
                    lineCount = 0;
                }
                lineCount+=1;
            }
        } catch (Exception e) {
            System.out.println("Error.");
        }
        return mi_RNAs;
    }
    public static ArrayList<Precursor> getPrecursors(String filename) {
         ArrayList<Precursor> precursors = new ArrayList<>();
         try(BufferedReader read_precursors = new BufferedReader(new FileReader(filename))){
             String line = null;
             while ((line = read_precursors.readLine()) != null){
                 String[] data = line.split("\t");
                 String name = data[1];
                 //create a new precursor instance with the name that was read in
                 Precursor p = new Precursor(name);
                 //format the name and set the precursors gene name to it
                 formatName(p);
                 precursors.add(p);
               
             }
         } catch (Exception e) {
             System.out.println("Error.");
         }
        //remove the first item, which is the column name
        precursors.remove(0);
        
        return precursors;
    }

    public static void formatName(Precursor precursor){
        String name = precursor.name;
        String formatted = "";
        formatted = name.replaceAll("hsa-", "");
        formatted = formatted.replaceAll("let-", "mirlet");
        formatted = formatted.replaceAll("mir-", "mir");
        formatted = formatted.replaceAll("-[1-9]SPLICE", "");
        formatted = formatted.replaceAll("-MIRTRON", "");
        formatted = formatted.replaceAll("-DICER", "");
        formatted = formatted.replaceAll("-PAT", "");
        formatted = formatted.replaceAll("-RNASEN", "");
        formatted = formatted.replaceAll("a-", "a");
        formatted = formatted.replaceAll("b-", "b");
        formatted = formatted.replaceAll("f-", "f");
        formatted = formatted.replaceAll("-A6G", "");
       // formatted = formatted.replace("-[^-]+$","");
        formatted = formatted.replaceAll("-$","");
        formatted = formatted.toUpperCase();
        //begin hardcoded precursors
        formatted = formatted.replaceAll("MIR30C-1", "MIR30C1");
        formatted = formatted.replaceAll("MIR365-1", "MIR365A");
        formatted = formatted.replaceAll("MIR365-2", "MIR365B");
        formatted = formatted.replaceAll("MIR1270-1","MIR1270");
        formatted = formatted.replaceAll("MIR1270-2","MIR1270" );
        formatted = formatted.replaceAll("MIR517A1", "MIR517A");
        formatted = formatted.replaceAll("MIR517A2", "MIR517B");
        formatted = formatted.replaceAll("MIR103-2", "MIR103A2");
        formatted = formatted.replaceAll("MIR103-1","MIR103A1" );
        formatted = formatted.replaceAll("MIR30C-2", "MIR30C2");
        formatted = formatted.replaceAll("MIR219-1", "MIR219A1");
        formatted = formatted.replaceAll("MIR550-1", "MIR550A1");
        formatted = formatted.replaceAll("MIR550-2", "MIR550A2");
        formatted = formatted.replaceAll("MIR219-2", "MIR219A2");
        formatted = formatted.replaceAll("MIR3690-1","MIR3690" );
        formatted = formatted.replaceAll("MIR3690-2", "MIR3690");
        precursor.geneId = formatted;

    }
    
}
