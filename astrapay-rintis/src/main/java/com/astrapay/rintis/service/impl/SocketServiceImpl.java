package com.astrapay.rintis.service.impl;

import com.astrapay.rintis.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

@Service
public class SocketServiceImpl implements SocketService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${socket-server.address}")
    private String address;
    @Value("${socket-server.port}")
    private Integer port;
    private final RefundServiceImpl service = new RefundServiceImpl();

    public String sendMessage(String isoMessage) {
        String response = "";
        try (Socket clientSocket = new Socket(address, port)) {

            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(isoMessage);

            InputStream input = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            System.out.println(data);
            response = data.toString();
            return response;

        } catch (UnknownHostException ex) {
            log.error("Server not found : " + ex.getMessage(),ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("I/O error : " + ex.getMessage(),ex);
            ex.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings("Duplicates")
    public String sendMessagePayment(String isoMessage) {
        String response = "";
        try (Socket clientSocket = new Socket(address, 11111)) {
            clientSocket.setSoTimeout(60*1000);
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(isoMessage);

            InputStream input = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            System.out.println(data);
            response = data.toString();
            return response;

        } catch (UnknownHostException ex) {
            log.error("Server not found  : " + ex.getMessage(),ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("I/O error  : " + ex.getMessage(),ex);
            if (ex instanceof SocketTimeoutException) {
                log.info("catch timeout");
                response = "Timeout";
                return response;
            }
        }
        return response;
    }

    @SuppressWarnings("Duplicates")
    public String sendMessageInquiry(String isoMessage) {
        String response = "";
        try (Socket clientSocket = new Socket(address, 11112)) {

            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(isoMessage);

            InputStream input = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            System.out.println(data);
            response = data.toString();
            return response;

        } catch (UnknownHostException ex) {
            log.error("Server not found  : " + ex.getMessage(),ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("I/O error   : " + ex.getMessage(),ex);
            ex.printStackTrace();
        }
        return response;
    }

    //Alternative
    public String sendMessage2(String message) {
        String serverResponse = "";
        short messageLength = (short) (message.length());
        log.info("iso message length : " + messageLength);
        try {
            // send iso message
            Socket connection = new Socket(address, port);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeShort(messageLength);
            out.writeBytes(message);
            out.flush();
            log.info("iso message send");

            // receive response
            DataInputStream in = new DataInputStream(connection.getInputStream());
            short respLength = in.readShort();
            System.out.println("response length = " + respLength);
            byte[] responseData = new byte[respLength];
            in.readFully(responseData);
            log.info("Response = " + new String(responseData));
            connection.close();
            serverResponse = new String(responseData);
            return serverResponse;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return serverResponse;
    }

    @SuppressWarnings("Duplicates")
    public String sendMessageCheckStatus(String isoMessage) {
        String response = "";
        try (Socket clientSocket = new Socket(address, 11113)) {
        	clientSocket.setSoTimeout(60*1000);
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(isoMessage);

            InputStream input = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            System.out.println(data);
            response = data.toString();
            return response;

        } catch (UnknownHostException ex) {
            log.error("Server not found: " + ex.getMessage(),ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("I/O error: " + ex.getMessage(),ex);
            if (ex instanceof SocketTimeoutException) {
                log.info("catch timeout");
                response = "Timeout";
                return response;
            }
        }
        return response;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void sendMessageWithoutClose(String isoMessage) throws IOException {
        String response = "";
        boolean connected = true;
        boolean closed = false;
        try (Socket clientSocket = new Socket("whois.internic.net", 43)) {

            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(isoMessage);

            InputStream input = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            System.out.println(data);
            response = data.toString();
            connected = clientSocket.isConnected();
            System.out.println(connected);
            closed = clientSocket.isClosed();
            System.out.println(closed);
//            input.close();
//            return response;

        } catch (UnknownHostException ex) {
            log.error("Server not found: " + ex.getMessage(),ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("I/O error: " + ex.getMessage(),ex);
            ex.printStackTrace();
        }
        System.out.println(connected);
        System.out.println(closed);
//        return response;
    }

}

