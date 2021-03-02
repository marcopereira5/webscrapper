package Persistence;
/**
 * This class was created to instantiate the various type's of loading and saving files
 * @author Afonso Cunha e Marco Pereira
 */
public class DaoFactory {
    /**
     * Creates the desired type and returns it
     * @param type type of DAO
     * @return GraphDao
     */
    public static GraphDao createGraphViewDao(String type){
        switch (type){
            case "file": return new GraphDaoFile();
            case "json": return new GraphDaoJson();
            default: throw new IllegalArgumentException("This type doesn't exist");
        }
    }
}
