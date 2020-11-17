package com.astrapay.rintis.util;

import com.astrapay.rintis.domain.response.MerchantDataResponse;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataElementConverter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
    private static final Random rnd = new Random();
    //Combine Request
    @SuppressWarnings("Duplicates")
    public static String requestIso (String mti, Map<Integer, String> request){
        Set<Integer> keySet = request.keySet();
        Object[] deActive = keySet.toArray();
        Integer[] activeDE = Arrays.stream(deActive)
                .toArray(Integer[]::new);
        String bitmapElement = "";
        bitmapElement = DataElementConverter.getHexaBitmapFromActiveDE(activeDE);
        String mtiValue = mti;
        String mtiBitmap = mtiValue + bitmapElement;
        StringBuilder isoMessage = new StringBuilder();
        for (Integer key:keySet) {
            isoMessage.append(request.get(key));
        }
        String isoMessages = mtiBitmap + isoMessage.toString();
        return isoMessages;
    }

	public static String getHexaBitmapFromActiveDE(Integer[] activeDE) {
        StringBuilder finalHexaBitmap = new StringBuilder();
        StringBuilder binaryBitmapForReply = new StringBuilder();
 
        boolean secondarBitmapActive = false;
        int panjangBitmap = 16;
        // pengecekan secondary bitmap
        for (int i=0; i<activeDE.length;i++) {
            if (activeDE[i] > 64) {
                secondarBitmapActive = true;
                panjangBitmap = 32;
            }
        }
 
        // x4 untuk mendapatkan jumlah seluruh data elemen
        panjangBitmap *= 4;
        int counterBitmap=0;
        String active = "";
        for (int i=0;i<panjangBitmap; i++) {
            counterBitmap++;
            active = "0";
            for (int j=0; j<activeDE.length; j++) {
                if (counterBitmap == activeDE[j]) active = "1";
            }
 
            binaryBitmapForReply.append(active);
        }
 
        // karena secondary bitmap active, bit pertama ganti jadi 1
        if (secondarBitmapActive) {
            binaryBitmapForReply = new StringBuilder("1"+binaryBitmapForReply.toString().substring(1, binaryBitmapForReply.length()));
        }
 
        char[] binaryBitmapChar = binaryBitmapForReply.toString().toCharArray();
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<binaryBitmapChar.length;i++) {
            sb.append(binaryBitmapChar[i]);
            counter++;
 
            if (counter == 4) {
                finalHexaBitmap.append(DataElementConverter.binaryToHexa(sb.toString()));
                sb = new StringBuilder();
                counter=0;
            }
        }
 
        return finalHexaBitmap.toString();
    }
    public static String decimalToHexa(Integer decimalNumber) {
        return Integer.toHexString(decimalNumber);
    }
 
    public static String decimalToBinary(Integer decimalNumber) {
        StringBuilder binaryNumber = new StringBuilder();
        StringBuilder sbBinary = new StringBuilder();
        String binaryString = Integer.toBinaryString(decimalNumber);
        char[] binary = binaryString.toCharArray();
        int counter = 0;
        // ambil dari index karakter terakhir
        for (int i=binary.length-1; i>=0; i--) {
            counter++;
            sbBinary.append(binary[i]);
            // reset counter ke nol jika berhasil mengambil 4 digit karakter
            if (counter == 4) counter = 0;
        }
 
        // 4 adalah panjang karakter tiap blok di binary
        // ex: dec [100] == binary [0110 0100]
        for (int i=0; i<4-counter; i++) {
            if (counter > 0) sbBinary.append("0");
        }
 
        // sekarang dibalik
        for (int i=sbBinary.length()-1; i>=0;i--) {
            binaryNumber.append(sbBinary.toString().charAt(i));
        }
 
        return binaryNumber.toString();
    }
 
    public static Integer binaryToDecimal(String binaryNumber) {
        return Integer.parseInt(binaryNumber, 2);
    }
 
    public static String binaryToHexa(String binaryNumber) {
        return decimalToHexa(binaryToDecimal(binaryNumber));
    }
 
    public static Integer hexaToDecimal(String hexaNumber) {
        return Integer.parseInt(hexaNumber, 16);
    }
 
    public static String hexaToBinary(String hexaNumber) {
        return decimalToBinary(hexaToDecimal(hexaNumber));
    }

    //convert Merchant Pan
    public static String convertBit2(String merchantPan){
	    int length = merchantPan.length();
	    String merchantPanLength  =  String.format("%2s", String.valueOf(length)).replace(' ', '0');
	    String valueBit2 = merchantPanLength + merchantPan;
        return valueBit2;
    }

    //convert Processing Code
    public static String convertBit3(String transactionCode, String fromAccountCode, String toAccountCode){
	    String valueBit3 = transactionCode + fromAccountCode + toAccountCode;
	    return valueBit3;
    }

    //convert Amount
    public static String convertBit4(BigDecimal amount, BigDecimal fee){
	    BigDecimal finalAmount;
        String decimalValue = "00";
	    if(fee != null && !fee.equals(new BigDecimal(0))){
	        finalAmount = amount.add(fee);
        }else{
	        finalAmount = amount;
        }
        String valueBit4 = String.format("%010d", finalAmount.toBigInteger()) + decimalValue;
        return valueBit4;
    }

    @SuppressWarnings("Duplicates")
    public static BigDecimal convertBit4FromIsoFormat(String amount){
        String deleteZeroPad;
        String finalAmount;
        String regex = "^0+";
        deleteZeroPad = amount.replaceAll(regex, "");
        finalAmount = deleteZeroPad.substring(0, deleteZeroPad.length() - 2);
        BigDecimal amountBigDec = new BigDecimal(finalAmount);
        return amountBigDec;
    }

    @SuppressWarnings("Duplicates")
    public static BigDecimal convertBit28FromIsoFormat(String fee){
	    String deletePaymentCode;
        String deleteZeroPad;
        String finalFee;
        String regex = "^0+";
        deletePaymentCode = fee.substring(1);
        deleteZeroPad = deletePaymentCode.replaceAll(regex, "");
        finalFee = deleteZeroPad.substring(0, deleteZeroPad.length() - 2);
        BigDecimal feeBigDec = new BigDecimal(finalFee);
        return feeBigDec;
    }

    //convert Date Time
    public static String convertBit7(String oldDate) {
        Date convertedDate;
        String valueBit7 = "";
        try {
            convertedDate = dateFormat.parse(oldDate);
            SimpleDateFormat sdfNewFormat = new SimpleDateFormat("MMddHHmmss");
            sdfNewFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            valueBit7 = sdfNewFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valueBit7;
    }

    //convert Time
    public static String convertBit12(String oldDate) {
        Date convertedDate;
        String valueBit12 = "";
        try {
            convertedDate = dateFormat.parse(oldDate);
            SimpleDateFormat sdfNewFormat = new SimpleDateFormat("HHmmss");
            valueBit12 = sdfNewFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valueBit12;
    }

    //convert Date
    public static String convertBit13(String oldDate) {
        Date convertedDate;
        String valueBit13 = "";
        try {
            convertedDate = dateFormat.parse(oldDate);
            SimpleDateFormat sdfNewFormat = new SimpleDateFormat("MMdd");
            valueBit13 = sdfNewFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valueBit13;
    }

    //convert Convenience Fee
    public static String convertBit28(BigDecimal fee, String paymentCode){
	    String decimalValue = "00";
        String valueBit28 = paymentCode + String.format("%06d", fee.toBigInteger()) + decimalValue;
        return valueBit28;
    }

    //Acquiring Institution Identification Code
    public static String convertBit32 (String issuerNns){
        int length = issuerNns.length();
        String issuerNnsLength  =  String.format("%2s", String.valueOf(length)).replace(' ', '0');
        String valueBit32 = issuerNnsLength + issuerNns;
        return valueBit32;
    }

    //Card Acceptor Terminal Identification
    public static String convertBit41 (String terminalLabel){
        String valueBit41;
        if (terminalLabel.length() > 16) {
            valueBit41 = terminalLabel.substring(terminalLabel.length() - 16);
        } else {
            valueBit41 = String.format("%-16s", terminalLabel);
        }
	    return valueBit41;
    }

    //convert Date to YYMM
    public static String convertBit14(String oldDate) {
        Date convertedDate;
        String valueBit14 = "";
        try {
            convertedDate = dateFormat.parse(oldDate);
            SimpleDateFormat sdfNewFormat = new SimpleDateFormat("yyMM");
            valueBit14 = sdfNewFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valueBit14;
    }
    //convert Date to MMDD
    public static String convertBit15(String oldDate) {
        Date convertedDate;
        String valueBit15 = "";
        try {
            convertedDate = dateFormat.parse(oldDate);
            SimpleDateFormat sdfNewFormat = new SimpleDateFormat("MMdd");
            valueBit15 = sdfNewFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valueBit15;
    }

    //convert Date MMDD
    public static String convertBit17(String oldDate) {
        Date convertedDate;
        String valueBit17 = "";
        Integer cutOfftime = 2330;
        Integer jmlDate = 1;
        try {
            convertedDate = dateFormat.parse(oldDate);
            SimpleDateFormat sdfNewFormat = new SimpleDateFormat("MMdd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HHmm");
            String hour = hourFormat.format(convertedDate);
            
            if(Integer.valueOf(hour) > cutOfftime){
                Calendar cal = Calendar.getInstance();
                cal.setTime(convertedDate);
                cal.add(Calendar.DATE, jmlDate);
                valueBit17 = sdfNewFormat.format(cal.getTime());
            } else {
                valueBit17 = sdfNewFormat.format(convertedDate);
            }            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valueBit17;
    }
    
    public static String convertBit11() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    
  //Card Acceptor Name and Location
    public static String convertBit43 (String merchantName, String merchantCity, String countryCode) {
    	StringBuilder valueBit43 = new StringBuilder();

    	if (merchantName.length() > 25) {
			merchantName = merchantName.substring(0,25);
		}else if(merchantName.length() < 25) {
			merchantName = String.format("%-25s", merchantName);
		}
    	
    	if (merchantCity.length() > 13) {
			merchantCity = merchantCity.substring(0, 13);
		}else if (merchantCity.length() < 13) {
            merchantCity = String.format("%-13s", merchantCity);
		}
    	
    	if (countryCode.length() > 2) {
			countryCode = countryCode.substring(0,2);
		}else if(countryCode.length() < 2) {
            countryCode = String.format("%-2s", countryCode);
		}
    	
    	valueBit43.append(merchantName);
    	valueBit43.append(merchantCity);
    	valueBit43.append(countryCode);
    	
    	return valueBit43.toString();
    }

    //Additional Data - National
    //Perhatian - supaya lebih mudah  
    //string element ini berasal dari parameter khusus dari android
    //ciri-cirinya : jika inquiry isinya diawali dengan angka antara 26 hingga 45, jika bukan inquiry diawali dengan 61
    //Terima kasih - salam olahraga
    public static String convertBit57(String element) {
    	String LLLVAR = "";
    	Integer elementLength = element.length();
    	String valueBit57 = "";
    	
    	if (elementLength < 10) {
			LLLVAR = "00"+elementLength.toString();
		}else if(elementLength < 100) {
			LLLVAR = "0"+elementLength.toString();
		}else {
			LLLVAR = elementLength.toString();
		}
    	
    	valueBit57 = LLLVAR+element;
    	return valueBit57;
    }

    // Issuer Institution Identification Code
    public static String convertBit100(String accoutNumber) {
    	String LLVAR = "08";
    	String valueBit100 = "";
    	valueBit100 = LLVAR+accoutNumber.substring(0,8);
    	
    	return valueBit100;
    }

    // From Account Number
    public static String convertBit102(String element){ //element ini customerPan jika bukan request refund
	    String LLVAR = "";
        Integer elementLength = element.length();
        String valueBit102 = "";

        if (elementLength < 10) {
            LLVAR = "0" + elementLength.toString();
        }else {
            LLVAR = elementLength.toString();
        }
        valueBit102 = LLVAR+element;
        return valueBit102;
    }

    @SuppressWarnings("Duplicates")
    public static Map<String,String> converterIsoToJson(String iso) {
        Map<String,String>converted = new HashMap<>();
        try{
            GenericPackager packager = new GenericPackager("packager/iso8583.xml");
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            byte[] bIsoMessage = new byte[iso.length()];
            for (int i = 0; i < bIsoMessage.length; i++) {
                bIsoMessage[i] = (byte) (int) iso.charAt(i);
            }
            isoMsg.unpack(bIsoMessage);
            converted.put("MTI",isoMsg.getMTI());
            for(int i=1; i<=isoMsg.getMaxField(); i++){
                if(isoMsg.hasField(i)){
                    converted.put("Bit"+i,isoMsg.getString(i));
                    System.out.println(i+"='"+isoMsg.getString(i)+"'");
                }
            }
            System.out.println(converted);
        }catch (Exception e){
            e.printStackTrace();
        }
        return converted;
    }

    // Additional Data
    public static String convertBit48AdditionalData(String mpmCode, String customer, String merchantCriteria){
        String LLLVAR = "";
        String valueBit48 ="";
        String name = "";

        Integer mpmLength = mpmCode.length();
        String LLVARMPM = "";
        if(mpmLength <10){
            LLVARMPM = "0" + mpmLength.toString();
        }
        String mpm = "PI"+LLVARMPM+mpmCode;

        if(customer.length() > 30){
            name = customer.substring(0, 30);
        } else {
            name = customer;
        }        
        String dataCustomer = "CD"+name.length()+name;

        Integer mcLength = merchantCriteria.length();
        String LLVARMC = "";
        if(mcLength<10){
            LLVARMC = "0" +mcLength.toString();
        }
        String MC = "MC"+LLVARMC+merchantCriteria;

        String element = mpm+dataCustomer+MC;
        if(element.length()<10){
            LLLVAR = "00" + element.length();
        } else if(element.length() <100) {
            LLLVAR = "0" + element.length();
        } else {
            LLLVAR = String.valueOf(element.length());
        }
        valueBit48 = LLLVAR + element;
        return valueBit48;
    }

    //Transaction Currency Code
    public static String convertBit49(String currencyCode){
        String valueBit49 = "";
        if(currencyCode.equals("IDR")){
            valueBit49 = "360";
        } else {
            valueBit49 = currencyCode;
        }
        return valueBit49;
    }

    //Invoice Number
    public static String convertBit123(String response, String invoiceNumberAcquirer, String invoiceNumberOriginal){
        String valueBit123 = "";
        String LLLVAR = "";
        Integer DELength = invoiceNumberAcquirer.length() + invoiceNumberOriginal.length();
        if(response.equals("00")){
            if(DELength<100){
                LLLVAR = "0" + DELength.toString();
            }
            valueBit123 = LLLVAR + invoiceNumberAcquirer + invoiceNumberOriginal;
        } else {
            valueBit123 = "0";
        }
        return valueBit123;
    }

    //Retrieval Reference Number
    public static String convertBit37() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    //Authorization Identification Response
    public static String convertBit38(String response, String authorizenID){
        // authorizenID length = 6
        String valueBit38 = "";
        if(response.equals("00")){
            valueBit38 = authorizenID;
        } else {
            valueBit38 = "0";
        }
        return valueBit38;
    }

    //masih belum jelas dari rintisnya
    public static String convertBit22(String statusPin){
        String valueBit22 = "";
        if(statusPin.equals("true")){
            valueBit22 = "01"+"1";
        } else {
            valueBit22 = "01"+"2";
        }
        return valueBit22;
    }

    public static String convertBit33 (String institutionCode){
        //code rintis (360000)
        int length = institutionCode.length();
        String institutionCodeLength  =  String.format("%2s", String.valueOf(length)).replace(' ', '0');
        String valueBit33 = institutionCodeLength + institutionCode;
        return valueBit33;
    }

    public static List<MerchantDataResponse> convertBit57Response (String isoMessage){
        List<MerchantDataResponse> merchantDataResponses = new ArrayList<>();
        MerchantDataResponse merchantDataResponse = new MerchantDataResponse();

        while (!isoMessage.equalsIgnoreCase("")){
            String tag = isoMessage.substring(0, 2);
            String lengthTag = isoMessage.substring(2, 4);
            String dataTag = isoMessage.substring(4, 4 + Integer.parseInt(lengthTag));

            String lengthSubTag01 = dataTag.substring(2, 4);
            String valueSubTag01 = dataTag.substring(4, 4 + Integer.parseInt(lengthSubTag01));
            merchantDataResponse.setMerchant_pan(valueSubTag01);

            String dataSubTag02 = dataTag.substring(4 + Integer.parseInt(lengthSubTag01));
            String lengthSubTag02 = dataSubTag02.substring(2, 4);
            String valueSubTag02 = dataSubTag02.substring(4, 4 + Integer.parseInt(lengthSubTag02));
            merchantDataResponse.setMerchant_id(valueSubTag02);

            String dataSubTag03 = dataSubTag02.substring(4 + Integer.parseInt(lengthSubTag02));
            String lengthSubTag03 = dataSubTag03.substring(2, 4);
            String valueSubTag03 = dataSubTag03.substring(4, 4 + Integer.parseInt(lengthSubTag03));
            merchantDataResponse.setMerchant_criteria(valueSubTag03);

            merchantDataResponses.add(merchantDataResponse);

            isoMessage = isoMessage.substring(tag.length() + lengthTag.length() + Integer.parseInt(lengthTag));
        }
        return merchantDataResponses;
    }

    public static String bit70(String request){
        String valueBit70 ="";
        if(request.equals("logon")){
            valueBit70 = "001";
        } else if(request.equals("logoff")){
            valueBit70 = "002";
        } else if(request.equals("cutover")){
            valueBit70 = "201";
        } else {
            valueBit70 = "301";
        }
        return valueBit70;
    }

    public static String alfanumeric(int codeLength) {
        String randomUppercase = createRandomCode(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String randomLowercase = createRandomCode(4, "abcdefghijklmnopqrstuvwxyz");
        String randomNumber = createRandomCode(1, "0123456789");
        int randomInt = (int) (Math.random() * 500 + 100);
        String combine = randomUppercase + randomLowercase + randomInt + randomNumber;
        String value = createRandomCode(6, combine);
        return value;
    }

    private static String createRandomCode(int codeLength, String code) {
        List<Character> temp = code.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.toList());
        Collections.shuffle(temp, new SecureRandom());
        return temp.stream()
                .map(Object::toString)
                .limit(codeLength)
                .collect(Collectors.joining());
    }
}
