public class Precursor {
    public String name;
    public String geneId;
    public String transcriptId;
    public String sequence;
//precursor object to represent a precursor
    public Precursor(String name){
        this.name = name;
        this.geneId = "";
        this.transcriptId = ""; 
        this.sequence = "";
    }
    public String getName(){
        return this.name;
    }
    public String getGeneId(){
        return this.geneId;
    }
    public String getTranscriptId(){
        return this.geneId;
    }
    public String getSequence(){
        return this.sequence;
    }

    public void setGeneId(String id){
        this.geneId = id;
    }

}
