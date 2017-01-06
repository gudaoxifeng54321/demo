/*
 * Copyright (c) 2011-2012, IUnknown. All rights reserved.
 * 
 * Project: AuthentiKate Me!
 * 
 * Description: This project is a small tool that automates the login-procedure of Firewall authentication for accessing NetAct labs in NSN.
 * This program will perform auto-authentication to Tampere, Bangalore and Munich Juniper Infranet Controller's.
 */

package com.ly.common.script;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
 * Class : NotificationTray
 * 
 * Description: This is a helper class that shows an icon on the system tray of the underlying Operating System.
 * 
 * The status of the authentication is color coded, w/ state. GREEN ==> Authentication is successful (UP) RED ==> Authentication failed
 * (DOWN) PARTIAL ==> In between cases (9?) (PARTIAL)
 * 
 * This class maintains the presently shown ICON (GREEN or RED or PARTIAL). The caller of this class can switch the state (GREEN or RED or
 * PARTIAL).
 */
public class NotificationTray {

  private TrayIcon mTrayIcon;

  enum STATE {
    DOWN, UP, PARTIAL, DOWN_RECONN, UP_RECONN, PARTIAL_RECONN
  }

  private STATE              mCurrentState         = STATE.DOWN;

  private boolean            mTrayInitialized      = false;

  private static String      MENU_CHECK_STATUS     = "Check Status";
  private static String      MENU_SIGN_IN          = "Sign in";
  private static String      MENU_SIGN_OUT         = "Sign out";
  private static String      MENU_FORCED_SIGN_IN   = "Force Sign-in";
  private static String      MENU_ABOUT            = "About...";
  private static String      MENU_EXIT             = "Exit";

  MenuItem                   mCheckStatusMenuItem  = new MenuItem(MENU_CHECK_STATUS);
  MenuItem                   mSignInOutMenuItem    = new MenuItem(MENU_SIGN_IN);
  MenuItem                   mForcedSignInMenuItem = new MenuItem(MENU_FORCED_SIGN_IN);
  MenuItem                   mAboutMenuItem        = new MenuItem(MENU_ABOUT);
  MenuItem                   mExitMenuItem         = new MenuItem(MENU_EXIT);

  private String             mstrStatusMessage     = "Not Signed-in";
  private MessageType        mMessageType          = MessageType.INFO;

  private AuthentiKateMe     mAuthentiKateMe;
  private AuthentiKateMeMain mAuthentiKateMeMain;

  public NotificationTray(AuthentiKateMe authentiKateMe, AuthentiKateMeMain authentiKateMeMain) {

    mAuthentiKateMe = authentiKateMe;
    mAuthentiKateMeMain = authentiKateMeMain;
  }

  public boolean initialize(STATE state) {

    mCurrentState = state;

    if (!SystemTray.isSupported()) {

      System.err.println("AutentiKateMe! ERROR: System Tray not supported in this platform!");

      return false;
    }

    Image image = null;

    if (state == STATE.DOWN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/down.png"));
    } else if (state == STATE.UP) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/up.png"));
    } else if (state == STATE.PARTIAL) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/partial.png"));
    } else if (state == STATE.DOWN_RECONN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/down-reconnect.png"));
    } else if (state == STATE.UP_RECONN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/up-reconnect.png"));
    } else if (state == STATE.PARTIAL_RECONN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/partial-reconnect.png"));
    } else {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/down.png"));
    }

    SystemTray tray = SystemTray.getSystemTray();

    MouseListener mouseListener = new MouseListener() {

      public void mouseClicked(MouseEvent e) {
        // Intentionally left blank!
      }

      public void mouseEntered(MouseEvent e) {
        // Intentionally left blank!
      }

      public void mouseExited(MouseEvent e) {
        // Intentionally left blank!
      }

      public void mousePressed(MouseEvent e) {
        // Intentionally left blank!
      }

      public void mouseReleased(MouseEvent e) {
        // Intentionally left blank!
      }
    };

    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        // Control comes here, when user and double-clicked the
        // notification tray icon of our application.

        performCheckStatus();
      }
    };

    ActionListener menuActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String strCommand = e.getActionCommand();

        if (strCommand == MENU_CHECK_STATUS) {

          // Check Status has be selected.
          performCheckStatus();
        }

        if (strCommand == MENU_SIGN_IN) {

          // Sign In has be selected.
          performSignIn();
        }

        if (strCommand == MENU_SIGN_OUT) {

          // Sign Out has be selected.
          performSignOut();
        }

        if (strCommand == MENU_FORCED_SIGN_IN) {

          // Forced Sign-in has be selected.
          performForcedSignIn();
        }

        if (strCommand == MENU_ABOUT) {

          // About menu has be selected.
          showBalloonMessage("AuthentiKate Me! Version " + AuthentiKateMeMain.BUILD_VERSION
              + "\nCopyright � 2012 IUnknown a.k.a V3NK4T~\nAll rights reserved", MessageType.INFO);
        }

        if (strCommand == MENU_EXIT) {

          // Exit menu has be selected.
          System.out.println("AutentiKateMe! INFO: Exiting...");
          System.exit(0);
        }

      }
    };

    PopupMenu popup = new PopupMenu();

    mCheckStatusMenuItem.addActionListener(menuActionListener);
    mSignInOutMenuItem.addActionListener(menuActionListener);
    mForcedSignInMenuItem.addActionListener(menuActionListener);
    mAboutMenuItem.addActionListener(menuActionListener);
    mExitMenuItem.addActionListener(menuActionListener);

    mCheckStatusMenuItem.setFont(new Font("Tahoma", Font.BOLD, 12));
    mSignInOutMenuItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
    mForcedSignInMenuItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
    mAboutMenuItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
    mExitMenuItem.setFont(new Font("Tahoma", Font.PLAIN, 12));

    popup.add(mCheckStatusMenuItem);
    // popup.addSeparator();
    // popup.add(mSignInOutMenuItem);
    popup.addSeparator();
    popup.add(mForcedSignInMenuItem);
    popup.addSeparator();
    popup.add(mAboutMenuItem);
    popup.addSeparator();
    popup.add(mExitMenuItem);

    setForceSignInEnabled(false);

    mTrayIcon = new TrayIcon(image, "AuthentiKate Me!\nVersion " + AuthentiKateMeMain.BUILD_VERSION
        + "\nDesigned by: IUnknown a.k.a V3NK4T", popup);

    mTrayIcon.setImageAutoSize(true);
    mTrayIcon.addActionListener(actionListener);
    mTrayIcon.addMouseListener(mouseListener);

    try {
      tray.add(mTrayIcon);
    } catch (AWTException e) {
      System.err.println("AutentiKateMe! ERROR: TrayIcon could not be added.");
      return false;
    }

    mTrayInitialized = true;

    return mTrayInitialized;
  }

  public void setState(STATE state) {

    mCurrentState = state;

    if (!mTrayInitialized)
      return;

    setImageState(state);
  }

  public Image setImageState(STATE state) {

    Image image = null;

    if (state == STATE.DOWN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/down.png"));
    } else if (state == STATE.UP) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/up.png"));
    } else if (state == STATE.PARTIAL) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/partial.png"));
    } else if (state == STATE.DOWN_RECONN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/down-reconnect.png"));
    } else if (state == STATE.UP_RECONN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/up-reconnect.png"));
    } else if (state == STATE.PARTIAL_RECONN) {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/partial-reconnect.png"));
    } else {
      image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/down.png"));
    }

    mTrayIcon.setImage(image);

    return image;
  }

  public STATE getState() {

    return mCurrentState;
  }

  public void setToolTipMessage(String strMessage) {

    if (!mTrayInitialized)
      return;

    mTrayIcon.setToolTip("AuthentiKateMe! " + strMessage);
  }

  public void showBalloonMessage(String strMessage, TrayIcon.MessageType msgType) {

    if (!mTrayInitialized)
      return;

    mTrayIcon.displayMessage("AuthentiKate Me!", strMessage, msgType);
  }

  private void performCheckStatus() {

    String strMessage = mstrStatusMessage;

    if (mAuthentiKateMeMain.lstrNextRefreshTime != -1) {

      long lTimeLeft = mAuthentiKateMeMain.lstrNextRefreshTime - System.currentTimeMillis();

      if (lTimeLeft <= 0) {

        strMessage = mstrStatusMessage + "\n\nConnectivity check in-progress...";
      } else {

        int nMinutes = 0;
        int nSeconds = (int) (lTimeLeft / 1000);

        if (nSeconds >= 60) {

          nMinutes = nSeconds / 60;
          nSeconds = nSeconds % 60;
        }

        String strMinuteString = " minute";
        if (nMinutes > 1) {
          strMinuteString = " minutes";
        }

        if (nMinutes == 0) {

          strMessage = mstrStatusMessage + "\n\nNext connectivity check in : " + nSeconds + " seconds";
        } else {

          strMessage = mstrStatusMessage + "\n\nNext connectivity check in : " + nMinutes + strMinuteString + " and " + nSeconds
              + " seconds";
        }

      }
    }

    mTrayIcon.displayMessage("AuthentiKate Me!", strMessage, mMessageType);

    // This is very bad code ==>
    if (mstrStatusMessage.compareTo("Not Signed-in") == 0 || mstrStatusMessage.contains("Authentication failure") == true
        || mstrStatusMessage.contains("Connection lost") == true || mstrStatusMessage.contains("Login failed") == true)
      mAuthentiKateMeMain.setVisible(true);
  }

  private void performSignIn() {
    mSignInOutMenuItem.setLabel(MENU_SIGN_OUT);
  }

  private void performSignOut() {
    mSignInOutMenuItem.setLabel(MENU_SIGN_IN);
  }

  private void performForcedSignIn() {
    this.mAuthentiKateMeMain.forceSignIn();
  }

  public void setStatusMessage(String statusMessage, MessageType messageType) {

    mstrStatusMessage = statusMessage;
    mMessageType = messageType;
  }

  public void setForceSignInEnabled(boolean bEnabled) {

    mForcedSignInMenuItem.setEnabled(bEnabled);
  }
}
