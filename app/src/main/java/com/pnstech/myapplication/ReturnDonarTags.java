package com.pnstech.myapplication;

public class ReturnDonarTags {

    private String donarName;
    private String donationAmmount;
    private String donationDate;
    private String donationID;


    public ReturnDonarTags(String donarName, String donationAmmount, String donationDate, String donationID)
    {
        this.donarName = donarName;
        this.donationAmmount = donationAmmount;
        this.donationID = donationID;
        this.donationDate = donationDate;
    }



    public String getDonarName()
    {
        return donarName;
    }



    public String getDonationAmmount()
    {
        return donationAmmount;
    }



    public String getDonationDate()
    {
        return donationDate;
    }


    public String getDonationID()
    {
        return donationID;
    }

}
