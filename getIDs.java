import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getIDs{
    //runs the script to get the transcript id of each precursor
    public static void getTranscriptIds(ArrayList<Precursor> precursors, String wd) throws IOException{
        int numPrecursors = precursors.size();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        int failed = 0;
        ArrayList<Precursor> failed_ids = new ArrayList<>();
        for(int i=0; i<numPrecursors;i++){
            String geneName = (precursors.get(i)).geneId;
            String id_o = null;
            try{
                Runtime r = Runtime.getRuntime();
                String[] cmd = {"/bin/sh", wd + "/transcriptid.sh",  geneName};
                Process p = r.exec(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine = null;
                while((inputLine = in.readLine()) != null){
                    id_o = inputLine;
   		    System.out.println("Inside While Loop "+id_o);
                }

                Matcher matcher = pattern.matcher(id_o);
                //if theres a match, set the transcript id of the precursor
                if(matcher.find()) {
                    String id = matcher.group(1);
                   precursors.get(i).transcriptId = id;
                }
                else{
                  //if formatting failed
                    failed_ids.add(precursors.get(i));
                    System.out.println("Failed to find matching transcript id for gene name " + geneName);
                    failed +=1;
                   }

                in.close();
                p.destroy();
            } catch (IOException e){
                System.out.println(e);
            }
        }
        String result = "Found transcript id of " + (numPrecursors - failed) + " precursors out of " + numPrecursors + " total.";
        if(failed>=1){
            String f = "Failed to find transcript id of " + failed + " precursors out of " + numPrecursors + ".";
            System.out.println(f);
        }

        System.out.println(result);


    }
    
}
