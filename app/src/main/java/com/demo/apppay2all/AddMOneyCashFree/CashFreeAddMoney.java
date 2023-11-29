package com.demo.apppay2all.AddMOneyCashFree;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cashfree.pg.CFPaymentService;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.R;
import com.demo.apppay2all.ReceiptDetail.Receipt;

import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_BANK_CODE;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_CVV;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_HOLDER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_MM;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_NUMBER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_YYYY;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_NOTIFY_URL;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.cashfree.pg.CFPaymentService.PARAM_WALLET_CODE;

public class CashFreeAddMoney extends AppCompatActivity {

    TextView tv_balance;
    EditText ed_amount;
    Button bt_proceed;

    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }

    SeamlessMode currentMode = SeamlessMode.CARD;

    private static final String TAG = "MainActivity";

    ProgressDialog dialog;

    String token="",appId="",orderId="",amount="",mode="",callbackurl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_cash_free);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_balance=findViewById(R.id.tv_balance);
        tv_balance.setText("Rs "+ SharePrefManager.getInstance(CashFreeAddMoney.this).mGetMainBalance());

        
        ed_amount=findViewById(R.id.ed_amount);
        TextView tv_amount_word=findViewById(R.id.tv_amount_word);
        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals(""))
                {
                    tv_amount_word.setText(new NumberToWord().convert(Integer.parseInt(editable.toString())));
                }
                else
                {
                    tv_amount_word.setText("");
                }
            }
        });

        
        bt_proceed=findViewById(R.id.bt_proceed);
        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(CashFreeAddMoney.this))
                {
                    if (ed_amount.getText().toString().equals(""))
                    {
                        Toast.makeText(CashFreeAddMoney.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        amount=ed_amount.getText().toString();
                        mGetOrderId(amount);
                    }
                }
                else
                {
                    Toast.makeText(CashFreeAddMoney.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void mAddMoney()
    {

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);

        cfPaymentService.doPayment(CashFreeAddMoney.this,getInputParams(),token,mode);
//        cfPaymentService.doPayment(AddMoney.this, getSeamlessCheckoutParams(), token, stage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);

//        String oder_id="";
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d(TAG, key + " : " + bundle.getString(key));

                        if (!key.equals("")&&key.equalsIgnoreCase("orderId"))
                        {
                            orderId=bundle.getString(key);
                        }
                    }
                }

            finish();
        }

//        mCheckStatus(orderId);

    }

    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */

        String orderAmount = amount;
        String orderNote = "Load_wallet";
        String customerName = SharePrefManager.getInstance(this).mGetName();
        String customerPhone = SharePrefManager.getInstance(this).mGetUsername();
        String customerEmail = SharePrefManager.getInstance(this).mGetEmail();

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_NOTIFY_URL, callbackurl);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }

    private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams();
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, "VALID_CARD_NUMBER");
                params.put(PARAM_CARD_YYYY, "YYYY");
                params.put(PARAM_CARD_MM, "MM");
                params.put(PARAM_CARD_HOLDER, "CARD_HOLDER_NAME");
                params.put(PARAM_CARD_CVV, "CVV");
                break;
            case WALLET:
                params.put(PARAM_PAYMENT_OPTION, "wallet");
                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
                break;
            case NET_BANKING:
                params.put(PARAM_PAYMENT_OPTION, "nb");
                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
                break;
            case UPI_COLLECT:
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "VALID_VPA");
                break;
            case PAY_PAL:
                params.put(PARAM_PAYMENT_OPTION, "paypal");
                break;
        }
        return params;
    }


    @SuppressLint("StaticFieldLeak")
    protected void mGetOrderId(String amount)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(CashFreeAddMoney.this).mGetApiToken());
        builder.appendQueryParameter("amount",amount);

        String sending_url= BaseURL.BASEURL_B2C+"api/add-money/v2/create-order";

        new CallResAPIPOSTMethod(CashFreeAddMoney.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(CashFreeAddMoney.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data "+s);


                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(s);

                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (jsonObject.has("orderId"))
                    {
                        orderId=jsonObject.getString("orderId");
                    }

                    if (jsonObject.has("app_id"))
                    {
                        appId=jsonObject.getString("app_id");
                    }

                    if (jsonObject.has("order_token"))
                    {
                        token=jsonObject.getString("order_token");
                    }

                    if (jsonObject.has("mode"))
                    {
                        mode=jsonObject.getString("mode");
                    }
                    if (jsonObject.has("notify_url"))
                    {
                        callbackurl=jsonObject.getString("notify_url");
                    }

                    if (!status.equals(""))
                    {
                        if (status.equalsIgnoreCase("success"))
                        {
                            mAddMoney();
                        }
                        else if (!status.equalsIgnoreCase("success"))
                        {
                            if (!message.equals(""))
                            {
                                Toast.makeText(CashFreeAddMoney.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(CashFreeAddMoney.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(CashFreeAddMoney.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    protected void mCheckStatus(final String orderId)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(CashFreeAddMoney.this).mGetApiToken());
        builder.appendQueryParameter("orderId",orderId);

        String sending_url=BaseURL.BASEURL_B2C+"api/add-money/check-status";
        new CallResAPIPOSTMethod(CashFreeAddMoney.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(CashFreeAddMoney.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(s);

                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

//                    Intent intent = new Intent(CashFreeAddMoney.this, Receipt.class);
//                    intent.putExtra("status", status);
//                    intent.putExtra("message", message);
//                    intent.putExtra("number", SharePrefManager.getInstance(CashFreeAddMoney.this).mGetUsername());
//                    intent.putExtra("amount", amount);
//                    intent.putExtra("txnno", orderId);
//                    intent.putExtra("type", "recharge");
//                    intent.putExtra("provider", "Load Wallet");
//                    startActivity(intent);
//                    finish();

                    Intent intent = new Intent(CashFreeAddMoney.this, Receipt.class);
                    Reports_Item item=new Reports_Item();
                    item.setType("load");
                    item.setStatus(status);
                    item.setNumber( SharePrefManager.getInstance(CashFreeAddMoney.this).mGetUsername());

                    item.setAmount(amount);
                    item.setCompany("Load Wallet");
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("DATA",item);
                    bundle.putString("message",message);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}