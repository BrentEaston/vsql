package VSQL;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import VASSAL.build.GameModule;
import VASSAL.build.module.DieManager;
import VASSAL.build.module.InternetDiceButton;
import VASSAL.configure.StringConfigurer;

public class VSQLInternetDiceButton extends InternetDiceButton {

  public VSQLInternetDiceButton() {
    super();
  }

  protected void initDieManager() {
    if (dieManager == null) {
      dieManager = new VSQLDieManager();
      dieManager.build(null);
    }
  }

  public class VSQLDieManager extends DieManager {

    public static final String ACCOUNT_USER = "accountuser";
    public static final String ACCOUNT_PW = "accountpw";

    public VSQLDieManager() {
      super();
      final StringConfigurer accountUser = new StringConfigurer(ACCOUNT_USER, "Die Server User Name");
      final StringConfigurer accountPw = new StringConfigurer(ACCOUNT_PW, "Die Server Password");
      GameModule.getGameModule().getPrefs().addOption(DIE_MANAGER, accountUser);
      GameModule.getGameModule().getPrefs().addOption(DIE_MANAGER, accountPw);
      Authenticator.setDefault (new MyAuthenticator ());
    }

    class MyAuthenticator extends Authenticator {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication((String) GameModule.getGameModule().getPrefs().getValue(ACCOUNT_USER), ((String) GameModule.getGameModule().getPrefs()
            .getValue(ACCOUNT_PW)).toCharArray());
      }
    }

  }

}
