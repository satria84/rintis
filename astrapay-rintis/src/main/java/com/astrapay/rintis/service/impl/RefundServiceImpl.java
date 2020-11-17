package com.astrapay.rintis.service.impl;


import com.astrapay.rintis.domain.request.RefundIssuerCoreRequest;
import com.astrapay.rintis.domain.response.CoreRefundIssuerResponse;
import com.astrapay.rintis.service.MessageClientService;
import com.astrapay.rintis.service.RefundService;
import com.astrapay.rintis.util.DataElementConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RefundServiceImpl implements RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundServiceImpl.class);
    private final DataElementConverter dataElementConverter = new DataElementConverter();
    private MessageClientService messageClientService;
    @Value("${message-type.financial-transaction-response}")
    private String financialResponse;
    @Value("${transaction-code.refund}")
    private String transactionCode;
    @Value("${from-account-type.savings}")
    private String fromAccountCode;
    @Value("${to-account-type.unspecified}")
    private String toAccountCode;

    @SuppressWarnings("Duplicates")
    public String refundIssuer(String iso) {
        RefundIssuerCoreRequest coreRequest = new RefundIssuerCoreRequest();
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        try {
            Map<String, String> isoValue = DataElementConverter.converterIsoToJson(iso);
            BigDecimal amount = DataElementConverter.convertBit4FromIsoFormat(isoValue.get("Bit4"));
            coreRequest.setAmount(amount);
            coreRequest.setDateTime(sdf.format(currentTime));
            coreRequest.setInvoiceNo(isoValue.get("Bit123"));//need to follow up
            String coreResponse = requestRefundToCore(coreRequest);
            LinkedHashMap<Integer,String> isoResponse = new LinkedHashMap<Integer, String>();
            String mti = financialResponse;
            isoResponse.put(2,DataElementConverter.convertBit2(isoValue.get("Bit2")));
            isoResponse.put(3,isoValue.get("Bit3"));
            isoResponse.put(4,isoValue.get("Bit4"));
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            isoResponse.put(7,DataElementConverter.convertBit7(sdf.format(currentTime)));
            isoResponse.put(11, isoValue.get("Bit11"));
            isoResponse.put(12, isoValue.get("Bit12"));
            isoResponse.put(13, isoValue.get("Bit13"));
            isoResponse.put(15, DataElementConverter.convertBit15(sdf.format(currentTime)));
            isoResponse.put(17,isoValue.get("Bit17"));
            isoResponse.put(18,isoValue.get("Bit18"));
            isoResponse.put(22, isoValue.get("Bit22"));
            isoResponse.put(28,isoValue.get("Bit28"));
            isoResponse.put(32, DataElementConverter.convertBit32(isoValue.get("Bit32")));
            isoResponse.put(33, "06360002");
            isoResponse.put(37,isoValue.get("Bit37"));
            isoResponse.put(38, DataElementConverter.alfanumeric(6));
            switch (coreResponse) {
                case "SUCCESS_REFUND":
                    isoResponse.put(39,"00");
                    log.info("Success refund");
                    break;
                case "INVALID_AMOUNT":
                    isoResponse.put(39,"13");
                    log.error("Invalid Amount");
                    break;
                case "FAILED_NOT_FOUND":
                    isoResponse.put(39,"12");
                    log.error("Failed Refund Because Transaction Not Found In Core");
                    break;
                case "FAILED_REFUND":
                    isoResponse.put(39,"12");
                    log.error("Failed Refund Error From Core");
                    break;
                case "INVALID_TRANSACTION_TYPE":
                    isoResponse.put(39,"12");
                    log.error("Failed Refund Because Transaction Type Is Already Refund In Core");
                    break;
                case "FAILED_TIMEOUT":
                    isoResponse.put(39,"91");
                    log.error("TIMEOUT");
                    break;
                case "ERROR_INTERNAL":
                    isoResponse.put(39,"91");
                    log.error("Error Internal");
                    break;
                case "CORE_NORESPONSE":
                    isoResponse.put(39,"91");
                    log.error("Core Not Give Response");
                    break;
                default:
                    isoResponse.put(39,"91");
                    log.error("Unpredict Error See LOG Core");
                    break;
            }
            isoResponse.put(41,isoValue.get("Bit41"));
            isoResponse.put(42,isoValue.get("Bit42"));
            isoResponse.put(48, isoValue.get("Bit48"));
            isoResponse.put(49,isoValue.get("Bit49"));
            isoResponse.put(100,DataElementConverter.convertBit100(isoValue.get("Bit100")));
            isoResponse.put(102,isoValue.get("Bit102"));
            isoResponse.put(123,isoValue.get("Bit123"));
            Set<Integer> keySet = isoResponse.keySet();
            Object[] deActive = keySet.toArray();
            Integer[] activeDE = Arrays.stream(deActive)
                    .toArray(Integer[]::new);
            String bitmapElement = "";
            bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
            String mtiBitmap = mti + bitmapElement.toUpperCase();
            StringBuilder isoMessage = new StringBuilder();
            for (Integer key:keySet) {
                isoMessage.append(isoResponse.get(key));
            }
            String isoMessages = mtiBitmap + isoMessage.toString();
            log.info(isoMessages);
            return isoMessages;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error";
        }
    }

    public String requestRefundToCore(RefundIssuerCoreRequest requestCore) throws NoSuchAlgorithmException {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Gson gson = new Gson();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS).build();
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, gson.toJson(requestCore));
            CoreRefundIssuerResponse coreResponse;
            okhttp3.Request request = new okhttp3.Request.Builder()
//                    .url("http://localhost:8090/fifpayrest/pds/issuerrefund/advanced").method("POST", body)//jangan hardcode
                    .url("https://cygnus.astrapay.com//fifpayrest/pds/issuerrefund/advanced").method("POST", body)
                    .addHeader("Content-Type", "application/json").build();
            okhttp3.Response response = client.newCall(request).execute();
            // TODO Auto-generated method stub
            String resp = response.body().string();
            System.out.println("Response CORE : " + resp.replaceAll("/(\"[^\"]*\")|\\s/g", ""));
            JsonObject jsonObject = new JsonParser().parse(resp).getAsJsonObject();
            coreResponse = gson.fromJson(jsonObject.getAsJsonObject(), CoreRefundIssuerResponse.class);
            if (coreResponse == null) {
                return "CORE_NORESPONSE";
            } else {
                return coreResponse.getErrorJson().getType();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof SocketTimeoutException) {
                log.error("Time Out");
                return "FAILED_TIMEOUT";
            }
            return "ERROR_INTERNAL";
        }
    }
}
