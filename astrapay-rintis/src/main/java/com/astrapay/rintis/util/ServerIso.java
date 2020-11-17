package com.astrapay.rintis.util;

import com.astrapay.rintis.service.RefundService;
import com.astrapay.rintis.service.impl.RefundServiceImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerIso {
    private static final Integer PORT = 12345;
    private final RefundServiceImpl service = new RefundServiceImpl();


    @SuppressWarnings("Duplicates")
    public void refundSocket(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            boolean conn = true;//jangan diapus
            boolean selainRefund = true; //kalau true untuk selain refund ya kawan kawan
            int typeApi = 2;
            //1 = payment
            //2 = check status
            //3 = inquiry
            while (conn) {
                System.out.println("Server is listening on port " + port);

                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String text;
                text = reader.readLine();
                if (!selainRefund) {
                    String isoResponse = service.refundIssuer(text);
                    System.out.println(isoResponse);
                    writer.println(isoResponse);
                } else {
                    switch (typeApi) {
                        case 1:
                            text = "0210F23AC4118EC1800000000000140000201893600153100290563426100000000190070007100312050005191017520710071007100000011C005000000893600153063600020000003888430050870090015258        733868086622871049PI04Q001CD30AGATHA7LIE                    MC03UMI360089360011119936001114610100100302000001234560000123456?";
                            break;
                        case 2:
                            text = "0210F23AC4118EC1800000000000140000201893600153100290563436100000000190070007100315010005211020480710071007100000011C005000000893600153063600020000003888430172760090015258        733868086622871049PI04Q001CD30AGATHA7LIE                    MC03UMI360089360011119936001114610100100302000001234560000123456?";
                            break;
                        case 3:
                            text = "0210F23AC4118EC18080000000001400000019936000003456789012937100000000127770007260237063004490937020726072607260000011C005000000893600000063600029000001003281003280090015258        ID1234567890129008PI04IQ023609362648011893600153100290563402157338680866228710303UMI27480118936000771234567890021500000000000077 0303UKI28480118936000140987654321021500000000000014 0303UPO29480118936002221111111111021500000222       0303OLO30480118936003331212121212021500000000131313 0303TOL31480118936004269988774455021593600426       0303UMI32480118936004448877887788021593600444       0303OSO33480118936005551111111111021593600555       0303DOI34480118936000081112223334021593600811223322 0303WOW354801189360000922112211120215936009         0303LOI36480118936000881234567890021593600088       0303UME37480118936009991235512421021593600999       0303POL38480118936009908877996654021593600990000111 0303SUP39480118936009801234121251021593600980444444 0303UME40480118936009701231231231021593600970       0303KRI41480118936009603123441211021593600960       0303WOW42480118936009501234551212021593600950       0303PUS43480118936009401111222333021593600940       0303HAI0893600426199360042605508758831?";
                            break;
                    }
                    System.out.println(text);
                    writer.println(text);
                }
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
