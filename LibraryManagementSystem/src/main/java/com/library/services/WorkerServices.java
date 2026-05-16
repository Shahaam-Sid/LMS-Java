package com.library.services;

import com.library.exceptions.DuplicatePupilException;
import com.library.exceptions.WorkerNotFoundException;
import com.library.models.Admin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for WorkerServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class WorkerServices {

    private final Map<String, Admin> workers = new HashMap<>();
    /**
     * Checks if list is empty
     * @return true if empty, else not
     */
    public boolean isEmpty() {return workers.isEmpty();}

    /**
     * register new member
     * @param worker to register
     * @throws DuplicatePupilException if worker already exist
     */
    public void admitWorker(Admin worker) throws DuplicatePupilException {
        if (workers.containsKey(worker.getWorkerID()))
            throw new DuplicatePupilException(worker.getWorkerID());

        workers.put(worker.getWorkerID(), worker);
    }
    /**
     * get admin object
     * @param id of worker to get
     * @return admin
     * @throws WorkerNotFoundException if worker not found
     */
    public Admin getWorker(String id) throws WorkerNotFoundException {
        Admin worker = workers.get(id);
        if (worker == null) throw new WorkerNotFoundException(id);

        return worker;
    }
    /**
     * removes worker
     * @param id of worker to remove
     * @throws WorkerNotFoundException if worker not found
     */
    public void removeWorker(String id) throws WorkerNotFoundException {
        if (!workers.containsKey(id)) throw new WorkerNotFoundException(id);

        workers.remove(id);
    }

    /**
     * searches for worker
     * @param query string for worker to search
     * @return list of results matched
     */
    public List<Admin> searchWorker(String query) {
        List<Admin> result = new ArrayList<>();
        for (Admin worker : workers.values()) {
            if (worker.matchesQuery(query)) result.add(worker);
        }

        return result;
    }
    /**
     * returns list of all workers
     * @return list of workers
     */
    public List<Admin> getAllWorkers() {
        return new ArrayList<>(workers.values());
    }
}