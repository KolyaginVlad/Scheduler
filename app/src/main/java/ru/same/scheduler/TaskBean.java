package ru.same.scheduler;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class TaskBean extends RealmObject {
    private String title;
    private String body;
    private String time;
    private String path1;
    private String path2;
    private String path3;
    private String path4;
    private String path5;

    public TaskBean() {
    }

    public String getPath1() {
        return path1;
    }

    public void setPath1(String path1) {
        this.path1 = path1;
    }

    public String getPath2() {
        return path2;
    }

    public void setPath2(String path2) {
        this.path2 = path2;
    }

    public String getPath3() {
        return path3;
    }

    public void setPath3(String path3) {
        this.path3 = path3;
    }

    public String getPath4() {
        return path4;
    }

    public void setPath4(String path4) {
        this.path4 = path4;
    }

    public String getPath5() {
        return path5;
    }

    public void setPath5(String path5) {
        this.path5 = path5;
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
