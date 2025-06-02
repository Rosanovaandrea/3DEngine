package org.tutorial.engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {
    public void prepare(){

        //utilize a red backgroud
        GL11.glClearColor(1,0,0,1);

    }

    public void render(RawModel model){

        //set the vao to take the data
        GL30.glBindVertexArray(model.getVaoID());

        //select the first vbo of vao to take the data and render the figure
        GL20.glEnableVertexAttribArray(0);

        //draw the object
        //GL11.GL_TRIANGLES set the primitives to render
        // - 0: The starting index in the enabled arrays.
        GL11.glDrawElements(GL11.GL_TRIANGLES,model.getVertexCount(),GL11.GL_UNSIGNED_INT,0);

        // Disable the vertex attribute array at index 0.
        // It's good practice to disable attribute arrays when you're done with them.
        GL20.glDisableVertexAttribArray(0);

        //unbing operation
        GL30.glBindVertexArray(0);
    }
}
