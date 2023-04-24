package com.example.braguia.model;

import com.example.braguia.model.Trail.Edge;
import com.example.braguia.model.Trail.Point;

import java.util.List;

public class find_Point {
    public Trail.Point encontraPonto(String name, List<Edge> list) {
        for(int i=0;i<list.size();i++) {
            Edge aux = list.get(i);
            Point start = aux.getpoint_start();
            if (start.getName().equals(name)) {return start;}
            Point end = aux.getpoint_end();
            if(end.getName().equals(name)) {return end;}
        }
        return null;
    }
}
