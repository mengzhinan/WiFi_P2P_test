package com.duke.p2plib.sockethelper;

import android.text.TextUtils;

import com.duke.baselib.DExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: duke
 * @DateTime: 2019-05-25 17:41
 * @Description:
 */
public class ClientSocketHelper extends SocketBase {

    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private boolean isQuitReadMessage;
    private String serverIP;

    @Override
    public void clear() {
        super.clear();
        isQuitReadMessage = true;
        IOHelper.closeOutputStream(outputStream);
        IOHelper.closeInputStream(inputStream);
        IOHelper.closeSocket(socket);
    }

    public ClientSocketHelper(OnReceiveListener onReceiveListener) {
        super(onReceiveListener);
    }

    public void postConnectToServer(final String ip) {
        if (TextUtils.isEmpty(ip)) {
            return;
        }
        serverIP = ip;
        DExecutor.Companion.get().execute(new Runnable() {
            @Override
            public void run() {
                initSocket();
            }
        });
        DExecutor.Companion.get().execute(new Runnable() {
            @Override
            public void run() {
                receive();
            }
        });
    }

    private void initSocket() {
        try {
            socket = new Socket(serverIP, SERVER_PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            String text = "连接到服务端 -> " + PHONE_INFO;
            IOHelper.writeText(outputStream, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        try {
            while (!isQuitReadMessage) {
                Thread.sleep(500);
                String text = IOHelper.readText(inputStream);
                if (TextUtils.isEmpty(text)) {
                    continue;
                }
                postToUI(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(final String text) {
        if (outputStream == null) {
            return;
        }
        DExecutor.Companion.get().execute(new Runnable() {
            @Override
            public void run() {
                if (outputStream == null) {
                    return;
                }
                try {
                    IOHelper.writeText(outputStream, text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
