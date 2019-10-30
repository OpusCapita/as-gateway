package com.opuscapita.gateway.as;

import com.helger.as2lib.client.AS2Client;
import com.helger.as2lib.client.AS2ClientRequest;
import com.helger.as2lib.client.AS2ClientSettings;
import com.helger.security.keystore.EKeyStoreType;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AS2ServletTests extends BaseTest {

    @Test
    public void shouldSendEDIAS2Message() throws Exception {
        File testFile = new ClassPathResource(
                "edi/850_X12-4010.txt").getFile();
        File clientKeystore = new ClassPathResource(
                "testclient.pkcs12").getFile();


        AS2ClientRequest request = new AS2ClientRequest("testing")
                .setData(testFile, UTF_8);

        AS2ClientSettings settings = new AS2ClientSettings()
                .setPartnershipName("test-partnership")
                .setKeyStore(EKeyStoreType.PKCS12, clientKeystore, "test")
                .setMDNRequested(true)
                .setReceiverData("receiverid", "server", "http://127.0.0.1:3056/as2")
                .setSenderData("senderid", "test@testy.com", "testclient");

        AS2Client client = new AS2Client();
        client.sendSynchronous(settings, request);
    }

}
