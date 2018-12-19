package com.joeribv.joerisgame;

public class DataBaseSorter implements Comparable<DataBaseSorter> {
    private String name;
    private int score;
    public DataBaseSorter(String name, int score){
        this.name = name;
        this.score = score;
    }
    public int getScore(){return score;}
    public String getName(){return name;}
    @Override
    public int compareTo(DataBaseSorter current){
        if(this.getScore()>current.getScore())
            return -1;
        else if(current.getScore()>this.getScore())
            return 1;
        return 0;
    }
    @Override
    public String toString(){
        return "Name: " + this.name + " score: " + this.score;
    }
}
