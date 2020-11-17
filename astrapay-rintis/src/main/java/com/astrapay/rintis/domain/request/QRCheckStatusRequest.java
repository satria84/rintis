package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRCheckStatusRequest {
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
	public String fee;
	public String amount_convenience_fee;
	public String payment_code;
	public String acquiring_institution_id;
	public String forwarding_institution_id;
	public String retrieval_reference_number;
	public String approval_code;
	public String card_acceptor_terminal_identification;
	public String card_acceptor_id;
	public String card_acceptor_name_location;
	public String additional_data;
	public String transaction_currency_code;
	public String additional_data_national;
	public String issuer_id;
	public String account_identification_1;
	public String currency_number;
	public String customer_pan;
	public String customer_name;
	public CheckStatusDataMerchantRequest merchant_request;
	
}
