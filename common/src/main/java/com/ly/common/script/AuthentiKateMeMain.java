/*
 * Copyright (c) 2011-2012, IUnknown. All rights reserved.
 * 
 * Project: AuthentiKate Me!
 * 
 * Description: This project is a small tool that automates the login-procedure of Firewall authentication for accessing NetAct labs in NSN.
 * This program will perform auto-authentication to the Tampere, Bangalore and Munich Juniper Infranet Controller's.
 */

package com.ly.common.script;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.Document;

import com.ly.common.script.AuthentiKateMe.ERROR_CODE;
import com.ly.common.script.AuthentiKateMe.PING_STATUS;
import com.ly.common.script.NotificationTray.STATE;

/**
 * Application main launcher.
 * 
 * This module creates the Application's main window. GUI creation, user login & other shit happens in this class.
 */
public class AuthentiKateMeMain extends JFrame {

  private int                myInsideX                = 0;

  private int                myInsideY                = 0;

  private int                myX                      = 0;

  private int                myY                      = 0;

  private Action             signInAction;

  private FadingLabel        myTipTextArea;

  private JTextField         mUserNameTxtFld;
  private JPasswordField     mPwdTxtFld;

  private Document           mUsernameDocument;
  private Document           mPwdDocument;

  private JButton            mSignInButton;

  AuthentiKateMe             mAuthInstance            = new AuthentiKateMe();

  public long                lstrNextRefreshTime      = -1;

  private boolean            mbTimerAlreadyCreated    = false;

  public static final String BUILD_VERSION            = "2.4.3";

  public static boolean      PERFORM_FOR_TAMPERE      = true;
  public static boolean      PERFORM_FOR_BANGALORE    = true;
  public static boolean      PERFORM_FOR_MUNICH       = true;

  // int mTimeDelay = 36000000; // 10 hour
  // int mTimeDelay = 3600000; // 1 hour
  // int mTimeDelay = 900000; // 15 minutes
  // int mTimeDelay = 300000; // 5 minutes
  // int mTimeDelay = 120000; // 2 minutes
  int                        mTimeDelay               = 60000;                                                                                                           // 60
                                                                                                                                                                          // seconds
  // int mTimeDelay = 30000; // 30 seconds
  // int mTimeDelay = 15000; // 15 seconds

  int                        mTimerExpiryTimeout      = 15000;

  int                        mCountOfNetworkChecks    = 0;

  private final String       TIME_DELAYKEY            = "Timedelay-" + BUILD_VERSION;

  String                     mstrPrevInterfaces       = "";

  private static String      NETWORK_INCONSISTENT_MSG = "Networking parameters have changed on this system.\nIt's recommended to do a Force Sign-in to stay consistent!";

  public AuthentiKateMeMain() {
    super();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(true);
    setResizable(false);
    setSize(500, 250);
    setTitle("AuthentiKate Me! - By IUnknown a.k.a V3NK4T");
    setLocationRelativeTo(null);

    buildUI();

    myX = getLocation().x;
    myY = getLocation().y;

    addMouseListener(new MyMouseListenerAdapter());
    addMouseMotionListener(new MyMouseMotionAdapter());

    mAuthInstance.initialize(this);

    // Check if we have any value stored in the user preferences for the
    // delay time period
    Preferences prefs = Preferences.userNodeForPackage(AuthentiKateMe.class);

    String strDelay = prefs.get(TIME_DELAYKEY, "");

    if (strDelay.isEmpty())
      prefs.put(TIME_DELAYKEY, String.valueOf(mTimeDelay));
    else
      mTimeDelay = Integer.valueOf(strDelay);

    if (mTimeDelay < 15000)
      mTimeDelay = 15000;
  }

  private void buildUI() {
    //
    // JPanel panel = new JPanel(new GridBagLayout());
    // panel.setOpaque(false);
    // // panel.setBorder(PBorderFactory.getBasicNetBorder(NetTheme.COLOR_BLUE_5));
    //
    // ExtendedGridBagConstraints gbc = new ExtendedGridBagConstraints();
    // gbc.weighty = 1;
    // gbc.weightx = 1;
    //
    // gbc.setTopInset(36);
    // gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    // gbc.fillBoth();
    // gbc.setLeftInset(64);
    //
    // JLabel labelMain = new JLabel("AuthentiKate Me!");
    // labelMain.setForeground(NetTheme.COLOR_GRAY_3);
    // labelMain.setFont(new Font("Dialog", Font.BOLD, 18));
    // panel.add(labelMain, gbc);
    //
    // JLabel labelSub = new JLabel("\n\nLab Authenticator");
    // labelSub.setForeground(NetTheme.COLOR_GRAY_3);
    // labelSub.setFont(new Font("Dialog", Font.PLAIN, 12));
    // panel.add(labelSub, gbc);
    //
    // gbc.setTopInset(14);
    // gbc.setRightInset(45);
    // gbc.setLeftInset(0);
    // gbc.fillNone();
    // gbc.anchor = GridBagConstraints.NORTHEAST;
    // gbc.nextColumn();
    //
    // JLabel logoLabel = new JLabel(new ImageIcon(getClass().getResource(
    // "/nsn.png")));
    // panel.add(logoLabel, gbc);
    //
    // gbc.setRightInset(10);
    // gbc.setTopInset(8);
    //
    // JButton exitButton = new JButton();
    // exitButton.setContentAreaFilled(false);
    // exitButton.setFocusable(false);
    // exitButton.setBorder(BorderFactory.createEmptyBorder());
    // exitButton.setRolloverEnabled(false);
    // exitButton.setIcon(new ImageIcon(getClass().getResource(
    // "/Exit_Normal.gif")));
    // exitButton.setPressedIcon(new ImageIcon(getClass().getResource(
    // "/Exit_Pressed.gif")));
    //
    // exitButton.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // // Exit the application.
    // // Make sure you logout from all the Auth Links.
    //
    // System.exit(0);
    // }
    // });
    //
    // panel.add(exitButton, gbc);
    //
    // gbc.fillBoth();
    // gbc.setBottomInset(0);
    // gbc.setRightInset(20);
    // gbc.setLeftInset(64);
    // gbc.nextRow();
    // gbc.setColumnWidth(1);
    //
    // panel.add(new JLabel("Username:"), gbc);
    //
    // gbc.setBottomInset(0);
    // gbc.setRightInset(20);
    // gbc.setLeftInset(64);
    // gbc.nextRow();
    //
    // mUserNameTxtFld = new JTextField("", 10);
    //
    // panel.add(mUserNameTxtFld, gbc);
    // mUserNameTxtFld.setToolTipText("Enter your NSN-INTRA user account here (including '@nsn-intra')");
    //
    // mUserNameTxtFld.addKeyListener(new KeyAdapter() {
    //
    // public void keyReleased(KeyEvent e) {
    // if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    // mSignInButton.doClick();
    // }
    // }
    // });
    //
    // mUsernameDocument = mUserNameTxtFld.getDocument();
    // mUsernameDocument.addDocumentListener(new DocumentListener() {
    //
    // public void removeUpdate(DocumentEvent e) {
    // if (e.getDocument() == mUsernameDocument)
    // disableIfEmpty(mUsernameDocument);
    // }
    //
    // public void insertUpdate(DocumentEvent e) {
    // if (e.getDocument() == mUsernameDocument)
    // disableIfEmpty(mUsernameDocument);
    // }
    //
    // public void changedUpdate(DocumentEvent e) {
    // if (e.getDocument() == mUsernameDocument)
    // disableIfEmpty(mUsernameDocument);
    // }
    // });
    //
    // gbc.nextRow();
    // panel.add(new JLabel("Password:"), gbc);
    //
    // gbc.nextColumn();
    // panel.add(new JLabel("Build: " + BUILD_VERSION), gbc);
    //
    // gbc.nextRow();
    //
    // mPwdTxtFld = new JPasswordField("", 10);
    // panel.add(mPwdTxtFld, gbc);
    // mPwdTxtFld.setToolTipText("Enter your NSN-INTRA password here");
    //
    // mPwdTxtFld.addKeyListener(new KeyAdapter() {
    //
    // public void keyReleased(KeyEvent e) {
    // if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    // mSignInButton.doClick();
    // }
    // }
    // });
    //
    // mPwdDocument = mPwdTxtFld.getDocument();
    // mPwdDocument.addDocumentListener(new DocumentListener() {
    //
    // public void removeUpdate(DocumentEvent e) {
    // if (e.getDocument() == mPwdDocument)
    // disableIfEmpty(mPwdDocument);
    // }
    //
    // public void insertUpdate(DocumentEvent e) {
    // if (e.getDocument() == mPwdDocument)
    // disableIfEmpty(mPwdDocument);
    // }
    //
    // public void changedUpdate(DocumentEvent e) {
    // if (e.getDocument() == mPwdDocument)
    // disableIfEmpty(mPwdDocument);
    // }
    // });
    //
    // gbc.nextColumn();
    // panel.add(new JLabel("IUnknown a.k.a V3NK4T"), gbc);
    //
    // gbc.nextRow();
    // panel.add(new JLabel(""), gbc);
    //
    // gbc.nextRow();
    // gbc.setLeftInset(0);
    // gbc.setRightInset(20);
    // gbc.setBottomInset(5);
    // gbc.setTopInset(5);
    // gbc.fillNone();
    //
    // signInAction = new AbstractAction("Sign in", new ImageIcon(
    // getClass().getResource("/signin.png"))) {
    // public void actionPerformed(ActionEvent e) {
    //
    // doSignIn();
    // }
    // };
    //
    // signInAction.setEnabled(false);
    //
    // mSignInButton = createButton(signInAction, "Sign in");
    // panel.add(mSignInButton, gbc);
    //
    // gbc.nextColumn();
    // gbc.setBottomInset(0);
    // gbc.setRightInset(45);
    // gbc.setLeftInset(10);
    // panel.add(new JLabel(""), gbc);
    //
    // JPanel root = new JPanel( new GridBagLayout() );
    // GridBagConstraints rootGbc = new GridBagConstraints();
    // rootGbc.gridx = 0;
    // rootGbc.gridy = 0;
    // rootGbc.weightx = 1.0;
    // rootGbc.weighty = 1.0;
    // rootGbc.fill = GridBagConstraints.BOTH;
    // rootGbc.anchor = GridBagConstraints.NORTHWEST;
    //
    // JPanel background = new JPanel( new BorderLayout() );
    // background.add( new JLabel( new ImageIcon( getClass().getResource(
    // "/nsn_texture.png" ) ), SwingConstants.RIGHT ), BorderLayout.SOUTH );
    // background.setBackground( Color.WHITE );
    //
    // root.add( panel, rootGbc );
    // root.add( background, rootGbc );
    // add( root, BorderLayout.CENTER );
    //
    // TexturePanel texturePanel = new TexturePanel();
    // texturePanel.setSize( getSize() );
    // setGlassPane( texturePanel );
    // texturePanel.setVisible( true );
    //
    // setIconImage(Toolkit.getDefaultToolkit().getImage(
    // getClass().getResource("/frame.png")));
  }

  protected void doSignIn() {

    // Remove the top level GUI.
    setVisible(false);

    ERROR_CODE errCode;
    errCode = mAuthInstance.signIn(mUserNameTxtFld.getText(), new String(mPwdTxtFld.getPassword()));

    // We re-show the GUI only if there is a failure.
    if (errCode != ERROR_CODE.SUCCESS) {

      mAuthInstance.mTray.setForceSignInEnabled(false);

      setVisible(true);

      mstrPrevInterfaces = "";
    } else {

      lstrNextRefreshTime = System.currentTimeMillis() + mTimeDelay;

      mAuthInstance.mTray.setForceSignInEnabled(true);

      // Login successful!
      // Start a timer for every 'n' secs/mins, this will do re-sign-in.

      if (mbTimerAlreadyCreated == false) {

        mbTimerAlreadyCreated = true;

        new java.util.Timer(true).schedule(new TimerTask() {

          public void run() {

            try {

              // See if the network interface parameters have changed.
              // If any changes, just report it.
              reportNetworkInterfaceChanges();

              mCountOfNetworkChecks++;

              if (mCountOfNetworkChecks != mTimeDelay / mTimerExpiryTimeout) {
                return;
              } else {
                mCountOfNetworkChecks = 0;
              }

              // Ping the authentication servers
              PING_STATUS pingStatus = mAuthInstance.ping();

              if (pingStatus == PING_STATUS.PING_SUCCESS) {

                // All servers are reachable. Nothing do to.
                lstrNextRefreshTime = this.scheduledExecutionTime() + mTimeDelay;

                mAuthInstance.mTray.setForceSignInEnabled(true);

              } else {

                // One or more servers NOT reachable.
                // Cancel this timer
                cancel();

                setVisible(true);

                lstrNextRefreshTime = -1;

                mAuthInstance.mTray.setForceSignInEnabled(false);

                mbTimerAlreadyCreated = false;

                mstrPrevInterfaces = "";
              }
            } catch (Exception ex) {
            }

          }
        }, mTimerExpiryTimeout, mTimerExpiryTimeout);
      }
    }
  }

  private void reportNetworkInterfaceChanges() throws Exception {

    Map<String, List<String>> intfRightNow = NetworkInterfaces.getInterfaceDetails();

    String strIntfRightNow = intfRightNow.toString();

    if (mstrPrevInterfaces.isEmpty())
      mstrPrevInterfaces = strIntfRightNow;

    if (mstrPrevInterfaces.compareTo(strIntfRightNow) != 0) {

      // Network Interface values have changed.

      System.out.println("...\n" + new Date() + " - Networking parameters changed");
      System.out.println(new Date() + " - Old: " + mstrPrevInterfaces);
      System.out.println(new Date() + " - New: " + strIntfRightNow + "\n");

      mAuthInstance.mTray.setState(STATE.PARTIAL);

      mAuthInstance.mTray.setImageState(STATE.PARTIAL);

      mAuthInstance.mTray.showBalloonMessage(NETWORK_INCONSISTENT_MSG, MessageType.WARNING);

      mAuthInstance.mTray.setStatusMessage(NETWORK_INCONSISTENT_MSG, MessageType.WARNING);
    } else {

      System.out.println("...\n" + new Date() + " - Networking parameters intact");
      System.out.println(new Date() + " - Old: " + mstrPrevInterfaces);
      System.out.println(new Date() + " - New: " + strIntfRightNow + "\n");
    }

    mstrPrevInterfaces = strIntfRightNow;
  }

  public void forceSignIn() {

    doSignIn();
  }

  protected void disableIfEmpty(Document d) {

    if (mUsernameDocument.getLength() > 0 && mPwdDocument.getLength() > 0)
      signInAction.setEnabled(true);
    else
      signInAction.setEnabled(false);
  }

  private JButton createButton(final Action action, final String tipText) {

    JButton button = new JButton(action);
    button.setIconTextGap(10);
    button.setText(button.getText());
    button.setBackground(Color.WHITE);
    button.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        myTipTextArea.setText(tipText);
        myTipTextArea.fadeIn();
        myTipTextArea.repaint();
      }

      public void mouseExited(MouseEvent e) {
        myTipTextArea.fadeOut();
      }
    });

    return button;
  }

  class TexturePanel extends JPanel {
    TexturePanel() {
      super(new GridBagLayout());
      setOpaque(false);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = GridBagConstraints.SOUTH;
      gbc.insets = new Insets(215, 210, 0, 0);

      myTipTextArea = new FadingLabel();
      myTipTextArea.setMinimumSize(new Dimension(200, 100));
      myTipTextArea.setPreferredSize(new Dimension(200, 100));
      myTipTextArea.setMaximumSize(new Dimension(200, 100));
      myTipTextArea.setEditable(false);
      myTipTextArea.setPreferredSize(new Dimension(150, 110));
      add(myTipTextArea, gbc);

      JLabel label = new JLabel("");
      gbc.gridy = 1;
      gbc.anchor = GridBagConstraints.SOUTHEAST;
      gbc.insets = new Insets(100, 0, 2, 2);
      add(label, gbc);
    }
  }

  private class MyMouseListenerAdapter extends MouseAdapter {
    public void mousePressed(MouseEvent e) {

      // remembers where the mouse was pressed down
      myInsideX = e.getX();
      myInsideY = e.getY();
    }
  }

  private class MyMouseMotionAdapter extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent e) {

      // if mouse is dragged, draw the screen into new position
      myX = (myX + e.getX()) - myInsideX;
      myY = (myY + e.getY()) - myInsideY;

      setLocation(myX, myY);
      setMaximizedBounds(new Rectangle(myX, myY, AuthentiKateMeMain.this.getWidth(), AuthentiKateMeMain.this.getHeight()));
    }
  }

  private class FadingLabel extends JTextArea {
    private Timer   timer;

    private boolean fadeIn = true;

    public FadingLabel() {
      super();

      addMouseListener(new MyMouseListenerAdapter());
      addMouseMotionListener(new MyMouseMotionAdapter());

      setEditable(false);
      setBorder(null);
      setLineWrap(true);
      setWrapStyleWord(true);
      setFocusable(false);
      setOpaque(false);

      Color c = UIManager.getColor("Label.systemForeground");
      super.setForeground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0));

      // change this if too slow/fast
      timer = new Timer(45, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          Color c = getForeground();
          int alpha = c.getAlpha();

          if (fadeIn) {
            if (alpha <= 225) {
              alpha = alpha + 25;
            } else {
              timer.stop();
              alpha = 255;
            }
          } else {
            if (alpha >= 25) {
              alpha = alpha - 25;
            } else {
              timer.stop();
              alpha = 0;
            }
          }

          FadingLabel.this.setForeground(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
        }
      });
    }

    public void fadeIn() {
      fadeIn = true;
      Color c = getForeground();
      super.setForeground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0));
      timer.start();
    }

    public void fadeOut() {
      fadeIn = false;
      timer.start();
    }
  }

  public static void main(String[] args) {

    // try {
    //
    // // UIManager.setLookAndFeel(new NetLookAndFeel());
    //
    // } catch (UnsupportedLookAndFeelException e) {
    //
    // JOptionPane.showMessageDialog(null,
    // "Failed to set the NSN Look & Feel. Exiting!!",
    // "AuthentiKate Me!", JOptionPane.ERROR_MESSAGE);
    //
    // return;
    // }

    final AuthentiKateMeMain mainFrm = new AuthentiKateMeMain();
    mainFrm.setVisible(true);

    new java.util.Timer(true).schedule(new TimerTask() {

      public void run() {

        mainFrm.mUserNameTxtFld.setText("@nsn-intra");
        mainFrm.mUserNameTxtFld.setCaretPosition(0);

      }
    }, 500);

    Runtime.getRuntime().addShutdownHook(new Thread() {

      public void run() {

        mainFrm.exitApplication();
      }
    });
  }

  protected void exitApplication() {

    mAuthInstance.signOut();
  }
}
