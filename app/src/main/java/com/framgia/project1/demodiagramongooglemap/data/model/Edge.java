package com.framgia.project1.demodiagramongooglemap.data.model;

import android.database.Cursor;

/**
 * Created by nguyenxuantung on 24/06/2016.
 */
public class Edge {
    private int idStart;
    private int idEnd;
    private float edge;
    public Edge(Cursor cursor){
        this.idStart = cursor.getInt(cursor.getColumnIndex("id_start"));
        this.idEnd = cursor.getInt(cursor.getColumnIndex("id_end"));
        this.edge= cursor.getFloat(cursor.getColumnIndex("edge"));
    }
    public Edge(int idStart, int idEnd, float edge) {
        this.idStart = idStart;
        this.idEnd = idEnd;
        this.edge= edge;
    }

    public int getIdStart() {
        return idStart;
    }

    public void setIdStart(int idStart) {
        this.idStart = idStart;
    }

    public int getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(int idEnd) {
        this.idEnd = idEnd;
    }

    public float getDistance() {
        return edge;
    }

    public void setDistance(float edge) {
        edge = edge;
    }

    public int getSource() {
        return idStart;
    }

    public int getDestination() {
        return idEnd;
    }

    public float getWeigth() {
        return edge;
    }
}