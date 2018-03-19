package edu.bhcc.sebastianv.fieldreservationapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

public class ReservationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.reservation_main);

        if(fragment==null){
            fragment=new ReservationFragment();
            fm.beginTransaction().add(R.id.reservation_main,fragment).commit();
        }
    }
}
