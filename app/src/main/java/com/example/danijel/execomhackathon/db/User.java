package com.example.danijel.execomhackathon.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Danijel on 5/22/2016.
 */
@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String name;
    @DatabaseField
    String email;
    @DatabaseField
    String password;
    @DatabaseField
    int is;

    public int getId() {
        return id;
    }

    public User(){

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", is=" + is +
                '}';
    }

    public User(String name, String email, String password, int is) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.is = is;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getIs() {
        return is;
    }
}
