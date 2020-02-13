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
    private String is_approved;
    private String is_issued;
    private String actual_book_id;
    private String approve_date;
    private String issue_date;

    public ReturnBookRequestTags(String bookId, String bookName, String userId, String userName,
                                 String userDistrict, String userPhone,
                                 String requestDate, String num_copy, String requested_copy,
                                 String is_approved, String is_issued,String actual_book_id, String approve_date, String issue_date)
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
        this.is_approved = is_approved;
        this.is_issued = is_issued;
        this.actual_book_id = actual_book_id;
        this.approve_date = approve_date;
        this.issue_date = issue_date;
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

    public String getActualBookId()
    {
        return actual_book_id;
    }

    public String getIsIssued()
    {
        return is_issued;
    }

    public String getIssueDatex()
    {
        return issue_date;
    }

    public String getApproveDatex()
    {
        return approve_date;
    }

}
