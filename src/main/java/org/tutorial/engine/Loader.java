package org.tutorial.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Loader {
    private final List<Integer> vaos;
    private final  List<Integer> vbos;

    public Loader(){
    vaos = new LinkedList<Integer>();
    vbos = new LinkedList<Integer>();
    }

    public RawModel loadToVao(float[] positions, int[] indices){
        int vaoID = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributesList(0,positions);
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }
    public int createVao(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);

        // with this line of code(bind to vaoID) all the operations will be made on the vao crated
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public void cleanUP(){
        for(int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo :vbos){
            GL15.glDeleteBuffers(vbo);
        }
    }

    private void storeDataInAttributesList(int attributesNumber, float[] data){
        int vboID = glGenBuffers();
        vbos.add(vboID);

        // with this line of code(bind to vaoID) all the operations will be made on the vbo crated
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);

        FloatBuffer buffer = storeDataInFloatBuffer(data);

        // GL_STATIC_DRAW indicates that the data will be set once and used many times.
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer, GL15.GL_STATIC_DRAW);

        /*"Configures the vertex attribute with index 0 to read its data from the VBO currently bound to the GL_ARRAY_BUFFER point.
         Each instance of this attribute consists of 3 values, each of which is a floating point number (float).
          These values do not need to be normalized.
          The data for this attribute is tightly packed into the buffer (stride calculated automatically),
         and the data for the first vertex begins at an offset of 0 bytes from the start of the VBO."*/
        glVertexAttribPointer(attributesNumber,3, GL11.GL_FLOAT,false,0,0);


        /*this operation is called unbind and it is needed to prevent the modify of the object buffers,
         it works with the set of id to 0*/
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    private void unbindVAO(){
        /*this operation is called unbind and it is needed to prevent the modify of the object buffers,
         it works with the set id to 0*/
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){

        int vboID = glGenBuffers();

        vbos.add(vboID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vboID);

        IntBuffer buffer = storeDataInIntBuffer(indices);

        /* Why There Isn't a glVertexAttribPointer for the Index Buffer

This is a fundamental difference in how OpenGL handles vertex data versus index data.

glVertexAttribPointer: This command is used to configure vertex attributes (like position,

color, texture coordinates, normals, etc.).

It tells OpenGL how to interpret the data in the VBO bound to GL_ARRAY_BUFFER for a specific attribute (identified by the index attributesNumber).

It indicates how many components the attribute has (e.g., 3 for XYZ position), their type (GL_FLOAT), whether to normalize,

the stride (space between one vertex and the next for this attribute),

and the offset (where to start reading within the buffer). This is a process for defining the data structure of each vertex.

Index Buffer (GL_ELEMENT_ARRAY_BUFFER): The index buffer does not contain data describing the properties of a single vertex (like its position or color).

Instead, it contains a list of integers (the indices) that specify in which order the vertices (which have been defined using the various attributes and the VBOs bound to GL_ARRAY_BUFFER) should be used to form primitives (triangles, lines, etc.)

with drawing commands like glDrawElements.

Key Difference: Indices are not a vertex "attribute" in the sense of glVertexAttribPointer.

They are separate data that controls the topology of the geometry. OpenGL knows that the VBO bound to GL_ELEMENT_ARRAY_BUFFER contains indices and uses them directly with indexed drawing calls (glDrawElements)

 to determine which vertices, defined by the configured attributes, should be fetched to build the geometry.

Consequence: It doesn't make sense to use glVertexAttribPointer for the index buffer because you are not defining how to read an attribute per vertex;

you are simply providing a sequence of indices to select which vertices to draw. OpenGL implicitly handles the use of the index buffer when you execute an indexed drawing command and there is a VBO bound to GL_ELEMENT_ARRAY_BUFFER

(or, more commonly and as in your case, when the bound VAO contains an association to an index buffer VBO).*/

        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer, GL_STATIC_DRAW);

    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer  storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
