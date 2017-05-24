package oracle.apps.hcm.hwr.extwebclient.rest;

/**
 * Used to validate the result object from the RestQueryEngine.
 * 
 * Throws an exception if the results are found to be non-valid.
 * 
 * 
 * @author tgsnyder
 *
 */
public interface IRestQueryResultValidation {
    
    /**
     * Method that is used to validate the response from the RestQueryEngine.
     * 
     * @param pResult RestQuerySimpleResult object from the RestQueryEngine
     * @throws RuntimeException Throws runTimeException if error is found.
     */
    public void validateResponse(RestQuerySimpleResult pResult) throws RuntimeException;
}
