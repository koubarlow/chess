package service;

import dataaccess.clearapplication.ClearApplicationDAO;

public class ClearService {

    private final ClearApplicationDAO clearApplicationDAO;

    public ClearService(ClearApplicationDAO clearApplicationDAO) {
        this.clearApplicationDAO = clearApplicationDAO;
    }

    public void clearApplication() throws Exception {
        this.clearApplicationDAO.clearApplication();
    }
}
