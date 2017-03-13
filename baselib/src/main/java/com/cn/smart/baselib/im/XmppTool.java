package com.cn.smart.baselib.im;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * author：leo on 2016/12/5 15:32
 * email： leocheung4ever@gmail.com
 * description: XMPP 工具类
 * what & why is modified:
 */

public class XmppTool {

    private static XMPPConnection con = null;

    private static void openConnection() {
        try {
            //声明XMPP连接 ConnectionConfiguration类的参数 分别为ip地址,端口号,
            ConnectionConfiguration connConfig = new ConnectionConfiguration("192.168.1.116", 5222);
            con = new XMPPConnection(connConfig);
            //开始连接
            con.connect();
        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
    }

    public static XMPPConnection getConnection() {
        if (con == null) {
            openConnection();
        }
        return con;
    }

    public static void closeConnection() {
        con.disconnect();
        con = null;
    }

}
