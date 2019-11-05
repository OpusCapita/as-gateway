package com.opuscapita.gateway.as;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.phase4.CAS4;
import com.helger.phase4.client.AS4ClientUserMessage;
import com.helger.phase4.ebms3header.Ebms3Property;
import com.helger.phase4.messaging.domain.MessageHelperMethods;
import com.helger.phase4.soap.ESOAPVersion;
import com.helger.phase4.util.AS4ResourceHelper;
import com.helger.security.keystore.EKeyStoreType;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.serialize.read.DOMReader;
import org.junit.Test;

import javax.annotation.Nonnull;

public class AS4ServletTests extends BaseTest {

    private static String DEFAULT_AGREEMENT = "an-agreement-ref";

    @Test
    public void shouldSendNonEncryptedXMLMessage() throws Exception {
        AS4ResourceHelper resourceHelper = new AS4ResourceHelper();
        final AS4ClientUserMessage aClient = _getMandatoryAttributesSuccessMessage(resourceHelper);
        aClient.setPayload(DOMReader.readXMLDOM(new ClassPathResource("data/helloworld.xml")));
        final IMicroDocument aDoc = aClient.sendMessageAndGetMicroDocument(getServerUrl() + AS4_MAPPING);
    }

    @Nonnull
    private AS4ClientUserMessage _getMandatoryAttributesSuccessMessage(AS4ResourceHelper resourceHelper) {
        final AS4ClientUserMessage aClient = new AS4ClientUserMessage(resourceHelper);
        aClient.setSOAPVersion(ESOAPVersion.SOAP_12);

        final String sSenderID = "MyPartyIDforSending";
        final String sResponderID = "MyPartyIDforReceving";

        // Use a pmode that you know is currently running on the server your trying
        // to send the message too
        aClient.setAction("AnAction");
        aClient.setServiceType("MyServiceType");
        aClient.setServiceValue("OrderPaper");
        aClient.setConversationID(MessageHelperMethods.createRandomConversationID());
        aClient.setAgreementRefValue(DEFAULT_AGREEMENT);
        aClient.setFromRole(CAS4.DEFAULT_ROLE);
        aClient.setFromPartyID(sSenderID);
        aClient.setToRole(CAS4.DEFAULT_ROLE);
        aClient.setToPartyID(sResponderID);
        aClient.ebms3Properties()
                .setAll(MessageHelperMethods.createEmbs3PropertiesOriginalSenderFinalRecipient("C1-test", "C4-test"));

        return aClient;
    }

    private static AS4ClientUserMessage setKeyStoreTestData (@Nonnull final AS4ClientUserMessage aClient)
    {
        aClient.setKeyStoreResource(new ClassPathResource("testclient.pkcs12"));
        aClient.setKeyStorePassword("test");
        aClient.setKeyStoreType(EKeyStoreType.PKCS12);
        aClient.setKeyStoreAlias("testclient");
        aClient.setKeyStoreKeyPassword("test");
        aClient.cryptParams().setAlias("server");
        return aClient;
    }

}
