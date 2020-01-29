package com.pnstech.myapplication;

public class ReturnNotificationTags {

    private String notificationText;
    private String notificationId;
    private String notificationDate;

    public ReturnNotificationTags(String notificationId, String notificationText, String notificationDate)
    {
        this.notificationId = notificationId;
        this.notificationText = notificationText;
        this.notificationDate = notificationDate;
    }

    public String getNotificationId()
    {
        return notificationId;
    }

    public String getNotificationText()
    {
        return notificationText;
    }

    public String getNotificationDate()
    {
        return  notificationDate;
    }

}
