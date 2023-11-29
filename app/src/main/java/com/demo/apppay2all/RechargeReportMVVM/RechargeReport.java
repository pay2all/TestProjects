package com.demo.apppay2all.RechargeReportMVVM;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargeReports.Reports;
import com.demo.apppay2all.SharePrefManager;
import com.demo.apppay2all.Statment.Account_Statemnt_CardAdaptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RechargeReport extends AppCompatActivity {

    RecyclerView recyclerview_reports;
    List<ReportModel> reportModels;
    RechargeReportAdapter rechargeReportAdapter;

    ProgressDialog dialog;

    String username,password;

    AlertDialog alertDialog;

    RadioGroup radiogroup_group;

    TextView textview_icdate,radiobutton_today;

    ImageView imageView_storage;

    RelativeLayout rl_from,rl_to,rl_status;
    TextView tv_from,tv_to,tv_status;
    EditText ed_number;
    Button bt_search;

    private int mYear, mMonth, mDay;

    String fromdate="",todate="";
    String searchkey="All";

    String number="";

    PopupMenu popupMenu;
    String status_id="";

    ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rl_from=findViewById(R.id.rl_from);
        rl_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RechargeReport.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fromdate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tv_from.setText(fromdate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        rl_to=findViewById(R.id.rl_to);
        rl_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RechargeReport.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                todate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tv_to.setText(todate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        rl_status=findViewById(R.id.rl_status);
        popupMenu=new PopupMenu(RechargeReport.this,rl_status);
        rl_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopupMenu popupMenu=new PopupMenu(Reports.this,rl_status);

//                popupMenu.getMenu().add("All");
//                popupMenu.getMenu().add("Success");
//                popupMenu.getMenu().add("Failure");
//                popupMenu.getMenu().add("Pending");
//                popupMenu.getMenu().add("Dispute");
//                popupMenu.getMenu().add("Profit");
//                popupMenu.getMenu().add("Debit");


                if (popupMenu.getMenu().size()!=0) {
                    popupMenu.show();
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        searchkey=menuItem.getTitle().toString();

                        status_id=menuItem.getItemId()+"";

                        tv_status.setText(searchkey);

                        return true;
                    }
                });
            }
        });

        tv_from=findViewById(R.id.tv_from);
        tv_to=findViewById(R.id.tv_to);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        fromdate = formatter.format(new Date(System.currentTimeMillis()));
        todate = formatter.format(new Date(System.currentTimeMillis()));

        tv_from.setText(fromdate);
        tv_to.setText(todate);

        tv_status=findViewById(R.id.tv_status);
        ed_number=findViewById(R.id.ed_number);


        recyclerview_reports =findViewById(R.id.recyclerview_report);

        recyclerview_reports.setLayoutManager(new LinearLayoutManager(RechargeReport.this));
        reportModels =new ArrayList<>();
        rechargeReportAdapter =new RechargeReportAdapter(RechargeReport.this, reportModels);
        recyclerview_reports.setAdapter(rechargeReportAdapter);

        reportViewModel =new ViewModelProvider(this).get(ReportViewModel.class);
        reportViewModel.getReportList().observe(this, new Observer<List<ReportModel>>() {
            @Override
            public void onChanged(List<ReportModel> reportModel) {
                if (reportModel!=null)
                {
                    reportModels=reportModel;
                    rechargeReportAdapter.mUpdateReports(reportModels);

                }
            }
        });

        reportViewModel.mCallApi(SharePrefManager.getInstance(RechargeReport.this).mGetApiToken(),fromdate,todate,status_id,number);

        bt_search=findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromdate.equals("")){

                    Toast.makeText(RechargeReport.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }

                else if (todate.equals("")){

                    Toast.makeText(RechargeReport.this, "Please Select To Date", Toast.LENGTH_SHORT).show();

                }
                else {
                    number=ed_number.getText().toString();
                    reportViewModel =new ViewModelProvider(RechargeReport.this).get(ReportViewModel.class);
                    reportViewModel.getReportList().observe(RechargeReport.this, new Observer<List<ReportModel>>() {
                        @Override
                        public void onChanged(List<ReportModel> reportModel) {
                            if (reportModel!=null)
                            {
                                reportModels=reportModel;
                                Toast.makeText(RechargeReport.this, reportModels.get(0).getName(), Toast.LENGTH_SHORT).show();
                                rechargeReportAdapter.mUpdateReports(reportModels);

                            }
                        }
                    });

                    reportViewModel.mCallApi(SharePrefManager.getInstance(RechargeReport.this).mGetApiToken(),fromdate,todate,status_id,number);


                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}