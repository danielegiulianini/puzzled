package org.danielegiulianini.puzzled.commons;

import java.awt.image.BufferedImage;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PuzzleTile implements Comparable<PuzzleTile> {
	private String id;
	private int originalPosition;
	private int currentPosition;
	private CustomImage image;

	//for jackson de-serialization
	public PuzzleTile(){
	}
	
    public PuzzleTile(String id, final BufferedImage image, final int originalPosition, final int currentPosition) {
        this.id = id;
        this.originalPosition = originalPosition;
        this.currentPosition = currentPosition;
        this.image = new CustomImage(image);
    }
    
    public CustomImage getImage() {
    	return image;
    }
    
    @JsonIgnore
    public boolean isInRightPlace() {
    	return currentPosition == originalPosition;
    }
    
    public int getCurrentPosition() {
    	return currentPosition;
    }
    
    public int getOriginalPosition() {
    	return originalPosition;
    }
    
    public void setCurrentPosition(final int newPosition) {
    	currentPosition = newPosition;
    }

    @JsonIgnore @Override
	public int compareTo(PuzzleTile other) {	//(sorted by currentPosition)
		return this.currentPosition < other.currentPosition ? -1 
				: (this.currentPosition == other.currentPosition ? 0 : 1);
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "[tile" + this.id + ", o.p.:" + originalPosition + ", c.p.: "+ currentPosition+"]";
	}
	
}
