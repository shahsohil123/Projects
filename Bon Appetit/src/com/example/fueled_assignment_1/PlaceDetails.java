package com.example.fueled_assignment_1;

import java.io.Serializable;
import java.lang.ref.Reference;

import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class PlaceDetails implements Serializable {
 
    @Key
    public String status;
 
    @Key
    public Place result;
 
    
    
    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}