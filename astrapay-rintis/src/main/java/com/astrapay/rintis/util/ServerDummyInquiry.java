package com.astrapay.rintis.util;

import org.jpos.iso.ISOException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
@SuppressWarnings("Duplicates")
public class ServerDummyInquiry {
    private static final Integer PORT = 11112;
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
            Map<Integer, String> iso8583InquiryResponse = new LinkedHashMap<Integer, String>();
            Map<String, String> objectResponse = DataElementConverter.converterIsoToJson(text);
            Integer lengthBit2 = objectResponse.get("Bit2").length();
            String formatLengthBit2 = String.format("%2s", String.valueOf(lengthBit2)).replace(' ', '0');

            Integer lengthBit32 = objectResponse.get("Bit32").length();
            String formatLengthBit32 = String.format("%2s", String.valueOf(lengthBit32)).replace(' ', '0');

            Integer lengthBit33 = objectResponse.get("Bit33").length();
            String formatLengthBit33 = String.format("%2s", String.valueOf(lengthBit33)).replace(' ', '0');

            Integer lengthBit48 = objectResponse.get("Bit48").length();
            String formatLengthBit48 = String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');

            Integer lengthBit100 = objectResponse.get("Bit100").length();
            String formatLengthBit100 = String.format("%2s", String.valueOf(lengthBit100)).replace(' ', '0');
//
            Integer lengthBit102 = objectResponse.get("Bit102").length();
            String formatLengthBit102 = String.format("%2s", String.valueOf(lengthBit102)).replace(' ', '0');

            iso8583InquiryResponse.put(2, (formatLengthBit2 + objectResponse.get("Bit2")));
            iso8583InquiryResponse.put(3, objectResponse.get("Bit3"));
            iso8583InquiryResponse.put(4, objectResponse.get("Bit4"));
            iso8583InquiryResponse.put(7, "0710031205");
            iso8583InquiryResponse.put(11, "000519");

            iso8583InquiryResponse.put(12, objectResponse.get("Bit12"));
            iso8583InquiryResponse.put(13, objectResponse.get("Bit13"));
            iso8583InquiryResponse.put(15, objectResponse.get("Bit15"));
            iso8583InquiryResponse.put(17, objectResponse.get("Bit17"));
            iso8583InquiryResponse.put(18, objectResponse.get("Bit18"));

            iso8583InquiryResponse.put(22, objectResponse.get("Bit22"));
            if (objectResponse.get("Bit28") != null) {
                iso8583InquiryResponse.put(28, objectResponse.get("Bit28"));
            }
            iso8583InquiryResponse.put(32, (formatLengthBit32 + objectResponse.get("Bit32")));
            iso8583InquiryResponse.put(33, (formatLengthBit33 + objectResponse.get("Bit33")));
            iso8583InquiryResponse.put(37, objectResponse.get("Bit37"));

            iso8583InquiryResponse.put(38, objectResponse.get("Bit38"));
            iso8583InquiryResponse.put(39, "00");
            iso8583InquiryResponse.put(41, objectResponse.get("Bit41"));
            iso8583InquiryResponse.put(42, objectResponse.get("Bit42"));
            iso8583InquiryResponse.put(48, (formatLengthBit48 + objectResponse.get("Bit48")));
            iso8583InquiryResponse.put(49, objectResponse.get("Bit49"));
//            iso8583InquiryResponse.put(57, "0522648011893600153100290563402157338680866228710303UMI");
            iso8583InquiryResponse.put(57, "9362648011893600153100290563402157338680866228710303UMI27480118936000771234567890021500000000000077 0303UKI28480118936000140987654321021500000000000014 0303UPO29480118936002221111111111021500000222       0303OLO30480118936003331212121212021500000000131313 0303TOL31480118936004269988774455021593600426       0303UMI32480118936004448877887788021593600444       0303OSO33480118936005551111111111021593600555       0303DOI34480118936000081112223334021593600811223322 0303WOW354801189360000922112211120215936009         0303LOI36480118936000881234567890021593600088       0303UME37480118936009991235512421021593600999       0303POL38480118936009908877996654021593600990000111 0303SUP39480118936009801234121251021593600980444444 0303UME40480118936009701231231231021593600970       0303KRI41480118936009603123441211021593600960       0303WOW42480118936009501234551212021593600950       0303PUS43480118936009401111222333021593600940       0303HAI");
            iso8583InquiryResponse.put(100, (formatLengthBit100 + objectResponse.get("Bit100")));
            iso8583InquiryResponse.put(102, (formatLengthBit102 + objectResponse.get("Bit102")));


            String isoMessages = DataElementConverter.requestIso("0210", iso8583InquiryResponse);
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
