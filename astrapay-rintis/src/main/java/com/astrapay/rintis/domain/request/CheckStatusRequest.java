package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckStatusRequest {
	public String command;
	public QRCheckStatusRequest data;
}
