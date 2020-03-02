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
     * This method process the request from the user.
     * Basing on the user selection the backend will respond with the correct data.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wikiPage = getServletConfig().getInitParameter("pagesPath") + "\\wikipediaPages\\" + request.getParameter("pageTitle") + ".txt"; 
        String formattedPage = getServletConfig().getInitParameter("pagesPath") + "\\formattedPages\\" + request.getParameter("pageTitle") + ".txt";
        
        //Processing the correct data based on the button clicked on the frontend ("action")
        switch (request.getParameter("action")) {
            case "Calculate word frequency":
                //Creating the word frequency object to process the file:
                WordFrequency frequency = new WordFrequency(wikiPage);
                
                String values = frequency.getValuesJSON();
                String keys = frequency.getKeysJSON();
                //Preparing frontend data:
                request.setAttribute("keys", keys);
                request.setAttribute("values", values);
                request.setAttribute("length", values.length()*50);
                request.setAttribute("type", "horizontal: true");
                request.setAttribute("message", request.getParameter("pageTitle"));
                //Opening the page:
                request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                break;
            case "Stats on 10 most word frequency":
            case "Stats on 10 least word frequency":
                WordFrequency frequency1 = new WordFrequency(wikiPage);
                
                String type;
                //Sorting the word frequency in order to retrieve the 10 most or least word frequency
                //This action is based on the choose of the user (value of the "action" button)
                if(request.getParameter("action").equals("Stats on 10 most word frequency")){
                    frequency1.sort(false);
                    type = "most";
                }else{
                    frequency1.sort(true);
                    type = "least";
                }
                //Getting the sorted values
                String v = frequency1.getSortedValuesJSON();
                String k = frequency1.getSortedKeysJSON();                 
                //Preparing frontend data:
                request.setAttribute("keys", k);
                request.setAttribute("values", v);
                request.setAttribute("length", 800);
                request.setAttribute("type", "vertical: true");
                request.setAttribute("message", "10 " + type + " frequence word on page " + request.getParameter("pageTitle"));
                //Opening the page:
                request.getRequestDispatcher("displayProcessedData.jsp").forward(request, response);
                break;
            case "Create a word cloud":                
                WordFrequency frequency2 = new WordFrequency(wikiPage);
                //Getting data from the WordFrequency object:
                String values2 = frequency2.getValuesJSON();
                String keys2 = frequency2.getKeysJSON();
                
                request.setAttribute("keys", keys2);
                request.setAttribute("values", values2);
                request.setAttribute("message", request.getParameter("pageTitle"));
                request.getRequestDispatcher("displayWordCloud.jsp").forward(request, response);
                break;
            case "Bucketing":
                //Creating the "Bucketing" object:
                Bucketing buckets = new Bucketing(formattedPage);
                //Getting the data:
                String values3 = buckets.getValuesJSON();
                String keys3 = buckets.getKeysJSON();
                //Send them to frontend
                request.setAttribute("keys", keys3);
                request.setAttribute("values", values3);                
                request.setAttribute("percentages", buckets.getPercentage());
                request.setAttribute("message", request.getParameter("pageTitle"));
                request.getRequestDispatcher("displayBuckets.jsp").forward(request, response);            
                break;
            default:
                break;
        }    
    }
    
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
    }
}
