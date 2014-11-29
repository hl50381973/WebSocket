package org.conan.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;


public class DemoServlet extends WebSocketServlet {

    private static final long serialVersionUID = -4853540828121130946L;
    private static ArrayList<MyMessageInbound> mmiList = new ArrayList<MyMessageInbound>();

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol) {
		// TODO Auto-generated method stub
		 return new MyMessageInbound();
	}

    
    
    protected StreamInbound createWebSocketInbound(String str, HttpServletRequest request) {
        return new MyMessageInbound();
    }

    private class MyMessageInbound extends MessageInbound {
        WsOutbound myoutbound;

        @Override
        public void onOpen(WsOutbound outbound) {
            try {
                System.out.println("Open Client.");
                this.myoutbound = outbound;
                mmiList.add(this);
                outbound.writeTextMessage(CharBuffer.wrap("Hello! now is"+new Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(int status) {
            System.out.println("Close Client.");
            mmiList.remove(this);
        }

        @Override
        public void onTextMessage(CharBuffer cb) throws IOException {
            System.out.println("Accept Message1 : " + cb);
            for (MyMessageInbound mmib : mmiList) {
                CharBuffer buffer = CharBuffer.wrap("send from server:"+cb);
                mmib.myoutbound.writeTextMessage(buffer);
                mmib.myoutbound.flush();
            }
        }

        @Override
        public void onBinaryMessage(ByteBuffer bb) throws IOException {
        }

		public int getReadTimeout() {
			// TODO Auto-generated method stub
			return 0;
		}
    }


}