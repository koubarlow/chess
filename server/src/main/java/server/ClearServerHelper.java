package server;

import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import service.ClearService;

public class ClearServerHelper {

    private final ClearService clearService;

    public ClearServerHelper(ClearService clearService) { this.clearService = clearService; }

    public void clearApplication(Context context) throws Exception {
        try {
            clearService.clearApplication();
        } catch (DataAccessException e) {
            context.json(e.toJson());
            context.status(500);
        }
    }
}
