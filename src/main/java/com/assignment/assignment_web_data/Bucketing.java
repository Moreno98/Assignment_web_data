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
    
    public Bucketing(Reader pageReader) throws IOException{
        BufferedReader formattedReader = new BufferedReader(pageReader);
        this.bucketsMap = bucketing(formattedReader);
        
        this.total = this.bucketsMap.get("Total");
        this.bucketsMap.remove("Total");
        this.valuesJSON = calculateJson(bucketsMap.values().toArray());
        this.keysJSON = calculateJson(bucketsMap.keySet().toArray());
        this.setPercentage();
    }

    public Map<String, Integer> getBucketsMap() {
        return bucketsMap;
    }

    public String getValuesJSON() {
        return valuesJSON;
    }

    public String getKeysJSON() {
        return keysJSON;
    }

    public int getTotal() {
        return total;
    }

    public List<Double> getPercentage() {
        return percentage;
    }    

    public void setBucketsMap(Map<String, Integer> bucketsMap) {
        this.bucketsMap = bucketsMap;
    }

    public void setValuesJSON(String valuesJSON) {
        this.valuesJSON = valuesJSON;
    }

    public void setKeysJSON(String keysJSON) {
        this.keysJSON = keysJSON;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    private void setPercentage(){
        List<Double> percentages = new ArrayList<>();
        Object[] values = bucketsMap.values().toArray();
        for (Object value1 : values) {
            double value = (double) ((int) value1) / total;
            percentages.add(value*100);
        }        
        this.percentage = percentages;
    }
    
    private Map<String,Integer> bucketing(BufferedReader reader) throws IOException{
        
        Map<String, Integer> map = new HashMap<>();
        String line = reader.readLine();
        
        Map<String,String> TagList;
        TagList = populateTagList();
        
        int total = 0;
        while(line != null){
            total++;
            StringTokenizer tokenizer = new StringTokenizer(line);
            List<String> list = getList(tokenizer);
            String tag = TagList.get(list.get(1));
            tag = (tag == null) ? list.get(1) : tag;
            Integer n = map.get(tag);
            n = (n == null) ? 1 : ++n;
            map.put(tag, n);
            line = reader.readLine();
            
        }
        
        map.put("Total", total);
        reader.close();
        return map;
    }
    
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
    
}
