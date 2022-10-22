package rubt.logic;

import rubt.net.Packet;
import rubt.net.PacketProvider;

public enum State implements PacketProvider {
    menu, game;

    @Override
    public Packet pack() {
        return null;
    }
    
}
