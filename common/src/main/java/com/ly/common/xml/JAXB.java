package com.ly.common.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXB {
  public static Marshaller getMarshaller(Class<?> c, boolean format) {
    Marshaller m;
    try {
      m = JAXBContext.newInstance(c).createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
      return m;
    } catch (JAXBException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Unmarshaller getUnmarshaller(Class<?> c) {
    Unmarshaller u;
    try {
      u = JAXBContext.newInstance(c).createUnmarshaller();
      return u;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
