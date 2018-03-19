package edu.bhcc.sebastianv.fieldreservationapp;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Sebastian Vasco on 11/2/2017.
 * Simple app allows user to make a reservation. The reservation is for a soccer field.  It asks for
 * reservation name, time & date, and allows you to send reservation confirmation text/call.
 *
 */

public class ReservationFragment extends Fragment {

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;


    public ReservationFragment() {
    }

    private ImageButton mDate;
    private ImageButton mTime;
    private Button mTextReservation;
    private Button mCallButton;
    private EditText mNameOnReservation;
    private EditText mPhoneNumber;
    private Reservation mReservation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation, container, false);

        mNameOnReservation=(EditText)v.findViewById(R.id.name);
        mNameOnReservation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //When a name is added to reservation, enable date and time button
                if(charSequence!=null) {
                    mReservation = new Reservation(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhoneNumber=(EditText)v.findViewById(R.id.phoneNumber);
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //When a name is added to reservation, enable date and time button,call and reservation button as well
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence!=null) {

                    mDate.setVisibility(View.VISIBLE);
                    mTime.setVisibility(View.VISIBLE);

                    mTextReservation.setVisibility(View.VISIBLE);
                    mCallButton.setVisibility(View.VISIBLE);
                    mReservation.setPhoneNumber(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDate=(ImageButton) v.findViewById(R.id.date_button);
        mDate.setVisibility(View.INVISIBLE);

        //pick date
        mDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mReservation.getDateStamp());
                dialog.setTargetFragment(ReservationFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });


        //pick time
        mTime=(ImageButton) v.findViewById(R.id.time_button);
        mTime.setVisibility(View.INVISIBLE);
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mReservation.getTimeStamp());
                dialog.setTargetFragment(ReservationFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        //text reservation information
        mTextReservation=(Button)v.findViewById(R.id.reservation_report);
        mTextReservation.setVisibility(View.INVISIBLE);
        mTextReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String phoneNumber = mReservation.getPhoneNumber();
                //This allows user to enter number and it will create a text to that specified number
                Uri uri = Uri.parse("smsto:" + mReservation.getPhoneNumber());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", getReservationInfo());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.reservation_text_subject));
                startActivity(intent);


                mNameOnReservation.setText("");
                mPhoneNumber.setText("");
            }
        });

        //call button
        mCallButton = (Button) v.findViewById(R.id.reservation_call);
        mCallButton.setVisibility(View.INVISIBLE);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String select = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                String[] number = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                String[] selectParameter = {Long.toString(mReservation.getContact())};

                Cursor cursor = getActivity().getContentResolver().query(contentUri, number,
                        select, selectParameter, null);

                if (cursor != null && cursor.getCount() > 0) {
                    try {
                        cursor.moveToFirst();
                        String numberPos = cursor.getString(0);

                        Uri phonenum = Uri.parse("tel: " + numberPos);
                        Intent intent = new Intent(Intent.ACTION_DIAL, phonenum);

                        startActivity(intent); //calling number above
                    } finally {
                        cursor.close();
                    }
                }*/

                Uri phonenum = Uri.parse("tel: " + mReservation.getPhoneNumber()); //grab phone number
                Intent intent = new Intent(Intent.ACTION_DIAL, phonenum); //pass intent to dial

                startActivity(intent); //Start calling activity
            }
        });

        return v;
    }

    //grab information from date/time dialog
    @Override
    public void onActivityResult(int requesCode, int resultCode,Intent data){
        if(requesCode==REQUEST_DATE){
            Date date=(Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mReservation.setDate(date);

        }else if(requesCode==REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            mReservation.setTime(time);

            //Toast.makeText(getActivity(), getReservationInfo(), Toast.LENGTH_LONG).show();
        }
    }


    //string information to be texted
    private String getReservationInfo() {
        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat,
                mReservation.getDateStamp()).toString();

        String timeString=mReservation.getTime();
        String report=getString(R.string.reservation_report, dateString,timeString,mReservation.getName());

        return report;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


