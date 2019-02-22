package utilitiy;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MyBase64 {
    public MyBase64() {
    }

    public static String getBASE64(String s) {
        return s == null ? null : (new BASE64Encoder()).encode(s.getBytes());
    }

    public static String getFromBASE64(String s) {
        if (s == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(s);
                return new String(b);
            } catch (Exception var3) {
                return null;
            }
        }
    }
}
