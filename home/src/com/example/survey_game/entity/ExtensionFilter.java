package com.example.survey_game.entity;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilter implements FilenameFilter {
	 
    private String ext;
    
    public ExtensionFilter(String ext){
        this.ext = "."+ext;
    }
     
    @Override
    public boolean accept(File dir, String filename) {
        return filename.startsWith("survey") && filename.endsWith(ext);
    }
     
}