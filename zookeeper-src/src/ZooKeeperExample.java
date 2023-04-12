package src;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperExample implements Watcher {
    private static final int SESSION_TIMEOUT = 5000;
    private static final String ZOOKEEPER_SERVER = "localhost:2181";
    private static final String ZNODE_PATH = "/myznode";
    private static CountDownLatch connectedSignal = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, new ZooKeeperExample());
        connectedSignal.await();
        System.out.println("Connected to ZooKeeper server");
        if (zk.exists(ZNODE_PATH, true) == null) {
            zk.create(ZNODE_PATH, "Hello World".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        Stat stat = new Stat();
        byte[] data = zk.getData(ZNODE_PATH, true, stat);
        System.out.println("Data in znode: " + new String(data));
    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }
}
