import com.github.aom.core.Engine;
import com.github.aom.core.EngineAPI;
import com.github.aom.core.SimpleEngine;
import com.github.aom.core.event.EventHandler;
import com.github.aom.core.event.EventPriority;
import com.github.aom.core.event.protocol.SessionClosedEvent;
import com.github.aom.core.event.protocol.SessionConnectedEvent;
import com.github.aom.core.event.protocol.SessionMessageEvent;
import com.github.aom.core.protocol.Message;
import com.github.aom.core.protocol.SessionManager;
import com.github.aom.core.protocol.proxy.ProxyClientMessage;
import com.github.aom.core.protocol.proxy.ProxyServerMessage;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Bootstrap {
    private Map<Integer, String> mNames = new HashMap<>();

    /**
     * Entry point of the bootstrap.
     */
    public static void main(String[] argv) {
        final Bootstrap nBootstrap = new Bootstrap();
        new SimpleEngine().initialise(nBootstrap::onStart, nBootstrap::onEnd);
    }

    /**
     * Called when the engine is started.
     */
    private void onStart(Engine engine) {
        System.out.println("BEGIN");
        engine.getEventManager().registerEvents(null, this);
    }

    /**
     * Called when the engine is stopped.
     */
    private void onEnd(Engine engine) {
        System.out.println("END");
    }

    /**
     * Handle when the DLL has been connected.
     */
    @EventHandler(priority = EventPriority.HIGH)
    private void onSessionConnect(SessionConnectedEvent event) {
        System.out.println("CONNECTED");
    }

    /**
     * Handle when the DLL has been disconnected.
     */
    @EventHandler(priority = EventPriority.HIGH)
    private void onSessionDisconnect(SessionClosedEvent event) {
        System.out.println("DISCONNECTED");
    }

    /**
     * Handle when the DLL has send a message.
     */
    @EventHandler(priority = EventPriority.HIGH)
    private void onSessionMessage(SessionMessageEvent event) {
        final Message nMessage = event.getMessage();
        if (nMessage instanceof ProxyClientMessage) {
            onProxyClientMessage((ProxyClientMessage) nMessage);
        } else if (nMessage instanceof ProxyServerMessage) {
            onProxyServerMessage((ProxyServerMessage) nMessage);
        }
    }

    /**
     * Handle when the DLL has send a {@linkProxyClientMessage}.
     */
    private void onProxyClientMessage(ProxyClientMessage message) {
        System.out.println("CLIENT: " + HexBin.encode(message.getBytes()));
    }

    /**
     * Handle when the DLL has send a {@ProxyServerMessage}.
     */
    private void onProxyServerMessage(ProxyServerMessage message) {
        System.out.println("SERVER: " + HexBin.encode(message.getBytes()));

        final ByteBuf buf = Unpooled.wrappedBuffer(message.getBytes());

        switch (buf.readByte()) {
            case 0x1F:
                final int index = readUnsignedShort(buf);
                buf.skipBytes(2 + 2 + 1 + 1 + 1 + 2 + 2 + 2 + 2 + 2);

                final String name = readString(buf);
                mNames.put(index, name);
                break;
            case 0x6F:
                final String chat = readString(buf);
                final String who = mNames.get(readUnsignedShort(buf));

                if (!who.equals("Wolftein") && !chat.equals("") && !chat.equals(" "))
                    speak(who + ": " + chat);
                break;
        }
    }

    public String readString(ByteBuf buf) {
        final int len = readUnsignedShort(buf);
        final byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new String(bytes);
    }

    public int readUnsignedShort(ByteBuf buf) {
        final int b0 = buf.readByte();
        final int b1 = buf.readByte();
        return (short) ((b1 & 0xff) << 8 | (b0 & 0xff));
    }

    public void speak(String text) {
        final ByteOutputStream out = new ByteOutputStream(3 + text.length());
        final DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeByte(0x02);
            dos.writeByte(text.length() & 0xFF);
            dos.writeByte((text.length() >> 8) & 0xFF);
            dos.writeBytes(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ProxyServerMessage nMessage = new ProxyServerMessage(out.getBytes());
        EngineAPI.getSessionManager().sendAll(SessionManager.ALL, nMessage);
    }
}
