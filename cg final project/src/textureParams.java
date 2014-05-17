/**
 *
 * textureParams.java
 *
 * Simple class for setting up the textures for the textures
 * Assignment.
 *
 * Students are to complete this class.
 *
 */

import java.io.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*;

import com.jogamp.opengl.util.texture.*;

public class textureParams
{
    
	/**
	 * constructor
	 */
	public textureParams()
	{
        
	}
    
    /**
     * This functions loads texture data to the GPU.
     *
     * You will need to write this function, and maintain all of the values needed
     * to be sent to the various shaders.
     *
     * @param filename - The name of the texture file.
     *
     */
    public void loadTexture (String filename)
    {
    	InputStream s = getClass().getResourceAsStream(filename);
    	try {
			TextureData tex = TextureIO.newTextureData(GLProfile.getDefault(), s, false, ".jpg");
			TextureIO.newTexture(tex);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    }

    
    /**
     * This functions sets up the parameters
     * for texture use.
     *
     * You will need to write this function, and maintain all of the values needed
     * to be sent to the various shaders.
     *
     * @param program - The ID of an OpenGL (GLSL) program to which parameter values
     *    are to be sent
     *
     * @param gl2 - GL2 object on which all OpenGL calls are to be made
     *
     */
    public void setUpTextures (int program, GL2 gl2)
    {
    	gl2.glEnable(GL.GL_TEXTURE_2D);
    	gl2.glActiveTexture(0);
    	
    }
}
