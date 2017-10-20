package br.pucpcaldas.inf.playerjava;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ScrollingActivity extends AppCompatActivity {

    Conexao c = new Conexao();
    Integer progressPercent;
    boolean configurado = false;
    boolean conectado = false;
    private Button[] btn = new Button[4];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3};
    String resposta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(configurado) {
                    conectar(view);
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                    fab.setVisibility(View.INVISIBLE);

                    FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);

                    fab2.setVisibility(View.VISIBLE);

                    EditText actionLabel = (EditText) findViewById(R.id.actionLabel);
                    actionLabel.setText("Pronto para enviar tags");
                    actionLabel.setTextColor(0xff99cc00);

                    Snackbar.make(view, "Conectado a: " + c.address, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    conectado = true;
                }else{
                    Snackbar.make(view, "Configure sua conexão! ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    conectado=false;
                }
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.Disconnect();
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                fab.setVisibility(View.VISIBLE);

                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);

                fab2.setVisibility(View.INVISIBLE);

                EditText actionLabel = (EditText) findViewById(R.id.actionLabel);
                actionLabel.setText("Conecte-se a um servidor!");
                actionLabel.setTextColor(0xffff0000);

                Snackbar.make(view, "Desconectado de: "+c.address   , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                conectado = false;
            }
        });

        FloatingActionButton fabConf = (FloatingActionButton) findViewById(R.id.fabConf);
        fabConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View settings = findViewById(R.id.settings_layout);
                settings.setVisibility(View.VISIBLE);
                View content = findViewById(R.id.content);
                content.setVisibility(View.INVISIBLE);
            }
        });

        FloatingActionButton fabConf2 = (FloatingActionButton) findViewById(R.id.fabConf2);
        fabConf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View settings = findViewById(R.id.settings_layout);
                settings.setVisibility(View.VISIBLE);
                View content = findViewById(R.id.content);
                content.setVisibility(View.INVISIBLE);
            }
        });

        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setBackgroundColor(0xff99cc00);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText caixaTexto = (EditText) findViewById(R.id.email_box);
                if(!(caixaTexto.getText().toString().matches(""))){
                    //Atualiza os dados de c
                    caixaTexto = (EditText) findViewById(R.id.server_ip_box);
                    c.address = caixaTexto.getText().toString();
                    caixaTexto = (EditText) findViewById(R.id.server_port_box);
                    c.port = Integer.parseInt(caixaTexto.getText().toString());
                    caixaTexto = (EditText) findViewById(R.id.email_box);
                    c.username = caixaTexto.getText().toString();

                    //Atualiza botôes e telas
                    View settings = findViewById(R.id.settings_layout);
                    settings.setVisibility(View.INVISIBLE);
                    View content = findViewById(R.id.content);
                    content.setVisibility(View.VISIBLE);

                    FloatingActionButton fabConf = (FloatingActionButton) findViewById(R.id.fabConf);
                    fabConf.setVisibility(View.INVISIBLE);

                    FloatingActionButton fabConf2 = (FloatingActionButton) findViewById(R.id.fabConf2);

                    fabConf2.setVisibility(View.VISIBLE);

                    EditText actionLabel = (EditText) findViewById(R.id.actionLabel);
                    actionLabel.setText("Conecte-se a um servidor!");

                    Snackbar.make(view, "Configurado com Sucesso!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    configurado = true;
                }else{
                    FloatingActionButton fabConf = (FloatingActionButton) findViewById(R.id.fabConf);
                    fabConf.setVisibility(View.VISIBLE);

                    FloatingActionButton fabConf2 = (FloatingActionButton) findViewById(R.id.fabConf2);

                    fabConf2.setVisibility(View.INVISIBLE);
                    Snackbar.make(view, "Preencha os campos!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    configurado=false;
                }
            }
        });

        EditText mEdit = (EditText) findViewById(R.id.actionLabel);
        mEdit.setEnabled(false);


        Button tagButton = (Button) findViewById(R.id.tag_button);
        tagButton.setBackgroundColor(0xff99cc00);

        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conectado){
                    enviarTag(view);
                }else{
                    Snackbar.make(view, "Conecte-se a um servidor!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        //Toggle Buttons
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.btn0 :
                            setFocus(btn_unfocus, btn[0]);
                            resposta = "A";
                            break;

                        case R.id.btn1 :
                            setFocus(btn_unfocus, btn[1]);
                            resposta = "B";
                            break;

                        case R.id.btn2 :
                            setFocus(btn_unfocus, btn[2]);
                            resposta = "C";
                            break;

                        case R.id.btn3 :
                            setFocus(btn_unfocus, btn[3]);
                            resposta = "D";
                            break;
                    }

                }
            });
        }

        btn_unfocus = btn[0];


        // Open Question Page
        FloatingActionButton questionButton = (FloatingActionButton) findViewById(R.id.question_button);
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View question_page = findViewById(R.id.question_page);
                question_page.setVisibility(View.VISIBLE);
                View content = findViewById(R.id.content);
                content.setVisibility(View.INVISIBLE);
            }
        });

        // Send Answer
        Button answerSubmit = (Button) findViewById(R.id.enviarResposta);
        answerSubmit.setBackgroundColor(0xff99cc00);
        answerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarResposta(view);
            }
        });



    }


    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }

    private void clearFocus(Button btn_unfocus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        this.btn_unfocus = btn[0];
    }


    public void conectar(View view){
        if (c.isConnected == false)
        {
            new ConnectToSocket().execute(c);
            c.ListenThread();

        } else if (c.isConnected == true)
        {
            //ta_chat.append("You are already connected. \n");
            //System.out.println("Já está conectado");
            Snackbar.make(view, "Já está conectado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void enviarTag(View view) {
        EditText caixaTexto = (EditText) findViewById(R.id.tag_text);
        if(caixaTexto.getText().toString().matches("")){
            Snackbar.make(view, "Preencha o campo Tag!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            caixaTexto.requestFocus();
        } else {
            try {
                c.writer.println(c.username + ":" + caixaTexto.getText().toString()+":"+"Chat");
                c.writer.flush(); // flushes the buffer
                Snackbar.make(view, "Tag enviada com sucesso!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } catch (Exception ex) {
                Snackbar.make(view, "Tag não enviada...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            caixaTexto.setText("");
            caixaTexto.requestFocus();
        }

        caixaTexto.setText("");
        caixaTexto.requestFocus();
    }

    public void enviarResposta(View view) {
        EditText caixaTexto = (EditText) findViewById(R.id.tag_text);
        if(resposta.matches("")){
            Snackbar.make(view, "Selecione uma resposta", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            caixaTexto.requestFocus();
        } else {
            try {
                c.writer.println(c.username + ":" + resposta +":"+"Resposta");
                c.writer.flush(); // flushes the buffer
                Snackbar.make(view, "Resposta enviada com sucesso!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } catch (Exception ex) {
                Snackbar.make(view, "Resposta não enviada...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            View question_page = findViewById(R.id.question_page);
            question_page.setVisibility(View.INVISIBLE);
            View content = findViewById(R.id.content);
            content.setVisibility(View.VISIBLE);

            resposta = "";
            clearFocus(btn_unfocus);
        }

        resposta = "";
        clearFocus(btn_unfocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    private class ConnectToSocket extends AsyncTask<Conexao, Integer, Long> {

        @Override
        protected Long doInBackground(Conexao... params) {
            try{
                params[0].sock = new Socket(params[0].address, params[0].port);
                InputStreamReader streamreader = new InputStreamReader(params[0].sock.getInputStream());
                params[0].reader = new BufferedReader(streamreader);
                params[0].writer = new PrintWriter(params[0].sock.getOutputStream());
                params[0].writer.println(c.username + ":has connected.:Connect");
                params[0].writer.flush();
                params[0].isConnected = true;

                //DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                //dout.writeUTF("sua mensagem");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            System.out.println(result);
        }
    }



}
