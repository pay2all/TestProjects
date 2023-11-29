package com.demo.apppay2all.DesputDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.apppay2all.R;

public class DisputeTypes extends AppCompatActivity {

    LinearLayout ll_pending,ll_solved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_types);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ll_pending=findViewById(R.id.ll_pending);
        ll_solved=findViewById(R.id.ll_solved);



        ll_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisputeTypes.this, DisputeActivity.class);
                intent.putExtra("type","pending");
                startActivity(intent);
            }
        });


        ll_solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisputeTypes.this, DisputeActivity.class);
                intent.putExtra("type","solved");
                startActivity(intent);
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