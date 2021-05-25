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
 * OOVCounter.java
 * A script counting the OOV words in a given embedding vectors datafile (.vec file).
 * @author Georgios M. Moschovis (p3150113@aueb.gr)
 */
public class OOVCounter {
    private Set<String> embeddings;
    private List<Set<String>> initialTermsList;
    private String embeddingsFile, collectionRepository;

    /*
     * Params: collectionRepository, embeddingsFile (the .vec file)
     * Check the examples at the bottom of the page!
     */
    public OOVCounter(String collectionRepository, String embeddingsFile) {
        embeddings = new HashSet<String>();
        initialTermsList = new ArrayList<Set<String>>();
            for(int i = 0; i < 10; i++) initialTermsList.add(new HashSet<String>());
        this.collectionRepository = collectionRepository;
        this.embeddingsFile = embeddingsFile;
    }

    public void PopulateCollectionData() {
        List<String>Result_data_filenames = listAllFiles(collectionRepository);

        // Searching for the above listed files.
        int numOfTargetFiles = Result_data_filenames.size();
        for(int file = 0; file < numOfTargetFiles; file++) {
            String sQuestion = Result_data_filenames.get(file).split("_")[0];
            int question = Integer.parseInt(sQuestion.substring(1, sQuestion.length()));
            System.out.println(question);
            ReadCollectionFile(collectionRepository + "/", Result_data_filenames.get(file), question-1);
        }
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
    public void ReadCollectionFile(String dir, String file, int question) {
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
                    if(!initialTermsList.get(question).contains(token)) initialTermsList.get(question).add(token);
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
        List<Integer> counts = new ArrayList<Integer>();
            for(int i = 0; i < 10; i++) counts.add(new Integer(0));
        for(int i = 0; i < initialTermsList.size(); i++) {
            Set<String> initialTerms = initialTermsList.get(i);
            for (String term : initialTerms) {
                if (embeddings.contains(term)) counts.set(i, counts.get(i) + 1);
            }
        }

        //Debugging
        int V = 0;
            for(Set<String> initialTerms: initialTermsList) V += initialTerms.size();
        int E = embeddings.size();
        System.out.println("V = " + V);
        System.out.println("E = " + E);
        System.out.println("diff = V - E = " + (V-E) + ", " + ((long)((V-E)/V)*100) + "%");
        System.out.println("Terms included in set E per query:");
        for(int i = 0; i < 10; i++) {
            System.out.println("Q0" + (i+1) + " - " + counts.get(i) + "/" + initialTermsList.get(i).size() + " have an embedding vector");
        }
    }

    public static void run() {
        OOVCounter oovc = new OOVCounter("/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings/QueriesFiles4",
                "/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings/Example/modelbasic_phr_mcnt6.vec");
        oovc.PopulateCollectionData();
        oovc.ReadEmbeddingsFile();
        oovc.writeMassiveFile();
    }

}
