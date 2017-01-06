package com.ly.common.script;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkInterfaces {

  public static Map<String, List<String>> getInterfaceDetails() throws Exception {

    Map<String, List<String>> map = new HashMap<String, List<String>>();

    Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

    for (NetworkInterface netint : Collections.list(nets)) {
      List<String> list = getInterfaceAddresses(netint);

      map.put(netint.getDisplayName(), list);
    }

    return map;
  }

  private static List<String> getInterfaceAddresses(NetworkInterface netint) throws Exception {

    List<String> list = new ArrayList<String>();

    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
    for (InetAddress inetAddress : Collections.list(inetAddresses)) {

      list.add(inetAddress.toString());
    }

    return list;
  }
}
