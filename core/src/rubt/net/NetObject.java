package rubt.net;

public abstract class NetObject {

    private static int idprov;
    public int id;

    public NetObject() {
        this.id = idprov++;
    }
}
