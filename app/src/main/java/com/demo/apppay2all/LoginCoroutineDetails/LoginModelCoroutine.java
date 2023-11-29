package com.demo.apppay2all.LoginCoroutineDetails;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginModelCoroutine {
    @SerializedName("status")
    public String status="";

    @SerializedName("message")
    public String message="";

    @SerializedName("userdetails")
    public Userdetails userdetails;

    @SerializedName("userservices")
    public Userservices userservices;

    @SerializedName("companydetails")
    public Companydetails companydetails;

    @SerializedName("banner")
    public ArrayList<Banner> banner;

    @SerializedName("notification")
    public ArrayList<Notification> notification;

    @SerializedName("recharge_badge")
    public ArrayList<RechargeBadge> recharge_badge;

    @SerializedName("broadcast")
    public Broadcast broadcast;

    @SerializedName("sales")
    public Sales sales;



    public class Banner{

        @SerializedName("id")
        public int id;

        @SerializedName("image")
        public String image;
    }

    public class Broadcast{

        @SerializedName("heading")
        public String heading;


        @SerializedName("image_url")
        public String image_url;


        @SerializedName("img_status")
        public int img_status;


        @SerializedName("message")
        public String message;


        @SerializedName("status_id")
        public int status_id;
    }

    public class Companydetails{


        @SerializedName("company_name")
        public String company_name;


        @SerializedName("company_email")
        public String company_email;


        @SerializedName("company_address")
        public String company_address;


        @SerializedName("company_address_two")
        public String company_address_two;


        @SerializedName("support_number")
        public String support_number;


        @SerializedName("whatsapp_number")
        public String whatsapp_number;


        @SerializedName("company_logo")
        public String company_logo;


        @SerializedName("company_website")
        public String company_website;


        @SerializedName("news")
        public String news;


        @SerializedName("sender_id")
        public String sender_id;


        @SerializedName("recharge")
        public int recharge;


        @SerializedName("money")
        public int money;


        @SerializedName("money_two")
        public int money_two;


        @SerializedName("aeps")
        public int aeps;


        @SerializedName("payout")
        public int payout;


        @SerializedName("view_plan")
        public int view_plan;


        @SerializedName("pancard")
        public int pancard;


        @SerializedName("ecommerce")
        public int ecommerce;


        @SerializedName("upi_id")
        public Object upi_id;


        @SerializedName("collection")
        public int collection;


        @SerializedName("color_start")
        public String color_start;

        @SerializedName("color_end")
        public String color_end;


        @SerializedName("transaction_pin")
        public int transaction_pin;
    }

    public class Notification{


        @SerializedName("notification_id")
        public String notification_id;


        @SerializedName("notification_title")
        public String notification_title;


        @SerializedName("notification_data")
        public String notification_data;
    }

    public class RechargeBadge{


        @SerializedName("service_id")
        public int service_id;


        @SerializedName("service_name")
        public String service_name;


        @SerializedName("service_image")
        public String service_image;


        @SerializedName("bbps")
        public int bbps;
    }


    public class Sales{


        @SerializedName("today_sale")
        public String today_sale;


        @SerializedName("today_profit")
        public String today_profit;
    }

    public class Userdetails{


        @SerializedName("first_name")
        public String first_name="";


        @SerializedName("last_name")
        public String last_name;


        @SerializedName("email")
        public String email;


        @SerializedName("mobile")
        public String mobile;


        @SerializedName("role_id")
        public int role_id;


        @SerializedName("scheme_id")
        public Object scheme_id;


        @SerializedName("joining_date")
        public String joining_date;


        @SerializedName("address")
        public String address;


        @SerializedName("city")
        public String city;


        @SerializedName("state_id")
        public int state_id;


        @SerializedName("district_id")
        public int district_id;


        @SerializedName("pin_code")
        public int pin_code;


        @SerializedName("shop_name")
        public String shop_name;


        @SerializedName("office_address")
        public String office_address;


        @SerializedName("call_back_url")
        public Object call_back_url;


        @SerializedName("profile_photo")
        public String profile_photo;


        @SerializedName("shop_photo")
        public String shop_photo;


        @SerializedName("gst_regisration_photo")
        public String gst_regisration_photo;


        @SerializedName("pancard_photo")
        public String pancard_photo;


        @SerializedName("cancel_cheque")
        public String cancel_cheque;


        @SerializedName("address_proof")
        public String address_proof;


        @SerializedName("kyc_status")
        public int kyc_status;


        @SerializedName("kyc_remark")
        public String kyc_remark;


        @SerializedName("mobile_verified")
        public int mobile_verified;


        @SerializedName("lock_amount")
        public int lock_amount;


        @SerializedName("session_id")
        public String session_id;


        @SerializedName("active")
        public int active;


        @SerializedName("reason")
        public String reason;


        @SerializedName("api_token")
        public String api_token;


        @SerializedName("user_balance")
        public String user_balance;


        @SerializedName("aeps_balance")
        public String aeps_balance;


        @SerializedName("lien_amount")
        public String lien_amount;


        @SerializedName("account_number")
        public String account_number;


        @SerializedName("ifsc_code")
        public String ifsc_code;


        @SerializedName("pan_username")
        public Object pan_username;


        @SerializedName("ekyc")
        public int ekyc;


        @SerializedName("pan_number")
        public String pan_number;


        @SerializedName("agentonboarding")
        public int agentonboarding;
    }

    public class Userservices{


        @SerializedName("recharge")
        public int recharge;


        @SerializedName("money")
        public int money;


        @SerializedName("money_two")
        public int money_two;


        @SerializedName("aeps")
        public int aeps;


        @SerializedName("payout")
        public int payout;


        @SerializedName("pancard")
        public int pancard;

        @SerializedName("ecommerce")
        public int ecommerce;
    }

}