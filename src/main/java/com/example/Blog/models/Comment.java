package com.example.Blog.models;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Entity
public class Comment {
    public Comment(String text, Date dateOfPost, int likes, int views, Post post) {
        this.text = text;
        this.dateOfPost = dateOfPost;
        this.likes = likes;
        this.views = views;
        this.post = post;
    }

    public Comment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    public byte[] getPhoto() {
        return photo;
    }

    @NotNull
    @NotEmpty(message = "Комментарий должен быть не пустым!")
    @Size(min = 5, max = 250, message = "Напишите комментарий на 5-250 символов")
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfPost;
    private int likes;
    private int views;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    //    public void setPhoto(byte[] photo) {
//        this.photo = photo;
//    }
//
//    @Transient
//    private String normalPhoto;
//    public String getNormalPhoto() throws UnsupportedEncodingException {
//        byte[] encodeBase64 = Base64.encodeBase64(getPhoto());
//        String base64Encoded = new String(encodeBase64, "UTF-8");
//        return base64Encoded;
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
