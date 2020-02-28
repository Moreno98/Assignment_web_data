/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.assignment_web_data;

import com.google.gson.Gson;
import java.io.BufferedReader;
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
    
    public WordFrequency(Reader PageReader) throws IOException{
        BufferedReader reader = new BufferedReader(PageReader);
        map = getFrequencies(reader);
        valuesJSON = calculateJson(map.values().toArray());
        keysJSON = calculateJson(map.keySet().toArray());
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public void setSortedMap(Map<String, Integer> sortedMap) {
        this.sortedMap = sortedMap;
    }

    public void setSortedValuesJSON(String sortedValuesJSON) {
        this.sortedValuesJSON = sortedValuesJSON;
    }

    public void setSortedKeysJSON(String sortedKeysJSON) {
        this.sortedKeysJSON = sortedKeysJSON;
    }

    public void setValuesJSON(String valuesJSON) {
        this.valuesJSON = valuesJSON;
    }

    public void setKeysJSON(String keysJSON) {
        this.keysJSON = keysJSON;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public String getValuesJSON() {
        return valuesJSON;
    }

    public String getKeysJSON() {
        return keysJSON;
    }    

    public Map<String, Integer> getSortedMap() {
        return sortedMap;
    }

    public String getSortedValuesJSON() {
        return sortedValuesJSON;
    }

    public String getSortedKeysJSON() {
        return sortedKeysJSON;
    }
    
    private Map<String, Integer> getFrequencies(BufferedReader reader) throws IOException{
        String line = reader.readLine();
                
        StringTokenizer tokenizer = new StringTokenizer(line);
        List<String> list = getList(tokenizer);

        Map<String, Integer> map = new HashMap<>();

        for (String word : list) {
            Integer n = map.get(word);
            n = (n == null) ? 1 : ++n;
            map.put(word, n);
        }
        reader.close();
        return map;
    }
    
    private List<String> getList(StringTokenizer tokenizer){
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }
    
    private String calculateJson(Object[] object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    
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
        
        this.sortedKeysJSON = calculateJson(key);
        this.sortedValuesJSON = calculateJson(value);
        this.sortedMap = sortedMap;
    }
    
    private void printHashMap(Map<String, Integer> map){
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    
}
