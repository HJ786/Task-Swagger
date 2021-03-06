package com.tutorialsdesk.rest.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
 
import com.example.exceptions.TaskAlreadyExistsException;
import com.example.exceptions.TaskNotFoundException;
import com.tutorialsdesk.rest.dao.TaskDao;
import com.tutorialsdesk.rest.model.Task;
 

public class TaskResource {
	private final ConcurrentMap< String, Task > tasks = new ConcurrentHashMap< String, Task >(); 
	
	public Collection< Task > getPeople( int page, int pageSize ) {
		final Collection< Task > slice = new ArrayList< Task >( pageSize );
		
        final Iterator< Task > iterator = tasks.values().iterator();
        for( int i = 0; slice.size() < pageSize && iterator.hasNext(); ) {
        	if( ++i > ( ( page - 1 ) * pageSize ) ) {
        		slice.add( iterator.next() );
        	}
        }
		
		return slice;
	}
	  
@Context
  UriInfo uriInfo;
  @Context
  Request request;
  String id;
  public TaskResource(UriInfo uriInfo, Request request, String id) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.id = id;
  }
   
//Application integration     
/*@GETpackage com.tutorialsdesk.rest.resources;
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Task getTodo() {
    Task task = TaskDao.instance.getModel().get(id);
    if(task==null)
      throw new RuntimeException("Get: Task with " + id +  " not found");
    return task;
  }*/
   
 
 public void removeTask( final String id ) {
		if( tasks.remove( id ) == null ) {
			throw new TaskNotFoundException( id );
		}
	}
 
  
  
	public Task addTask( final String id, final String summary, final String description ) {
		final Task task = new Task( id );
		task.setSummary( summary );
		task.setDescription( description );
				
		if( tasks.putIfAbsent( id, task ) != null ) {
			throw new TaskAlreadyExistsException( id );
		}
		
		return task;
	} 
	
	
	
  public Task getByID( final String id ) {
		final Task task = tasks.get( id );
		
		if( task == null ) {
			throw new TaskNotFoundException( id );
		}
		
		return task;
	} 
  
  
  /*
//for the browser
 @GET
 @Produces(MediaType.TEXT_XML)
 public Task getTodoHTML() {
   Task task = TaskDao.instance.getModel().get(id);
   if(task==null)
     throw new RuntimeException("Get: Task with " + id +  " not found");
   return task;
 }
  
 @PUT
 @Consumes(MediaType.APPLICATION_XML)
 public Response putTodo(JAXBElement<Task> task) {
   Task c = task.getValue();
   return putAndGetResponse(c);
 }
  
 @DELETE
 public void deleteTodo() {
   Task c = TaskDao.instance.getModel().remove(id);
   if(c==null)
     throw new RuntimeException("Delete: Task with " + id +  " not found");
 }
  
 private Response putAndGetResponse(Task task) {
   Response res;
   if(TaskDao.instance.getModel().containsKey(task.getId())) {
     res = Response.noContent().build();
   } else {
     res = Response.created(uriInfo.getAbsolutePath()).build();
   }
   TaskDao.instance.getModel().put(task.getId(), task);
   return res;
 } */
  
  
}
