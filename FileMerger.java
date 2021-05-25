import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * FileMerger.java
 * A script merging phrases Collection with a documents Collection.
 * @author Georgios M. Moschovis (p3150113@aueb.gr)
 */
public class FileMerger {
    private String collectionRepository, phrasesRepository, outputRepository, analyzedText;
    private List<String> collectionDocuments, collectionPhrases;
    private boolean phrases;
    /*
     * Params: collectionDocumentsRepository, collectionPhrasesRepository, outputRepository
     * Check the examples at the bottom of the page!
     */
    public FileMerger(String collectionRepository, String phrasesRepository, String outputRepository) {
        this.collectionRepository = collectionRepository;
        this.phrasesRepository = phrasesRepository;
        this.outputRepository = outputRepository;
        this.analyzedText = "";
        this.phrases = false;
    }

    public void PopulateCollectionData() {
        //Initializations
        collectionDocuments = new ArrayList<String>();
        collectionPhrases = new ArrayList<String>();

        //Mode 1
        this.PopulateCollectionData(collectionRepository);

        //Mode 2        
        phrases = true;
        this.PopulateCollectionData(phrasesRepository);
    }

    public void PopulateCollectionData(String repos) {
        List<String>Result_data_filenames = listAllFiles(repos);
        analyzedText = "";
        // Searching for the above listed files.
        int numOfTargetFiles = Result_data_filenames.size();
        for(int file = 0; file < numOfTargetFiles; file++)
            ReadCollectionFile(repos + "/", Result_data_filenames.get(file));
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

    public void ReadCollectionFile(String dir, String file) {
        int line = 1;
        BufferedReader br = null;
        String path = dir + file, sCurrentLine, document = "";
        try {
            br = new BufferedReader(new java.io.FileReader(path));
            System.out.println(path); //debugging
            while ((sCurrentLine = br.readLine()) != null) {
                document += (sCurrentLine.toLowerCase() + " ");
                System.out.println((line++) + ": Reading... " + sCurrentLine); //debugging
            }
            if(!phrases) collectionDocuments.add(document.replaceAll("[^-A-Za-z0-9]+", " ").trim().replaceAll(" +", " "));
            else collectionPhrases.add(document.replaceAll("[^-A-Za-z0-9]+", " ").trim().replaceAll(" +", " "));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    //Writing documents payload back to disk.
    public void writeMassiveFile() {
        for(int i = 0; i < 18316; i++) {
            System.out.println("Merging... " + i);
            analyzedText += (collectionDocuments.get(i) + " " + collectionPhrases.get(i) + "\n");
        }
        //Data stream
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outputRepository + "/corbis_microsoft_doublecollection.txt");
            pw.write(analyzedText); pw.flush(); pw.close();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }

    }

        public static void run() {
            FileMerger fm = new FileMerger("/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings/Collection_Preprocessed",
                    "/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings/microsoft90_Collection",
                    "/media/DataHD/users/tzk/Documents");
            fm.PopulateCollectionData();
            fm.writeMassiveFile();
        }
    }