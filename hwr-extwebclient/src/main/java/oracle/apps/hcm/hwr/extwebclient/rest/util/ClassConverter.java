package oracle.apps.hcm.hwr.extwebclient.rest.util;

public class ClassConverter {

	/**
	 * Safely casts an Object to a String.
	 * 
	 * @param pObject the Object to cast
	 * @return the Object in String form
	 */
	public static String castToString(Object pObject){
		
		if (pObject == null){
			return null;
		}
		
		if (pObject instanceof String){
			return (String)pObject;
		}
		
		return pObject.toString();
	}
	
	
	public static Number castToNumber(Object pObject){

		if (pObject == null){
			return null;
		}
		
		if (pObject instanceof Number){
			return (Number)pObject;
		}
		
		/* Object is not a Number, return null */
		return null;
	}
	
	
	public static Boolean castToBoolean(Object pObject){

		if (pObject == null){
			return null;
		}
		
		if (pObject instanceof Boolean){
			return (Boolean)pObject;
		}
		
		/* Object is not a Boolean, return null */
		return null;
	}

}
