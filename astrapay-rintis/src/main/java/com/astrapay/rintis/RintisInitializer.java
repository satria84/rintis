package com.astrapay.rintis;

import com.astrapay.rintis.config.GreetClient;
import com.astrapay.rintis.service.LogonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class RintisInitializer implements CommandLineRunner {
    @Autowired
    LogonService logon;
    TCPClient tcpClient;
	@Override
	public void run(String... args) throws Exception {
        // logon.doLogon();
        // GreetClient greetClient = new GreetClient();
        // greetClient.startConnection("qris-dev.astrapay.com", 2506);
	}
}
