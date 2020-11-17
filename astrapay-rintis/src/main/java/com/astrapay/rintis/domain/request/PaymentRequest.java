package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String pan;//bit2(merchant pan)
    private String processingCode;//bit3
    private String transactionAmount;//bit4
    private String transmissionDateTime;//bit7
    private String systemsTraceAuditNumber;//bit11
    private String localTransactionTime;//bit12
    private String localTransactionDate;//bit13
    private String settlementDate;//bit15 --> conditional
    private String captureDate;//bit17
    private String merchantsType;//bit18(mcc)
    private String pointOfServiceEntryMode;//bit22
    private String amountConvenienceFee;//bit28 (tips) --> conditional
    private String acquiringInstitutionId;//bit32(issuer NNS)
    private String forwardingInstitutionId;//bit33 --> conditional
    private String retrievalReferenceNumber;//bit37
    private String approvalCode;//bit38
    private String cardAcceptorTerminalIdentification;//bit41(terminal label)
    private String cardAcceptorId;//bit42(merchant id)
    private String cardAcceptorNameAndLocation;//bit43
    private String additionalData;//bit48
    private String transactionCurrencyCode;//bit49
    private String additionalDataNational;//bit57
    private String issuerId;//bit100
    private String accountIdentification1;//bit102 (customer pan)
}
