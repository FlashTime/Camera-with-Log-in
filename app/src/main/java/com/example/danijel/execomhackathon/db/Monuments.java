package com.example.danijel.execomhackathon.db;

import android.media.Image;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Danijel on 5/22/2016.
 */
@DatabaseTable (tableName = "monument")
public class Monuments {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String naziv;
    @DatabaseField
    String image;
    @DatabaseField
    String description;
    @DatabaseField
    int user;
    @DatabaseField
    String tip;

    @Override
    public String toString() {
        return "Monuments{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", tip='" + tip + '\'' +
                '}';
    }

    public Monuments(){}
    public Monuments(String naziv, String image, String description, int user, String tip) {
        this.naziv = naziv;
        this.image = image;
        this.description = description;
        this.user = user;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getUser() {
        return user;
    }

    public String getTip() {
        return tip;
    }

}
