package com.opuscapita.gateway.as;

import com.helger.as2lib.client.AS2Client;
import com.helger.as2lib.client.AS2ClientRequest;
import com.helger.as2lib.client.AS2ClientResponse;
import com.helger.as2lib.client.AS2ClientSettings;
import com.helger.security.keystore.EKeyStoreType;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class AS2ServletTests extends BaseTest {

    @Test
    public void shouldSendAS2MessageEDI() throws Exception {
        File testFile = new ClassPathResource(
                "data/edi-850_X12-4010.txt").getFile();
        File clientKeystore = new ClassPathResource(
                "testclient.pkcs12").getFile();


        AS2ClientRequest request = new AS2ClientRequest("testing")
                .setData(testFile, UTF_8);

        AS2ClientSettings settings = new AS2ClientSettings()
                .setPartnershipName("test-partnership")
                .setKeyStore(EKeyStoreType.PKCS12, clientKeystore, "test")
                .setMDNRequested(true)
                .setReceiverData("receiverid", "server", getServerUrl() + "/as2")
                .setSenderData("senderid", "test@testy.com", "testclient");

        AS2Client client = new AS2Client();
        AS2ClientResponse response = client.sendSynchronous(settings, request);
        assertTrue(response.hasMDN());
        assertFalse(response.hasException());

    }

}
