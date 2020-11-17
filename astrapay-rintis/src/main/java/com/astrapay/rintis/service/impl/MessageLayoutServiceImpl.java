package com.astrapay.rintis.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.astrapay.rintis.config.GreetClient;
import com.astrapay.rintis.domain.request.CutOverRequest;
import com.astrapay.rintis.domain.request.EchoTestRequest;
import com.astrapay.rintis.domain.request.LogoffRequest;
import com.astrapay.rintis.domain.request.LogonRequest;
import com.astrapay.rintis.domain.response.CutOverResponse;
import com.astrapay.rintis.domain.response.EchoTestResponse;
import com.astrapay.rintis.domain.response.LogoffResponse;
import com.astrapay.rintis.domain.response.LogonResponse;
import com.astrapay.rintis.service.MessageClientService;
import com.astrapay.rintis.service.MessageLayoutService;
import com.astrapay.rintis.util.DataElementConverter;
import com.astrapay.rintis.util.IsoUtil;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageLayoutServiceImpl implements MessageLayoutService{
    
    private static final Logger log = LoggerFactory.getLogger(MessageLayoutServiceImpl.class);
    @Value("${message-type.request-logon-logoff}")
    private String requestType;
    @Value("${network-management.data}")
    private String valueBit48;
    @Value("${network-management.information-code.logon}")
    private String valueBit70Logon;
    @Value("${network-management.information-code.logoff}")
    private String valueBit70Logoff;
    @Value("${greet-client-socket.address}")
    private String urlClientSocket;
    @Value("${greet-client-socket.port}")
    private Integer portClientSocket;
    @Value("${greet-server-socket.address}")
    private String urlServerSocket;
    @Value("${greet-server-socket.port}")
    private Integer portServerSocket;
    @Value("${tcp.client.host}")
    private String host;
    @Value("${tcp.client.port}")
    private int port;
    
    // private GreetClient greetClient = new GreetClient();
    private MessageClientService messageService;
    public MessageLayoutServiceImpl(MessageClientService messageService) {
        this.messageService = messageService;
    }
    

    private String bit7 = "Bit7";
    private String bit11 = "Bit11";
    private String bit15 = "Bit15";
    private String bit39 = "Bit39";
    private String bit48 = "Bit48";
    private String bit70 = "Bit70";

    SimpleDateFormat sdfNewFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    String valueBit7 = sdfNewFormat.format(new Date());
    String valueBit15 = sdfNewFormat.format(new Date());
    Gson gson = new Gson();

    public Object logon (LogonRequest request){
        LogonResponse response = new LogonResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            message.put(7, DataElementConverter.convertBit7(valueBit7));
            message.put(11, DataElementConverter.convertBit11());
            message.put(48, request.getAdditional_data());
            message.put(70, DataElementConverter.bit70(request.getNetwork_management_information_code())); //Bit 70 isinya "001"

            Set<Integer> keySet = message.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mti = requestType;
            String mtiBitmap = mti + bitmapElement;
            StringBuilder isoMessage = new StringBuilder();

            for (Integer key:keySet) {
                isoMessage.append(message.get(key));
            }
            String isoMessages;
            if(request.getMessage() !=null && !request.getMessage().equals("")){
                isoMessages = request.getMessage();
            }else{
                isoMessages = mtiBitmap + isoMessage.toString()+"?";
            }
            //String isoMessages = mtiBitmap + isoMessage.toString()+"?";
            log.info(isoMessages);
            log.info("kirim ke socket "+urlClientSocket+" "+portClientSocket);
            GreetClient greetClient = new GreetClient();
            greetClient.startConnection(urlClientSocket, portClientSocket);

            String isoMessageFromServer = greetClient.sendMessage(isoMessages);
            //String isoMessageFromServer = messageService.sendMessage(isoMessages);
            String logBalikanLogonMessage = "Response iso logon message = " +isoMessageFromServer;
            log.info(logBalikanLogonMessage);
            if(isoMessageFromServer != null){
                Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
                String objResp = gson.toJson(objectResponse);
                String logDataLogon = "Data iso logon json = "+objResp;
                log.info(logDataLogon);
                String formatLengthBit48  = "";
                if(objectResponse.get("Bit48") != null){
                    Integer lengthBit48 = objectResponse.get(bit48).length();
                    formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');
                    
                    response.setAdditional_data(formatLengthBit48 + objectResponse.get(bit48));
                }
                response.setMessage_type(objectResponse.get("MTI"));
                response.setPrimary_bit_map("");
                response.setSecondary_bit_map(objectResponse.get("Bit1"));
                response.setTransmission_date_and_time(objectResponse.get(bit7));
                response.setSystem_trace_audit_number(objectResponse.get(bit11));
                response.setResponse_code(objectResponse.get(bit39));
                response.setNetwork_management_information_code(objectResponse.get(bit70));
            }else{
                log.info("koneksi diputus karna null "+urlClientSocket+" "+portClientSocket);
                greetClient.stopConnection();

            }
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return response;
    }
    
    

    public Object logon_v2 (LogonRequest request){
        LogonResponse response = new LogonResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            message.put(7, DataElementConverter.convertBit7(valueBit7));
            message.put(11, DataElementConverter.convertBit11());
            message.put(48, request.getAdditional_data());
            message.put(70, DataElementConverter.bit70(request.getNetwork_management_information_code())); //Bit 70 isinya "001"

            Set<Integer> keySet = message.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mti = requestType;
            String mtiBitmap = mti + bitmapElement;
            StringBuilder isoMessage = new StringBuilder();

            for (Integer key:keySet) {
                isoMessage.append(message.get(key));
            }
            String isoMessages = mtiBitmap + isoMessage.toString()+"?";
            log.info(isoMessages);
            log.info("kirim ke socket v2 "+host+" "+port);
            String isoMessageFromServer = messageService.sendMessage(isoMessages);
            String logBalikanLogonMessage = "Response iso logon message = " +isoMessageFromServer;
            log.info(logBalikanLogonMessage);
            if(isoMessageFromServer != null){
                Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
                String objResp = gson.toJson(objectResponse);
                String logDataLogon = "Data iso logon json = "+objResp;
                log.info(logDataLogon);
                String formatLengthBit48  = "";
                if(objectResponse.get("Bit48") != null){
                    Integer lengthBit48 = objectResponse.get(bit48).length();
                    formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');

                    response.setAdditional_data(formatLengthBit48 + objectResponse.get(bit48));
                }
                response.setMessage_type(objectResponse.get("MTI"));
                response.setPrimary_bit_map("");
                response.setSecondary_bit_map(objectResponse.get("Bit1"));
                response.setTransmission_date_and_time(objectResponse.get(bit7));
                response.setSystem_trace_audit_number(objectResponse.get(bit11));
                response.setResponse_code(objectResponse.get(bit39));
                response.setNetwork_management_information_code(objectResponse.get(bit70));
            }
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return response;
    }

    public Object logoff(LogoffRequest request){
        LogoffResponse response = new LogoffResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            message.put(7, DataElementConverter.convertBit7(valueBit7));
            message.put(11, DataElementConverter.convertBit11());
            message.put(70, DataElementConverter.bit70(request.getNetwork_management_information_code())); //Bit 70 isinya "002"

            Set<Integer> keySet = message.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mti = requestType;
            String mtiBitmap = mti + bitmapElement;
            StringBuilder isoMessage = new StringBuilder();

            for (Integer key:keySet) {
                isoMessage.append(message.get(key));
            }
            String isoMessages = mtiBitmap + isoMessage.toString()+"?";

            log.info("kirim ke socket "+urlClientSocket+" "+portClientSocket);
            GreetClient greetClient = new GreetClient();
            greetClient.startConnection(urlClientSocket, portClientSocket);

            String isoMessageFromServer = greetClient.sendMessage(isoMessages);
            //String isoMessageFromServer = messageService.sendMessage(isoMessages);
            log.info("Response iso logoff message = "); log.info(isoMessageFromServer);
            Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
            String objResp = gson.toJson(objectResponse);
            log.info("Data iso logoff json = "); log.info(objResp);
            response.setMessage_type(objectResponse.get("MTI"));
            response.setPrimary_bit_map("");
            response.setSecondary_bit_map("");
            response.setTransmission_date_and_time(objectResponse.get(bit7));
            response.setSystem_trace_audit_number(objectResponse.get(bit11));
            response.setResponse_code(objectResponse.get(bit39));
            response.setNetwork_management_information_code(objectResponse.get(bit70));
            
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return response;
    }

    public Object cutOver(CutOverRequest request){
        CutOverResponse response = new CutOverResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            message.put(7, DataElementConverter.convertBit7(valueBit7));
            message.put(11, DataElementConverter.convertBit11());
            message.put(15, DataElementConverter.convertBit15(valueBit15));
            message.put(70, DataElementConverter.bit70(request.getNetwork_management_information_code())); //Bit 70 isinya "301"

            Set<Integer> keySet = message.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mti = requestType;
            String mtiBitmap = mti + bitmapElement;
            StringBuilder isoMessage = new StringBuilder();

            for (Integer key:keySet) {
                isoMessage.append(message.get(key));
            }
            String isoMessages = mtiBitmap + isoMessage.toString()+"?";

            log.info("kirim ke socket "+urlClientSocket+" "+portClientSocket);
            String isoMessageFromServer = messageService.sendMessage(isoMessages);
            log.info("Response iso cut over message = ");log.info(isoMessageFromServer);
            Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
            String objResp = gson.toJson(objectResponse);
            log.info("Data iso cut over json = "); log.info(objResp);
            response.setMessage_type(objectResponse.get("MTI"));
            response.setPrimary_bit_map("");
            response.setSecondary_bit_map("");
            response.setTransmission_date_and_time(objectResponse.get(bit7));
            response.setSystem_trace_audit_number(objectResponse.get(bit11));
            response.setSettlement_date(objectResponse.get(bit15));
            response.setResponse_code(objectResponse.get(bit39));
            response.setNetwork_management_information_code(objectResponse.get(bit70));            
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return response;
    }

    public Object echoTest(EchoTestRequest request){
        EchoTestResponse response = new EchoTestResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            message.put(7, DataElementConverter.convertBit7(valueBit7));
            message.put(11, DataElementConverter.convertBit11());
            message.put(70, DataElementConverter.bit70(request.getNetwork_management_information_code())); //Bit 70 isinya "301"

            Set<Integer> keySet = message.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mti = requestType;
            String mtiBitmap = mti + bitmapElement;
            StringBuilder isoMessage = new StringBuilder();

            for (Integer key:keySet) {
                isoMessage.append(message.get(key));
            }
            String isoMessages = mtiBitmap + isoMessage.toString()+"?";

            log.info("kirim ke socket "+urlClientSocket+" "+portClientSocket);
            log.info(isoMessages);
            GreetClient greetClient = new GreetClient();
            if(request.server != null && request.server.toLowerCase().equals("rintis")){
                greetClient.startConnection(urlClientSocket, portClientSocket);
            }
            else if(request.server != null && request.server.toLowerCase().equals("astrapay")){
                greetClient.startConnection(urlServerSocket, portServerSocket);
            }else{
                greetClient.startConnection(urlServerSocket, portServerSocket);
            }

            String isoMessageFromServer = greetClient.sendMessage(isoMessages);
            //String isoMessageFromServer = messageService.sendMessage(isoMessages);
            String logBalikanIsoMessage = "Response iso message = " + isoMessageFromServer;
            log.info(logBalikanIsoMessage);
            Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
            String objResp = gson.toJson(objectResponse);
            String logDataIsoEchoJson = "Data iso echo json = "+objResp;
            log.info(logDataIsoEchoJson);
            response.setMessage_type(objectResponse.get("MTI"));
            response.setPrimary_bit_map("");
            response.setSecondary_bit_map("");
            response.setTransmission_date_and_time(objectResponse.get(bit7));
            response.setSystem_trace_audit_number(objectResponse.get(bit11));
            response.setResponse_code(objectResponse.get(bit39));
            response.setNetwork_management_information_code(objectResponse.get(bit70));
            
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return response;
    }

    public String testLogon (){
        String objResp = "";
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            log.info("kirim ke socket "+urlClientSocket+" "+portClientSocket);
            GreetClient greetClient = new GreetClient();
            greetClient.startConnection(urlClientSocket, portClientSocket);
            System.out.println("SEND STRING KOSONG");
            String isoMessageFromServer = greetClient.sendMessage("");
            String logBalikanLogonMessage = "Response iso logon message = " +isoMessageFromServer;
            log.info(logBalikanLogonMessage);
            if(isoMessageFromServer != null){
                Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
                objResp = gson.toJson(objectResponse);
                String logDataLogon = "Data iso logon json = "+objResp;
                log.info(logDataLogon);
            } else {
                objResp = isoMessageFromServer;
            }
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return objResp;
    }

    public String testLogon2 (){
        String objResp = "";
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            log.info("kirim ke socket "+urlClientSocket+" "+portClientSocket);
            GreetClient greetClient = new GreetClient();
            greetClient.startConnection(urlClientSocket, portClientSocket);
            System.out.println("HANYA KONEKSI");
            objResp = "URL : "+urlClientSocket+" PORT : "+portClientSocket;
        } catch (Exception e) {
            //TODO: handle exception
            log.error(e.getMessage(),e);
        }
        return objResp;
    }
    
    public Object logon_v3 (LogonRequest request) throws UnknownHostException, IOException{
    	LogonResponse res = new LogonResponse();
        Socket clientSocket = new Socket("10.126.205.26", 2505);
        String networkRequest = buildNetworkReqMessage(request);

        PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream());
        InputStreamReader incoming = new InputStreamReader(clientSocket.getInputStream());
 
        outgoing.print(networkRequest);
        outgoing.flush();
 
        int data;
        StringBuffer sb = new StringBuffer();
        int counter = 0;
        int lengthOfMsg = 1;
        System.out.println("Lenght incoming = "+incoming.read());
        String theString = IOUtils.toString(incoming); 

        res.setResponse_code(sb.toString());;
        outgoing.close();
        incoming.close();
        clientSocket.close();
        return sb.toString();
    }
    
    private static String buildNetworkReqMessage(LogonRequest request) {
        StringBuilder networkReq = new StringBuilder();
 
        // MTI 0800
        networkReq.append("0800");
        // untuk request, DE yang aktif adalah DE[3,7,11,12,13,48 dan 70]
        String bitmapReq = IsoUtil.getHexaBitmapFromActiveDE(new int[] {1,7,11,48,70});
        networkReq.append(bitmapReq);
        System.out.println("bitmapReq "+bitmapReq);
        // DE 7 transmission date and time
        networkReq.append(new SimpleDateFormat("MMddHHmmss").format(new Date()));
        System.out.println("DE 7 "+new SimpleDateFormat("MMddHHmmss").format(new Date()));

        // DE 11 system trace audit number
        networkReq.append(DataElementConverter.convertBit11());
        System.out.println("ditambah DE 11 "+networkReq.toString());

        // DE 48 Additional Private Data
        final String clientID = request.getAdditional_data();
        // length de 48
        String lengthBit48 = "";
        if (clientID.length() < 10) lengthBit48 = "00" + clientID.length();
        if (clientID.length() < 100 && clientID.length() >= 10) lengthBit48 = "0" + clientID.length();
        if (clientID.length() == 100) lengthBit48 = String.valueOf(clientID.length());
//        networkReq.append(lengthBit48);
//        System.out.println("lengthBit48 "+lengthBit48);

        networkReq.append(request.getAdditional_data());
        System.out.println("request.getAdditional_data() "+request.getAdditional_data());

        // DE 70 Network Information Code
        networkReq.append("001");
        System.out.println("networkReq sblm header "+networkReq.toString());
        // tambahkan 4 digit length of msg sbg header
        String msgHeader = "";
        if (networkReq.toString().length() < 10) msgHeader = "000" + networkReq.toString().length();
        if (networkReq.toString().length() < 100 && networkReq.toString().length() >= 10) msgHeader = "00" + networkReq.toString().length();
        if (networkReq.toString().length() < 1000 && networkReq.toString().length() >= 100) msgHeader = "0" + networkReq.toString().length();
        if (networkReq.toString().length() >= 1000) msgHeader = String.valueOf(networkReq.toString().length());
        
        StringBuilder finalNetworkReqMsg = new StringBuilder();
        
        int iAsciiValue=Integer.parseInt(msgHeader);
        String strAsciiTab = Character.toString((char) iAsciiValue);
        finalNetworkReqMsg.append("L");
        finalNetworkReqMsg.append(networkReq.toString());
        System.out.println("Message dikirim "+finalNetworkReqMsg.toString());
        return finalNetworkReqMsg.toString();
    }
}
