/**
 * 
 */
package com.taskmanager.model;

import java.util.UUID;

/**
 * This class represents the object process 
 * @author Fei
 *
 */
public class Process {

	/**
	 * A unique identifier (PID). As this PID is unmodifiable, there is no set method.
	 */
	private final String pid;
	
	/**
	 * the initialized priority which can not be change
	 */
	private final Priority initPriority;
	
	/**
	 * the priority of the process
	 */
	private Priority priority;

	public Process(Priority priority) {
		this.pid = UUID.randomUUID().toString();
		this.initPriority = priority;
		this.priority = priority;
	}
	
	/**
	 * the priority is set to the initial value before dying
	 */
	public void kill() {
		this.priority = this.initPriority;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getPid() {
		return pid;
	}
	
	
	
}
