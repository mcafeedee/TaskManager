package com.taskmanager.model;

/**
 * Contains all possible values for process priority.
 * 
 * @author fei
 *
 */
public enum Priority {

	LOW(0), MEDIUM(1), HIGH(2);

	/**
	 * Value for priority comparison. Low priority has the lowest value, and high priority has the highest value.
	 */
	private int value;

	private Priority(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
