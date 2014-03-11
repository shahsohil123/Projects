package com.example.fueled_assignment_1;

import java.io.Serializable;
import java.util.List;
 
import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* */
public class PlacesList implements Serializable {
 
    @Key
    public String status;
 
    @Key
    public List<Place> results;
 
}