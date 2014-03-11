package com.example.fueled_assignment_1;

public class Review {

	private String id;
	  private String review;

	  public String getId() {
	    return id;
	  }

	  public void setId(String id) {
	    this.id = id;
	  }

	  public String getReview() {
	    return review;
	  }

	  public void setReview(String review) {
	    this.review = review;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return review;
	  }
}
