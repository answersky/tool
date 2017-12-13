import java.util.UUID;

/**
 * @author answer
 *         2017/12/4
 */
public class Uuid {
    public static void main(String[] args) {
        for (int i = 1; i <= 2982; i++) {
            String uuid = UUID.randomUUID().toString();
            System.out.println(uuid);
        }
    }
}
