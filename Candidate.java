public class Candidate {
    //A class to represent potential microRNAs, not currently in use
    public String sequence;
    public RnaType type;
    
    public enum RnaType{
        miRNA, 
        snoRNA,
        tRNA
    }
   


    public Candidate(String seq){
        this.sequence = seq;
        this.type = RnaType.miRNA;
    }
    public String getSequence(){
        return this.sequence;
    }
    public RnaType getType(){
        return this.type;
    }
   
}
