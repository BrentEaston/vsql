/*
 * Copyright (c) 2000-2003 by David Sullivan
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package CASL.Map;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

/**
 * Title:        PrefabBuilding.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class PrefabBuilding
	implements Serializable {

	// serial ID: version 1.0.0
	static final long serialVersionUID = 000100L;

	public static final int INVALID_FILE_INDEX = -1;

	// private variables
	private	int		terrainType;
	private	Polygon	shape;
	private	String	imageName;
	private	int		fileIndex	= INVALID_FILE_INDEX;
	private	int		rotation	= 0;
	private	int		x;
	private	int		y;

	public PrefabBuilding(int terrainType, Polygon shape, String imageName){

		this.terrainType 	= terrainType;
		this.shape		= shape;
		this.imageName	= imageName;
	}

	// setters/getters
	public int		getTerrainType(){ return terrainType;}
	public String	getImageName(){ return imageName;}
	public void		setFileIndex(int fileIndex){ this.fileIndex = fileIndex;}
	public int		getFileIndex(){ return fileIndex;}
	public void		setRotation(int rotation){ this.rotation = rotation;}
	public int		getRotation(){ return rotation;}
	public void		setCenter(int x, int y){ this.x = x; this.y = y;}
	public int		getCenterX(){ return x;}
	public int		getCenterY(){ return y;}
	public Polygon	getShapePolygon(){ return shape;}
	public Shape	getShape(){

		return (getAffineTransform()).createTransformedShape(shape);
	}
	public AffineTransform getAffineTransform(){

		// create and rotate
		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rotation), x, y);
		at.concatenate(AffineTransform.getTranslateInstance(
			(double) (x - shape.getBounds().getWidth()/2),
			(double) (y - shape.getBounds().getHeight()/2)));
		return at;
	}
}

