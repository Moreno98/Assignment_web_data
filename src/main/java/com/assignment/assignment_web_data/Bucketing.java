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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author moren
 */
public class Bucketing {
    
    Map<String, Integer> bucketsMap;
    String valuesJSON, keysJSON;
    int total;
    List<Double> percentage;
    
    /**
     * This constructor creates a map based on the tag frequency of the third column of the input WARC file
     * The file has already been preprocessed, the new file contains only 2 column, the word and the original third column.
     * @param pageTitle The title of the file to process
     * @throws IOException 
     */
    public Bucketing(String pageTitle) throws IOException{
        Reader pageReader = new FileReader(pageTitle);
        BufferedReader formattedReader = new BufferedReader(pageReader);
        //The "bucketing" function counts the occurrances of the tokens
        this.bucketsMap = bucketing(formattedReader);

        this.valuesJSON = calculateJson(bucketsMap.values().toArray());
        this.keysJSON = calculateJson(bucketsMap.keySet().toArray());
        //This function set the percentage for the pie chart
        this.setPercentage();
    }

    /**
     * This method returns the map
     * @return Map
     */
    public Map<String, Integer> getBucketsMap() {
        return bucketsMap;
    }

    /**
     * This method returns the values of the map in JSON format
     * @return The values in JSON format
     */
    public String getValuesJSON() {
        return valuesJSON;
    }

    /**
     * This method returns the keys in JSON format
     * @return The keys in JSON
     */
    public String getKeysJSON() {
        return keysJSON;
    }

    /**
     * This method returns the total number of entities stored in the map
     * @return The total number of entities
     */
    public int getTotal() {
        return total;
    }

    /**
     * This function gets the list percentage for the pie chart
     * @return The list of percentage
     */
    public List<Double> getPercentage() {
        return percentage;
    }    

    /**
     * This method sets the Map
     * @param bucketsMap 
     */
    public void setBucketsMap(Map<String, Integer> bucketsMap) {
        this.bucketsMap = bucketsMap;
    }

    /**
     * This method sets the values JSON
     * @param valuesJSON 
     */
    public void setValuesJSON(String valuesJSON) {
        this.valuesJSON = valuesJSON;
    }

    /**
     * This method sets the keys JSON
     * @param keysJSON 
     */
    public void setKeysJSON(String keysJSON) {
        this.keysJSON = keysJSON;
    }

    /**
     * This method sets the total number of entities stored in the map
     * @param total 
     */
    public void setTotal(int total) {
        this.total = total;
    }
    
    /**
     * This method has the goal to calculate the percentage for every entry of the map
     */
    private void setPercentage(){
        List<Double> percentages = new ArrayList<>();
        Object[] values = bucketsMap.values().toArray();
        for (Object value1 : values) {
            double value = (double) ((int) value1) / total;
            percentages.add(value*100);
        }
        this.percentage = percentages;
    }
    
    /**
     * This method returns a map of word frequency stored on the buffer passed by parameter.
     * This method contains also a TagList map, it is used to store the description for each symbol.
     * If there is no description for a specific symbol, the symbol itself is stored into the frequency map.
     * @param reader The buffer to process
     * @return The word frequency map
     * @throws IOException 
     */
    private Map<String,Integer> bucketing(BufferedReader reader) throws IOException{
        
        Map<String, Integer> map = new HashMap<>();
        String line = reader.readLine();
        
        //The tag list is used to store the description for each symbol
        Map<String,String> TagList;
        TagList = populateTagList();
        
        int total = 0;
        while(line != null){
            total++;
            StringTokenizer tokenizer = new StringTokenizer(line);
            //Retrieve the description for the symbol using the TagList map
            List<String> list = getList(tokenizer);
            String tag = TagList.get(list.get(1));
            tag = (tag == null) ? list.get(1) : tag;
            Integer n = map.get(tag);
            //Count the occurency of the symbol
            n = (n == null) ? 1 : ++n;
            map.put(tag, n);
            line = reader.readLine();
            
        }
        this.setTotal(total);
        reader.close();
        return map;
    }
    
    /**
     * This method populates a map with keys and values. 
     * The keys are the post-tagging symbol and the values are the description of each tag.
     * @return The tagList map
     */
    private Map<String,String> populateTagList(){
        Map<String,String> list = new HashMap<>();
        //Description from https://spacy.io/api/annotation#pos-tagging
        list.put("$","Currency ($)");list.put("ADD","Email (ADD)");list.put("AFX","Affix (AFX)");
        list.put("CC","Conjunction (CC)");list.put("CD","Cardinal number (CD)");list.put("DT","Determiner (DT)");
        list.put("EX","Existential there (EX)");list.put("FW","Foreign word (FW)");list.put("GW","Additional word in multi-word expression (GW)");
        list.put("HYPH","Punctuation mark (HYPH)");list.put("IN","Conjunction (IN)");list.put("JJ","Adjective (JJ)");
        list.put("JJR","Comparative Adjective (JJR)");list.put("JJS","Superlative Adjective (JJS)");list.put("LS","List item marker (LS)");
        list.put("MD","Modal auxiliary verb (MD)");list.put("NFP","superfluous punctuation (NFP)");list.put("NIL","Missing tag (NIL)");
        list.put("NN","Noun (NN)");list.put("NNP","Proper singular noun (NNP)");list.put("NNPS","Proper plural noun (NNPS)");
        list.put("NNS","Plural noun (NNS)");list.put("PDT","Predeterminer (PDT)");list.put("POS","Possessive ending (POS)");
        list.put("PRP","Personal pronoun (PRP)");list.put("PRP$","Possessive pronoun (PRP$)");list.put("RB","Adverb (RB)");
        list.put("RBR","Comparative adverb (RBR)");list.put("RBS","Superlative adverb (RBS)");list.put("RP","Particle adverb (RP)");
        list.put("SYM","Symbol (SYM)");list.put("TO","Infinitival \"to\" (TO)");list.put("UH","Interjection (UH)");
        list.put("VB","Verb, base form (VB)");list.put("VBD","Verb, past tense (VBD)");list.put("VBG","Verb, gerund or present participle (VBG)");
        list.put("VBN","Verb, past participle (VBN)");list.put("VBP","Verb, non-3rd person singular present (VBP)");list.put("VBZ","Verb, 3rd person singular present (VBZ)");
        list.put("WDT","Wh-determiner (WDT)");list.put("WP","Wh-pronoun, personal (WP)");list.put("WP$","Wh-pronoun, possessive (WP$)");
        list.put("WRB","Wh-adverb (WRB)");list.put("X","Unknown (X)");
        return list;        
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
     * @param object
     * @return The JSON string
     */
    private String calculateJson(Object[] object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    
}
