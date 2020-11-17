package com.astrapay.rintis.service.impl;

import com.astrapay.rintis.domain.request.*;
import com.astrapay.rintis.domain.response.*;
import com.astrapay.rintis.service.MessageClientService;
import com.astrapay.rintis.service.PaymentCreditService;
import com.astrapay.rintis.util.DataElementConverter;
import com.astrapay.rintis.util.MappingResponseCode;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentCreditServiceImpl implements PaymentCreditService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${message-type.request-payment-credit}")
    private String mtiPaymentCredit;
    @Value("${transaction-code.payment-credit}")
    private String transactionCodePaymentCredit;
    @Value("${transaction-code.inquiry-mpan}")
    private String transactionCodeInquiryMpan;
    @Value("${transaction-code.check-status}")
    private String transactionCodeCheckStatus;
    @Value("${from-account-type.savings}")
    private String fromAccountCode;
    @Value("${to-account-type.unspecified}")
    private String toAccountCode;
    @Value("${convenience-fee.credit}")
    private String paymentCode;
    @Value("${additional-data.mpm-code}")
    private String mpmCode;

    private QRInquiryMpanServiceImpl qrInquiryMpanService;
    private QRCheckStatusServiceImpl qrCheckStatusService;
    private MessageClientService messageClientService;

    public PaymentCreditServiceImpl(QRInquiryMpanServiceImpl qrInquiryMpanService, QRCheckStatusServiceImpl qrCheckStatusService, MessageClientService messageClientService) {
        this.qrInquiryMpanService = qrInquiryMpanService;
        this.qrCheckStatusService = qrCheckStatusService;
        this.messageClientService = messageClientService;
    }

    @SuppressWarnings("Duplicates")
    public Object paymentCreditRintis(QRISPaymentRequest request) {
        Gson gson = new Gson();
        String command = "qr-payment-credit";
        QRISPaymentCreditResponse qrisPaymentCreditResponse = new QRISPaymentCreditResponse();
        QRISPaymentCreditDataResponse qrisPaymentCreditDataResponse = new QRISPaymentCreditDataResponse();
        RintisPaymentResponse rintisPaymentResponse = new RintisPaymentResponse();
        PaymentRequest paymentRequest = new PaymentRequest();
        try {
            //Data parsing from Json to format Bit.
            paymentRequest.setPan(DataElementConverter.convertBit2(request.getData().getMerchant().getPan()));
            paymentRequest.setProcessingCode(DataElementConverter.convertBit3(transactionCodePaymentCredit, fromAccountCode, toAccountCode));
            paymentRequest.setTransactionAmount(DataElementConverter.convertBit4(request.getData().getAmount(), request.getData().getFee()));
            paymentRequest.setTransmissionDateTime(DataElementConverter.convertBit7(request.getData().getDate_time()));
            paymentRequest.setSystemsTraceAuditNumber(DataElementConverter.convertBit11());
            paymentRequest.setLocalTransactionTime(DataElementConverter.convertBit12(request.getData().getDate_time()));
            paymentRequest.setLocalTransactionDate(DataElementConverter.convertBit13(request.getData().getDate_time()));
            paymentRequest.setCaptureDate(DataElementConverter.convertBit17(request.getData().getDate_time()));
            paymentRequest.setMerchantsType(request.getData().getMerchant().getMcc());
            paymentRequest.setPointOfServiceEntryMode(DataElementConverter.convertBit22("true"));
            paymentRequest.setAcquiringInstitutionId(DataElementConverter.convertBit32(request.getData().getIssuer_nns()));
            paymentRequest.setRetrievalReferenceNumber(DataElementConverter.convertBit37());
            paymentRequest.setApprovalCode(request.getData().getAuthorization_id());
            paymentRequest.setCardAcceptorTerminalIdentification(DataElementConverter.convertBit41(request.getData().getTerminal_label()));
            paymentRequest.setCardAcceptorId(request.getData().getMerchant().getId());
            paymentRequest.setCardAcceptorNameAndLocation(DataElementConverter.convertBit43(request.getData().getMerchant().getName(), request.getData().getMerchant().getCity(), request.getData().getMerchant().getCountry_code()));
            paymentRequest.setAdditionalData(DataElementConverter.convertBit48AdditionalData(mpmCode, request.getData().getCustomer().getName(), request.getData().getMerchant().getCriteria()));
            paymentRequest.setTransactionCurrencyCode(request.getData().getCurrency_number());
            paymentRequest.setAdditionalDataNational(DataElementConverter.convertBit57(request.getData().getAdditional_data_national()));
            paymentRequest.setIssuerId(DataElementConverter.convertBit100(request.getData().getCustomer().getPan()));
            paymentRequest.setAccountIdentification1(DataElementConverter.convertBit102(request.getData().getCustomer().getPan()));

            //Inquiry
            if (request.getData().getQris_type().equals("Static")) {
                MerchantDataResponse merchantDataResponse = paymentInquiry(request, paymentRequest);
                paymentRequest.setPan(DataElementConverter.convertBit2(merchantDataResponse.getMerchant_pan()));
                paymentRequest.setCardAcceptorId(merchantDataResponse.getMerchant_id());
                paymentRequest.setAdditionalData(DataElementConverter.convertBit48AdditionalData(mpmCode, request.getData().getCustomer().getName(), merchantDataResponse.getMerchant_criteria()));
            }
            String checkRequest = checkRequestValue(paymentRequest);
            log.info(checkRequest);
            //Mapping payment request
            Map<Integer, String> rintisPaymentRequest = new LinkedHashMap<>();
            rintisPaymentRequest.put(2, paymentRequest.getPan());
            rintisPaymentRequest.put(3, paymentRequest.getProcessingCode());
            rintisPaymentRequest.put(4, paymentRequest.getTransactionAmount());
            rintisPaymentRequest.put(7, paymentRequest.getTransmissionDateTime());
            rintisPaymentRequest.put(11, paymentRequest.getSystemsTraceAuditNumber());
            rintisPaymentRequest.put(12, paymentRequest.getLocalTransactionTime());
            rintisPaymentRequest.put(13, paymentRequest.getLocalTransactionDate());
            rintisPaymentRequest.put(17, paymentRequest.getCaptureDate());
            rintisPaymentRequest.put(18, paymentRequest.getMerchantsType());
            rintisPaymentRequest.put(22, paymentRequest.getPointOfServiceEntryMode());
            if (!request.getData().getFee().equals(new BigDecimal(0))) {
                paymentRequest.setAmountConvenienceFee(DataElementConverter.convertBit28(request.getData().getFee(), paymentCode));
                rintisPaymentRequest.put(28, paymentRequest.getAmountConvenienceFee());
            }
            rintisPaymentRequest.put(32, paymentRequest.getAcquiringInstitutionId());
            rintisPaymentRequest.put(37, paymentRequest.getRetrievalReferenceNumber());
            rintisPaymentRequest.put(38, paymentRequest.getApprovalCode());
            rintisPaymentRequest.put(41, paymentRequest.getCardAcceptorTerminalIdentification());
            rintisPaymentRequest.put(42, paymentRequest.getCardAcceptorId());
            rintisPaymentRequest.put(43, paymentRequest.getCardAcceptorNameAndLocation());
            rintisPaymentRequest.put(48, paymentRequest.getAdditionalData());
            rintisPaymentRequest.put(49, paymentRequest.getTransactionCurrencyCode());
            rintisPaymentRequest.put(57, paymentRequest.getAdditionalDataNational());
            rintisPaymentRequest.put(100, paymentRequest.getIssuerId());
            rintisPaymentRequest.put(102, paymentRequest.getAccountIdentification1());
            String isoMessages = DataElementConverter.requestIso(mtiPaymentCredit, rintisPaymentRequest);
            log.info("payment request : {}", isoMessages);

            //Send iso message
            String isoMessageFromServer = messageClientService.sendMessage(isoMessages);
            log.info("isi isoMessageFromServer : {}", isoMessageFromServer);
            if (isoMessageFromServer.equals("Timeout")) {
                Object timeoutCheckStatus = paymentCheckStatus(rintisPaymentRequest, request.getData().getCurrency_code(), request.getData().getCustomer_reference_number(), command);
                String jsonInString = gson.toJson(timeoutCheckStatus);
                log.info("Response timeout : {}", jsonInString);
                return timeoutCheckStatus;
            } else {
                Map<String, String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
                String paymentResponseCode = objectResponse.get("Bit39");
                if (paymentResponseCode.equals("68")) {
                    Object timeoutCheckStatus = paymentCheckStatus(rintisPaymentRequest, request.getData().getCurrency_code(), request.getData().getCustomer_reference_number(), command);
                    String jsonInString = gson.toJson(timeoutCheckStatus);
                    log.info("Response timeout : {}", jsonInString);
                    return timeoutCheckStatus;
                } else {
                    rintisPaymentResponse.setMti(objectResponse.get("MTI"));
                    rintisPaymentResponse.setBit0(isoMessageFromServer.substring(4, 16 + 4));
                    rintisPaymentResponse.setBit1(isoMessageFromServer.substring(20, 16 + 20));
                    rintisPaymentResponse.setBit2(objectResponse.get("Bit2"));
                    rintisPaymentResponse.setBit3(objectResponse.get("Bit3"));
                    rintisPaymentResponse.setBit4(objectResponse.get("Bit4"));
                    rintisPaymentResponse.setBit7(objectResponse.get("Bit7"));
                    rintisPaymentResponse.setBit11(objectResponse.get("Bit11"));
                    rintisPaymentResponse.setBit12(objectResponse.get("Bit12"));
                    rintisPaymentResponse.setBit13(objectResponse.get("Bit13"));
                    rintisPaymentResponse.setBit15(objectResponse.get("Bit15"));
                    rintisPaymentResponse.setBit17(objectResponse.get("Bit17"));
                    rintisPaymentResponse.setBit18(objectResponse.get("Bit18"));
                    rintisPaymentResponse.setBit22(objectResponse.get("Bit22"));
                    rintisPaymentResponse.setBit28(objectResponse.get("Bit28"));
                    rintisPaymentResponse.setBit32(objectResponse.get("Bit32"));
                    rintisPaymentResponse.setBit33(objectResponse.get("Bit33"));
                    rintisPaymentResponse.setBit37(objectResponse.get("Bit37"));
                    rintisPaymentResponse.setBit38(objectResponse.get("Bit38"));
                    rintisPaymentResponse.setBit39(objectResponse.get("Bit39"));
                    rintisPaymentResponse.setBit41(objectResponse.get("Bit41"));
                    rintisPaymentResponse.setBit42(objectResponse.get("Bit42"));
                    rintisPaymentResponse.setBit48(objectResponse.get("Bit48"));
                    rintisPaymentResponse.setBit49(objectResponse.get("Bit49"));
                    rintisPaymentResponse.setBit100(objectResponse.get("Bit100"));
                    rintisPaymentResponse.setBit102(objectResponse.get("Bit102"));//
                    rintisPaymentResponse.setBit123(objectResponse.get("Bit123"));//

                    //Mapping response payment credit to qris-engine
                    BigDecimal convertAmount = DataElementConverter.convertBit4FromIsoFormat(rintisPaymentResponse.getBit4());
                    log.info("convert amount : {}", convertAmount);
                    BigDecimal convertFee = new BigDecimal(0);
                    if (rintisPaymentResponse.getBit28() != null) {
                        convertFee = DataElementConverter.convertBit28FromIsoFormat(rintisPaymentResponse.getBit28());
                        log.info("convert fee : {}", convertFee);
                    }
                    String responseText = MappingResponseCode.getResponseText(rintisPaymentResponse.getBit39());
                    //Data
                    qrisPaymentCreditDataResponse.setCustomer_reference_number(request.getData().getCustomer_reference_number());
                    qrisPaymentCreditDataResponse.setForwarding_customer_reference_number(rintisPaymentResponse.getBit37());
                    qrisPaymentCreditDataResponse.setInvoice_no(rintisPaymentResponse.getBit123());
                    qrisPaymentCreditDataResponse.setCurrency_code(request.getData().getCurrency_code());
                    qrisPaymentCreditDataResponse.setAmount(convertAmount);
                    qrisPaymentCreditDataResponse.setFee(convertFee);

                    //Final Response
                    qrisPaymentCreditResponse.setCommand(command);
                    qrisPaymentCreditResponse.setResponse_code(rintisPaymentResponse.getBit39());
                    qrisPaymentCreditResponse.setResponse_text(responseText);
                    qrisPaymentCreditResponse.setData(qrisPaymentCreditDataResponse);
                    return qrisPaymentCreditResponse;
                }
            }
        } catch (Exception e) {
            qrisPaymentCreditResponse.setResponse_code("30");
            log.error(e.getMessage(), e);
        }
        return qrisPaymentCreditResponse;
    }

    @SuppressWarnings("Duplicates")
    public Object paymentCheckStatus(Map<Integer, String> request, String currencyCode, String customerReferenceNumber, String command) {
        QRISPaymentCreditResponse qrisPaymentCreditResponse = new QRISPaymentCreditResponse();
        QRISPaymentCreditDataResponse qrisPaymentCreditDataResponse = new QRISPaymentCreditDataResponse();
        Gson gson = new Gson();
        CheckStatusRequest csRequest = new CheckStatusRequest();
        QRCheckStatusRequest csDataRequest = new QRCheckStatusRequest();
        CheckStatusDataMerchantRequest csDataMerchantRequest = new CheckStatusDataMerchantRequest();
        String processingCodeCheckStatus = DataElementConverter.convertBit3(transactionCodeCheckStatus, fromAccountCode, toAccountCode);
        //Merchant
        csDataMerchantRequest.setPan(request.get(2));
        //Data
        csDataRequest.setProcessing_code(processingCodeCheckStatus);
        csDataRequest.setTransaction_amount(request.get(4));
        csDataRequest.setTransmission_date_and_time(request.get(7));
        csDataRequest.setSystem_trace_audit_number(request.get(11));
        csDataRequest.setLocal_transaction_time(request.get(12));
        csDataRequest.setLocal_transaction_date(request.get(13));
        csDataRequest.setSettlement_date(request.get(15));
        csDataRequest.setCapture_date(request.get(17));
        csDataRequest.setMerchants_type(request.get(18));
        csDataRequest.setPoint_of_service_entry_mode(request.get(22));
        if (request.get(28) != null) {
            csDataRequest.setAmount_convenience_fee(request.get(28));
        }
        csDataRequest.setAcquiring_institution_id(request.get(32));
        csDataRequest.setRetrieval_reference_number(request.get(37));
        csDataRequest.setApproval_code(request.get(38));
        csDataRequest.setCard_acceptor_terminal_identification(request.get(41));
        csDataRequest.setCard_acceptor_id(request.get(42));
        csDataRequest.setCard_acceptor_name_location(request.get(43));
        csDataRequest.setAdditional_data(request.get(48));
        csDataRequest.setTransaction_currency_code(request.get(49));
        csDataRequest.setAdditional_data_national(request.get(57));
        csDataRequest.setIssuer_id(request.get(100));
        csDataRequest.setAccount_identification_1(request.get(102));
        csDataRequest.setMerchant_request(csDataMerchantRequest);

        csRequest.setCommand("check-status");
        csRequest.setData(csDataRequest);

        int loopingCheckStatus = 4;
        for (int i = 1; i < loopingCheckStatus; i++) {
            QRCheckStatusResponse checkStatusResponse;
            try {
                checkStatusResponse = qrCheckStatusService.checkStatus2(csRequest);
                String jsonInString = gson.toJson(checkStatusResponse);
                log.info("response check status : {}", jsonInString);
                String csResponseCode = checkStatusResponse.getResponse_code();
                if (csResponseCode.equalsIgnoreCase("00") || csResponseCode.equalsIgnoreCase("A0")) {
                    BigDecimal convertAmount = DataElementConverter.convertBit4FromIsoFormat(checkStatusResponse.getTransaction_amount());
                    log.info("convert amount : {}", convertAmount);
                    BigDecimal convertFee = new BigDecimal(0);
                    if (checkStatusResponse.getAmount_convenience_fee() != null) {
                        convertFee = DataElementConverter.convertBit28FromIsoFormat(checkStatusResponse.getAmount_convenience_fee());
                        log.info("convert fee : {}", convertFee);
                    }
                    String responseText = MappingResponseCode.getResponseText(csResponseCode);
                    //Data
                    qrisPaymentCreditDataResponse.setCustomer_reference_number(customerReferenceNumber);
                    qrisPaymentCreditDataResponse.setForwarding_customer_reference_number(checkStatusResponse.getRetrieval_reference_number());
                    qrisPaymentCreditDataResponse.setInvoice_no(checkStatusResponse.getInvoice_number());
                    qrisPaymentCreditDataResponse.setCurrency_code(currencyCode);
                    qrisPaymentCreditDataResponse.setAmount(convertAmount);
                    qrisPaymentCreditDataResponse.setFee(convertFee);

                    //Final Response
                    qrisPaymentCreditResponse.setCommand(command);
                    qrisPaymentCreditResponse.setResponse_code(checkStatusResponse.getResponse_code());
                    qrisPaymentCreditResponse.setResponse_text(responseText);
                    qrisPaymentCreditResponse.setData(qrisPaymentCreditDataResponse);
                    return qrisPaymentCreditResponse;
                } else {
                    checkStatusResponse = qrCheckStatusService.checkStatus2(csRequest);
                    log.info("Response code : {} ", checkStatusResponse.getResponse_code());
                    log.info("Lopping check status : {}", i);
                    if (i == 3) {
                        qrisPaymentCreditDataResponse.setCustomer_reference_number("");
                        qrisPaymentCreditDataResponse.setForwarding_customer_reference_number("");
                        qrisPaymentCreditDataResponse.setInvoice_no("");
                        qrisPaymentCreditDataResponse.setCurrency_code("");
                        qrisPaymentCreditDataResponse.setAmount(new BigDecimal(0));
                        qrisPaymentCreditDataResponse.setFee(new BigDecimal(0));

                        //Final Response
                        qrisPaymentCreditResponse.setCommand(command);
                        qrisPaymentCreditResponse.setResponse_code("999");
                        qrisPaymentCreditResponse.setResponse_text("Force Credit");
                        qrisPaymentCreditResponse.setData(qrisPaymentCreditDataResponse);
                        return qrisPaymentCreditResponse;
                    }
                }
            } catch (Exception e) {
                qrisPaymentCreditResponse.setResponse_code("30");
                log.error(e.getMessage(), e);
                return qrisPaymentCreditResponse;
            }
        }
        return qrisPaymentCreditResponse;
    }

    public MerchantDataResponse paymentInquiry(QRISPaymentRequest request, PaymentRequest paymentRequest) {
        Gson gson = new Gson();
        MerchantDataResponse merchantDataResponseValue = new MerchantDataResponse();
        try {
            QRInquiryMpanRequest inquiryRequest = new QRInquiryMpanRequest();
            String processingCodeInquiry = DataElementConverter.convertBit3(transactionCodeInquiryMpan, fromAccountCode, toAccountCode);
            String settlementDate = DataElementConverter.convertBit15(request.getData().getDate_time());
            String nationalMidValue = request.getData().getNational_mid().substring(request.getData().getNational_mid().length() - 11);
            String panInquiry = DataElementConverter.convertBit2(request.getData().getIssuer_nns() + nationalMidValue);
            inquiryRequest.setPan(panInquiry);//bit2
            inquiryRequest.setProcessing_code(processingCodeInquiry);//bit3
            inquiryRequest.setTransaction_amount(paymentRequest.getTransactionAmount());//bit4
            inquiryRequest.setLocal_transaction_time(paymentRequest.getLocalTransactionTime());//bit7
            inquiryRequest.setSystem_trace_audit_number(paymentRequest.getSystemsTraceAuditNumber());
            inquiryRequest.setTransmission_date_and_time(paymentRequest.getTransmissionDateTime());
            inquiryRequest.setSystem_trace_audit_number(paymentRequest.getSystemsTraceAuditNumber());
            inquiryRequest.setLocal_transaction_time(paymentRequest.getLocalTransactionTime());
            inquiryRequest.setLocal_transaction_date(paymentRequest.getLocalTransactionDate());
            inquiryRequest.setSettlement_date(settlementDate);
            inquiryRequest.setCapture_date(paymentRequest.getCaptureDate());
            inquiryRequest.setMerchants_type(request.getData().getMerchant().getMcc());
            inquiryRequest.setPoint_of_service_entry_mode(paymentRequest.getPointOfServiceEntryMode());
            if (!request.getData().getFee().equals(new BigDecimal(0))) {
                inquiryRequest.setAmount_convenience_fee(paymentRequest.getAmountConvenienceFee());
            }
            inquiryRequest.setAcquiring_institution_id(paymentRequest.getAcquiringInstitutionId());
            inquiryRequest.setForwarding_institution_id("06360002");//Hardcode LLVAR + Rintis Code
            inquiryRequest.setRetrieval_reference_number(paymentRequest.getRetrievalReferenceNumber());
            inquiryRequest.setApproval_code(request.getData().getAuthorization_id());//--> generate
            inquiryRequest.setCard_acceptor_terminal_identification(paymentRequest.getCardAcceptorTerminalIdentification());
            inquiryRequest.setCard_acceptor_id(paymentRequest.getCardAcceptorId());//iniiiiiiiiiiii
            inquiryRequest.setCard_acceptor_name_location(paymentRequest.getCardAcceptorNameAndLocation());
            inquiryRequest.setAdditional_data("008PI04IQ02");
            inquiryRequest.setTransaction_currency_code(paymentRequest.getTransactionCurrencyCode());//iniiiiiiiiiiii
            inquiryRequest.setIssuer_id(paymentRequest.getIssuerId());
            inquiryRequest.setAccount_identification_1(paymentRequest.getAccountIdentification1());
            String jsonInString = gson.toJson(inquiryRequest);
            log.info("request inquiry : {}", jsonInString);

            QRInquiryMpanResponse inquiryResponse = qrInquiryMpanService.inquiryMpan2(inquiryRequest);
            String jsonInString2 = gson.toJson(inquiryResponse);
            log.info("response inquiry : {}", jsonInString2);
            List<MerchantDataResponse> merchantDataResponses = inquiryResponse.getMerchantDataResponses();
            if (inquiryResponse.getResponse_code().equalsIgnoreCase("00")) {
                for (MerchantDataResponse merchantDataResponse : merchantDataResponses) {
                    if (merchantDataResponse.getMerchant_pan().contains("93600822")) {
                        merchantDataResponseValue.setMerchant_pan(merchantDataResponse.getMerchant_pan());
                        merchantDataResponseValue.setMerchant_id(merchantDataResponse.getMerchant_id());
                        merchantDataResponseValue.setMerchant_criteria(merchantDataResponse.getMerchant_criteria());
                        break;
                    } else {
                        merchantDataResponseValue.setMerchant_pan(merchantDataResponse.getMerchant_pan());
                        merchantDataResponseValue.setMerchant_id(merchantDataResponse.getMerchant_id());
                        merchantDataResponseValue.setMerchant_criteria(merchantDataResponse.getMerchant_criteria());
                    }
                }
            } else {
                log.info("Response code error Inquiry : {}", inquiryResponse.getResponse_code());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return merchantDataResponseValue;
    }

    public String checkRequestValue(PaymentRequest paymentRequest) {
        String confirmation = "checked";
        if (paymentRequest.getPan().length() > 21)//bit2
            log.info("length merchant pan > 19");
        if (paymentRequest.getTransactionAmount().length() > 12)//bit4
            log.info("length amount > 12");
        if (paymentRequest.getMerchantsType().length() != 4)//bit18
            log.info("length mcc != 4");
        if (paymentRequest.getAmountConvenienceFee() != null && paymentRequest.getAmountConvenienceFee().length() > 9)//bit28
            log.info("length tips amount > 9");
        if (paymentRequest.getAcquiringInstitutionId().length() > 11)//bit32
            log.info("length nns > 11");
        if (paymentRequest.getCardAcceptorTerminalIdentification().length() != 16)//bit41
            log.info("length terminal label != 16");
        if (paymentRequest.getCardAcceptorId().length() != 15)//bit42
            log.info("length merchant id != 15");
        if (paymentRequest.getCardAcceptorNameAndLocation().length() != 40)//bit43
            log.info("length merchant id != 40");
        if (paymentRequest.getIssuerId().length() > 13)//bit100
            log.info("length issuer id > 11");
        if (paymentRequest.getAccountIdentification1().length() > 21)//bit102
            log.info("length customer pan > 19");
        return confirmation;
    }
}
