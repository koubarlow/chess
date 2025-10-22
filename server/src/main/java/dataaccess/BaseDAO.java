package dataaccess;

import java.util.UUID;

public interface BaseDAO {

    static String generateId() {
        return UUID.randomUUID().toString();
    }
}
