package com.taskmanager.model;

/**
 * Contains all possible add options.
 * @author fei
 *
 */
public enum AddOption {

	/**
	 * The default behaviour is that we can accept new processes 
	 * till when there is capacity inside the Task Manager, 
	 * otherwise we won’t accept any new process
	 */
	DEFAULT,
	
	/**
	 * Accept all new processes until the max size is reached.
	 * If the max size is reached, killing and removing from the TM list 
	 * the oldest one (First-In, First-Out)
	 */
	FIFO,
	
	/**
	 * When the max size is reached, should result into an evaluation: if the new 
	 * process passed in the add() call has a higher priority compared to any of 
	 * the existing one, we remove the lowest priority that is the oldest, 
	 * otherwise we skip it
	 */
	PRIORITY
}
