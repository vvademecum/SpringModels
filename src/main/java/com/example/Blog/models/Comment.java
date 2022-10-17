package com.example.Blog.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    public Comment(String text, Date dateOfPost, int likes, int views, Post post){
        this.text = text;
        this.dateOfPost = dateOfPost;
        this.likes = likes;
        this.views = views;
        this.post = post;
    }
    public Comment(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String photo;
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfPost;
    private int likes;
    private int views;

    @ManyToOne
    @JoinColumn(name="post_id", referencedColumnName = "id")
    private Post post;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(Date dateOfPost) {
        this.dateOfPost = dateOfPost;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
