package com.maratodev.civeforum;

public class ModelComment
{
    String cId;
    String comment;
    String ptime;
    String udp;
    String uemail;
    String uid;
    String uname;
    
    public ModelComment() {
    }
    
    public ModelComment(final String cId, final String comment, final String ptime, final String udp, final String uemail, final String uid, final String uname) {
        this.cId = cId;
        this.comment = comment;
        this.ptime = ptime;
        this.udp = udp;
        this.uemail = uemail;
        this.uid = uid;
        this.uname = uname;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public String getPtime() {
        return this.ptime;
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
    
    public String getcId() {
        return this.cId;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    public void setPtime(final String ptime) {
        this.ptime = ptime;
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
    
    public void setcId(final String cId) {
        this.cId = cId;
    }
}
