package com.kidane.yosief.SMSDemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/***
 * @author Kidane Yosief
 * @version 11/12/2015
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    EditText mPhone, mSms;;
    Button mSend;
    String mMessage, mPhoneNumber, SENT, DELIVERED;
    MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intentFilterr  for our received messages
        mPhone = (EditText)findViewById(R.id.num);
        mSms = (EditText)findViewById(R.id.sms);
        mSend = (Button)findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                   /* case R.id.action_exit:
                        finish(); //Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.show:
                        // Toast.makeText(getApplicationContext(),"Send Selected",Toast.LENGTH_SHORT).show();
                        return true;*/
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

        protected void sendMessage() {
            SENT = "Message Sent";
            DELIVERED = "Message Delivered";
            mMessage = mSms.getText().toString();
            mPhoneNumber = mPhone.getText().toString();

            PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

            //registering broadcast reciever for the sent message
        registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()){
                        case RESULT_OK:
                            Toast.makeText(getApplicationContext(),"SMS Sent",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getApplicationContext(),"Generic Failure",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getApplicationContext(),"No Service",Toast.LENGTH_LONG).show();
                            break;

                    }
                }
            }, new IntentFilter(SENT));

//registering broadcast reciever for the delivered message
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()){
                        case AppCompatActivity.RESULT_OK:
                            Toast.makeText(getApplicationContext(),"SMS Delivered",Toast.LENGTH_LONG).show();
                            break;
                        case AppCompatActivity.RESULT_CANCELED:
                            Toast.makeText(getApplicationContext(),"SMS not Delivered",Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));


            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mPhoneNumber,null,mMessage,sentPI,deliveredPI);
            Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_LONG).show();

        }

    //receiving a message from the broadcastReceiver

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            TextView  textView = (TextView) findViewById(R.id.recvd);
            textView.setText(arg1.getExtras().getString("message"));

        }

    }

    @Override
    protected void onResume() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("kiduney");
        registerReceiver(myReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }
    // @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.money:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
