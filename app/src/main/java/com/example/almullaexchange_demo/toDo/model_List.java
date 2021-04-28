package com.example.almullaexchange_demo.toDo;

public class model_List
{

    private String title;
    private String time;
    private String id;


    public model_List()
    {
    }


    public model_List(String title, String time)
    {
        this.title = title;
        this.time = time;
    }

    public model_List(String id, String title, String time)
    {
        this.title = title;
        this.time = time;
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
