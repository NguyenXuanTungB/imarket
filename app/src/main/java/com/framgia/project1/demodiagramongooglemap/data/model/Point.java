package com.framgia.project1.demodiagramongooglemap.data.model;

import android.database.Cursor;

/**
 * Created by nguyenxuantung on 24/06/2016.
 */
public class Point {
    private int id;
    private double lat, lng;
    public Point(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex("id"));
        this.lat = cursor.getInt(cursor.getColumnIndex("latitude"));
        this.lng = cursor.getInt(cursor.getColumnIndex("longtitude"));
    }
    public Point(int id, double lat, double lng  ) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == -1) ? 0 : id);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (id == -1) {
            if (other.id != -1)
                return false;
        } else if (id!=other.id)
            return false;
        return true;
    }
}
