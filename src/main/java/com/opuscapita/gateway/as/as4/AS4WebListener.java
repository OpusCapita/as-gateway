package com.opuscapita.gateway.as.as4;

import com.helger.commons.debug.GlobalDebug;
import com.helger.httpclient.HttpDebugger;
import com.helger.phase4.crypto.AS4CryptoFactory;
import com.helger.phase4.model.pmode.resolve.DefaultPModeResolver;
import com.helger.phase4.model.pmode.resolve.IPModeResolver;
import com.helger.phase4.servlet.AS4ServerInitializer;
import com.helger.phase4.servlet.mgr.AS4ServerConfiguration;
import com.helger.photon.ajax.GlobalAjaxInvoker;
import com.helger.photon.api.GlobalAPIInvoker;
import com.helger.photon.core.PhotonCoreInit;
import com.helger.photon.core.locale.GlobalLocaleManager;
import com.helger.photon.core.servlet.WebAppListener;
import com.helger.photon.security.CSecurity;
import com.helger.photon.security.mgr.PhotonSecurityManager;
import com.helger.photon.security.user.UserManager;
import com.helger.xservlet.requesttrack.RequestTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import java.util.Locale;

@WebListener
public final class AS4WebListener extends WebAppListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AS4WebListener.class);

    @Override
    protected String getInitParameterDebug(@Nonnull final ServletContext aSC) {
        return Boolean.toString(AS4ServerConfiguration.isGlobalDebug());
    }

    @Override
    protected String getInitParameterProduction(@Nonnull final ServletContext aSC) {
        return Boolean.toString(AS4ServerConfiguration.isGlobalProduction());
    }

    @Override
    protected String getInitParameterNoStartupInfo(@Nonnull final ServletContext aSC) {
        return Boolean.toString(AS4ServerConfiguration.isNoStartupInfo());
    }

    @Override
    protected String getDataPath(@Nonnull final ServletContext aSC) {
        return AS4ServerConfiguration.getDataPath();
    }

    @Override
    protected boolean shouldCheckFileAccess(@Nonnull final ServletContext aSC) {
        return false;
    }

    @Override
    protected void initGlobalSettings() {
        // Logging: JUL to SLF4J
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        if (GlobalDebug.isDebugMode())
            RequestTracker.getInstance().getRequestTrackingMgr().setLongRunningCheckEnabled(false);

        HttpDebugger.setEnabled(false);
    }

    @Override
    protected void initSecurity() {
        // Ensure user exists
        final UserManager aUserMgr = PhotonSecurityManager.getUserMgr();
        if (!aUserMgr.containsWithID(CSecurity.USER_ADMINISTRATOR_ID)) {
            aUserMgr.createPredefinedUser(CSecurity.USER_ADMINISTRATOR_ID,
                    CSecurity.USER_ADMINISTRATOR_LOGIN,
                    CSecurity.USER_ADMINISTRATOR_EMAIL,
                    CSecurity.USER_ADMINISTRATOR_PASSWORD,
                    "Admin",
                    "istrator",
                    null,
                    Locale.US,
                    null,
                    false);
        }
    }

    @Override
    protected void initManagers() {
        final IPModeResolver aPModeResolver = DefaultPModeResolver.DEFAULT_PMODE_RESOLVER;
        final AS4CryptoFactory aCryptoFactory = AS4CryptoFactory.DEFAULT_INSTANCE;
        AS4ServerInitializer.initAS4Server(aPModeResolver, aCryptoFactory);
    }


    @Override
    protected void beforeContextInitialized(@Nonnull final ServletContext aSC) {
        /*
         * Hack to bypass issues with spring boot and this library thats caused by the webscopemanager being
         * called twice even though its already enabled.
         */
        setOnlyOneInstanceAllowed(false);

        /*
         * The content below is basically copied from contextInitialized in WebListener. All of this is basically done
         * just to ensure WebScopeManager.onGlobalBegin(aSC) isn't called at this stage of the initialization.
         */
        PhotonCoreInit.startUp();
        initPaths(aSC);
        initGlobalIDFactory();
        initDefaultGlobalSettings();
        initGlobalSettings();
        initLocales(GlobalLocaleManager.getInstance());
        initMenu();
        initAjax(GlobalAjaxInvoker.getInstance().getRegistry());
        initAPI(GlobalAPIInvoker.getInstance().getRegistry());
        initSecurity();
        initUI();
        initManagers();
        initJobs();

    }

    @Override
    protected void afterContextInitialized(@Nonnull final ServletContext aSC) {
        /*
         * Restore the state of setOnlyOneInstanceAllowed.
         */
        setOnlyOneInstanceAllowed(true);
        super.afterContextInitialized(aSC);
    }
}
