package com.astrapay.rintis.config;

import com.astrapay.rintis.service.MessageService;
import com.astrapay.rintis.util.DataElementConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    int port = 2505;

    MessageService messageService;

    public void start() {
        try {
            System.out.println("Starting server on port " + port);
            serverSocket = new ServerSocket(port);
            while (true) {
                new EchoClientHandler(serverSocket.accept(), messageService).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(GreetServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(GreetServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class EchoClientHandler extends Thread {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private MessageService messageService;

        public EchoClientHandler(Socket socket, MessageService messageService) {
            this.clientSocket = socket;
            this.messageService = messageService;
            System.out.println("REQUEST FROM RINTIS" + socket.getInetAddress().getHostAddress());
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                String responseContent = "";
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("INPUTLINE : "+inputLine);
                    Map<String, String> objectResponse = DataElementConverter.converterIsoToJson(inputLine);
                    if (objectResponse.get("MTI").equals("0800")) {
                        if(objectResponse.get("Bit48") != null){
                            responseContent = logonResponse(objectResponse); // untuk test logon local/dummy
                        } else if(objectResponse.get("Bit15") != null){
                            // responseContent = cutOverResponse(objectResponse); // cutover
                        } else {
                            responseContent = echoTestResponse(objectResponse); // echo test response
                        }
                    }
                    
                    out.println(responseContent);
                    System.out.println("RESPONSE FROM ASTRAPAY : " +responseContent);

                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private String echoTestResponse(Map<String, String> objectResponse) {
            String responseContent = "";
            try {
                Map<Integer, String> iso8583EchoTestResponse = new LinkedHashMap<Integer, String>();
                iso8583EchoTestResponse.put(7, objectResponse.get("Bit7"));
                iso8583EchoTestResponse.put(11, objectResponse.get("Bit11"));
                iso8583EchoTestResponse.put(39, "00");
                iso8583EchoTestResponse.put(70, objectResponse.get("Bit70"));
    
                responseContent = DataElementConverter.requestIso("0810", iso8583EchoTestResponse);
            } catch (Exception e) {
                Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, e);
            }
            return responseContent;
        }

        private String logonResponse(Map<String, String> objectResponse) {
            String responseContent = "";
            try {
                Map<Integer, String> iso8583LogonResponse = new LinkedHashMap<Integer, String>();
                Integer lengthBit48 = objectResponse.get("Bit48").length();
                String formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');
                iso8583LogonResponse.put(7, objectResponse.get("Bit7"));
                iso8583LogonResponse.put(11, objectResponse.get("Bit11"));
                iso8583LogonResponse.put(39, "00");
                iso8583LogonResponse.put(48, (formatLengthBit48 + objectResponse.get("Bit48")));
                iso8583LogonResponse.put(70, objectResponse.get("Bit70"));
    
                responseContent = DataElementConverter.requestIso("0810", iso8583LogonResponse);
            } catch (Exception e) {
                Logger.getLogger(EchoClientHandler.class.getName()).log(Level.SEVERE, null, e);
            }
            return responseContent;
        }
    }

}

