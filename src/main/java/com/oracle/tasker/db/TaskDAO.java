package com.oracle.tasker.db;

import com.oracle.tasker.api.Task;
import com.oracle.tasker.resources.TaskResource;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SQLUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskDAO extends AbstractDAO<Task> {

    private static final Logger logger = LoggerFactory.getLogger(TaskDAO.class);

    public TaskDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Task> findByTaskId(String taskId) {
        return Optional.ofNullable(get(taskId));
    }

    public Task create(Task task) {
        if(task.getTaskId() == null) {
            logger.debug("TaskId in Task is null. so generating the taskId.");
            task.setTaskId(UUID.randomUUID().toString());
        }
        return persist(task);
    }

    public List<Task> findAll() {
        return list(namedTypedQuery("com.oracle.tasker.api.Task.findAll"));
    }

    public boolean deleteByTaskId(String taskId) {

        Optional<Task> task = findByTaskId(taskId);
        if(task.isPresent()) {
            currentSession().delete(task.get());
            logger.debug("Task was deleted by TaskId: {}", taskId);
            return true;
        }

        logger.debug("No Task deleted by TaskId: {}", taskId);
        return false;
    }
}
