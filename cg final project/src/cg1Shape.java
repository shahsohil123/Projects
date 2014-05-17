/**
 * cg1Shape.java
 *
 * Class that includes routines for tessellating a number of basic shapes
 *
 * Students are to supply their implementations for the
 * functions in this file using the function "addTriangle()" to do the 
 * tessellation.
 *
 */

import java.awt.*;
import java.nio.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

//import Jama.Matrix;

import java.io.*;

public class cg1Shape extends simpleShape {
	/**
	 * constructor
	 */
	public cg1Shape() {
	}

	/**
	 * makeDefaultShape - creates a "unit" shape of your choice using your
	 * tesselation routines.
	 * @param zco 
	 * @param yco 
	 * @param xco 
	 * 
	 * 
	 */
	public void makeDefaultShape(float xco, float yco, float zco, float scale) {
		
		makeCube(xco,yco,zco,scale);
		makeCube(xco+0.4f,yco + 0.4f,zco,scale);
	}
	
	public void makeCube(float xco,float yco, float zco,float scale){

		
		int subdivisions = 3;
		float interval = (float) ((scale*2) / subdivisions);
		float y1 = (float) yco+scale;
		float y2 = (float)(y1 - interval);
		float x1 = (float) xco-scale;
		float x2 = (float) (x1 + interval);
		float z1 = (float) zco+scale;
		float z2 = zco+0;
		for (int i = 0; i < subdivisions; i++) {

			for (int j = 0; j < subdivisions; j++) {

				addTriangle(x2, y1, z1,Math.abs(x2-scale),Math.abs(y1-scale), x1, y2, z1,Math.abs(x1-scale),Math.abs(y2-scale), x1, y1, z1,Math.abs(x1-scale),Math.abs(y1-scale));
				addTriangle(x2, y2, z1,Math.abs(x2-scale),Math.abs(y2-scale), x1, y2, z1,Math.abs(x1-scale),Math.abs(y2-scale), x2, y1, z1,Math.abs(x2-scale),y1-scale);
				x1 = x2;
				x2 = x2 + interval;

			}
			y1 = y2;
			y2 = y2 - interval;
			x1 = (float) -scale + xco;
			x2 = x1 + interval;
		}
		y1 = (float) scale + yco;
		y2 = (y1 - interval);
		x1 = (float) -scale+xco;
		x2 = (x1 + interval);
		z2 = (float) -scale+ zco;
		for (int i = 0; i < subdivisions; i++) {

			for (int j = 0; j < subdivisions; j++) {

				addTriangle(x1, y1, z2,Math.abs(x1-scale),Math.abs(y1-scale), x1, y2, z2,Math.abs(x1-scale),Math.abs(y2-scale), x2, y1, z2,Math.abs(x2-scale),Math.abs(y1-scale));
				addTriangle(x2, y1, z2,Math.abs(x2-scale),Math.abs(y1-scale), x1, y2, z2,Math.abs(x1-scale),Math.abs(y2-scale), x2, y2, z2,Math.abs(x2-scale),Math.abs(y2-scale));
				x1 = x2;
				x2 = x2 + interval;

			}
			y1 = y2;
			y2 = y2 - interval;
			x1 = (float) -scale + xco;
			x2 = x1 + interval;
		}
		y1 = (float) scale + yco;
		y2 = y1 - interval;
		x1 = (float) -scale + xco;
		x2 = (float) scale + xco;
		z1 = (float) -scale + zco;
		z2 = z1 + interval;
		for (int i = 0; i < subdivisions; i++) {

			for (int j = 0; j < subdivisions; j++) {

				addTriangle(x2, y1, z1,Math.abs(y1-scale),Math.abs(z1-scale), x2, y2, z1,Math.abs(y2-scale),Math.abs(z1-scale) ,x2, y1, z2,Math.abs(y1-scale),Math.abs(z2-scale));
				addTriangle(x2, y1, z2,Math.abs(y1-scale),Math.abs(z1-scale), x2, y2, z1,Math.abs(y2-scale),Math.abs(z1-scale), x2, y2, z2,Math.abs(y2-scale),Math.abs(z2-scale));
				z1 = z2;
				z2 = z2 + interval;
			}
			y1 = y2;
			y2 = y2 - interval;
			z1 = (float) -scale+ zco;
			z2 = z1 + interval;
		}
		y1 = (float) scale + yco;
		y2 = y1 - interval;
		x1 = (float) -scale+ xco;
		x2 = (float) scale+xco;
		z1 = (float) -scale+zco;
		z2 = z1 + interval;
		for (int i = 0; i < subdivisions; i++) {

			for (int j = 0; j < subdivisions; j++) {

				addTriangle(x1, y1, z2,Math.abs(y1-scale),Math.abs(z1-scale), x1, y2, z1,Math.abs(y2-scale),Math.abs(z1-scale), x1, y1, z1,Math.abs(y1-scale),Math.abs(z1-scale));
				addTriangle(x1, y2, z2,Math.abs(y2-scale),Math.abs(z2-scale), x1, y2, z1,Math.abs(y2-scale),Math.abs(z1-scale), x1, y1, z2,Math.abs(y1-scale),Math.abs(z2-scale));
				z1 = z2;
				z2 = z2 + interval;
			}
			y1 = y2;
			y2 = y2 - interval;
			z1 = (float) -scale + zco;
			z2 = z1 + interval;
		}
		y1 = (float) scale+yco;
		y2 = (float) -scale+yco;
		x1 = (float) -scale+xco;
		x2 = x1 + interval;
		z1 = (float) -scale+zco;
		z2 = z1 + interval;

		for (int i = 0; i < subdivisions; i++) {

			for (int j = 0; j < subdivisions; j++) {
				addTriangle(x1, y1, z2,Math.abs(x1-scale),Math.abs(z2-scale), x1, y1, z1,Math.abs(x1-scale),Math.abs(z1-scale), x2, y1, z1,Math.abs(x2-scale),Math.abs(z1-scale));
				addTriangle(x2, y1, z1,Math.abs(x2-scale),Math.abs(z1-scale), x2, y1, z2,Math.abs(x2-scale),Math.abs(z2-scale), x1, y1, z2,Math.abs(x1-scale),Math.abs(z2-scale));
				z1 = z2;
				z2 = z2 + interval;
			}
			x1 = x2;
			x2 = x2 + interval;
			z1 = -scale + zco;
			z2 = z1 + interval;
		}
		y1 = (float) scale + yco;
		y2 = (float) -scale + yco;
		x1 = (float) -scale+ xco;
		x2 = x1 + interval;
		z1 = (float) -scale + zco;
		z2 = z1 + interval;
		for (int i = 0; i < subdivisions; i++) {

			for (int j = 0; j < subdivisions; j++) {
				addTriangle(x2, y2, z1,Math.abs(x2-scale),Math.abs(z1-scale), x1, y2, z1,Math.abs(x1-scale),Math.abs(z1-scale), x1, y2, z2,Math.abs(x1-scale),Math.abs(z1-scale));
				addTriangle(x1, y2, z2,Math.abs(x1-scale),Math.abs(z2-scale), x2, y2, z2,Math.abs(x2-scale),Math.abs(z2-scale), x2, y2, z1,Math.abs(x2-scale),Math.abs(z2-scale));
				z1 = z2;
				z2 = z2 + interval;
			}
			x1 = x2;
			x2 = x2 + interval;
			z1 = -scale+ zco;
			z2 = z1 + interval;
		}
	System.out.println("lol2 ");
	}
}
