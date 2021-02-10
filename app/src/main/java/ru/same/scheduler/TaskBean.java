package ru.same.scheduler;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class TaskBean extends RealmObject {
    private String title;
    private String body;
    private String time;

    public TaskBean() {
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
}
