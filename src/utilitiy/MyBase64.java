package utilitiy;

import sun.misc.BASE64Decoder;

public class MyBase64 {
    public MyBase64() {
    }

    public static String getFromBASE64(String s, String c) {
        if (s == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(s);
                return new String(b, c);
            } catch (Exception var3) {
                return null;
            }
        }
    }
}
