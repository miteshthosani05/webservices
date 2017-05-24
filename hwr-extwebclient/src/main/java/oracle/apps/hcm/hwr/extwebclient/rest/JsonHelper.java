/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * JSON utility class
 * @author Yenal Kal
 */
public final class JsonHelper {
    
    public static final String TOKEN_COPY_DICT = "#__copydict";
    
    public static final String TOKEN_DICT_TO_ARRAY = "#__dict";

    /**
     * Builds a flattened map from a JSONObject
     * @param pObject Can be one of String, Number, Boolean, null, JSONObject or JSONArray
     * @param pCurrentKey The current key for the map
     * @param pMap The map for the current JSONObject
     * @param pMapDepth Depth of 0 is the JSONObject initially passed to the function. Each new map
     *        generated for JSONObjects increases the depth.
     * @param pList The list for the current JSONArray
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void buildFlattenedMapFromJson(Object pObject, String pCurrentKey,
        Map<String, Object> pMap, List<Object> pList, Map<String, String> pKeyReplacementMap) {
        if (pObject instanceof Map) {
            Map myJSONObject = (Map) pObject;

            // Process all items in the map
            Iterator iter = myJSONObject.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();

                String myNewKey = (String) entry.getKey();

                List<Object> myNewList = null;
                Map<String, Object> myMap = pMap;

                if (pCurrentKey != null) {
                    if (pKeyReplacementMap != null
                        && pKeyReplacementMap.get(pCurrentKey + TOKEN_DICT_TO_ARRAY) != null) {

                        // Special dictionary handling
                        myNewKey = pCurrentKey + TOKEN_DICT_TO_ARRAY;
                        if (pMap.get(myNewKey) == null) {
                            myNewList = new ArrayList<Object>();
                            pMap.put(myNewKey, myNewList);
                        }
                        myNewList = (List<Object>) pMap.get(myNewKey);

                        myMap = null;
                    } else {
                        myNewKey = pCurrentKey + "#" + myNewKey;
                    }
                }

                if (pKeyReplacementMap != null
                    && pKeyReplacementMap.containsKey(myNewKey + TOKEN_COPY_DICT)) {
                    
                    pMap.put(pKeyReplacementMap.get(myNewKey), entry.getValue());
                    
                } else {
                    buildFlattenedMapFromJson(entry.getValue(), myNewKey, myMap, myNewList,
                        pKeyReplacementMap);
                }
            }
        } else if (pObject instanceof List) {
            List<Object> myNewList = new ArrayList<Object>();

            boolean skip = false;

            if (pList != null) {
                pList.add(myNewList);
            } else if (pMap != null) {
                if (pKeyReplacementMap != null) {
                    String pReplacedKey = pKeyReplacementMap.get(pCurrentKey);
                    if (pReplacedKey != null && !pReplacedKey.isEmpty()) {
                        pMap.put(pReplacedKey, myNewList);
                    } else {
                        skip = true;
                    }
                } else {
                    pMap.put(pCurrentKey, myNewList);
                }
            }

            if (!skip) {
                // Could be JSONArray
                List myList = (List) pObject;

                // Process all items in the array
                for (int i = 0; i < myList.size(); i++) {
                    Object myCurObject = myList.get(i);

                    // Add the basic types directly
                    if (myCurObject == null || myCurObject instanceof String
                        || myCurObject instanceof Number || myCurObject instanceof Boolean) {
                        myNewList.add(myCurObject);
                    } else if (myCurObject instanceof Map) {
                        // Generate a new map for the object
                        Map<String, Object> myMap = new LinkedHashMap<String, Object>();
                        buildFlattenedMapFromJson(myCurObject, pCurrentKey, myMap, null,
                            pKeyReplacementMap);
                        myNewList.add(myMap);
                    } else if (myCurObject instanceof List) {
                        buildFlattenedMapFromJson(myCurObject, null, null, myNewList,
                            pKeyReplacementMap);
                    }
                }
            }
        } else if (pObject == null || pObject instanceof String || pObject instanceof Number
            || pObject instanceof Boolean) {
            if (pList != null) {
                pList.add(pObject);
            } else if (pMap != null) {
                if (pKeyReplacementMap != null) {
                    String pReplacedKey = pKeyReplacementMap.get(pCurrentKey);
                    if (pReplacedKey != null && !pReplacedKey.isEmpty()) {
                        pMap.put(pReplacedKey, pObject);
                    }
                } else {
                    pMap.put(pCurrentKey, pObject);
                }
            }
        }
    }

    /**
     * Converts the JSON string into a Map<String, Object>. The string must be a JSON object meaning
     * that the string must be enclosed with { and }.
     * @param pJsonString pJsonString The JSON string.
     * @return The resulting map
     */
    public static Map<String, Object> jsonStringToMap(String pJsonString) {
        return jsonStringToMap(pJsonString, null);
    }

    /**
     * Converts the JSON string into a Map<String, Object>. The string must be a JSON object meaning
     * that the string must be enclosed with { and }.
     * @param pJsonString The JSON string.
     * @param pKeyReplacementMap If this map is supplied, the output map will only contain values
     *        keyed by the keys in the map. Also the key is renamed to the value in the map entry.
     * @return The resulting map
     */
    public static Map<String, Object> jsonStringToMap(String pJsonString,
        Map<String, String> pKeyReplacementMap) {
        // Parse the JSON string
        Object myParsedJsonObject = JSONValue.parse(pJsonString);

        if (!(myParsedJsonObject instanceof JSONObject)
            && !(myParsedJsonObject instanceof JSONArray)) {
            return null;
        }

        return flattenJsonObject(myParsedJsonObject, pKeyReplacementMap);
    }

    /**
     * Flattens the JSON object structure.
     * @param pJsonObject The parsed JSON object
     * @param pKeyReplacementMap The map which is used to remove/replace keys.
     * @return The flattened map built from the JSON object.
     */
    public static Map<String, Object> flattenJsonObject(Object pJsonObject,
        Map<String, String> pKeyReplacementMap) {
        Map<String, Object> myMap = new LinkedHashMap<String, Object>();

        if (pJsonObject instanceof JSONArray) {
            buildFlattenedMapFromJson(pJsonObject, "items", myMap, null, pKeyReplacementMap);
        } else {
            buildFlattenedMapFromJson(pJsonObject, null, myMap, null, pKeyReplacementMap);
        }

        return myMap;
    }

    /**
     * Parses the JSON string and returns the results.
     * @param pJsonString The JSON string
     * @return Returns the JSON object using the following mapping: JSON string => java.lang.String
     *         JSON number => java.lang.Number JSON true|false => java.lang.Boolean JSON null =>
     *         null JSON array => java.util.List JSON object => java.util.Map
     */
    public static Object parse(String pJsonString) {
        return JSONValue.parse(pJsonString);
    }
    
}
