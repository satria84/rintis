package com.astrapay.rintis.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QRInquiryMpanResponse {
    public String message_type;
    public String primary_bit_map;
    public String secondary_bit_map;
    public String primary_account_number;
    public String processing_code;
    public String transaction_amount;
    public String transmission_date_and_time;
    public String system_trace_audit_number;
    public String local_transaction_time;
    public String local_transaction_date;
    public String settlement_date;
    public String capture_date;
    public String merchants_type;
    public String point_of_service_entry_mode;
    public String amount_convenience_fee;
    public String acquiring_institution_id;
    public String forwarding_institution_id;
    public String retrieval_reference_number;
    public String authorization_identification_response;
    public String response_code;
    public String card_acceptor_terminal_identification;
    public String card_acceptor_id;
    public String additional_data;
    public String transaction_currency_code;
    public String additional_data_national;
    public String receiving_institution_id;
    public String account_identification_1;
    public List<MerchantDataResponse> merchantDataResponses;
}