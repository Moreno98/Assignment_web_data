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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Moreno
 */
public class ProcessorDataServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wikiPage = getServletConfig().getInitParameter("pagesPath") + "\\wikipediaPages\\" + "page" + request.getParameter("dataSelection") + ".txt"; 
        String formattedPage = getServletConfig().getInitParameter("pagesPath") + "\\formattedPages\\" + "page" + request.getParameter("dataSelection") + ".txt";   
        
        //printHashMap(map);
        Reader fileWikiReader = new FileReader(wikiPage);  
        Reader fileFormattedReader = new FileReader(formattedPage);
        switch (request.getParameter("action")) {
            case "Calculate word frequency":
                BufferedReader reader = new BufferedReader(fileWikiReader);
                Map<String, Integer> map;
                map = getFrequencies(reader);
                
                String values = calculateJson(map.values().toArray());
                String keys = calculateJson(map.keySet().toArray());                

                request.setAttribute("keys", keys);
                request.setAttribute("values", values);
                request.setAttribute("length", values.length()*50);
                request.setAttribute("type", "horizontal: true");
                request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                break;
            case "Stats on 10 most word frequency":
            case "Stats on 10 least word frequency":
                BufferedReader reader2 = new BufferedReader(fileWikiReader);
                Map<String, Integer> map2;
                map2 = getFrequencies(reader2);
                Map<String, Integer> sortedMap;
                String type;
                if(request.getParameter("action").equals("Stats on 10 most word frequency")){
                    sortedMap = sortByComparator(map2, false);
                    type = "most";
                }else{
                    sortedMap = sortByComparator(map2, true);
                    type = "least";
                }

                Object[] sorted = sortedMap.values().toArray();
                Object[] value;

                Object[] set = sortedMap.keySet().toArray();
                Object[] key;

                if(sorted.length >= 10){
                    value = new Object[10];
                    key = new Object[10];
                    System.arraycopy(sorted, 0, value, 0, 10);
                    System.arraycopy(set, 0, key, 0, 10);
                }else{
                    value = new Object[sorted.length];
                    key = new Object[sorted.length];
                    System.arraycopy(sorted, 0, value, 0, sorted.length);
                    System.arraycopy(set, 0, key, 0, sorted.length);
                }                       
                
                String v = calculateJson(value);
                String k = calculateJson(key);

                request.setAttribute("keys", k);
                request.setAttribute("values", v);
                request.setAttribute("length", 800);
                request.setAttribute("type", "vertical: true");
                request.setAttribute("message", "10 " + type + " frequence word on page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                break;
            case "Create a word cloud":
                BufferedReader reader3 = new BufferedReader(fileWikiReader);
                Map<String, Integer> map3;
                map3 = getFrequencies(reader3);

                String values2 = calculateJson(map3.values().toArray());
                String keys2 = calculateJson(map3.keySet().toArray());

                request.setAttribute("keys", keys2);
                request.setAttribute("values", values2);
                request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayWordCloud.jsp").forward(request, response);
                break;
            case "Bucketing":
                BufferedReader formattedReader = new BufferedReader(fileFormattedReader);
                Map<String, Integer> bucketsMap;
                bucketsMap = bucketing(formattedReader);
                int total = bucketsMap.get("Total");
                bucketsMap.remove("Total");
                
                String values3 = calculateJson(bucketsMap.values().toArray());
                String keys3 = calculateJson(bucketsMap.keySet().toArray());

                request.setAttribute("keys", keys3);
                request.setAttribute("values", values3);                
                request.setAttribute("percentages", getPercentage(total, bucketsMap));
                request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayBuckets.jsp").forward(request, response);            
                break;
            default:
                break;
        }    
    }
    
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
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
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void printMap(Map<String, Integer> map)
    {
        for (Entry<String, Integer> entry : map.entrySet())
        {
            System.out.println(entry.getKey() + " = "+ entry.getValue());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    private List<String> getList(StringTokenizer tokenizer){
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }
    
    private void printHashMap(Map<String, Integer> map){
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
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
    
    private Map<String, Integer> bucketing(BufferedReader reader) throws IOException{
        
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
    
    private List<Double> getPercentage(int total, Map<String,Integer> map){
        List<Double> percentages = new ArrayList<>();
        Object[] values = map.values().toArray();
        for (Object value1 : values) {
            double value = (double) ((int) value1) / total;
            percentages.add(value*100);
        }        
        return percentages;
    }
    
    private String calculateJson(Object[] object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
