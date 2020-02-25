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
 *
 * @author moren
 */
public class PageParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String workingDirectory = System.getProperty("user.dir");
        workingDirectory += "\\src\\main\\java\\com\\assignment\\assignment_web_data\\data\\";
        
        String filePath = workingDirectory + "task_1.warc";
        Reader fileReader = new FileReader(filePath);
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
                    writer.write(element[1] + " ");
                }                
            }   
            line = reader.readLine();
        }
        writer.close();
    }
    
    private static String findNewData(String line, BufferedReader reader) throws IOException{
        while(line.length() == 0 || line.charAt(0) != '1'){
            line = reader.readLine();
        }
        return line;
    }
    
}
