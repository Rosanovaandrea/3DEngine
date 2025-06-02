package org.tutorial.shaders;

public class StaticShader extends ShaderProgram {

    private  static final String VERTEX_FILE = "src/main/java/org/tutorial/shaders/vertexShader.glsl";
    private  static  final String FRAGMENT_FILE= "src/main/java/org/tutorial/shaders/fragmentShader.glsl";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    public void bindAttributes() {
        super.bindAttributes(0,"position");
    }
}
