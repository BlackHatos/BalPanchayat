package com.pnstech.myapplication;

public class ReturnTeamTags {

    private String imageUrl;
    private String memberName;
    private String memberPosition;
    private String memberId;
    private String memberPhone;

    public ReturnTeamTags(String imageUrl, String memberName, String memberPosition, String memberId, String memberPhone)
    {
        this.imageUrl = imageUrl;
        this.memberName  = memberName;
        this.memberPosition = memberPosition;
        this.memberId = memberId;
        this.memberPhone = memberPhone;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public String getMemberPosition()
    {
        return memberPosition;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public String getMemberPhone()
    {
        return memberPhone;
    }

}
