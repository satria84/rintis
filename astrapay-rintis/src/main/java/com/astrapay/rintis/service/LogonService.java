package com.astrapay.rintis.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.astrapay.rintis.config.GreetClient;
import com.astrapay.rintis.domain.response.LogonResponse;
import com.astrapay.rintis.util.DataElementConverter;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogonService {
    
    private static final Logger log = LoggerFactory.getLogger(LogonService.class);
    @Value("${message-type.request-logon-logoff}")
    private String requestType;
    @Value("${network-management.data}")
    private String valueBit48;
    @Value("${network-management.information-code.logon}")
    private String valueBit70;
    @Value("${greet-client-socket.address}")
    private String urlClientSocket;
    @Value("${greet-client-socket.port}")
    private Integer portClientSocket;
    @Autowired
    private MessageClientService messageService;
    @Autowired
    private EmailService emailService;
    @SuppressWarnings("Duplicates")
    public void doLogon(){
        Gson gson = new Gson();
        LogonResponse response = new LogonResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        SimpleDateFormat sdfNewFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        String valueBit7 = sdfNewFormat.format(new Date());
        String bit39 = "Bit39";
        try {
            message.put(7, DataElementConverter.convertBit7(valueBit7));
            message.put(11, DataElementConverter.convertBit11());
            message.put(48, valueBit48);
            message.put(70, valueBit70); //Bit 70 isinya "001"

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
            log.info("kirim ke socket");
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
                    Integer lengthBit48 = objectResponse.get("bit48").length();
                    formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');
                    
                    response.setAdditional_data(formatLengthBit48 + objectResponse.get("bit48"));
                }
                response.setMessage_type(objectResponse.get("MTI"));
                response.setPrimary_bit_map("");
                response.setSecondary_bit_map(objectResponse.get("Bit1"));
                response.setTransmission_date_and_time(objectResponse.get("bit7"));
                response.setSystem_trace_audit_number(objectResponse.get("bit11"));
                response.setResponse_code(objectResponse.get(bit39));
                response.setNetwork_management_information_code(objectResponse.get("bit70"));

                if(objectResponse.get("Bit39").equals("00")){
                    emailService.sendMail("awp1305@gmail.com", "LOGON STATUS SUCCESS", "Logon Anda Berhasil");
                    log.info("SEND EMAIL STATUS SUCCESS"); 
                } else {
                    emailService.sendMail("awp1305@gmail.com ", "LOGON STATUS FAILED", "Logon Anda gagal silahkan periksa. Response Code :"+objectResponse.get(bit39));
                    log.info("SEND EMAIL STATUS FAILED");  
                }
            } else {
                emailService.sendMail("awp1305@gmail.com", "LOGON STATUS FAILED", "Logon Anda gagal silahkan periksa. Response Code : null");
                log.info("SEND EMAIL STATUS FAILED");  
            }

        } catch (Exception e) {
            //TODO: handle exception
            log.info(e.getMessage(), e);
        }
    }
}
