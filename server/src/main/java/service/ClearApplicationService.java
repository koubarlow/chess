package service;

import dataaccess.ClearApplicationDAO;
import dataaccess.DataAccessException;

public class ClearApplicationService {

    private final ClearApplicationDAO clearApplicationDAO;

    public ClearApplicationService(ClearApplicationDAO clearApplicationDAO) {
        this.clearApplicationDAO = clearApplicationDAO;
    }

    public void clearApplication() throws Exception {
        this.clearApplicationDAO.clearApplication();
    }
}
