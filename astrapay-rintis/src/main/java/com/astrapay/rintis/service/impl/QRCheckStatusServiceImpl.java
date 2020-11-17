package com.astrapay.rintis.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.astrapay.rintis.service.MessageClientService;
import org.jpos.iso.ISOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.astrapay.rintis.domain.request.CheckStatusRequest;
import com.astrapay.rintis.domain.response.QRCheckStatusIso8583Response;
import com.astrapay.rintis.domain.response.QRCheckStatusResponse;
import com.astrapay.rintis.util.DataElementConverter;

@Service
public class QRCheckStatusServiceImpl{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${message-type.request-check-status}")
	private String requestCheckStatus;
	@Value("${transaction-code.check-status}")
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
	
	private String Bit32 = "Bit32";
    private MessageClientService messageClientService;
	
	public QRCheckStatusServiceImpl(MessageClientService messageClientService) {
		this.messageClientService = messageClientService;
	}
	
	public Object requestQRCheckStatus(CheckStatusRequest request) throws ISOException, IOException {
		QRCheckStatusResponse response = new QRCheckStatusResponse();
		QRCheckStatusIso8583Response checkStatusIso8583Response = new QRCheckStatusIso8583Response();
		
		
		String pan = DataElementConverter.convertBit2(request.getData().getMerchant_request().getPan());
		String processingCode = "361000";
		String transAmount = DataElementConverter.convertBit4(new BigDecimal(request.getData().getTransaction_amount()), BigDecimal.ZERO);
		String transmisionDateAndTime = DataElementConverter.convertBit7(request.getData().getTransmission_date_and_time());
		String systemTraceAuditNumber = request.getData().getSystem_trace_audit_number();
		String localTransactionTime = DataElementConverter.convertBit12(request.getData().getLocal_transaction_time());
		String localTransactionDate = DataElementConverter.convertBit13(request.getData().getLocal_transaction_date());
		String settlementDate = DataElementConverter.convertBit15(request.getData().getSettlement_date());
		String captureDate = DataElementConverter.convertBit17(request.getData().getCapture_date());
		String merchantsType = request.getData().getMerchants_type();//mcc
		String pointsOfServiceEntryMode = "011";	
		String paymentCode = request.getData().getPayment_code();
		BigDecimal fee = new BigDecimal(request.getData().getFee());
		String amountConvenienceFee = DataElementConverter.convertBit28(fee, paymentCode);
		String acquiringInstitutionId = DataElementConverter.convertBit32(request.getData().getAcquiring_institution_id());
		String forwardingInstitutionId = DataElementConverter.convertBit33(request.getData().getForwarding_institution_id());
		String retrievalReferenceNumber = request.getData().getRetrieval_reference_number();
		String approvalCode = request.getData().getApproval_code();
		String responseCode = "bit 39";
		String cardAcceptorTerminalIdentification = DataElementConverter.convertBit41(request.getData().getCard_acceptor_terminal_identification());
		String cardAcceptorId = request.getData().getCard_acceptor_id();
		String cardAcceptorName = DataElementConverter.convertBit43(request.getData().getMerchant_request().getName(), request.getData().getMerchant_request().getCity(), request.getData().getMerchant_request().getCountry_code());
		String mpmCode = "Q001";
		String customer = request.getData().getCustomer_name();
		String merchantCriteria = request.getData().getMerchant_request().getMerchant_criteria();
		String additionalData = DataElementConverter.convertBit48AdditionalData(mpmCode, customer, merchantCriteria);
		String transactionCurrencyCode = request.getData().getCurrency_number();
		String additionalDataNational = DataElementConverter.convertBit57(request.getData().getAdditional_data_national());
		String isssuerId = DataElementConverter.convertBit100(request.getData().getCustomer_pan());
		String accountIdentification1 = DataElementConverter.convertBit102(request.getData().getCustomer_pan());
		String invoiceNumber = "Bit 123";
		
		LinkedHashMap<Integer, String> checkStatusRequest = new LinkedHashMap<>();
		checkStatusRequest.put(2, pan);//pan
		checkStatusRequest.put(3, processingCode);//processingCode
		checkStatusRequest.put(4, transAmount);//transAmount
		checkStatusRequest.put(7, transmisionDateAndTime);//transmisionDateAndTime
		checkStatusRequest.put(11, systemTraceAuditNumber);//systemTraceAuditNumber
		checkStatusRequest.put(12, localTransactionTime);//localTransactionTime
		checkStatusRequest.put(13, localTransactionDate);//localTransactionDate
		checkStatusRequest.put(15, settlementDate);//settlementDate
		checkStatusRequest.put(17, captureDate);//captureDate
		checkStatusRequest.put(18, merchantsType);//merchantsType
		checkStatusRequest.put(22, pointsOfServiceEntryMode);//pointsOfServiceEntryMode
		checkStatusRequest.put(28, amountConvenienceFee);//amountConvenienceFee
		checkStatusRequest.put(32, acquiringInstitutionId);//acquiringInstitutionId
		checkStatusRequest.put(33, forwardingInstitutionId);//forwardingInstitutionId
		checkStatusRequest.put(37, retrievalReferenceNumber);//retrievalReferenceNumber
		checkStatusRequest.put(38, approvalCode);//approvalCode
		checkStatusRequest.put(41, cardAcceptorTerminalIdentification);//cardAcceptorTerminalIdentification
		checkStatusRequest.put(42, cardAcceptorId);//cardAcceptorId
		checkStatusRequest.put(43, cardAcceptorName);//cardAcceptorName
		checkStatusRequest.put(48, additionalData);//additionalData
		checkStatusRequest.put(49, transactionCurrencyCode);//transactionCurrencyCode
		checkStatusRequest.put(57, additionalDataNational);//additionalDataNational
		checkStatusRequest.put(100, isssuerId);//issuerId
		checkStatusRequest.put(102, accountIdentification1);//accountIdentification1
		
		Set<Integer> keySet = checkStatusRequest.keySet();
        Object[] deActive = keySet.toArray();
        Integer[] activeDE = Arrays.stream(deActive)
                .toArray(Integer[]::new);
        String bitmapElement = "";
        bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
		if (bitmapElement.length() == 16) {
			response.setPrimary_bit_map(bitmapElement);
		}else if(bitmapElement.length() == 32){
			response.setPrimary_bit_map(bitmapElement.substring(0, 16));
			response.setSecondary_bit_map(bitmapElement.substring(16, 32));
		}
		String mti = requestCheckStatus;
		String mtiBitmap = mti + bitmapElement;
		 StringBuilder isoMessage = new StringBuilder();
	        for (Integer key:keySet) {
	            isoMessage.append(checkStatusRequest.get(key));
	        }
	        String isoMessages = mtiBitmap + isoMessage.toString();
	        log.info("check status request : " ); log.info(isoMessages);
	        
	      //Send iso message
	    log.info("kirim ke socket");
        String isoMessageFromServer = messageClientService.sendMessage(isoMessages);
	    log.info("balikan iso message = "); log.info(isoMessageFromServer);
	    Map<String,String> objectResponse = DataElementConverter.converterIsoToJson(isoMessageFromServer);
	    checkStatusIso8583Response.setMti(objectResponse.get("MTI"));
	    checkStatusIso8583Response.setBit0(isoMessageFromServer.substring(4,16+4));
	    checkStatusIso8583Response.setBit1(isoMessageFromServer.substring(20,16+20));
	    checkStatusIso8583Response.setBit2(objectResponse.get("Bit2"));
        checkStatusIso8583Response.setBit3(objectResponse.get("Bit3"));
        checkStatusIso8583Response.setBit4(objectResponse.get("Bit4"));
        checkStatusIso8583Response.setBit7(objectResponse.get("Bit7"));
        checkStatusIso8583Response.setBit11(objectResponse.get("Bit11"));
        checkStatusIso8583Response.setBit12(objectResponse.get("Bit12"));
        checkStatusIso8583Response.setBit13(objectResponse.get("Bit13"));
        checkStatusIso8583Response.setBit15(objectResponse.get("Bit15"));
        checkStatusIso8583Response.setBit17(objectResponse.get("Bit17"));
        checkStatusIso8583Response.setBit18(objectResponse.get("Bit18"));
        checkStatusIso8583Response.setBit22(objectResponse.get("Bit22"));
        checkStatusIso8583Response.setBit28(objectResponse.get("Bit28"));
        checkStatusIso8583Response.setBit32(objectResponse.get(Bit32));
        checkStatusIso8583Response.setBit33(objectResponse.get("Bit33"));
        checkStatusIso8583Response.setBit37(objectResponse.get("Bit37"));
        checkStatusIso8583Response.setBit38(objectResponse.get("Bit38"));
        checkStatusIso8583Response.setBit39(objectResponse.get("Bit39"));
        checkStatusIso8583Response.setBit41(objectResponse.get("Bit41"));
        checkStatusIso8583Response.setBit42(objectResponse.get("Bit42"));
        checkStatusIso8583Response.setBit48(objectResponse.get("Bit48"));
        checkStatusIso8583Response.setBit49(objectResponse.get("Bit49"));
        checkStatusIso8583Response.setBit100(objectResponse.get("Bit100"));
        checkStatusIso8583Response.setBit102(objectResponse.get("Bit102"));//
        checkStatusIso8583Response.setBit123(objectResponse.get("Bit123"));//
        return checkStatusIso8583Response;
	}
	
	 public QRCheckStatusResponse checkStatus2(CheckStatusRequest request) {
		 	QRCheckStatusResponse response = new QRCheckStatusResponse();
	        LinkedHashMap<Integer,String> message = new LinkedHashMap<Integer, String>();
	        try {
	            message.put(2,request.getData().getMerchant_request().getPan());
	            message.put(3,request.getData().getProcessing_code());
	            message.put(4,request.getData().getTransaction_amount());
	            message.put(7,request.getData().getTransmission_date_and_time());
	            message.put(11,request.getData().getSystem_trace_audit_number());
	            message.put(12,request.getData().getLocal_transaction_time());
	            message.put(13,request.getData().getLocal_transaction_date());
	            if (request.getData().getSettlement_date()!=null) {
	            	message.put(15,request.getData().getSettlement_date());//bit 28
	            }
	            message.put(17,request.getData().getCapture_date());
	            message.put(18,request.getData().getMerchants_type()); //mcc
	            message.put(22,request.getData().getPoint_of_service_entry_mode()); // need ask to rintis
	            if (request.getData().getAmount_convenience_fee()!=null) {
	                message.put(28, request.getData().getAmount_convenience_fee());//bit 28
	            }
	            message.put(32,request.getData().getAcquiring_institution_id()); //NNS
	            if (request.getData().getForwarding_institution_id()!=null) {
	            	message.put(33,request.getData().getForwarding_institution_id());//bit 28
	            }
//	            message.put(33,request.getData().getForwarding_institution_id());// LLVAR + Rintis Code
	            message.put(37,request.getData().getRetrieval_reference_number());//generate random string
	            message.put(38,request.getData().getApproval_code()); // need ask (Authorization Identification Response)
	            message.put(41,request.getData().getCard_acceptor_terminal_identification()); //terminal label
	            message.put(42,request.getData().getCard_acceptor_id());// merchantId
	            message.put(43,request.getData().getCard_acceptor_name_location());
	            message.put(48,request.getData().getAdditional_data()); // masih hardcode
	            message.put(49,request.getData().getTransaction_currency_code());//dapet dari translate
	            message.put(57,request.getData().getAdditional_data_national());
	            message.put(100,request.getData().getIssuer_id());//8 digit pertama dari bit 102
	            message.put(102,request.getData().getAccount_identification_1());//value dari customer Pan (dari request qris)

	            String isoMessages = DataElementConverter.requestIso(requestCheckStatus, message);
	            log.info("check status request : "); log.info(isoMessages);

	            log.info("kirim ke socket");
                String isoMessageFromServer = messageClientService.sendMessage(isoMessages);
	            log.info("balikan iso message check status = "); log.info(isoMessageFromServer);
	            if (isoMessageFromServer.equalsIgnoreCase("Timeout")) {
	            	response.setResponse_code("Timeout");
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
	            response.setAdditional_data_national(objectResponse.get("Bit57"));
	            response.setReceiving_institution_id(objectResponse.get("Bit100"));
	            response.setAccount_identification_1(objectResponse.get("Bit102"));
	            response.setInvoice_number(objectResponse.get("Bit123"));
	            return response;

	        } catch (Exception e) {
	            log.error(e.getMessage(),e);
	            //TODO: handle exception
	        }
	        return response;
	    }
}
