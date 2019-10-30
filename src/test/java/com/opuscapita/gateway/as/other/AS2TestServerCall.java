package com.opuscapita.gateway.as.other;

import com.helger.as2lib.client.AS2Client;
import com.helger.as2lib.client.AS2ClientRequest;
import com.helger.as2lib.client.AS2ClientResponse;
import com.helger.as2lib.client.AS2ClientSettings;
import com.helger.as2lib.crypto.ECompressionType;
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import com.helger.as2lib.disposition.DispositionOptions;
import com.helger.as2lib.util.cert.AS2KeyStoreHelper;
import com.helger.commons.mime.CMimeType;
import com.helger.mail.cte.EContentTransferEncoding;
import com.helger.security.keystore.EKeyStoreType;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.File;
import java.security.cert.X509Certificate;

public class AS2TestServerCall {

    @Ignore
    @Test
    public void shouldSendAS2ToMendelson() throws Exception {

        final AS2ClientSettings aSettings = new AS2ClientSettings();

        aSettings.setKeyStore(EKeyStoreType.PKCS12, new File("src/test/resources/mendelson/key3.pfx"), "test");
        aSettings.setSenderData("mycompanyAS2", "test@testy.com", "key3");
        aSettings.setReceiverData("mendelsontestAS2",
                "mendelsontestAS2",
                "http://testas2.mendelson-e-c.com:8080/as2/HttpReceiver");
        final X509Certificate aReceiverCertificate = AS2KeyStoreHelper.readX509Certificate("src/test/resources/mendelson/key4.cer");
        aSettings.setReceiverCertificate(aReceiverCertificate);

        // AS2 stuff
        aSettings.setPartnershipName(aSettings.getSenderAS2ID() + "_" + aSettings.getReceiverAS2ID());
        // When a signed message is used, the algorithm for MIC and message must be
        // identical
        final ECryptoAlgorithmSign eSignAlgo = ECryptoAlgorithmSign.DIGEST_SHA_512;
        // CBC works, GCM is not supported
        final ECryptoAlgorithmCrypt eCryptAlgo = ECryptoAlgorithmCrypt.CRYPT_AES128_CBC;
        final ECompressionType eCompress = ECompressionType.ZLIB;
        final boolean bCompressBeforeSigning = AS2ClientSettings.DEFAULT_COMPRESS_BEFORE_SIGNING;

        aSettings.setMDNOptions(new DispositionOptions().setMICAlg(eSignAlgo)
                .setMICAlgImportance(DispositionOptions.IMPORTANCE_REQUIRED)
                .setProtocol(DispositionOptions.PROTOCOL_PKCS7_SIGNATURE)
                .setProtocolImportance(DispositionOptions.IMPORTANCE_REQUIRED));
        aSettings.setEncryptAndSign(eCryptAlgo, eSignAlgo);
        aSettings.setCompress(eCompress, bCompressBeforeSigning);
        aSettings.setMessageIDFormat("github-phax-as2-lib-$date.uuuuMMdd-HHmmssZ$-$rand.1234$@$msg.sender.as2_id$_$msg.receiver.as2_id$");
        aSettings.setRetryCount(1);
        aSettings.setConnectTimeoutMS(10_000);
        aSettings.setReadTimeoutMS(10_000);
        aSettings.setMDNRequested(false);

        // Build client request
        final AS2ClientRequest aRequest = new AS2ClientRequest("AS2 test message from as2-lib");
        File testfile = new ClassPathResource(
                "mendelson/testcontent.attachment").getFile();
        aRequest.setData(new DataHandler(new FileDataSource(testfile)));
        aRequest.setContentType(CMimeType.TEXT_PLAIN.getAsString());
        aRequest.setContentTransferEncoding(EContentTransferEncoding.BASE64);

        // Send message
        final AS2ClientResponse aResponse = new AS2Client().sendSynchronous(aSettings, aRequest);
        if (aResponse.hasException())
            System.out.println(aResponse.getAsString());

        System.out.println("Done");
    }
}
