/**
 * 
 */
package com.taskmanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Task manager
 * 
 * @author fei
 *
 */
public class TaskManager {

	private final int maxCapacity;

	/**
	 * Internal data structure to store processes.
	 */
	private List<Process> processes = new ArrayList<Process>();

	/**
	 * Constructor with a prefixed maximum capacity
	 * 
	 * @param maxCapacity
	 */
	public TaskManager(int maxCapacity) {
		if (maxCapacity < 1) {
			throw new IllegalArgumentException("The max. capacity can not less than 1");
		}

		this.maxCapacity = maxCapacity;
	}

	/**
	 * Add with default behavior, see {@link AddOption#DEFAULT}.
	 * 
	 * @param process Given process.
	 * @throws MaxCapacityReachedException If the maximum capacity is reached in
	 *                                     case of default behavior is chosen.
	 */
	public void add(Process process) throws MaxCapacityReachedException {
		this.add(process, AddOption.DEFAULT);
	}

	/**
	 * Add process with given add option.
	 * 
	 * @param process   Given process
	 * @param addOption Given add option
	 * @throws MaxCapacityReachedException
	 */
	public void add(Process process, AddOption addOption) throws MaxCapacityReachedException {
		if (process == null) {
			throw new IllegalArgumentException("Given process can not be null");
		}

		if (addOption == null) {
			throw new IllegalArgumentException("The add option is not set");
		}

		if (processes.size() == maxCapacity) {
			if (addOption == AddOption.DEFAULT) {
				throw new MaxCapacityReachedException(
						"Maximum capacity of " + this.maxCapacity + ". process(es) is reached");
			} else if (addOption == AddOption.FIFO) {
				this.processes.remove(0);
			} else {
				int indexOfOldestLowestPriorityProcess = this.findIndexOfOldestLowestPriorityProcess();

				if (indexOfOldestLowestPriorityProcess >= 0) {
					if (process.getPriority().getValue() <= this.processes.get(indexOfOldestLowestPriorityProcess)
							.getPriority().getValue()) {
						// skip
						return;
					} else {
						this.processes.remove(indexOfOldestLowestPriorityProcess);
					}
				}
			}
		}

		this.processes.add(process);
	}

	/**
	 * @param listOption
	 * @return
	 */
	public List<Process> list(ListOption listOption) {
		if (listOption == null) {
			throw new IllegalArgumentException("The list option is not set");
		}

		// in order to keep the internal list order unchanged.
		List<Process> clonedList = new ArrayList<Process>(this.processes);
		if (listOption == ListOption.TIME) {
			return clonedList;
		} else if (listOption == ListOption.PRIORITY) {
			Collections.sort(clonedList, priorityComparator);
		} else {
			Collections.sort(clonedList, idComparator);
		}

		return clonedList;
	}

	/**
	 * Find the index of process which is the oldest and has the lowest priority.
	 * @return -1 if the list is empty, otherwise the found index.
	 */
	private int findIndexOfOldestLowestPriorityProcess() {
		if (this.processes.size() == 0) {
			return -1;
		}

		int indexOfOldestLowestPriorityProcess = 0;
		Process oldestLowestPriorityProcess = this.processes.get(0);

		for (int i = 1; i < this.processes.size(); i++) {
			if (this.processes.get(i).getPriority().getValue() < oldestLowestPriorityProcess.getPriority().getValue()) {
				indexOfOldestLowestPriorityProcess = i;
			}
		}

		return indexOfOldestLowestPriorityProcess;
	}

	/**
	 * Compares processes by priority, highest first, lowest last. If the priority
	 * of 2 processes is the same, sort by PID.
	 */
	public static Comparator<Process> priorityComparator = new Comparator<Process>() {
		@Override
		public int compare(Process p1, Process p2) {
			if (p1.getPriority().getValue() > p2.getPriority().getValue()) {
				return -1;
			} else if (p1.getPriority().getValue() < p2.getPriority().getValue()) {
				return 1;
			}

			return p1.getPid().compareTo(p2.getPid());
		}
	};

	/**
	 * Compares processes by PID.
	 */
	public static Comparator<Process> idComparator = new Comparator<Process>() {
		@Override
		public int compare(Process p1, Process p2) {
			return p1.getPid().compareTo(p2.getPid());
		}
	};
}
