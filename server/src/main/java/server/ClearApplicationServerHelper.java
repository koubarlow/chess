package server;

import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import service.ClearApplicationService;

public class ClearApplicationServerHelper {

    private final ClearApplicationService clearApplicationService;

    public ClearApplicationServerHelper(ClearApplicationService clearApplicationService) { this.clearApplicationService = clearApplicationService; }

    public void clearApplication(Context context) throws Exception {
        try {
            clearApplicationService.clearApplication();
        } catch (DataAccessException e) {
            context.json(e.toJson());
            context.status(500);
        }
    }
}
