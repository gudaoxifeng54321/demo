package com.ly.common.shell;

import com.ly.common.X;

public class CommandExecutorThread implements Runnable {
  private static final String DOWNLOAD_DIRECTORY = "com.gf.httpdig.downloadDirectory";
  private String              cmd                = null;

  public CommandExecutorThread(String cmd) {
    this.cmd = cmd;
  }

  @Override
  public void run() {
    X.makeDir(X.getConfig(DOWNLOAD_DIRECTORY));
    String[] processResult = Terminal.execute(cmd, X.getConfig(DOWNLOAD_DIRECTORY), X.TRUE);
    if (X.SUCCESS.equals(processResult[0])) {
      Terminal.execute("echo '" + cmd + "' >> doneList", X.getConfig(DOWNLOAD_DIRECTORY), X.TRUE);
    }
  }
}
