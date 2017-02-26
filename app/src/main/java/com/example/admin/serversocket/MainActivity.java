package com.example.admin.serversocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DataDisplay{
    TextView serverMessage;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverMessage=(TextView)findViewById(R.id.textView);
    }
    public void connect(View view)
    {
        MyServer server=new MyServer();
        server.setEventListener(this);
        server.startListening();

    }
    public void Display(String message)
    {
        serverMessage.setText(""+message);

    }

}
