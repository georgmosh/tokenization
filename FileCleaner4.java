import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * FileCleaner4.java
 * This class merges multiple files into one; where each initial file is comprised
 * from one single line in the new file. During preprocessing; all non-alpharithmetic
 * characters are removed to make training input clearer.
 * @author Georgios M. Moschovis (p3150113@aueb.gr)
 */
public class FileCleaner4 {
    private String collectionRepository, outputRepository, analyzedText;
    /*
     * Params: collectionRepository, outputRepository
     * Check the examples at the bottom of the page!
     */
    public FileCleaner4(String collectionRepository, String outputRepository) {
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
        String path = dir + file;
        try {
            br = new BufferedReader(new java.io.FileReader(path));
            System.out.println(path); //debugging
            Stream<String> stream = br.lines();
            Object[] lines = stream.toArray();
            for(int i = 0; i < lines.length; i++) {
                String sCurrentLine = (String)lines[i];
                String[] currentLine = sCurrentLine.split(" ");
                for(String token: currentLine) {
                    token  = token.toLowerCase().trim().replaceAll("[^-\nA-Za-z0-9]+", "").replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "").replaceAll(" +", " ").trim();
                    analyzedText += (token + " ");
                }
                analyzedText += "\n";
                System.out.println((line++) + ": Reading... " + sCurrentLine); //debugging
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    //Writing documents payload back to SSD.
    public void writeMassiveFile() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outputRepository + "\\dblp_small_preprocessed.txt");
            pw.write(analyzedText); pw.flush(); pw.close();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    public static void run() {
        FileCleaner4 fc = new FileCleaner4("C:\\Users\\Georgios Moschovis\\Desktop\\dblp_titles_small",
                "C:\\Users\\Georgios Moschovis\\Desktop");
        fc.PopulateCollectionData();
        fc.writeMassiveFile();
    }
}