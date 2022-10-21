package rubt.client;

import arc.net.NetListener;
import rubt.net.PacketSerializer;

public class Client extends arc.net.Client implements NetListener {

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);
    }
}