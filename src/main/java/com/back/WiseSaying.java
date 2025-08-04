package com.back;

public class WiseSaying {
    static int lastId = 0;
    int id;
    String content;
    String author;

    public WiseSaying(String content, String author) {
        this.id = ++lastId;
        this.content = content;
        this.author = author;
    }
}
