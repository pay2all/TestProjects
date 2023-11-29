package com.demo.apppay2all.LoginMVPDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public UserServices getUserservices() {
        return userservices;
    }

    public void setUserservices(UserServices userservices) {
        this.userservices = userservices;
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("banner")
    List<Banners> banner;

    public List<Banners> getBanner() {
        return banner;
    }

    public void setBanner(List<Banners> banner) {
        this.banner = banner;
    }

    @SerializedName("userdetails")
    UserDetails userDetails;

    @SerializedName("userservices")
    UserServices userservices;

    @SerializedName("companydetails")
    CompanyDetails companyDetails;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @SerializedName("errors")
    Errors errors;

    class UserDetails{

        @SerializedName("first_name")
        String firstname;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRoleid() {
            return roleid;
        }

        public void setRoleid(String roleid) {
            this.roleid = roleid;
        }

        public String getScheme_id() {
            return scheme_id;
        }

        public void setScheme_id(String scheme_id) {
            this.scheme_id = scheme_id;
        }

        public String getJoing_date() {
            return joing_date;
        }

        public void setJoing_date(String joing_date) {
            this.joing_date = joing_date;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState_id() {
            return state_id;
        }

        public void setState_id(String state_id) {
            this.state_id = state_id;
        }

        public String getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(String district_id) {
            this.district_id = district_id;
        }

        public String getPin_code() {
            return pin_code;
        }

        public void setPin_code(String pin_code) {
            this.pin_code = pin_code;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getOffice_address() {
            return office_address;
        }

        public void setOffice_address(String office_address) {
            this.office_address = office_address;
        }

        public String getCall_back_url() {
            return call_back_url;
        }

        public void setCall_back_url(String call_back_url) {
            this.call_back_url = call_back_url;
        }

        public String getProfile_photo() {
            return profile_photo;
        }

        public void setProfile_photo(String profile_photo) {
            this.profile_photo = profile_photo;
        }

        public String getShop_photo() {
            return shop_photo;
        }

        public void setShop_photo(String shop_photo) {
            this.shop_photo = shop_photo;
        }

        public String getGst_regisration_photo() {
            return gst_regisration_photo;
        }

        public void setGst_regisration_photo(String gst_regisration_photo) {
            this.gst_regisration_photo = gst_regisration_photo;
        }

        public String getPancard_photo() {
            return pancard_photo;
        }

        public void setPancard_photo(String pancard_photo) {
            this.pancard_photo = pancard_photo;
        }

        public String getCancel_cheque() {
            return cancel_cheque;
        }

        public void setCancel_cheque(String cancel_cheque) {
            this.cancel_cheque = cancel_cheque;
        }

        public String getAddress_proof() {
            return address_proof;
        }

        public void setAddress_proof(String address_proof) {
            this.address_proof = address_proof;
        }

        public String getKyc_status() {
            return kyc_status;
        }

        public void setKyc_status(String kyc_status) {
            this.kyc_status = kyc_status;
        }

        public String getKyc_remark() {
            return kyc_remark;
        }

        public void setKyc_remark(String kyc_remark) {
            this.kyc_remark = kyc_remark;
        }

        public String getMobile_verified() {
            return mobile_verified;
        }

        public void setMobile_verified(String mobile_verified) {
            this.mobile_verified = mobile_verified;
        }

        public String getLock_amount() {
            return lock_amount;
        }

        public void setLock_amount(String lock_amount) {
            this.lock_amount = lock_amount;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getApi_token() {
            return api_token;
        }

        public void setApi_token(String api_token) {
            this.api_token = api_token;
        }

        public String getUser_balance() {
            return user_balance;
        }

        public void setUser_balance(String user_balance) {
            this.user_balance = user_balance;
        }

        public String getAeps_balance() {
            return aeps_balance;
        }

        public void setAeps_balance(String aeps_balance) {
            this.aeps_balance = aeps_balance;
        }

        public String getLien_amount() {
            return lien_amount;
        }

        public void setLien_amount(String lien_amount) {
            this.lien_amount = lien_amount;
        }

        public String getAccount_number() {
            return account_number;
        }

        public void setAccount_number(String account_number) {
            this.account_number = account_number;
        }

        public String getIfsc_code() {
            return ifsc_code;
        }

        public void setIfsc_code(String ifsc_code) {
            this.ifsc_code = ifsc_code;
        }

        public String getPan_username() {
            return pan_username;
        }

        public void setPan_username(String pan_username) {
            this.pan_username = pan_username;
        }

        public String getEkyc() {
            return ekyc;
        }

        public void setEkyc(String ekyc) {
            this.ekyc = ekyc;
        }

        public String getPan_number() {
            return pan_number;
        }

        public void setPan_number(String pan_number) {
            this.pan_number = pan_number;
        }

        public String getAgentonboarding() {
            return agentonboarding;
        }

        public void setAgentonboarding(String agentonboarding) {
            this.agentonboarding = agentonboarding;
        }

        @SerializedName("last_name")
        String lastname;

        @SerializedName("email")
        String email;

        @SerializedName("mobile")
        String mobile;

        @SerializedName("role_id")
        String roleid;


        @SerializedName("scheme_id")
        String scheme_id;


        @SerializedName("joing_date")
        String joing_date;

        @SerializedName("address")
        String address;

        @SerializedName("city")
        String city;

        @SerializedName("state_id")
        String state_id;

        @SerializedName("district_id")
        String district_id;

        @SerializedName("pin_code")
        String pin_code;

        @SerializedName("shop_name")
        String shop_name;

        @SerializedName("office_address")
        String office_address;

        @SerializedName("call_back_url")
        String call_back_url;

        @SerializedName("profile_photo")
        String profile_photo;

        @SerializedName("shop_photo")
        String shop_photo;

        @SerializedName("gst_regisration_photo")
        String gst_regisration_photo;

        @SerializedName("pancard_photo")
        String pancard_photo;

        @SerializedName("cancel_cheque")
        String cancel_cheque;

        @SerializedName("address_proof")
        String address_proof;

        @SerializedName("kyc_status")
        String kyc_status;

        @SerializedName("kyc_remark")
        String kyc_remark;

        @SerializedName("mobile_verified")
        String mobile_verified;

        @SerializedName("lock_amount")
        String lock_amount;

        @SerializedName("session_id")
        String session_id;

        @SerializedName("active")
        String active;

        @SerializedName("reason")
        String reason;

        @SerializedName("api_token")
        String api_token;

        @SerializedName("user_balance")
        String user_balance;

        @SerializedName("aeps_balance")
        String aeps_balance;

        @SerializedName("lien_amount")
        String lien_amount;

        @SerializedName("account_number")
        String account_number;

        @SerializedName("ifsc_code")
        String ifsc_code;

        @SerializedName("pan_username")
        String pan_username;

        @SerializedName("ekyc")
        String ekyc;

        @SerializedName("pan_number")
        String pan_number;

        @SerializedName("agentonboarding")
        String agentonboarding;

    }

    class UserServices
    {
        @SerializedName("recharge")
        String recharge;

        @SerializedName("money")
        String money;

        public String getRecharge() {
            return recharge;
        }

        public void setRecharge(String recharge) {
            this.recharge = recharge;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMoney_two() {
            return money_two;
        }

        public void setMoney_two(String money_two) {
            this.money_two = money_two;
        }

        public String getAeps() {
            return aeps;
        }

        public void setAeps(String aeps) {
            this.aeps = aeps;
        }

        public String getPayout() {
            return payout;
        }

        public void setPayout(String payout) {
            this.payout = payout;
        }

        public String getPancard() {
            return pancard;
        }

        public void setPancard(String pancard) {
            this.pancard = pancard;
        }

        public String getEcommerce() {
            return ecommerce;
        }

        public void setEcommerce(String ecommerce) {
            this.ecommerce = ecommerce;
        }

        @SerializedName("money_two")
        String money_two;

        @SerializedName("aeps")
        String aeps;

        @SerializedName("payout")
        String payout;

        @SerializedName("pancard")
        String pancard;

        @SerializedName("ecommerce")
        String ecommerce;
    }

    class CompanyDetails
    {
        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_email() {
            return company_email;
        }

        public void setCompany_email(String company_email) {
            this.company_email = company_email;
        }

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
        }

        public String getCompany_address_two() {
            return company_address_two;
        }

        public void setCompany_address_two(String company_address_two) {
            this.company_address_two = company_address_two;
        }

        public String getSupport_number() {
            return support_number;
        }

        public void setSupport_number(String support_number) {
            this.support_number = support_number;
        }

        public String getWhatsapp_number() {
            return whatsapp_number;
        }

        public void setWhatsapp_number(String whatsapp_number) {
            this.whatsapp_number = whatsapp_number;
        }

        public String getCompany_logo() {
            return company_logo;
        }

        public void setCompany_logo(String company_logo) {
            this.company_logo = company_logo;
        }

        public String getCompany_website() {
            return company_website;
        }

        public void setCompany_website(String company_website) {
            this.company_website = company_website;
        }

        public String getNews() {
            return news;
        }

        public void setNews(String news) {
            this.news = news;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getRecharge() {
            return recharge;
        }

        public void setRecharge(String recharge) {
            this.recharge = recharge;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMoney_two() {
            return money_two;
        }

        public void setMoney_two(String money_two) {
            this.money_two = money_two;
        }

        public String getAeps() {
            return aeps;
        }

        public void setAeps(String aeps) {
            this.aeps = aeps;
        }

        public String getPayout() {
            return payout;
        }

        public void setPayout(String payout) {
            this.payout = payout;
        }

        public String getView_plan() {
            return view_plan;
        }

        public void setView_plan(String view_plan) {
            this.view_plan = view_plan;
        }

        public String getPancard() {
            return pancard;
        }

        public void setPancard(String pancard) {
            this.pancard = pancard;
        }

        public String getEcommerce() {
            return ecommerce;
        }

        public void setEcommerce(String ecommerce) {
            this.ecommerce = ecommerce;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        @SerializedName("company_name")
        String company_name;

        @SerializedName("company_email")
        String company_email;

        @SerializedName("company_address")
        String company_address;

        @SerializedName("company_address_two")
        String company_address_two;

        @SerializedName("support_number")
        String support_number;

        @SerializedName("whatsapp_number")
        String whatsapp_number;

        @SerializedName("company_logo")
        String company_logo;

        @SerializedName("company_website")
        String company_website;

        @SerializedName("news")
        String news;

        @SerializedName("sender_id")
        String sender_id;

        @SerializedName("recharge")
        String recharge;

        @SerializedName("money")
        String money;

        @SerializedName("money_two")
        String money_two;

        @SerializedName("aeps")
        String aeps;

        @SerializedName("payout")
        String payout;

        @SerializedName("view_plan")
        String view_plan;

        @SerializedName("pancard")
        String pancard;

        @SerializedName("ecommerce")
        String ecommerce;

        @SerializedName("upi_id")
        String upi_id;

        @SerializedName("collection")
        String collection;
    }

    class Errors
    {
        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @SerializedName("device_id")
        String device_id;

        @SerializedName("username")
        String username;

        @SerializedName("password")
        String password;
    }

    class Banners
    {
        @SerializedName("imgae")
        String image;
    }
}