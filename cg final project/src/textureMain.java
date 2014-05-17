//
//  textureMain.java
//  
//
//  Main class for texture assignment.
//
//  Students should not be modifying this file.
//

import java.awt.*;
import java.nio.*;
import java.io.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*; 
import com.jogamp.opengl.util.texture.*;


public class textureMain implements GLEventListener, KeyListener
{
	public float angles[];    
	/**
	 * buffer info 
	 */
    private int vbuffer[];
    private int ebuffer[];
    private int numVerts[];
    
    /**
     * program and variable IDs
     */
    public int shaderProgID;
    
    // texture values
    public textureParams tex;
	
    /**
     * shape info
     */
    cg1Shape myShape;
    
    /**
     * my canvas
     */
    GLCanvas myCanvas;
	
	/**
	 * constructor
	 */
	public textureMain(GLCanvas G)
	{
		angles = new float[3];
        vbuffer = new int [1];
        ebuffer = new int[1];
        numVerts = new int [1];
        
        myCanvas = G;
        tex = new textureParams();
        
        G.addGLEventListener (this);
        G.addKeyListener (this);
	}
    
    private void errorCheck (GL2 gl2)
    {
        int code = gl2.glGetError();
        if (code == GL.GL_NO_ERROR) 
            System.err.println ("All is well");
        else
            System.err.println ("Problem - error code : " + code);
            
    }
    
    
	/**
	 * Called by the drawable to initiate OpenGL rendering by the client. 
	 */
	public void display(GLAutoDrawable drawable)
	{
        // get GL
		GL2 gl2 = (drawable.getGL()).getGL2();
        
        // clear your frame buffers
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
        
        // bind your vertex buffer
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);
            
        // bind your element array buffer
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
            
        // set up your attribute variables
        gl2.glUseProgram (shaderProgID);
        long dataSize = myShape.getNVerts() * 4l * 4l;
        int  vPosition = gl2.glGetAttribLocation (shaderProgID, "vPosition");
        gl2.glEnableVertexAttribArray ( vPosition );
        gl2.glVertexAttribPointer (vPosition, 4, GL.GL_FLOAT, false,
                                       0, 0l);
        int  vTex = gl2.glGetAttribLocation (shaderProgID, "vTexCoord");
        gl2.glEnableVertexAttribArray ( vTex );
        gl2.glVertexAttribPointer (vTex, 2, GL.GL_FLOAT, false,
                                   0, dataSize);
        
       
        
        angles[0] = 35.0f;
        angles[1] = -15.0f;
        angles[2] = 10.0f;
        // pass in your rotations as a uniform variable
        int theta = gl2.glGetUniformLocation (shaderProgID, "theta");
		gl2.glUniform3fv (theta, 1, angles, 0);
            
        // setup uniform variables for texture
        tex.setUpTextures (shaderProgID, gl2);
        
        
        // draw your shapes
        gl2.glDrawElements ( GL.GL_TRIANGLES, numVerts[0],  GL.GL_UNSIGNED_SHORT, 0l);
                        
    }
        
	
	/**
	 * Notifies the listener to perform the release of all OpenGL 
	 * resources per GLContext, such as memory buffers and GLSL 
	 * programs.
	 */
	public void dispose(GLAutoDrawable drawable)
	{
            
	}
        
	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized. 
	 */
	public void init(GLAutoDrawable drawable)
	{
		// get the gl object
		GL2 gl2 = drawable.getGL().getGL2();
			
		// Load shaders
        shaderProgram myShaders = new shaderProgram();
		shaderProgID = myShaders.readAndCompile (gl2, "vshader.glsl", "fshader.glsl");
		if (shaderProgID == 0) {
			System.err.println ("Error setting up shaders");
			System.exit (1);
		}
			
		// Other GL initialization
		gl2.glEnable (GL.GL_DEPTH_TEST);
		gl2.glEnable (GL.GL_CULL_FACE);
        gl2.glCullFace ( GL.GL_BACK );
        gl2.glFrontFace(GL.GL_CCW);
		gl2.glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
        gl2.glDepthFunc (GL.GL_LEQUAL);
        gl2.glClearDepth (1.0f);
			
		// initially create a new Shape
		createShapes(gl2);
        
        // Load textures
        tex.loadTexture ("yellow-texture-1.jpg");
	}
        
	/**
	 * Called by the drawable during the first repaint after the component
	 * has been resized. 
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                     int height)
	{
            
	}
	

	
	/**
	 * creates a new shape
	 */
	public void createShapes(GL2 gl2)
	{
        // clear the old shape
        myShape = new cg1Shape();
        
        // clear the old shape
        myShape.clear();
            
//        gl2.glLoadIdentity();
//        gl2.glPushMatrix();
//        gl2.glRotatef(arg0, arg1, arg2, arg3);
        
        // make a shape 
        myShape.makeDefaultShape(0.0f,0.0f,0.0f,0.0f);
                
        // get your verticies and elements
        Buffer points = myShape.getVerticies();
        Buffer elements = myShape.getElements();
        Buffer texCoords = myShape.getUV();
            
        // set up the vertex buffer
        int bf[] = new int[1];
        gl2.glGenBuffers (1, bf, 0);
        vbuffer[0] = bf[0];
        long vertBsize = myShape.getNVerts() * 4l * 4l;
        long tdataSize = myShape.getNVerts() * 2l * 4l;
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);
        gl2.glBufferData ( GL.GL_ARRAY_BUFFER, vertBsize + tdataSize , null, GL.GL_STATIC_DRAW);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, 0, vertBsize, points);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, vertBsize, tdataSize, texCoords);
            
        gl2.glGenBuffers (1, bf, 0);
        ebuffer[0] = bf[0];
        long eBuffSize = myShape.getNVerts() * 2l;
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
        gl2.glBufferData ( GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize,elements, 
                              GL.GL_STATIC_DRAW);
            
        numVerts[0] = myShape.getNVerts();
    }
    
    /**
     * Because I am a Key Listener...we'll only respond to key presses
     */
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    /** 
     * Invoked when a key has been pressed.
     */
    public void keyPressed(KeyEvent e)
    {
        // Get the key that was pressed
        char key = e.getKeyChar();
        
        // Respond appropriately
        switch( key ) {
                case 'q': case 'Q':
                System.exit( 0 );
                break;
        }
        
        // do a redraw
        myCanvas.display();
    }
    
        
    /**
     * main program
     */
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
		
		// create your tessMain
		textureMain myMain = new textureMain(canvas);
        
        
        Frame frame = new Frame("CG1 - Texture Assignment");
        frame.setSize(512, 512);
        frame.add(canvas);
        frame.setVisible(true);
        
        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
