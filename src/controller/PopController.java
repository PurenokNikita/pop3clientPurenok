package controller;

import java.io.*;
import java.net.Socket;
import java.util.Vector;
import view.MainFrame;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class PopController {
    final int POP_PORT = 995;
    String pop_server = "";
    String my_email_addr = "";
    String my_email_pass = "";
    Socket pop;
    BufferedReader pop_in;
    PrintWriter pop_out;
    MainFrame mainFrame;

    public PopController(String pop_server, String my_email_addr, String my_email_pass, MainFrame mainFrame) {
        this.pop_server = pop_server;
        this.my_email_addr = my_email_addr;
        this.my_email_pass = my_email_pass;
        this.login2PopServer();
    }

    public int getMailCount() {
        Vector<String> lines = this.getLines("LIST");
        System.out.println((String)lines.get(0));
        return lines.size() != 0 && !((String)lines.get(0)).startsWith("-ERR") ? lines.size() - 2 : -1;
    }

    public boolean deleteItem(String number) {
        String line = this.getSingleLine("DELE " + number);
        return line.length() != 0 && line.startsWith("+OK");
    }

    public Vector<String> getItemString(String number) {
        return this.getLines("RETR " + number);
    }

    private Vector<String> getLines(String command) {
        Vector lines = new Vector();

        try {
            boolean cont = true;
            String buf = null;
            this.pop_out.print(command + "\r\n");
            this.pop_out.flush();
            System.out.println("send-->" + command);
            String res = this.pop_in.readLine();
            lines.addElement(res);
            System.out.println("receive-->" + res);
            if (!"+OK".equals(res.substring(0, 3))) {
                return lines;
            }

            while(cont) {
                buf = this.pop_in.readLine();
                lines.addElement(buf);
                System.out.println("receive-->" + buf);
                if (".".equals(buf)) {
                    cont = false;
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return lines;
    }

    private String getSingleLine(String command) {
        String res = null;

        try {
            this.pop_out.print(command + "\r\n");
            this.pop_out.flush();
            System.out.println("send-->" + command);
            res = this.pop_in.readLine();
            System.out.println("receive-->" + res);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return res;
    }

    private void login2PopServer() {
        try {
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.pop = sf.createSocket(this.pop_server, 995);
            OutputStream out = this.pop.getOutputStream();
            out.write("\nConnection established.\n\n".getBytes());
            out.flush();
            this.pop_in = new BufferedReader(new InputStreamReader(this.pop.getInputStream()));
            this.pop_out = new PrintWriter(this.pop.getOutputStream());
            String res = this.pop_in.readLine();
            System.out.println("receive--->" + res);
            if (!"+OK".equals(res.substring(0, 3))) {
                this.pop.close();
                System.exit(0);
            }

            this.getSingleLine("USER " + this.my_email_addr);
            this.getSingleLine("PASS " + this.my_email_pass);
        } catch (Exception var2) {
            var2.printStackTrace();
        }


    }

    public void quitPop() {
        this.getSingleLine("QUIT");

        try {
            this.pop.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
