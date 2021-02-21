package ru.same.scheduler;

import android.content.Context;
import android.widget.LinearLayout;


public class Task extends LinearLayout {
    private String title;
    private String body;
    private String time;

    public Task(Context context) {
        super(context);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void setAllInfo(String tit, String bod, String tim) {
        title = tit;
        body = bod;
        time = tim;
    }
}
