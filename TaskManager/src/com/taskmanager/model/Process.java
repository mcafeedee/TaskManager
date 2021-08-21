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
	 * the priority of the process
	 */
	private Priority priority;

	public Process(Priority priority) {
		this.pid = UUID.randomUUID().toString();
		this.priority = priority;
	}
	
	public void kill() {
	}

	public Priority getPriority() {
		return priority;
	}

	public String getPid() {
		return pid;
	}

	@Override
	public int hashCode() {
		return pid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Process other = (Process) obj;
		
		return pid.equals(other.pid);
	}
	
	
	
}
