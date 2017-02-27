package com.example.admin.serversocket;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DataDisplay {
    public String message;
    public String ch = "null";
    public int cs;
    public ImageButton button;
    public TextView textView;
    TextView serverMessage;
    Thread m_objThread;
    ServerSocket m_server;
    String m_strMessage;
    DataDisplay m_dataDisplay;
    Object m_connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.TVresult);
        button = (ImageButton) findViewById(R.id.imageButton);
        serverMessage=(TextView)findViewById(R.id.textView);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptSpeechInput();
                    }
                }
        );
    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "Sorry your device does not support speech language", Toast.LENGTH_LONG).show();

        }

    }
   // public void connect(View view)
    //{
       // MyServer server=new MyServer();
       // server.setEventListener(this);
       // server.startListening();

    //}
    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);
        switch (request_code) {
            case 100:
                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Log.d("MAINACTIVITY", result.get(0));
                    message = result.get(0);
                    textView.setText(message);
                    check(message);
                }
                break;
        }
    }
    private void check(String message) {
        cs = 0;
        if (message.equalsIgnoreCase("move forward") || message.equalsIgnoreCase("forward")) {
            ch = "f";
            cs = 1;
            Log.d("MAINACTIVITY", ch);
        } else if (message.equalsIgnoreCase("move backward") || message.equalsIgnoreCase("backward")
                || message.equalsIgnoreCase("move backwards") || message.equalsIgnoreCase("awkward")
                || message.equalsIgnoreCase("backwards")) {
            ch = "b";
            cs = 1;
            Log.d("MAINACTIVITY", ch);
        } else if (message.equalsIgnoreCase("move right") || message.equalsIgnoreCase("right")
                || message.equalsIgnoreCase("light") || message.equalsIgnoreCase("turn right")) {
            ch = "r";
            cs = 1;
            Log.d("MAINACTIVITY", ch);
        } else if (message.equalsIgnoreCase("move left") || message.equalsIgnoreCase("left")
                || message.equalsIgnoreCase("turn left")) {
            ch = "l";
            cs = 1;
            Log.d("MAINACTIVITY", ch);
        } else if (message.equalsIgnoreCase("stop")) {
            ch = "t";
            cs = 1;
            Log.d("MAINACTIVITY", ch);
        }
        m_objThread=new Thread(new Runnable()
        {
            public void run()
            {
                try {
                    m_server=new ServerSocket(1880);
                    Socket connectedSocket=m_server.accept();
                    Message clientmessage= Message.obtain();
                    //ObjectInputStream ois= new ObjectInputStream(connectedSocket.getInputStream());
                    //String strMessage=(String)ois.readObject();
                    //clientmessage.obj=strMessage;
                   // mHandler.sendMessage(clientmessage);
                    ObjectOutputStream oos=new ObjectOutputStream(connectedSocket.getOutputStream());
                    oos.writeObject(ch);
                    //ois.close();
                    oos.close();
                    m_server.close();

                }
                catch(Exception e)
                {
                    Message msg3=Message.obtain();
                    msg3.obj=e.getMessage();
                    mHandler.sendMessage(msg3);
                }
            }

        });
        m_objThread.start();
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message status){
            m_dataDisplay.Display(status.obj.toString());
        }
    };
    public void Display(String message)
    {
        serverMessage.setText(""+message);

    }
}
