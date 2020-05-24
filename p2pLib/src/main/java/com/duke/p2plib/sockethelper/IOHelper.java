package com.duke.p2plib.sockethelper;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author: duke
 * @DateTime: 2020-05-24 12:52
 * @Description:
 */
public class IOHelper {

    public static void closeServerSocket(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket = null;
            }
        }
    }

    public static void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket = null;
            }
        }
    }

    public static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream = null;
            }
        }
    }

    public static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputStream = null;
            }
        }
    }

    public static void writeText(OutputStream outputStream, String text) throws IOException {
        if (outputStream == null || TextUtils.isEmpty(text)) {
            return;
        }
        outputStream.write(text.getBytes());
    }

    public static String readText(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        byte[] bytes = new byte[inputStream.available()];
        int length = inputStream.read(bytes);
        return new String(bytes, 0, length, Charset.defaultCharset());
    }

}
