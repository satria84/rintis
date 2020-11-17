package com.astrapay.rintis.service.impl;

import com.astrapay.rintis.domain.request.QRInquiryMpanRequest;
import com.astrapay.rintis.domain.response.QRInquiryMpanResponse;
import com.astrapay.rintis.service.MessageClientService;
import com.astrapay.rintis.service.QRInquiryMpanService;

import com.astrapay.rintis.util.DataElementConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class QRInquiryMpanServiceImpl implements QRInquiryMpanService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${message-type.request-inquiry-mpan}")
    private String requestInquiryMpan;
    @Value("${transaction-code.inquiry-mpan}")
    private String transactionCode;
    @Value("${from-account-type.savings}")
    private String fromAccountCode;
    @Value("${to-account-type.unspecified}")
    private String toAccountCode;
    @Value("${additional-data.product-indicator}")
    private String productIndicator;
    @Value("${additional-data.customer-data}")
    private String customerData;
    @Value("${additional-data.merchant-criteria}")
    private String merchantCriteria;
    @Value("${convenience-fee.credit}")
    private String paymentCode;

    private String timeout = "Timeout";
    private String Bit32 = "Bit32";
    private String Bit57 = "Bit57";
    private MessageClientService messageClientService;

    public QRInquiryMpanServiceImpl(MessageClientService messageClientService) {
        this.messageClientService = messageClientService;
    }


    @SuppressWarnings("Duplicates")
    public Object inquiryMpan(QRInquiryMpanRequest request) {
        QRInquiryMpanResponse response = new QRInquiryMpanResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();

        try {
            message.put(2,DataElementConverter.convertBit2(request.getPan()));
            message.put(3,DataElementConverter.convertBit3(transactionCode, fromAccountCode, toAccountCode));
            message.put(4,DataElementConverter.convertBit4(new BigDecimal(request.getTransaction_amount()), BigDecimal.ZERO));
            message.put(7,DataElementConverter.convertBit7(request.getTransmission_date_and_time()));
            message.put(11,DataElementConverter.convertBit11());
            message.put(12,DataElementConverter.convertBit12(request.getLocal_transaction_time()));
            message.put(13,DataElementConverter.convertBit13(request.getLocal_transaction_date()));
            message.put(15,DataElementConverter.convertBit13(request.getSettlement_date()));
            message.put(17,DataElementConverter.convertBit13(request.getCapture_date()));
            message.put(18,request.getMerchants_type()); //mcc
            message.put(22, "011"); // need ask to rintis
            if (request.getAmount_convenience_fee()!=null) {
                BigDecimal fee = new BigDecimal(request.getAmount_convenience_fee());
                message.put(28, DataElementConverter.convertBit28(fee, paymentCode));//bit 28
            }
            message.put(32,DataElementConverter.convertBit32(request.getAcquiring_institution_id())); //NNS
            message.put(33,DataElementConverter.convertBit33(request.getForwarding_institution_id()));// LLVAR + Rintis Code
            message.put(37,request.getRetrieval_reference_number());//generate random string
            message.put(38,request.getApproval_code()); // need ask (Authorization Identification Response)
            message.put(41,DataElementConverter.convertBit41(request.getCard_acceptor_terminal_identification())); //terminal label
            message.put(42,request.getCard_acceptor_id());// merchantId
            message.put(43,DataElementConverter.convertBit43(request.getMerchant_name(), request.getMerchant_city(), request.getMerchant_country_code()));
            message.put(48,"008PI04IQ02"); // masih hardcode
            message.put(49,request.getTransaction_currency_code());//dapet dari translate
            message.put(100,DataElementConverter.convertBit100(request.getIssuer_id()));//8 digit pertama dari bit 102
            message.put(102,DataElementConverter.convertBit102(request.getAccount_identification_1()));//value dari customer Pan (dari request qris)

            Set<Integer> keySet = message.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mti = requestInquiryMpan;
            String mtiBitmap = mti + bitmapElement;
            StringBuilder isoMessage = new StringBuilder();

            for (Integer key:keySet) {
                isoMessage.append(message.get(key));
            }
            String isoMessages = mtiBitmap + isoMessage.toString()+"?";

            log.info("kirim ke socket");
            String isoMessageFromServer = messageClientService.sendMessage(isoMessages);
            log.info("balikan iso message = "); log.info(isoMessageFromServer);
            if (isoMessageFromServer.equalsIgnoreCase(timeout)) {
                response.setResponse_code(timeout);
                return response;
            }
            Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
            response.setPrimary_account_number(objectResponse.get("Bit2"));
            response.setProcessing_code(objectResponse.get("Bit3"));
            response.setTransaction_amount(objectResponse.get("Bit4"));
            response.setTransmission_date_and_time(objectResponse.get("Bit7"));
            response.setSystem_trace_audit_number(objectResponse.get("Bit11"));
            response.setLocal_transaction_time(objectResponse.get("Bit12"));
            response.setLocal_transaction_date(objectResponse.get("Bit13"));
            response.setSettlement_date(objectResponse.get("Bit15"));
            response.setCapture_date(objectResponse.get("Bit17"));
            response.setMerchants_type(objectResponse.get("Bit18"));
            response.setPoint_of_service_entry_mode(objectResponse.get("Bit22"));
            response.setAmount_convenience_fee(objectResponse.get("Bit28"));
            response.setAcquiring_institution_id(objectResponse.get(Bit32));
            response.setForwarding_institution_id(objectResponse.get(Bit32));
            response.setRetrieval_reference_number(objectResponse.get("Bit37"));
            response.setAuthorization_identification_response(objectResponse.get("Bit38"));
            response.setResponse_code(objectResponse.get("Bit39"));
            response.setCard_acceptor_terminal_identification(objectResponse.get("Bit41"));
            response.setCard_acceptor_id(objectResponse.get("Bit42"));
            response.setAdditional_data(objectResponse.get("Bit48"));
            response.setTransaction_currency_code(objectResponse.get("Bit49"));
            response.setAdditional_data_national(objectResponse.get(Bit57));
            response.setMerchantDataResponses(DataElementConverter.convertBit57Response(objectResponse.get(Bit57)));
            response.setReceiving_institution_id(objectResponse.get("Bit100"));
            response.setAccount_identification_1(objectResponse.get("Bit102"));
            return response;

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            //TODO: handle exception
        }
        return response;
    }

    @SuppressWarnings("Duplicates")
    public QRInquiryMpanResponse inquiryMpan2(QRInquiryMpanRequest request) {
        QRInquiryMpanResponse response = new QRInquiryMpanResponse();
        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
        try {
            message.put(2,request.getPan());
            message.put(3,request.getProcessing_code());
            message.put(4,request.getTransaction_amount());
            message.put(7,request.getTransmission_date_and_time());
            message.put(11,request.getSystem_trace_audit_number());
            message.put(12,request.getLocal_transaction_time());
            message.put(13,request.getLocal_transaction_date());
            message.put(15,request.getSettlement_date());
            message.put(17,request.getCapture_date());
            message.put(18,request.getMerchants_type()); //mcc
            message.put(22,request.getPoint_of_service_entry_mode()); // need ask to rintis
            if (request.getAmount_convenience_fee()!=null) {
                message.put(28, request.getAmount_convenience_fee());//bit 28
            }
            message.put(32,request.getAcquiring_institution_id()); //NNS
            message.put(33,request.getForwarding_institution_id());// LLVAR + Rintis Code
            message.put(37,request.getRetrieval_reference_number());//generate random string
            message.put(38,request.getApproval_code()); // need ask (Authorization Identification Response)
            message.put(41,request.getCard_acceptor_terminal_identification()); //terminal label
            message.put(42,request.getCard_acceptor_id());// merchantId
            message.put(43,request.getCard_acceptor_name_location());
            message.put(48,request.getAdditional_data()); // masih hardcode
            message.put(49,request.getTransaction_currency_code());//dapet dari translate
            message.put(100,request.getIssuer_id());//8 digit pertama dari bit 102
            message.put(102,request.getAccount_identification_1());//value dari customer Pan (dari request qris)

            String isoMessages = DataElementConverter.requestIso(requestInquiryMpan, message);
            log.info("inquiry request : "); log.info(isoMessages);

            log.info("kirim ke socket");
            String isoMessageFromServer = messageClientService.sendMessage(isoMessages);
            log.info("balikan iso message = "); log.info(isoMessageFromServer);
            if (isoMessageFromServer.equalsIgnoreCase(timeout)) {
                response.setResponse_code(timeout);
                return response;
            }
            Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);

            Integer lengthBit2 = objectResponse.get("Bit2").length();
            String formatLengthBit2  =  String.format("%2s", String.valueOf(lengthBit2)).replace(' ', '0');

            response.setPrimary_account_number((formatLengthBit2 + objectResponse.get("Bit2")));
            response.setProcessing_code(objectResponse.get("Bit3"));
            response.setTransaction_amount(objectResponse.get("Bit4"));
            response.setTransmission_date_and_time(objectResponse.get("Bit7"));
            response.setSystem_trace_audit_number(objectResponse.get("Bit11"));
            response.setLocal_transaction_time(objectResponse.get("Bit12"));
            response.setLocal_transaction_date(objectResponse.get("Bit13"));
            response.setSettlement_date(objectResponse.get("Bit15"));
            response.setCapture_date(objectResponse.get("Bit17"));
            response.setMerchants_type(objectResponse.get("Bit18"));
            response.setPoint_of_service_entry_mode(objectResponse.get("Bit22"));
            response.setAmount_convenience_fee(objectResponse.get("Bit28"));
            response.setAcquiring_institution_id(objectResponse.get(Bit32));
            response.setForwarding_institution_id(objectResponse.get(Bit32));
            response.setRetrieval_reference_number(objectResponse.get("Bit37"));
            response.setAuthorization_identification_response(objectResponse.get("Bit38"));
            response.setResponse_code(objectResponse.get("Bit39"));
            response.setCard_acceptor_terminal_identification(objectResponse.get("Bit41"));
            response.setCard_acceptor_id(objectResponse.get("Bit42"));
            response.setAdditional_data(objectResponse.get("Bit48"));
            response.setTransaction_currency_code(objectResponse.get("Bit49"));
            response.setAdditional_data_national(objectResponse.get(Bit57));
            response.setMerchantDataResponses(DataElementConverter.convertBit57Response(objectResponse.get(Bit57)));
            response.setReceiving_institution_id(objectResponse.get("Bit100"));
            response.setAccount_identification_1(objectResponse.get("Bit102"));
            return response;

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            //TODO: handle exception
        }
        return response;
    }
}
