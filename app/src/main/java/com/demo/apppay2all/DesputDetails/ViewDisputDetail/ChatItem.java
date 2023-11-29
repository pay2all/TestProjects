package com.demo.apppay2all.DesputDetails.ViewDisputDetail;

public class ChatItem {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDispute_id() {
        return dispute_id;
    }

    public void setDispute_id(String dispute_id) {
        this.dispute_id = dispute_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    String id;
    String user_id;
    String dispute_id;
    String message;
    String created_at;
    String is_read;
}
