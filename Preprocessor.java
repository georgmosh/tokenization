import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Preprocessor {
    private List<String> XML_data_filenames;
    private String inputDataRepository, outputDataRepository, data;

    public Preprocessor() {
        this.inputDataRepository = "C:\\Users\\georg\\Desktop\\IR_Project_2020\\CollectionFiles";
        this.outputDataRepository = "C:\\Users\\georg\\Desktop\\IR_Project_2020\\CollectionTxt";
        this.GenerateSearchData();
        this.WriteTextFile();
    }

    public static void main(String[] args) { new Preprocessor(); }

    /**
     * A method detecting and writing down all filepaths contained in a given directory.
     */
    public void GenerateSearchData() {
        XML_data_filenames = listAllFiles( inputDataRepository);
        data = " /// \n";

        /*
         * Searching for the above listed files.
         */
        int numOfTargetFiles = XML_data_filenames.size();
        for(int file = 0; file < numOfTargetFiles; file++) {
            ReadTargetFile(inputDataRepository + "\\" + XML_data_filenames.get(file));
        }
    }

    /**
     * A method creating a dynamic container of all filepaths contained in a given directory.
     * @param path The directory to find all contained filepaths.
     */
    public static ArrayList<String> listAllFiles(String path){
        ArrayList<String> filenames = new ArrayList<String>();

        /*
         * Listing given repository's contained files.
         */
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        System.out.println("Folder: " + folder);
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) filenames.add(listOfFiles[i].getName());
        }

        return filenames;
    }

    /**
     * A method reading a specific XML file and generating its JSON equivalent.
     * @param filePath The relative path of the XML file.
     */
    public void ReadTargetFile(String filePath) {
        BufferedReader br = null;
        String sCurrentLine, contents = "";
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((sCurrentLine = br.readLine()) != null) {
                sCurrentLine = sCurrentLine.trim().replaceAll(" +", " "); // unify whitespaces
                contents += (sCurrentLine + "\n");
            }

            /*
             * Converting XML to text; while unifying "title" and "objective" into "text"..
             */
            Vec2<String, String> currentDoc = splitModules(contents);
            data += currentDoc.getTValue() + "\n"; //+ currentDoc.getYValue() + "\n /// \n";
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public Vec2<String, String> splitModules(String document) {
        //Extracting the document title.
        String splitter1 = "<title>", splitter2 = "</title>",
                title = document.split(splitter1)[1].split(splitter2)[0],
                objective, rcn, newDocument;

        //Extracting the document body.
        splitter1 = "<objective>"; splitter2 = "</objective>";
        objective = document.split(splitter1)[1].split(splitter2)[0];

        //Extracting the document ID.
        splitter1 = "<rcn>"; splitter2 = "</rcn>";
        rcn = document.split(splitter1)[1].split(splitter2)[0];

        //Merging components.
        newDocument = title + ": " + objective;
        System.out.println(newDocument);
        return new Vec2<String, String>(rcn, newDocument);
    }

    public void WriteTextFile() {
        try {
            PrintWriter pw = new PrintWriter(outputDataRepository + "\\cordis_clean.txt");
           pw.write(data); pw.flush(); pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
