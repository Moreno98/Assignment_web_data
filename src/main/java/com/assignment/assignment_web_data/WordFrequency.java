/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.assignment_web_data;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

/**
 *
 * @author moren
 */
public class WordFrequency {
    
    Map<String, Integer> map, sortedMap;
    String valuesJSON, keysJSON, sortedValuesJSON, sortedKeysJSON;
    
    /**
     * This constructor has the goal to create a map with all the word frequency from the file selected (pageTitle)
     * @param pageTitle This is the title of the page to process
     * @throws IOException 
     */
    public WordFrequency(String pageTitle) throws IOException{
        Reader pageReader = new FileReader(pageTitle);
        BufferedReader reader = new BufferedReader(pageReader);
        map = getFrequencies(reader);
        valuesJSON = calculateJson(map.values().toArray());
        keysJSON = calculateJson(map.keySet().toArray());
    }

    /**
     * Setting the map
     * @param map the map
     */
    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    /**
     * Setting the sorted map
     * @param sortedMap 
     */
    public void setSortedMap(Map<String, Integer> sortedMap) {
        this.sortedMap = sortedMap;
    }

    /**
     * Setting the sorted values, a String with the JSON format
     * @param sortedValuesJSON 
     */
    public void setSortedValuesJSON(String sortedValuesJSON) {
        this.sortedValuesJSON = sortedValuesJSON;
    }

    /**
     * Set the sorted keys, with JSON format
     * @param sortedKeysJSON 
     */
    public void setSortedKeysJSON(String sortedKeysJSON) {
        this.sortedKeysJSON = sortedKeysJSON;
    }

    /**
     * Set values with JSON format (not sorted)
     * @param valuesJSON 
     */
    public void setValuesJSON(String valuesJSON) {
        this.valuesJSON = valuesJSON;
    }

    /**
     * Set keys with JSON format (not sorted)
     * @param keysJSON 
     */
    public void setKeysJSON(String keysJSON) {
        this.keysJSON = keysJSON;
    }

    /**
     * Get map with the frequencies
     * @return map with the frequencies
     */
    public Map<String, Integer> getMap() {
        return map;
    }

    /**
     * Get values with JSON format
     * @return The values
     */
    public String getValuesJSON() {
        return valuesJSON;
    }

    /**
     * Get keys with JSON format
     * @return The keys
     */
    public String getKeysJSON() {
        return keysJSON;
    }    

    /**
     * Get map with the frequencies sorted
     * @return The sorted Map
     */
    public Map<String, Integer> getSortedMap() {
        return sortedMap;
    }

    /**
     * Get the sorted values with JSON format
     * @return The sorted values
     */
    public String getSortedValuesJSON() {
        return sortedValuesJSON;
    }

    /**
     * Get the sorted keys with JSON format
     * @return The sorted keys
     */
    public String getSortedKeysJSON() {
        return sortedKeysJSON;
    }
    
    /**
     * This method has the goal to retrieve a map of word frequency based on the reader passed by argument
     * @param reader The reader to process word frequencies
     * @return the map of word frequencies
     * @throws IOException 
     */
    private Map<String, Integer> getFrequencies(BufferedReader reader) throws IOException{
        //Getting first line
        String line = reader.readLine();
        //Splitting the line in tokens with tokenizer
        StringTokenizer tokenizer = new StringTokenizer(line);
        List<String> list = getList(tokenizer);

        Map<String, Integer> map = new HashMap<>();
        //This cycle counts every occurrances of a word
        for (String word : list) {
            Integer n = map.get(word);
            n = (n == null) ? 1 : ++n;
            map.put(word, n);
        }
        reader.close();
        return map;
    }
    
    /**
     * This method returns a list of each tokens inside a tokenizer object
     * @param tokenizer
     * @return The list of tokens
     */
    private List<String> getList(StringTokenizer tokenizer){
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }
    
    /**
     * This method returns a String with JSON format
     * It takes in input an object array and return the string
     * @param object Array of object
     * @return The JSON string
     */
    private String calculateJson(Object[] object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    
    /**
     * This method sorts the frequency map in order to obtain sorted keys and values.
     * The order parameter is used to sort in ascending (true) or descending (false) mode.
     * In the end it stores results into the corresponding variables.
     * @param order True -> ascending, False -> descending
     */
    public void sort(final boolean order){
        Map<String, Integer> unsortMap = this.getMap();
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        Object[] sortedValue = sortedMap.values().toArray();
        Object[] value;

        Object[] sortedSet = sortedMap.keySet().toArray();
        Object[] key;
        //Storing only the first 10 elements of value and key variables.
        if(sortedValue.length >= 10){
            value = new Object[10];
            key = new Object[10];
            System.arraycopy(sortedValue, 0, value, 0, 10);
            System.arraycopy(sortedSet, 0, key, 0, 10);
        }else{
            value = new Object[sortedValue.length];
            key = new Object[sortedValue.length];
            System.arraycopy(sortedValue, 0, value, 0, sortedValue.length);
            System.arraycopy(sortedSet, 0, key, 0, sortedValue.length);
        }        
        
        //Storing the new information
        this.sortedKeysJSON = calculateJson(key);
        this.sortedValuesJSON = calculateJson(value);
        this.sortedMap = sortedMap;
    }
    
    /**
     * This method print the map.
     * Used only for debugging purpose.
     * @param map The map to print
     */
    private void printHashMap(Map<String, Integer> map){
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    
}
