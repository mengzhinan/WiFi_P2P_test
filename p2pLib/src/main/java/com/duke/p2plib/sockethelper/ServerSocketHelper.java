package com.duke.p2plib.sockethelper;

import android.text.TextUtils;

import com.duke.baselib.DExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: duke
 * @DateTime: 2019-05-25 17:40
 * @Description:
 */
public class ServerSocketHelper extends SocketBase {

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private boolean isQuitReadMessage;
    private boolean isQuitReadClient;

    @Override
    public void clear() {
        super.clear();
        isQuitReadMessage = true;
        isQuitReadClient = true;
        IOHelper.closeOutputStream(outputStream);
        IOHelper.closeInputStream(inputStream);
        IOHelper.closeSocket(socket);
        IOHelper.closeServerSocket(serverSocket);
    }

    public ServerSocketHelper(OnReceiveListener onReceiveListener) {
        super(onReceiveListener);
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
            serverSocket = new ServerSocket(SERVER_PORT);
            // 需要设置为无限超时
//            serverSocket.setSoTimeout(10000);
            while (!isQuitReadClient) {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                String text = "连接到客户端 -> " + PHONE_INFO;
                IOHelper.writeText(outputStream, text);
            }
        } catch (Exception e) {
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
