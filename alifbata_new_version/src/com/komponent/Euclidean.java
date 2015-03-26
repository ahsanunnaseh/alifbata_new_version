/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.komponent;

/**
 *
 * @author it6
 */
public class Euclidean
{

    public static void main(String[] args)
    {
	double x1 = 0.1;
	double y1 = 0.2;
	double x2 = 0.3;
	double y2 = 0.4;

	double  xDiff = x1-x2;
        double  xSqr  = Math.pow(xDiff, 2);

	double yDiff = y1-y2;
	double ySqr = Math.pow(yDiff, 2);

	double output   = Math.sqrt(xSqr + ySqr);
	
	System.out.println("Distance = " + output);  

    }

}
