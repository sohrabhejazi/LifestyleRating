package com.example.lifestylerating;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by sohrab on 06.02.2016.
 */
public class FileSaveRead  {
    public File file;
    public String name;

public FileSaveRead(String name){
             this.name=name;

            if (file == null) {

                file = new File(MainActivity.app_Dir, name);

            }
        }

public String read() throws Exception {
        String retString = "";
        BufferedReader reader = null;
        try {
        FileInputStream in = new FileInputStream(file);
        reader = new BufferedReader(new InputStreamReader(in));
        String zeile;
        while ((zeile = reader.readLine()) != null)
        retString += zeile;
        } finally {
        if (reader != null)
        reader.close();
        }
        return retString;
        }

////////////////////////////////////////////////////////////////////////////////////////

public  void saveInFile( String text) {
        try {
            FileOutputStream out = new FileOutputStream(
            file);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(text);
            writer.close();
        } catch (FileNotFoundException fnfe) {
        Log.e("TAG", fnfe.toString());
        } catch (IOException ioe) {
            Log.e("TAG", ioe.toString());
        }
        }
//////////////////////////////////////////////////////////////////////////////////////


}
