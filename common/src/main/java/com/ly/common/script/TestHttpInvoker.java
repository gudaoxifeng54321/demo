package com.ly.common.script;

import java.awt.HeadlessException;
import java.io.IOException;

public class TestHttpInvoker {

  /**
   * @param args
   * @throws IOException
   * @throws HeadlessException
   */
  public static void main(String[] args) throws Exception {
    HttpInvoker invoker = HttpInvoker.getInstance();
    invoker.setHost("https://trlinb31.emea.nsn-net.net:8080");
    invoker.setWrap(true).setSSL();
    String s = invoker.get("user/egao/my-views/view/Gingko/job/NOKOMGW-Ui5.0/");
    // File f = invoker.download("otsweb/passCodeAction.do?rand=lrand&" + Math.random(), "jpg", null);
    // String code = JOptionPane.showInputDialog(new ImageIcon(ImageIO.read(f)));
    // System.out.println(code);
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("loginUser.user_name", "name");
    // params.put("user.password", "123456");
    // params.put("randCode", code);
    // String result = invoker.post("otsweb/loginAction.do?method=login", params);
    System.out.println(s);
    // invoker.destroy();
  }
}
