import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OOVHandler.java
 * A script finding and writing back to a file the OOV words in a given embedding vectors datafile (.vec file).
 * @author Georgios M. Moschovis (p3150113@aueb.gr)
 */
public class OOVHandler {
    private Set<String> initialTerms, embeddings;
    private String embeddingsFile, outputRepository, collectionRepository;

    public String model = "";
    
    public OOVHandler(String collectionRepository, String embeddingsFile, String outputRepository) {
        embeddings = new HashSet<String>();
        initialTerms = new HashSet<String>();
        this.collectionRepository = collectionRepository;
        this.outputRepository = outputRepository;
        this.embeddingsFile = embeddingsFile;
    }
    
     public void PopulateCollectionData() {
        List<String>Result_data_filenames = listAllFiles(collectionRepository);
        
        // Searching for the above listed files.
        int numOfTargetFiles = Result_data_filenames.size();
        for(int file = 0; file < numOfTargetFiles; file++) 
            ReadCollectionFile(collectionRepository + "/", Result_data_filenames.get(file));
    }
    
    public static ArrayList<String> listAllFiles(String path){
        ArrayList<String> filenames = new ArrayList<String>();

        // Listing given repository's contained files.
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        System.out.println("Folder: " + folder);
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) filenames.add(listOfFiles[i].getName());
        }
        return filenames;
    }
    
    //Transformations: punctuation, stopwords removal, lowercase
    public void ReadCollectionFile(String dir, String file) {
        BufferedReader br = null;
        String path = dir + file, sCurrentLine;
        try {
            br = new BufferedReader(new java.io.FileReader(path));
            System.out.println(path); //debugging
            while ((sCurrentLine = br.readLine()) != null) {
                sCurrentLine = sCurrentLine.replaceAll("[-()!;{}/_,.]", " ").replaceAll(" +", " ").trim();
                String[] splittedElem1 = sCurrentLine.split(" ");
                for(String token: splittedElem1) {
                    token = token.trim().replaceAll("[^A-Za-z0-9]+", "").toLowerCase().trim();
                    if(!initialTerms.contains(token)) initialTerms.add(token);
                }
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
    
    public void ReadEmbeddingsFile() {
        BufferedReader br = null;
        String sCurrentLine;
        int line = 0;
        try {
            br = new BufferedReader(new java.io.FileReader(embeddingsFile));
            while((sCurrentLine = br.readLine()) != null) {
                System.out.println("Line " + (line++) + ": " + sCurrentLine); //debugging
                String[] currentLine = sCurrentLine.trim().split(" ");
                String term = currentLine[0].trim().replaceAll("[^A-Za-z0-9]+", "").toLowerCase().trim();
                embeddings.add(term);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
    
    //Writing out of vocabulary words back to SSD.
    public void writeMassiveFile() {
        PrintWriter pw = null;
        try {
            String current = "";
            for(String term: initialTerms) {
                if(!embeddings.contains(term)) current += (term + "\n");
            }
            pw = new PrintWriter(outputRepository + "/OOVwords_model.txt");
            pw.write(current); pw.flush(); pw.close();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }

        //Debugging
        int V = initialTerms.size();
        int E = embeddings.size();
        System.out.println("VOCABULARY");
        for(String term: initialTerms) System.out.println(term);
        System.out.println("V = " + V);
        System.out.println("E = " + E);
        System.out.println("diff = V - E = " + (V-E) + ", " + ((long)((V-E)/V)*100) + "%");
    }
    
    public static void run1() {
        OOVHandler oovh = new OOVHandler("/media/DataHD/users/tzk/Documents/Embeddings/microsoft90_Collection",
                "/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings/Example/modelbasic_phr_mcnt6.vec",
                "/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings");
        oovh.PopulateCollectionData();
        oovh.ReadEmbeddingsFile();
        oovh.writeMassiveFile();
    }
}
