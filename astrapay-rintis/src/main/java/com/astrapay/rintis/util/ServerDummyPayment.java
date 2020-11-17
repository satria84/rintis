package com.astrapay.rintis.util;

import org.jpos.iso.ISOException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
@SuppressWarnings("Duplicates")
public class ServerDummyPayment {
    private static final Integer PORT = 11111;
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String text;
            text = reader.readLine();
            System.out.println("request : " + text);
            Map<Integer, String> iso8583PaymentResponse = new LinkedHashMap<Integer, String>();
            Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(text);
            Integer lengthBit2 = objectResponse.get("Bit2").length();
            String formatLengthBit2  =  String.format("%2s", String.valueOf(lengthBit2)).replace(' ', '0');
            Integer lengthBit32 = objectResponse.get("Bit32").length();
            String formatLengthBit32  =  String.format("%2s", String.valueOf(lengthBit32)).replace(' ', '0');
            Integer lengthBit48 = objectResponse.get("Bit48").length();
            String formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');
            Integer lengthBit100 = objectResponse.get("Bit100").length();
            String formatLengthBit100  =  String.format("%2s", String.valueOf(lengthBit100)).replace(' ', '0');
            Integer lengthBit102 = objectResponse.get("Bit102").length();
            String formatLengthBit102  =  String.format("%2s", String.valueOf(lengthBit102)).replace(' ', '0');

            iso8583PaymentResponse.put(2, (formatLengthBit2 + objectResponse.get("Bit2")));
            iso8583PaymentResponse.put(3, objectResponse.get("Bit3"));
            iso8583PaymentResponse.put(4, objectResponse.get("Bit4"));
            iso8583PaymentResponse.put(7, "0710031205");
            iso8583PaymentResponse.put(11, "000519");

            iso8583PaymentResponse.put(12, objectResponse.get("Bit12"));
            iso8583PaymentResponse.put(13, objectResponse.get("Bit13"));
            iso8583PaymentResponse.put(15, "0710");
            iso8583PaymentResponse.put(17, objectResponse.get("Bit17"));
            iso8583PaymentResponse.put(18, objectResponse.get("Bit18"));

            iso8583PaymentResponse.put(22, objectResponse.get("Bit22"));
            if(objectResponse.get("Bit28") !=null){
                iso8583PaymentResponse.put(28, objectResponse.get("Bit28"));
            }
            iso8583PaymentResponse.put(32, (formatLengthBit32 + objectResponse.get("Bit32")));
            iso8583PaymentResponse.put(33, "06360002");
            iso8583PaymentResponse.put(37, objectResponse.get("Bit37"));

            iso8583PaymentResponse.put(38, objectResponse.get("Bit38"));
            iso8583PaymentResponse.put(39, "00");
            iso8583PaymentResponse.put(41, objectResponse.get("Bit41"));
            iso8583PaymentResponse.put(42, objectResponse.get("Bit42"));
            iso8583PaymentResponse.put(48, (formatLengthBit48 + objectResponse.get("Bit48")));

            iso8583PaymentResponse.put(49, objectResponse.get("Bit49"));
            iso8583PaymentResponse.put(100, (formatLengthBit100 + objectResponse.get("Bit100")));
            iso8583PaymentResponse.put(102, (formatLengthBit102 + objectResponse.get("Bit102")));
            iso8583PaymentResponse.put(123, "02000001234560000123456");

            String isoMessages = DataElementConverter.requestIso("0210", iso8583PaymentResponse);
            System.out.println("payment response : " + isoMessages);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(isoMessages);
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
