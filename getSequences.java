import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class getSequences {
    //gets the precursor sequence using the transcript id by running preseq.sh
    public static String getSequence(String transcriptId, String wd){
        String seq = "";
       
        try{
            Runtime r = Runtime.getRuntime();
            String[] cmd = {"/bin/sh", wd + "/preseq.sh",  transcriptId};
          
            Process p = r.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine = "";

            while((inputLine = in.readLine()) != null){
                if(inputLine.isEmpty()){
                }
                else
                    seq = seq + inputLine;
             }


                in.close();
            } catch (IOException e){
                System.out.println(e);
            }

        return seq;
    }
}
