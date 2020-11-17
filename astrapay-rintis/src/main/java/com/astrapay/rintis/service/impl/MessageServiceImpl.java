package com.astrapay.rintis.service.impl;

import com.astrapay.rintis.service.MessageService;
import com.astrapay.rintis.service.RefundService;
import com.astrapay.rintis.util.DataElementConverter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final RefundService refundService;

    private final String Bit2 = "Bit2";
    private final String Bit3 = "Bit3";
    private final String Bit4 = "Bit4";
    private final String Bit7 = "Bit7";
    private final String Bit11= "Bit11";
    private final String Bit12= "Bit12";
    private final String Bit13= "Bit13";
    private final String Bit15= "Bit15";
    private final String Bit17= "Bit17";
    private final String Bit18= "Bit18";
    private final String Bit22= "Bit22";
    private final String Bit28= "Bit28";
    private final String Bit32= "Bit32";
    private final String Bit33= "Bit33";
    private final String Bit37= "Bit37";
    private final String Bit38= "Bit38";
    private final String Bit41= "Bit41";
    private final String Bit42= "Bit42";
    private final String Bit48= "Bit48";
    private final String Bit49= "Bit49";
    private final String Bit70= "Bit70";
    private final String Bit100="Bit100";
    private final String Bit102="Bit102";

    @Override
    @SuppressWarnings("Duplicates")
    public byte[] processMessage(byte[] message) {
        String messageContent = new String(message);
        LOGGER.info("ASTRAPAY Receive message: {}", messageContent);
        String responseContent = "";
        try {
            Map<String, String> objectResponse = DataElementConverter.converterIsoToJson(messageContent);
            if (objectResponse.get("MTI").equals("0800")) {
                if(objectResponse.get(Bit48) != null){
                    responseContent = logonResponse(objectResponse); // untuk test logon local/dummy
                } else if(objectResponse.get(Bit15) != null){
                    responseContent = cutOverResponse(objectResponse); // cutover
                } else {
                    responseContent = echoTestResponse(objectResponse); // echo test response
                }
            } else {
                switch (objectResponse.get(Bit3).substring(0, 2)) {
                    case "37":
                        responseContent = inquryResponse(objectResponse);
                        break;
                    case "26":
                        responseContent = paymentResponse(objectResponse);
                        break;
                    case "36":
                        responseContent = checkStatusResponse(objectResponse);
                        break;
                    case "20":
                        responseContent = refundService.refundIssuer(messageContent);
                        break;
                    default:
                        responseContent = messageContent;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent.getBytes();
    }

    public String processMessage2(String message) {
        String messageContent = message;
        LOGGER.info("ASTRAPAY Receive message: {}", messageContent);
        String responseContent = "";
        try {
            Map<String, String> objectResponse = DataElementConverter.converterIsoToJson(messageContent);
            if (objectResponse.get("MTI").equals("0800")) {
                if(objectResponse.get(Bit48) != null){
                    responseContent = logonResponse(objectResponse); // untuk test logon local/dummy
                } else if(objectResponse.get(Bit15) != null){
                    responseContent = cutOverResponse(objectResponse); // cutover
                } else {
                    responseContent = echoTestResponse(objectResponse); // echo test response
                }
            } else {
                switch (objectResponse.get(Bit3).substring(0, 2)) {
                    case "37":
                        responseContent = inquryResponse(objectResponse);
                        break;
                    case "26":
                        responseContent = paymentResponse(objectResponse);
                        break;
                    case "36":
                        responseContent = checkStatusResponse(objectResponse);
                        break;
                    case "20":
                        responseContent = refundService.refundIssuer(messageContent);
                        break;
                    default:
                        responseContent = messageContent;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

    @SuppressWarnings("Duplicates")
    private String inquryResponse(Map<String, String> objectResponse) {
        String responseContent = "";
        try {
            Map<Integer, String> iso8583InquiryResponse = new LinkedHashMap<Integer, String>();
            Integer lengthBit2 = objectResponse.get(Bit2).length();
            String formatLengthBit2 = String.format("%2s", String.valueOf(lengthBit2)).replace(' ', '0');

            Integer lengthBit32 = objectResponse.get(Bit32).length();
            String formatLengthBit32 = String.format("%2s", String.valueOf(lengthBit32)).replace(' ', '0');

            Integer lengthBit33 = objectResponse.get(Bit33).length();
            String formatLengthBit33 = String.format("%2s", String.valueOf(lengthBit33)).replace(' ', '0');

            Integer lengthBit48 = objectResponse.get(Bit48).length();
            String formatLengthBit48 = String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');

            Integer lengthBit100 = objectResponse.get(Bit100).length();
            String formatLengthBit100 = String.format("%2s", String.valueOf(lengthBit100)).replace(' ', '0');
//
            Integer lengthBit102 = objectResponse.get(Bit102).length();
            String formatLengthBit102 = String.format("%2s", String.valueOf(lengthBit102)).replace(' ', '0');

            iso8583InquiryResponse.put(2, (formatLengthBit2 + objectResponse.get(Bit2)));
            iso8583InquiryResponse.put(3, objectResponse.get(Bit3));
            iso8583InquiryResponse.put(4, objectResponse.get(Bit4));
            iso8583InquiryResponse.put(7, objectResponse.get(Bit7)); //"0710031205"
            iso8583InquiryResponse.put(11, objectResponse.get(Bit11)); //"000519"

            iso8583InquiryResponse.put(12, objectResponse.get(Bit12));
            iso8583InquiryResponse.put(13, objectResponse.get(Bit13));
            iso8583InquiryResponse.put(15, objectResponse.get(Bit15));
            iso8583InquiryResponse.put(17, objectResponse.get(Bit17));
            iso8583InquiryResponse.put(18, objectResponse.get(Bit18));

            iso8583InquiryResponse.put(22, objectResponse.get(Bit22));
            if (objectResponse.get(Bit28) != null) {
                iso8583InquiryResponse.put(28, objectResponse.get(Bit28));
            }
            iso8583InquiryResponse.put(32, (formatLengthBit32 + objectResponse.get(Bit32)));
            iso8583InquiryResponse.put(33, (formatLengthBit33 + objectResponse.get(Bit33)));
            iso8583InquiryResponse.put(37, objectResponse.get(Bit37));

            iso8583InquiryResponse.put(38, objectResponse.get(Bit38));
            iso8583InquiryResponse.put(39, "00");
            iso8583InquiryResponse.put(41, objectResponse.get(Bit41));
            iso8583InquiryResponse.put(42, objectResponse.get(Bit42));
            iso8583InquiryResponse.put(48, (formatLengthBit48 + objectResponse.get(Bit48)));
            iso8583InquiryResponse.put(49, objectResponse.get(Bit49));
            iso8583InquiryResponse.put(57, "9362648011893600153100290563402157338680866228710303UMI27480118936000771234567890021500000000000077 0303UKI28480118936000140987654321021500000000000014 0303UPO29480118936002221111111111021500000222       0303OLO30480118936003331212121212021500000000131313 0303TOL31480118936004269988774455021593600426       0303UMI32480118936004448877887788021593600444       0303OSO33480118936005551111111111021593600555       0303DOI34480118936000081112223334021593600811223322 0303WOW354801189360000922112211120215936009         0303LOI36480118936000881234567890021593600088       0303UME37480118936009991235512421021593600999       0303POL38480118936009908877996654021593600990000111 0303SUP39480118936009801234121251021593600980444444 0303UME40480118936009701231231231021593600970       0303KRI41480118936009603123441211021593600960       0303WOW42480118936009501234551212021593600950       0303PUS43480118936009401111222333021593600940       0303HAI");
            iso8583InquiryResponse.put(100, (formatLengthBit100 + objectResponse.get(Bit100)));
            iso8583InquiryResponse.put(102, (formatLengthBit102 + objectResponse.get(Bit102)));

            responseContent = DataElementConverter.requestIso("0210", iso8583InquiryResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

    @SuppressWarnings("Duplicates")
    private String paymentResponse(Map<String, String> objectResponse) {
        String responseContent = "";
        try {
            Map<Integer, String> iso8583PaymentResponse = new LinkedHashMap<Integer, String>();
            Integer lengthBit2 = objectResponse.get(Bit2).length();
            String formatLengthBit2  =  String.format("%2s", String.valueOf(lengthBit2)).replace(' ', '0');
            Integer lengthBit32 = objectResponse.get(Bit32).length();
            String formatLengthBit32  =  String.format("%2s", String.valueOf(lengthBit32)).replace(' ', '0');
            Integer lengthBit48 = objectResponse.get(Bit48).length();
            String formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');
            Integer lengthBit100 = objectResponse.get(Bit100).length();
            String formatLengthBit100  =  String.format("%2s", String.valueOf(lengthBit100)).replace(' ', '0');
            Integer lengthBit102 = objectResponse.get(Bit102).length();
            String formatLengthBit102  =  String.format("%2s", String.valueOf(lengthBit102)).replace(' ', '0');

            iso8583PaymentResponse.put(2, (formatLengthBit2 + objectResponse.get(Bit2)));
            iso8583PaymentResponse.put(3, objectResponse.get(Bit3));
            iso8583PaymentResponse.put(4, objectResponse.get(Bit4));
            iso8583PaymentResponse.put(7, "0710031205");
            iso8583PaymentResponse.put(11, "000519");

            iso8583PaymentResponse.put(12, objectResponse.get(Bit12));
            iso8583PaymentResponse.put(13, objectResponse.get(Bit13));
            iso8583PaymentResponse.put(15, "0710");
            iso8583PaymentResponse.put(17, objectResponse.get(Bit17));
            iso8583PaymentResponse.put(18, objectResponse.get(Bit18));

            iso8583PaymentResponse.put(22, objectResponse.get(Bit22));
            if(objectResponse.get(Bit28) !=null){
                iso8583PaymentResponse.put(28, objectResponse.get(Bit28));
            }
            iso8583PaymentResponse.put(32, (formatLengthBit32 + objectResponse.get(Bit32)));
            iso8583PaymentResponse.put(33, "06360002");
            iso8583PaymentResponse.put(37, objectResponse.get(Bit37));

            iso8583PaymentResponse.put(38, objectResponse.get(Bit38));
            iso8583PaymentResponse.put(39, "00");
            iso8583PaymentResponse.put(41, objectResponse.get(Bit41));
            iso8583PaymentResponse.put(42, objectResponse.get(Bit42));
            iso8583PaymentResponse.put(48, (formatLengthBit48 + objectResponse.get(Bit48)));

            iso8583PaymentResponse.put(49, objectResponse.get(Bit49));
            iso8583PaymentResponse.put(100, (formatLengthBit100 + objectResponse.get(Bit100)));
            iso8583PaymentResponse.put(102, (formatLengthBit102 + objectResponse.get(Bit102)));
            iso8583PaymentResponse.put(123, "02000001234561234567890");

          responseContent = DataElementConverter.requestIso("0210", iso8583PaymentResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

    @SuppressWarnings("Duplicates")
    private String checkStatusResponse(Map<String, String> objectResponse) {
        String responseContent = "";
        try {
            Map<Integer, String> iso8583CheckStatusResponse = new LinkedHashMap<Integer, String>();
            Integer lengthBit2 = objectResponse.get(Bit2).length();
            String formatLengthBit2  =  String.format("%2s", String.valueOf(lengthBit2)).replace(' ', '0');

            Integer lengthBit32 = objectResponse.get(Bit32).length();
            String formatLengthBit32  =  String.format("%2s", String.valueOf(lengthBit32)).replace(' ', '0');


            Integer lengthBit48 = objectResponse.get(Bit48).length();
            String formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');

            Integer lengthBit100 = objectResponse.get(Bit100).length();
            String formatLengthBit100  =  String.format("%2s", String.valueOf(lengthBit100)).replace(' ', '0');

            Integer lengthBit102 = objectResponse.get(Bit102).length();
            String formatLengthBit102  =  String.format("%2s", String.valueOf(lengthBit102)).replace(' ', '0');

            Integer lengthBit123 = "00001234560000123456".length();
            String formatLengthBit123  =  String.format("%3s", String.valueOf(lengthBit123)).replace(' ', '0');

            iso8583CheckStatusResponse.put(2, (formatLengthBit2 + objectResponse.get(Bit2)));
            iso8583CheckStatusResponse.put(3, objectResponse.get(Bit3));
            iso8583CheckStatusResponse.put(4, objectResponse.get(Bit4));
            iso8583CheckStatusResponse.put(7, "0710031205");
            iso8583CheckStatusResponse.put(11, "000519");

            iso8583CheckStatusResponse.put(12, objectResponse.get(Bit12));
            iso8583CheckStatusResponse.put(13, objectResponse.get(Bit13));
            if (objectResponse.get(Bit15) != null) {
                iso8583CheckStatusResponse.put(15, objectResponse.get(Bit15));
            }
            iso8583CheckStatusResponse.put(17, objectResponse.get(Bit17));
            iso8583CheckStatusResponse.put(18, objectResponse.get(Bit18));
            iso8583CheckStatusResponse.put(22, objectResponse.get(Bit22));
            if (objectResponse.get(Bit28) != null) {
                iso8583CheckStatusResponse.put(28, objectResponse.get(Bit28));
            }
            iso8583CheckStatusResponse.put(32, (formatLengthBit32 + objectResponse.get(Bit32)));
            if (objectResponse.get(Bit33) != null) {
                Integer lengthBit33 = objectResponse.get(Bit33).length();
                String formatLengthBit33  =  String.format("%2s", String.valueOf(lengthBit33)).replace(' ', '0');
                iso8583CheckStatusResponse.put(33, (formatLengthBit33 + objectResponse.get(Bit33)));
            }
            iso8583CheckStatusResponse.put(37, objectResponse.get(Bit37));
            iso8583CheckStatusResponse.put(38, objectResponse.get(Bit38));
            iso8583CheckStatusResponse.put(39, "00");
            iso8583CheckStatusResponse.put(41, objectResponse.get(Bit41));
            iso8583CheckStatusResponse.put(42, objectResponse.get(Bit42));
            iso8583CheckStatusResponse.put(48, (formatLengthBit48 + objectResponse.get(Bit48)));
            iso8583CheckStatusResponse.put(49, objectResponse.get(Bit49));
            iso8583CheckStatusResponse.put(100, (formatLengthBit100 + objectResponse.get(Bit100)));
            iso8583CheckStatusResponse.put(102, (formatLengthBit102 + objectResponse.get(Bit102)));
            iso8583CheckStatusResponse.put(123, ("02000001234560000123456"));

            responseContent = DataElementConverter.requestIso("0210", iso8583CheckStatusResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

    @SuppressWarnings("Duplicates")
    private String logonResponse(Map<String, String> objectResponse) {
        String responseContent = "";
        try {
            Map<Integer, String> iso8583LogonResponse = new LinkedHashMap<Integer, String>();
            Integer lengthBit48 = objectResponse.get(Bit48).length();
            String formatLengthBit48  =  String.format("%3s", String.valueOf(lengthBit48)).replace(' ', '0');
            iso8583LogonResponse.put(7, objectResponse.get(Bit7));
            iso8583LogonResponse.put(11, objectResponse.get(Bit11));
            iso8583LogonResponse.put(39, "00");
            iso8583LogonResponse.put(48, (formatLengthBit48 + objectResponse.get(Bit48)));
            iso8583LogonResponse.put(70, objectResponse.get(Bit70));

            responseContent = DataElementConverter.requestIso("0810", iso8583LogonResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

    @SuppressWarnings("Duplicates")
    private String echoTestResponse(Map<String, String> objectResponse) {
        String responseContent = "";
        try {
            Map<Integer, String> iso8583EchoTestResponse = new LinkedHashMap<Integer, String>();
            iso8583EchoTestResponse.put(7, objectResponse.get(Bit7));
            iso8583EchoTestResponse.put(11, objectResponse.get(Bit11));
            iso8583EchoTestResponse.put(39, "00");
            iso8583EchoTestResponse.put(70, objectResponse.get(Bit70));

            responseContent = DataElementConverter.requestIso("0810", iso8583EchoTestResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

    @SuppressWarnings("Duplicates")
    private String cutOverResponse(Map<String, String> objectResponse) {
        String responseContent = "";
        try {
            Map<Integer, String> iso8583CutOverResponse = new LinkedHashMap<Integer, String>();
            iso8583CutOverResponse.put(7, objectResponse.get(Bit7));
            iso8583CutOverResponse.put(11, objectResponse.get(Bit11));
            iso8583CutOverResponse.put(15, objectResponse.get(Bit15));
            iso8583CutOverResponse.put(39, "00");
            iso8583CutOverResponse.put(70, objectResponse.get(Bit70));

            responseContent = DataElementConverter.requestIso("0810", iso8583CutOverResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return responseContent;
    }

}
