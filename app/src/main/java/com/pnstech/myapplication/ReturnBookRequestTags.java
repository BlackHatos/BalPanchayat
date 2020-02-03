package com.pnstech.myapplication;

public class ReturnBookRequestTags {

    private String bookName;
    private String bookId;
    private String userId;
    private String userName;
    private String requestDate;
    private String userDistrict;
    private String userPhone;
    private String requested_copy; //copy requested number
    private String num_copy; //total copies available
    private String is_returned;
    private String is_approved;

    private String isAvailable; //checking availability of the book

    public ReturnBookRequestTags(String bookId, String bookName, String userId, String userName,
                                 String userDistrict, String userPhone,
                                 String requestDate, String num_copy, String requested_copy, String is_approved, String is_returned)
    {

        this.bookId = bookId;
        this.bookName = bookName;
        this.userId = userId;
        this.userName = userName;
        this.userDistrict  =userDistrict;
        this.userPhone  = userPhone;
        this.requestDate = requestDate;
        this.num_copy = num_copy;
        this.requested_copy = requested_copy;
        this.is_returned = is_returned;
        this.is_approved = is_approved;

    }

    public String getBookIdx()
    {
        return bookId;
    }

    public String getBookNamex()
    {
        return bookName;
    }


    public String getUserIdx()
    {
        return userId;
    }

    public String getUserNamex()
    {
        return userName;
    }

    public String getUserPhonex()
    {
        return userPhone;
    }

    public String getUserDistrictx()
    {
        return userDistrict;
    }

    public String getRequestDatex()
    {
        return requestDate;
    }

    public String getTotalCopyx() // total copies available of the book
    {
        return num_copy;
    }

    public String getRequestedCopyx() //number of copies requested
    {
        return requested_copy;
    }

    public String getIsApprovedy()
    {
        return  is_approved;
    }

    public String getIsReturned()
    {
        return is_returned;
    }

}
