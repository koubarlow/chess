package service;

import dataaccess.clearapplication.ClearApplicationDAO;

public class ClearApplicationService {

    private final ClearApplicationDAO clearApplicationDAO;

    public ClearApplicationService(ClearApplicationDAO clearApplicationDAO) {
        this.clearApplicationDAO = clearApplicationDAO;
    }

    public void clearApplication() throws Exception {
        this.clearApplicationDAO.clearApplication();
    }
}
