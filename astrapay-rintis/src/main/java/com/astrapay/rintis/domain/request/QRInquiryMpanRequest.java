package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRInquiryMpanRequest {
	public String pan;//bit2
	public String processing_code;//bit3
	public String transaction_amount;//bit4
	public String transmission_date_and_time;//bit7
	public String system_trace_audit_number;//bit11
	public String local_transaction_time;//bit12
	public String local_transaction_date;//bit13
	public String settlement_date;//bit15
	public String capture_date;//bit17
	public String merchants_type;//bit18
	public String point_of_service_entry_mode;//bit22
	public String amount_convenience_fee;//bit28
	public String acquiring_institution_id;//bit32
	public String forwarding_institution_id;//bit33
	public String retrieval_reference_number;//bit37
	public String approval_code;//Authorization Identification Response --> bit38
	public String card_acceptor_terminal_identification;//bit41
	public String card_acceptor_id;//bit42
	public String merchant_name;
	public String merchant_city;
	public String merchant_country_code;
	public String card_acceptor_name_location;//bit43
	public String additional_data;//bit48
	public String transaction_currency_code;//bit49
	public String issuer_id;//bit100
	public String account_identification_1;//bit102
}
