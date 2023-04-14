import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;

//import java.io.FileNotFoundException;

public class curate {
  
    public static void main(String args[]) throws IOException{
        //candidate below can be used in future to represent candidate miRNA
        //ArrayList<Candidate> potential = new ArrayList<>();
        //potential = mapping.getCandidates(fastq);
        //load all the neccessary modules (not currently working):
      //  loadModules();
        
        //String workingdir = "/global/home/hpc4982/submissionFiles";
        //default working directory
        String workingdir = "./";
        try{
            Runtime r = Runtime.getRuntime();
            //get working directory (where pipeline is located) to be used in future commands
            String cmd = "pwd";
            Process p = r.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine = null;
                while((inputLine = in.readLine()) != null){
                    workingdir = inputLine;
                }
            in.close();
            p.destroy();
        } catch (IOException e){
            System.out.println(e);
        }

    



        //open scanner to get user input
        Scanner dec = new Scanner(System.in);
        String decision;
        boolean yn = true;

        //default path of known precursors
        String prec_path = "precursors.fa";

        //default miRNA file to use
        String mirs = "artificialSeq.fastq";
        //Generate FASTA or use preexisting:
        System.out.println("Generate new fasta file for precursor sequences (if you already have a fasta file with precursors, type no): yes or no");
        decision = dec.nextLine();
        //3 gb file that can't be added to repo, so there is option to download it
        System.out.println("Download genome for alignment? If file 'h8.analysisSet.fa' already exists, enter no: yes/no");
        String dlChoice = dec.nextLine();
        if(dlChoice.equals("yes")){
            try{
                Runtime rundl = Runtime.getRuntime();
                String[] cmd = {"/bin/sh", workingdir + "/downloadGenome.sh"};
		        System.out.println("trying to run downloadGenome.sh");
                Process pdl = rundl.exec(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(pdl.getInputStream()));
                String inputLine = null;
                while((inputLine = in.readLine()) != null){
                    System.out.println(inputLine);
    
                }
                in.close();
               // p2.destroy();
            
            } catch (IOException e){
                System.out.println(e);
            }
        }
        //Get filename containing miRNAs to be evaluated
        System.out.println("Specify the name of the file containing the miRNAs (e.g. miRNAs.fastq): ");
        mirs = dec.nextLine();
        String allprecs = "no";
        switch(decision){
        case "yes":
            yn = true;
            System.out.println("Specify the name of the fasta file to write the precursors to (e.g. precursors.fa):");
            
            prec_path = dec.nextLine();
            System.out.println("Run on all precursors? 'no' will run on the first 15. [yes/no]");
            allprecs = dec.nextLine();
            break;
        case "no": 
            System.out.println("Specify the name of the fasta file containing the precursors (e.g. precursors.fa):");
            
            prec_path =  dec.nextLine();
            yn = false;
            break;
        
    }
    //Determine whether or not to build the genome, only needs to be done once
        boolean precursorbuild = false;
        boolean genomebuild = false;
        System.out.println("Run build on human genome: yes or no? (If this program has been run before, say no.)");
        String genomechoice = "no";
        genomechoice = dec.nextLine();
    
        switch(genomechoice){
        case "yes":
            genomebuild = true;
            break;
        case "no": 
            genomebuild = false;
            break;
        }
        //Determine whether or not to build the genome, only needs to be done once for a set of precursors
        System.out.println("Run build on precursors: yes or no");
        String precchoice = "no";
        precchoice = dec.nextLine();
    
        switch(precchoice){
        case "yes":
            precursorbuild = true;
            break;
        case "no": 
            precursorbuild = false;
            break;
        }
        dec.close();
       
        //if a precursor fasta file needs to be created
        if(yn){
            //get the precursors, creating a new precursor object for each one
            System.out.println("Getting precursor sequences...");
            ArrayList<Precursor> precursors = new ArrayList<>();
            String precfile = workingdir + "/miRNA_master.tsv";
            precursors = mapping.getPrecursors(precfile);
            
            //A test list that contains the first 15 precursors
            ArrayList<Precursor> testList = new ArrayList<>();
            testList.add(precursors.get(0));
            testList.add(precursors.get(1));
            testList.add(precursors.get(2));
            testList.add(precursors.get(3));
            testList.add(precursors.get(4));
            testList.add(precursors.get(5));
            testList.add(precursors.get(6));
            testList.add(precursors.get(7));
            testList.add(precursors.get(8));
            testList.add(precursors.get(9));
            testList.add(precursors.get(10));
            testList.add(precursors.get(11));
            testList.add(precursors.get(12));
            testList.add(precursors.get(13));
            testList.add(precursors.get(14));

            //if only using the first 15 precursors
            if(allprecs.equals("no")){
                getIDs.getTranscriptIds(testList, workingdir);
                getPrecursorSequences(testList, prec_path, workingdir);
            }
           
           //if running on all precursors
           else{
               //get the transcript id of each precursor
                getIDs.getTranscriptIds(precursors, workingdir);
                //get the full sequence of each precursor
                getPrecursorSequences(precursors, prec_path, workingdir);
           }
        }
        //run alignment of the miRNAs on the human genome
        genomeAlignment(genomebuild, workingdir, mirs);
        //run alignment of the miRNAs on the precursors
        precursorAlignment(precursorbuild, prec_path, workingdir, mirs);
        //fold the precursors
        performFolding(prec_path, workingdir);
        //the folder to hold the folding output, which will be converted to pdf
        String ps_folder = workingdir + "/folding/folded";
        ps2pdf(ps_folder, workingdir);
    }
    
    private static void getPrecursorSequences(ArrayList<Precursor> precursors, String precpath, String wd) throws IOException{
      //create a new fasta file for the precursor sequences
        File precursorSequences = new File(precpath);
        try {
            precursorSequences.createNewFile();
        } catch (Exception e) {
            System.out.println("Could not create precursor file.");
        }
       //write to this file
        FileWriter writeSequences = new FileWriter(precpath);
        String header = ">";
        int precWritten = 0;
        for(int j=0; j<precursors.size();j++){
            String transcript = (precursors.get(j)).transcriptId;
            //show progress - for every 50th precursor
            if(j%50 == 0){
                String progress = "Getting sequence for precursor number ";
                progress = progress + (j+1) + ": " + transcript;
                System.out.println(progress);
            
            }
            //create the fasta header ('>transcript_id')
            header = header + transcript + "\n";
            //get the sequence
            String sequence = getSequences.getSequence(transcript, wd);
            //if the sequence is empty (not in blast db) or 200 nucleotides or longer (won't fold), skip it
            if(!transcript.isEmpty() && sequence.length()<200){
                //write the precursor to the file
                precWritten +=1;
                writeSequences.write(header);
                writeSequences.write(sequence);
                writeSequences.write("\n");
            }
            header = ">";
            
        }
        writeSequences.close();
        System.out.println("Wrote " + precWritten + " precursors.");
    }
    //perform alignment with HISAT
    private static void performAlignment(String to_align, String fastq, String out, String wd) throws IOException{
        
        try{
            Runtime r1 = Runtime.getRuntime();
            String[] cmd = {"/bin/sh",  wd + "/alignment.sh",  to_align,  fastq,  out};
            System.out.println("trying to run alignment.sh");
	    System.out.println(to_align);
	    System.out.println(fastq);
	    System.out.println(out);
            Process p1 = r1.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                String inputLine = null;
                while((inputLine = in.readLine()) != null){
                    System.out.println(inputLine);
   
                }
            in.close();
            p1.destroy();
        } catch (IOException e){
            System.out.println(e);
        }
   }
   private static void genomeAlignment(boolean build, String wd, String mirs) throws IOException{
        //if build required, run alignment build on precursors
        if(build){
            runBuild("hg38.analysisSet.fa", "hg38");
        }
        // else{
        //      return;
        // }
        //call perform alignment on human genome
        performAlignment("hg38", mirs,"genomeAlignment.sam", wd);
        //get gene names of miRNAs 
        getGeneNames(wd);
        String results = wd + "/geneNames.txt";
        //get rna types of miRNAs
        miRNA.get_rna_types(results);
   }
   private static void precursorAlignment(boolean build, String precpath, String wd, String mirs) throws IOException{
        //if build required, run alignment build on precursors
        if(build){
            runBuild(precpath, "precursors");
        }
        String alignmentOutFile = "precursorAlignment.sam";

        //call perform alignment on human genome
        performAlignment("precursors ", mirs + " ",alignmentOutFile, wd);
        System.out.println("Performing Precursor Alignment...");
        try{
            Runtime r2 = Runtime.getRuntime();
            String[] cmd = {"/bin/sh",   wd + "/alignmentFile.sh", wd + "/" + precpath,  wd + "/" + alignmentOutFile};
	    System.out.println("trying to run alignmentFile.sh");
            Process p2 = r2.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            String inputLine = null;
            while((inputLine = in.readLine()) != null){
                System.out.println(inputLine);

            }
            in.close();
           // p2.destroy();
        
        } catch (IOException e){
            System.out.println(e);
        }
 }

   private static void runBuild(String fasta, String toBuild){
       //run build for alignment using build.sh
        System.out.println("Building...");
        try{
            Runtime r3 = Runtime.getRuntime();
            String[] cmd = {"/bin/sh",  "build.sh",  fasta,  toBuild};
	    System.out.println("trying to run build.sh");
	    Process p3 = r3.exec(cmd);

	    InputStream error = p3.getErrorStream();
	    for (int i = 0; i < error.available(); i++) {
		    System.out.println("" + error.read());
	    }
            BufferedReader in = new BufferedReader(new InputStreamReader(p3.getInputStream()));
            String inputLine = null;
            while((inputLine = in.readLine()) != null){
                System.out.println(inputLine);
            }
            in.close();
         //   p3.destroy();

        } catch (IOException e){
            System.out.println(e);
        }
   }

    private static void getGeneNames(String wd){
        //run getGeneNames.sh on each miRNA
        String referenceGenome = "hg38.refGene.gtf";
        try{
            Runtime r4 = Runtime.getRuntime();
            System.out.println("Getting gene names...");
            String[] cmd = {"/bin/sh", "getGeneNames.sh",  "genomeAlignment.sam",  referenceGenome};
	        System.out.println("trying to run getGeneNames.sh");
            Process p4 = r4.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p4.getInputStream()));
            String inputLine = null;
            
            while((inputLine = in.readLine()) != null){
                
                System.out.println(inputLine);
   
                }
           in.close();
           //p4.destroy();
        } catch (IOException e){
            System.out.println(e);
        }
    }
    private static void performFolding(String to_fold, String wd){
        //run folding.sh on precursors
        try{
            Runtime r5 = Runtime.getRuntime();
            String[] cmd = {"/bin/sh",  wd + "/folding/folding.sh",  wd + "/" + to_fold};
            System.out.println("Folding precursors...");
	        System.out.println("trying to run folding.sh");
            Process p5 = r5.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p5.getInputStream()));
                String inputLine = null;
                while((inputLine = in.readLine()) != null){
                    System.out.println(inputLine);
   
                }
            in.close();
          //  p5.destroy();
            } catch (IOException e){
                System.out.println(e);
            }
            
        }

    //converts postscript output from folding into pdf
    private static void ps2pdf(String ps_folder, String wd){
        //loop through folder with folding output
        File dir = new File(ps_folder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File f : directoryListing) {
                //get the path to each file
                String to_convert = f.getAbsolutePath();
                //get file extension
                int lenConv = to_convert.length();
                String end = to_convert.substring(lenConv-3, lenConv);
                //if its a postscript file
                if(end.equals(".ps")){
                    //run ps2pdf which converts it into a pdf
                    try{
                        Runtime r6 = Runtime.getRuntime();
                        String[] cmd = {"/bin/sh",  wd + "/folding/ps2pdf.sh",  to_convert};
			            System.out.println("trying to run ps2pdf.sh");	
                        Process p6 = r6.exec(cmd);
                        BufferedReader in = new BufferedReader(new InputStreamReader(p6.getInputStream()));
                        String inputLine = null;
                        while((inputLine = in.readLine()) != null){
                            System.out.println(inputLine);
                        }
                        in.close();
                    //    p6.destroy();
                    } catch (IOException e){
                        System.out.println(e);
                    }
                }
            }
        }
    }

    

    // private static void loadModules(){
    //     try{
    //         Runtime r = Runtime.getRuntime();
    //         String cmd = "/bin/sh /global/home/hpc4982/resources/loadModules.sh";
    //         //System.out.println("command:");
    //         //System.out.println(cmd);
    //         Process p = r.exec(cmd);
    //         p.destroy();
    //         //System.out.println(p.exitValue());
            
    //     } catch (IOException e){
    //             System.out.println(e);
    //         }
    //     }
    }
