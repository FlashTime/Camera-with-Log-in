package com.example.danijel.execomhackathon.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Danijel on 5/22/2016.
 */
@DatabaseTable(tableName = "tip")
public class TipMonumenta {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String nazivtipa;

    public TipMonumenta(){

    }

    public TipMonumenta(String nazivTipa) {
        this.nazivtipa = nazivTipa;
    }

    public int getId() {
        return id;
    }

    public String getNazivtipa() {
        return nazivtipa;
    }

    @Override
    public String toString() {
        return "TipMonumenta{" +
                "id=" + id +
                ", nazivtipa='" + nazivtipa + '\'' +
                '}';
    }
}
