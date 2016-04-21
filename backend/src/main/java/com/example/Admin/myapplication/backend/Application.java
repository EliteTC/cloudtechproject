package com.example.Admin.myapplication.backend; /**
 * Created by andrejvojtenko on 21.04.16.
 */
import com.google.appengine.repackaged.com.google.api.client.util.Data;import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Application {
    @Id
    Long id;
    private String name;
    private Data date;
    private String string;

    Application(String string){
        this.string = string;
    }

    Application(String name, Data datum){
        this.name = name;
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDate(Data date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Data getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getString() {
        return string;
    }
}


