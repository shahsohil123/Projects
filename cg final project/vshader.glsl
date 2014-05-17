attribute vec4 vPosition;
attribute vec2 vTexCoord;
uniform vec3 theta;
varying vec2 texCoord;
uniform vec3 translation;
uniform vec3 scaling;
uniform vec3 rotation;
uniform vec3 eyepoint;
uniform vec3 lookAt;
uniform vec3 up;
uniform float left;
uniform float right;
uniform float top;
uniform float bottom;
uniform float near;
uniform float far;
uniform int switch1;
void main()
{
    // Compute the sines and cosines of each rotation
    // about each axis
    vec3 angles = radians (theta);
    vec3 c = cos (angles);
    vec3 s = sin (angles);
    
    // rotation matricies
mat4 trans = mat4(1.0,0.0,0.0,0.0,
				  	  0.0,1.0,0.0,0.0,
				  	  0.0,0.0,1.0,0.0,
				      translation.x,translation.y,translation.z,1.0);
				  
	
	vec3 angle = radians(rotation);
				
	mat4 rotz = mat4(cos(angle.z),sin(angle.z),0.0,0.0,
					-sin(angle.z),cos(angle.z),0.0,0.0,
					0.0,0.0,1.0,0.0,
					0.0,0.0,0.0,1.0);
					
	 mat4 roty = mat4(cos(angle.y),0.0,-sin(angle.y),0.0,
	 				  0.0,1.0,0.0,0.0,
	 				  sin(angle.y),0.0,cos(angle.y),0.0,
	 				  0.0,0.0,0.0,1.0);
	 
	 mat4 scale = mat4 (scaling.x,0.0,0.0,0.0,
	 					  0.0,scaling.y,0.0,0.0,
	 					  0.0,0.0,scaling.z,0.0,
	 					  0.0,0.0,0.0,1.0);
	
	vec3 n = normalize(eyepoint - lookAt);
	vec3 u = normalize(cross(up,n));
	vec3 v = cross(n,u);
	
	mat4 worldToCamera = mat4(u.x,v.x,n.x, 0.0,
							  u.y,v.y,n.y, 0.0,
							  u.z,v.z,n.z, 0.0,
							  dot(-u,eyepoint), dot(-v,eyepoint),dot(-n,eyepoint), 1.0);
	
	mat4 project;	  
	if(switch1 == 2) {
			project   = mat4(    (2.0/(right-left))           ,0.0                       ,0.0            ,0.0,
								  0.0                          ,((2.0)/(top-bottom)) ,0.0            ,0.0,
								  0.0                          ,0.0                       ,(-2.0/(far-near)),0.0,
								  (-(right+left)/(right - left)),(-(top + bottom)/(top - bottom)),(-(far+near)/(far-near)),1.0);
			
			}
	
	else{
		project       = mat4((2.0*near)/(right-left)       , 0.0                             , 0.0   ,0.0,
							  0.0                         , (2.0*near)/(top-bottom)        ,0.0, 0.0,
							   (right+left)/(right - left),  (top + bottom)/(top - bottom),-(far+near)/(far-near)        ,-1.0,
							  0.0                         ,0.0                              ,(-2.0*far*near/(far-near)),0.0);
		
		}
	gl_Position =project*worldToCamera*trans*roty*rotz*scale*vPosition;
}

    
    texCoord = vTexCoord;
    
}
