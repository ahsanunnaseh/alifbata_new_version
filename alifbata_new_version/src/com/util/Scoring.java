/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.util;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Scoring {
    private final float euclidian_score;
    public Scoring(float euclidian_score){
        this.euclidian_score=euclidian_score;
    }
    public float getScore(){
        return (1 - euclidian_score) * 100;
    }
    
}
