import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * FileCleaner3.java
 * This class merges multiple files into one; where each initial file is comprised
 * from one single line in the new file. During preprocessing; all non-alpharithmetic
 * characters are removed to make training input clearer.
 * @author Georgios M. Moschovis (p3150113@aueb.gr)
 */
public class FileCleaner3 {
    private String collectionRepository, outputRepository, analyzedText;
    /*
     * Params: collectionRepository, outputRepository
     * Check the examples at the bottom of the page!
     */
    public FileCleaner3(String collectionRepository, String outputRepository) {
        this.collectionRepository = collectionRepository;
        this.outputRepository = outputRepository;
    }

    public void PopulateCollectionData() {
        List<String>Result_data_filenames = listAllFiles(collectionRepository);
        analyzedText = "";
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

    public void ReadCollectionFile(String dir, String file) {
        int line = 1;
        BufferedReader br = null;
        String path = dir + file, sCurrentLine, document = "";
        try {
            br = new BufferedReader(new java.io.FileReader(path));
            System.out.println(path); //debugging
            while ((sCurrentLine = br.readLine()) != null) {
                String[] currentLine = sCurrentLine.split(" ");
                for(String token: currentLine) {
                    token  = token.toLowerCase().replaceAll("[^-A-Za-z0-9]+", "").trim().replaceAll(" +", " ");
                    document += (token + " ");
                }
                System.out.println((line++) + ": Reading... " + sCurrentLine); //debugging
            }
            analyzedText += document + "\n";
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    //Writing documents payload back to SSD.
    public void writeMassiveFile() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outputRepository + "/phrases.txt");
            pw.write(analyzedText); pw.flush(); pw.close();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    public static void run() {
        FileCleaner3 fc = new FileCleaner3("/media/DataHD/users/tzk/Documents/MOSCHOVIS/Embeddings/microsoft90_Collection",
                "/media/DataHD/users/tzk/Documents");
        fc.PopulateCollectionData();
        fc.writeMassiveFile();
    }
}