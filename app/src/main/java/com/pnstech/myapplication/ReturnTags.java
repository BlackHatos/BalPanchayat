package com.pnstech.myapplication;

public class ReturnTags {

    private String imageUrl;
    private String bookName;
    private String bookWriter;
    private String bookDate;
    private String bookId;


    public ReturnTags(String imageUrl, String bookName, String bookWriter, String bookDate, String bookId)
    {
        this.imageUrl = imageUrl;
        this.bookName  = bookName;
        this.bookWriter = bookWriter;
        this.bookDate =  bookDate;
        this.bookId =  bookId;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getBookName()
    {
        return bookName;
    }


    public String getBookWriter()
    {
      return bookWriter;
    }


    public String getBookDate()
    {
      return bookDate;
    }


    public String  getbookId()
    {
        return bookId;
    }

}
