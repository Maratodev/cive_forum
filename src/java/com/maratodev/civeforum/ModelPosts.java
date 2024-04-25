package com.maratodev.civeforum;

public class ModelPosts
{
    String description;
    String pComments;
    String pid;
    String ptime;
    String title;
    String udp;
    String uemail;
    String uid;
    String uname;
    
    public ModelPosts() {
    }
    
    public ModelPosts(final String description, final String pid, final String ptime, final String pComments, final String title, final String udp, final String uemail, final String uid, final String uname) {
        this.description = description;
        this.pid = pid;
        this.ptime = ptime;
        this.pComments = pComments;
        this.title = title;
        this.udp = udp;
        this.uemail = uemail;
        this.uid = uid;
        this.uname = uname;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getPComments() {
        return this.pComments;
    }
    
    public String getPid() {
        return this.pid;
    }
    
    public String getPtime() {
        return this.ptime;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getUdp() {
        return this.udp;
    }
    
    public String getUemail() {
        return this.uemail;
    }
    
    public String getUid() {
        return this.uid;
    }
    
    public String getUname() {
        return this.uname;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setPComments(final String pComments) {
        this.pComments = pComments;
    }
    
    public void setPid(final String pid) {
        this.pid = pid;
    }
    
    public void setPtime(final String ptime) {
        this.ptime = ptime;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setUdp(final String udp) {
        this.udp = udp;
    }
    
    public void setUemail(final String uemail) {
        this.uemail = uemail;
    }
    
    public void setUid(final String uid) {
        this.uid = uid;
    }
    
    public void setUname(final String uname) {
        this.uname = uname;
    }
}
