package com.example.braguia.model;

import com.example.braguia.model.Trail.Edge;
        import com.example.braguia.model.Trail.Point;
        import java.util.List;

public class maps_model {
    public Trail.Point encontraPonto(String name, List<Trail.Edge> list) {
        for(int i=0;i<list.size();i++) {
            Trail.Edge aux = list.get(i);
            Trail.Point start = aux.getpoint_start();
            if (start.getName().equals(name)) {return start;}
            Trail.Point end = aux.getpoint_end();
            if(end.getName().equals(name)) {return end;}
        }
        return null;
    }

}