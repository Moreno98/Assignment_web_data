/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.assignment_web_data.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

/**
 * This class pars the warc file and extract the useful data.
 * @author moren
 */
public class PageParser {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String workingDirectory = System.getProperty("user.dir");
        workingDirectory += "\\src\\main\\java\\com\\assignment\\assignment_web_data\\data\\";
        
        String filePath = workingDirectory + "task_1.warc";
        Reader fileReader = new FileReader(filePath);
        
        boolean thirdColumn = true;
        
        if(thirdColumn){
            workingDirectory += "\\formattedPages\\";
        }else{
            workingDirectory += "\\wikipediaPages\\";
        }
        
        createPages(fileReader, workingDirectory, thirdColumn);
    }
    
    /**
     * This method returns the first line starting with a number (means the beginning of new words)
     * @param line The current line
     * @param reader The reader
     * @return The new line
     * @throws IOException 
     */
    private static String findNewData(String line, BufferedReader reader) throws IOException{
        while(line.length() == 0 || line.charAt(0) != '1'){
            line = reader.readLine();
        }
        return line;
    }
    
    /**
     * This method creates pages for each wikipedia pages founded in the dataset.
     * @param fileReader The file reader of the dataset
     * @param workingDirectory The working directory
     * @param thirdColumn If true it takes also the third column (NNP, VBZ, DT, NN ...) else it takes only the word
     * @throws IOException 
     */
    private static void createPages(Reader fileReader, String workingDirectory, final Boolean thirdColumn) throws IOException{
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        int count = 0;
        String fileName = workingDirectory + "page" + count + ".txt";
        File file = new File(fileName);
        count++;
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        line = findNewData(line, reader); 
        
        while(line != null){
            if(line.equals("WARC/1.0")){
                writer.close();
                fileName = workingDirectory + "page" + count + ".txt";
                file = new File(fileName);
                file.createNewFile();
                count++;
                writer = new BufferedWriter(new FileWriter(fileName));
                line = findNewData(line, reader);
            }
            if(!line.isEmpty()){
                String[] element = line.split("\t");
                if(!element[1].matches(".*[^A-Za-z].*")){
                    if(thirdColumn){
                        writer.write(element[1] + " " + element[2] + "\n");
                    }else{
                        writer.write(element[1] + " ");
                    }                    
                }                
            }   
            line = reader.readLine();
        }
        writer.close();
    }
    
}
