package server;

import dataaccess.ClearApplicationDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.ClearApplicationService;

public class ClearApplicationServerHelper {

    private final ClearApplicationService clearApplicationService;

    public ClearApplicationServerHelper(ClearApplicationService clearApplicationService) { this.clearApplicationService = clearApplicationService; }

    public void clearApplication(Context context) throws DataAccessException {
        clearApplicationService.clearApplication();
    }
}
