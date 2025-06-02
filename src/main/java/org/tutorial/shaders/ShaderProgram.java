package org.tutorial.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    public ShaderProgram(String vertexFile,String fragmentFile){
        vertexShaderID = loadShader(vertexFile,GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile,GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID,vertexShaderID);
        glAttachShader(programID,fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
    }

    public void start(){
        glUseProgram(programID);
    }

    public void stop(){
        glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        glDetachShader(programID,vertexShaderID);
        glDetachShader(programID,fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    public abstract void bindAttributes();

    public void bindAttributes(int attribute,String variableName){
        glBindAttribLocation(programID,attribute, variableName);
    }



    private static int loadShader(String file,int type){
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID,shaderSource);

        glCompileShader(shaderID);
        if(glGetShaderi(shaderID,GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("could not compile shader");
            System.exit(-1);
        }
            return shaderID;

    }
}
