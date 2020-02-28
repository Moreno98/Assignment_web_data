/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.assignment_web_data;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
     * This method pro
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wikiPage = getServletConfig().getInitParameter("pagesPath") + "\\wikipediaPages\\" + "page" + request.getParameter("dataSelection") + ".txt"; 
        String formattedPage = getServletConfig().getInitParameter("pagesPath") + "\\formattedPages\\" + "page" + request.getParameter("dataSelection") + ".txt";   

        Reader fileWikiReader = new FileReader(wikiPage);  
        Reader fileFormattedReader = new FileReader(formattedPage);
        switch (request.getParameter("action")) {
            case "Calculate word frequency":                
                WordFrequency frequency = new WordFrequency(fileWikiReader);
                
                String values = frequency.getValuesJSON();
                String keys = frequency.getKeysJSON();

                request.setAttribute("keys", keys);
                request.setAttribute("values", values);
                request.setAttribute("length", values.length()*50);
                request.setAttribute("type", "horizontal: true");
                request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                break;
            case "Stats on 10 most word frequency":
            case "Stats on 10 least word frequency":
                WordFrequency frequency1 = new WordFrequency(fileWikiReader);
                
                String type;
                if(request.getParameter("action").equals("Stats on 10 most word frequency")){
                    frequency1.sort(false);
                    type = "most";
                }else{
                    frequency1.sort(true);
                    type = "least";
                }
                
                String v = frequency1.getSortedValuesJSON();
                String k = frequency1.getSortedKeysJSON();                 
                
                request.setAttribute("keys", k);
                request.setAttribute("values", v);
                request.setAttribute("length", 800);
                request.setAttribute("type", "vertical: true");
                request.setAttribute("message", "10 " + type + " frequence word on page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                break;
            case "Create a word cloud":                
                WordFrequency frequency2 = new WordFrequency(fileWikiReader);

                String values2 = frequency2.getValuesJSON();
                String keys2 = frequency2.getKeysJSON();

                request.setAttribute("keys", keys2);
                request.setAttribute("values", values2);
                request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayWordCloud.jsp").forward(request, response);
                break;
            case "Bucketing":
                Bucketing buckets = new Bucketing(fileFormattedReader);
                
                String values3 = buckets.getValuesJSON();
                String keys3 = buckets.getKeysJSON();

                request.setAttribute("keys", keys3);
                request.setAttribute("values", values3);                
                request.setAttribute("percentages", buckets.getPercentage());
                request.setAttribute("message", "Page" + request.getParameter("dataSelection"));
                request.getRequestDispatcher("displayBuckets.jsp").forward(request, response);            
                break;
            default:
                break;
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
}
