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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String warcFile = getServletConfig().getInitParameter("wikipeadiaPagesPath") + "page" + request.getParameter("dataSelection") + ".txt";
        try(Reader fileReader = new FileReader(warcFile)) {
            try{
                BufferedReader reader = new BufferedReader(fileReader);
                String line = reader.readLine();
                
                StringTokenizer tokenizer = new StringTokenizer(line);
                List<String> list = getList(tokenizer);
                
                Map<String, Integer> map = new HashMap<>();
                
                for (String word : list) {
                    Integer n = map.get(word);
                    n = (n == null) ? 1 : ++n;
                    map.put(word, n);
                }
                
                //printHashMap(map);
                switch (request.getParameter("action")) {
                    case "Calculate word frequency":
                        Gson gson = new Gson();
                        String values = gson.toJson(map.values().toArray());
                        String keys = gson.toJson(map.keySet().toArray());
                        
                        request.setAttribute("keys", keys);
                        request.setAttribute("values", values);
                        request.setAttribute("length", values.length()*50);
                        request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                        request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                        break;
                    case "Stats on 10 most word frequency":
                    case "Stats on 10 least word frequency":
                        Map<String, Integer> sortedMap;
                        String type;
                        if(request.getParameter("action").equals("Stats on 10 most word frequency")){
                            sortedMap = sortByComparator(map, false);
                            type = "most";
                        }else{
                            sortedMap = sortByComparator(map, true);
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
                        
                        Gson gson2 = new Gson();
                        String v = gson2.toJson(value);
                        String k = gson2.toJson(key);
                        
                        request.setAttribute("keys", k);
                        request.setAttribute("values", v);
                        request.setAttribute("length", 800);
                        request.setAttribute("message", "10 " + type + " frequence word on page" + request.getParameter("dataSelection"));
                        request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                        break;
                    default:
                        break;
                }
                
                
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
            }
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
}
