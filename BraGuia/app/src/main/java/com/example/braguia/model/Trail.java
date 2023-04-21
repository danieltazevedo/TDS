package com.example.braguia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "trail",indices = @Index(value = {"id"},unique = true))
public class Trail implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("trail_img")
    @ColumnInfo(name = "trail_img")
    String image_url;


    @SerializedName("trail_name")
    @ColumnInfo(name = "trail_name")
    String trail_name;

    @SerializedName("trail_desc")
    @ColumnInfo(name = "trail_desc")
    String trail_desc;

    @SerializedName("trail_duration")
    @ColumnInfo(name = "trail_duration")
    int trail_duration;

    @SerializedName("trail_difficulty")
    @ColumnInfo(name = "trail_difficulty")
    String trail_difficulty;

    @TypeConverters(EdgeListTypeConverter.class)
    List<Edge> edges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return image_url;
    }

    public String getTrail_name() {
        return trail_name;
    }

    public String getTrail_desc() {
        return trail_desc;
    }

    public int getTrail_duration() {
        return trail_duration;
    }

    public String getTrail_difficulty() {
        return trail_difficulty;
    }


    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trail trail = (Trail) o;
        return id.equals(trail.id) &&
                Objects.equals(image_url, trail.image_url) &&
                Objects.equals(trail_name, trail.trail_name) &&
                Objects.equals(trail_desc, trail.trail_desc) &&
                Objects.equals(trail_duration, trail.trail_duration) &&
                Objects.equals(trail_difficulty, trail.trail_difficulty) &&
                Objects.equals(edges,trail.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image_url);
    }

    public class Edge implements Serializable {
        @SerializedName("edge_start")
        Point point_start;

        @SerializedName("edge_end")
        Point point_end;

        public Point getpoint_start() {
            return point_start;
        }

        public Point getpoint_end() {
            return point_end;
        }

    }

    public class Point implements Serializable {
        @SerializedName("id")
        String id;

        @SerializedName("pin_name")
        String name;

        @SerializedName("pin_desc")
        String desc;

        @SerializedName("pin_lat")
        double lat;

        @SerializedName("pin_lng")
        double lng;

        @SerializedName("media")
        List<Media> media;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

    }

    public class Media implements Serializable {
        @SerializedName("media_file")
        String file;

        @SerializedName("media_type")
        String type;

        public String getFile() {
            return file;
        }

        public String getType() {
            return type;
        }

    }
}

