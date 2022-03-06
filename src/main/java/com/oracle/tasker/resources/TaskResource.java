package com.oracle.tasker.resources;

import com.codahale.metrics.annotation.Timed;
import com.oracle.tasker.api.Task;
import com.oracle.tasker.db.TaskDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/tasker")
@Produces({MediaType.APPLICATION_JSON})
public class TaskResource {

    private static final Logger logger = LoggerFactory.getLogger(TaskResource.class);

    private TaskDAO taskDAO;

    public TaskResource(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Path("/create")
    @POST
    @Timed
    @UnitOfWork
    public Task create(@Valid Task task) {
        logger.trace("Tasker: {}", task);
        return taskDAO.create(task);
    }

    @DELETE
    @Timed
    @UnitOfWork
    public boolean deleteByTaskId(@QueryParam("taskId") String taskId) {
        logger.trace("Delete Task by taskId: {}", taskId);
        return taskId != null ? taskDAO.deleteByTaskId(taskId) : false;
    }

    @GET
    @Timed
    @UnitOfWork
    public Task findByTaskId(@QueryParam("taskId") String taskId) {
        logger.trace("find Task by taskId: {}", taskId);
        Optional<Task> task = taskDAO.findByTaskId(taskId);
        return task.isPresent() ? task.get() : null;
    }

    @Path("/findAll")
    @GET
    @Timed
    @UnitOfWork
    public List<Task> findAllTasks() {
        logger.trace("Find all tasks...");
        return taskDAO.findAll();
    }

}
