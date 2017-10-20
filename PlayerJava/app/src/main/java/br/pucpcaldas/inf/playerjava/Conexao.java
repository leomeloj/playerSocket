package br.pucpcaldas.inf.playerjava;

import android.view.View;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Leonardo on 10/16/2017.
 */


public class Conexao {
    String username, address = "192.168.0.106";
    ArrayList<String> users = new ArrayList();
    int port;
    Boolean isConnected = false;

    Socket sock;
    BufferedReader reader;
    PrintWriter writer;

    //--------------------------//

    public void ListenThread()
    {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    //--------------------------//

    public void userAdd(String data)
    {
        users.add(data);
    }

    public void userRemove(String data)
    {
        System.out.println(data + " is now offline.\n");
    }

    public void writeUsers()
    {
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);
        for (String token:tempList)
        {
            //users.append(token + "\n");
        }
    }

    public void sendDisconnect()
    {
        String bye = (username + ": :Disconnect");
        try
        {
            writer.println(bye);
            writer.flush();
        } catch (Exception e)
        {
            System.out.printf(e.getMessage());
            //ta_chat.append("Could not send Disconnect message.\n");
        }
    }

    public void Disconnect()
    {
        try
        {
            System.out.println("Disconnected.\n");
            sock.close();
        } catch(Exception ex) {
            System.out.println("Failed to disconnect. \n");
        }
        isConnected = false;
        //tf_username.setEditable(true);
    }

    public Conexao(){

    }

    public class IncomingReader implements Runnable
    {
        @Override
        public void run()
        {
            //data[0] = user; data[1]=mensagem;//data[2] = status
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

            try
            {
                while ((stream = reader.readLine()) != null)
                {
                    data = stream.split(":");

                    if (data[2].equals(chat))
                    {
                        //ta_chat.append(data[0] + ": " + data[1] + "\n");
                        //ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                    }
                    else if (data[2].equals(connect))
                    {
                        //ta_chat.removeAll();
                        userAdd(data[0]);
                    }
                    else if (data[2].equals(disconnect))
                    {
                        userRemove(data[0]);
                    }
                    else if (data[2].equals(done))
                    {
                        //users.setText("");
                        writeUsers();
                        users.clear();
                    }
                }
            }catch(Exception ex) { }
        }
    }



    private void disconectar(View view) {
        sendDisconnect();
        Disconnect();
    }

}
