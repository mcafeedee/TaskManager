package com.taskmanager.model;

/**
 * This exception is used if the maximum capacity in the task manager is reached.
 * @author fei
 *
 */
public class MaxCapacityReachedException extends Exception {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 1636327951318694169L;

	public MaxCapacityReachedException(String message) {
		super(message);
	}
}
