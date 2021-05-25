import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CollectionDAO.java
 * A class implementing a Data Access Object of the elements for the documents' Lucene index.
 * The query documents to compute similarities are also being stored to this DAO.
 * @author Georgios M. Moschovis (p3150113@aueb.gr)
 */
public class CollectionDAO {
    /**
     * Dynamic Containers to hold the collection documents.
     */
    private static List<Vec2<Integer, String>> colletion_docs;

    /**
     * Dynamic Containers to hold the queries performed.
     */
    private static List<String> queries_performed;

    /**
     * Initialize the dynamic container to hold the collection's documents.
     */
    public static void InitializeContainer() {
        colletion_docs = new ArrayList<Vec2<Integer, String>>();
    }

    /**
     * Initialize the dynamic container to hold the queries performed.
     */
    public static void InitializeContainerQueries() {
        queries_performed = new ArrayList<String>();
    }

    /**
     * Getter for the dynamic container storing the collection's documents.
     * @return The dynamic container storing the NPL context.
     */
    public static List<Vec2<Integer, String>> GetData() {
        return colletion_docs;
    }

    /**
     * Getter for the dynamic container storing the queriesperformed.
     * @return The dynamic container storing the queries.
     */
    public static List<String> GetQueries() {
        return queries_performed;
    }
}
