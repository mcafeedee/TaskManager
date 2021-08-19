package com.taskmanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.taskmanager.TaskManager;
import com.taskmanager.model.AddOption;
import com.taskmanager.model.ListOption;
import com.taskmanager.model.MaxCapacityReachedException;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Process;

public class TestTaskManager {

	private TaskManager underTest;
	private Process p0Low = new Process(Priority.LOW);
	private Process p1High = new Process(Priority.HIGH);
	private Process p2Medium = new Process(Priority.MEDIUM);
	private Process p3Low = new Process(Priority.LOW);

	@Test
	public void testNullCapacity() {

		Exception expectedException = null;
		try {
			new TaskManager(0);
		} catch (IllegalArgumentException e) {
			expectedException = e;
		}

		assertNotNull(expectedException);
	}

	@Test
	public void testAdd() {
		this.addProcessesWithinCapacity(3, p0Low, p1High, p2Medium);

		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p0Low.getPid(), list.get(0).getPid());
		assertEquals(p1High.getPid(), list.get(1).getPid());
		assertEquals(p2Medium.getPid(), list.get(2).getPid());
	}

	@Test
	public void testAddByDefault() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		Exception expectedException = null;
		Process newProcess = new Process(Priority.MEDIUM);
		// max capacity is reached, add causes an exception
		try {
			underTest.add(newProcess);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNotNull(expectedException);

		// the process list is the same before adding
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p0Low.getPid(), list.get(0).getPid());
		assertEquals(p1High.getPid(), list.get(1).getPid());
		assertEquals(p2Medium.getPid(), list.get(2).getPid());
		assertEquals(p3Low.getPid(), list.get(3).getPid());
	}

	@Test
	public void testAddByFifo() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);
		Exception expectedException = null;

		Process newProcess = new Process(Priority.MEDIUM);
		// max capacity is reached, add causes an exception
		try {
			underTest.add(newProcess, AddOption.FIFO);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// this oldest one is removed
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p1High.getPid(), list.get(0).getPid());
		assertEquals(p2Medium.getPid(), list.get(1).getPid());
		assertEquals(p3Low.getPid(), list.get(2).getPid());
		assertEquals(newProcess.getPid(), list.get(3).getPid());
	}

	@Test
	public void testAddByPriority_AddLowPriority() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);
		Exception expectedException = null;

		Process newProcess = new Process(Priority.LOW);
		// skipped
		try {
			underTest.add(newProcess, AddOption.PRIORITY);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// the process to add has the low priority, this process is skipped
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p0Low.getPid(), list.get(0).getPid());
		assertEquals(p1High.getPid(), list.get(1).getPid());
		assertEquals(p2Medium.getPid(), list.get(2).getPid());
		assertEquals(p3Low.getPid(), list.get(3).getPid());
	}

	@Test
	public void testAddByPriority_AddMediumPriority_1() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);
		Exception expectedException = null;

		Process newProcess = new Process(Priority.MEDIUM);
		try {
			underTest.add(newProcess, AddOption.PRIORITY);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// this oldest low prioritized process is removed, the new is added at the end
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p1High.getPid(), list.get(0).getPid());
		assertEquals(p2Medium.getPid(), list.get(1).getPid());
		assertEquals(p3Low.getPid(), list.get(2).getPid());
		assertEquals(newProcess.getPid(), list.get(3).getPid());
	}

	@Test
	public void testAddByPriority_AddMediumPriority_2() {
		this.addProcessesWithinCapacity(4, p1High, p2Medium, p3Low, p0Low);
		Exception expectedException = null;

		Process newProcess = new Process(Priority.MEDIUM);
		try {
			underTest.add(newProcess, AddOption.PRIORITY);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// this oldest low prioritized process is removed, the new is added at the end
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p1High.getPid(), list.get(0).getPid());
		assertEquals(p2Medium.getPid(), list.get(1).getPid());
		assertEquals(p0Low.getPid(), list.get(2).getPid());
		assertEquals(newProcess.getPid(), list.get(3).getPid());
	}

	@Test
	public void testListByTime() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(4, list.size());
		assertEquals(p0Low.getPid(), list.get(0).getPid());
		assertEquals(p1High.getPid(), list.get(1).getPid());
		assertEquals(p2Medium.getPid(), list.get(2).getPid());
		assertEquals(p3Low.getPid(), list.get(3).getPid());
	}

	@Test
	public void testListById() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		List<Process> list = underTest.list(ListOption.ID);
		assertEquals(4, list.size());
		assertTrue(list.get(0).getPid().compareTo(list.get(1).getPid()) < 0);
		assertTrue(list.get(1).getPid().compareTo(list.get(2).getPid()) < 0);
		assertTrue(list.get(2).getPid().compareTo(list.get(3).getPid()) < 0);
	}

	@Test
	public void testListByPriority() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		List<Process> list = underTest.list(ListOption.PRIORITY);
		assertEquals(4, list.size());
		assertEquals(p1High.getPid(), list.get(0).getPid());
		assertEquals(p2Medium.getPid(), list.get(1).getPid());

		if (p0Low.getPid().compareTo(p3Low.getPid()) < 0) {
			assertEquals(p0Low.getPid(), list.get(2).getPid());
			assertEquals(p3Low.getPid(), list.get(3).getPid());
		} else {
			assertEquals(p3Low.getPid(), list.get(2).getPid());
			assertEquals(p0Low.getPid(), list.get(3).getPid());
		}

	}

	@Test
	public void testKillByProcess() {
		this.addProcessesWithinCapacity(4, p1High, p2Medium, p3Low, p0Low);

		underTest.killByProcess(p2Medium);

		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(3, list.size());
		assertEquals(p1High.getPid(), list.get(0).getPid());
		assertEquals(p3Low.getPid(), list.get(1).getPid());
		assertEquals(p0Low.getPid(), list.get(2).getPid());
	}

	@Test
	public void testKillByPriority_1() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		underTest.killByPriority(Priority.LOW);

		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(2, list.size());
		assertEquals(p1High.getPid(), list.get(0).getPid());
		assertEquals(p2Medium.getPid(), list.get(1).getPid());
	}

	@Test
	public void testKillByPriority_2() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		underTest.killByPriority(Priority.MEDIUM);

		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(3, list.size());
		assertEquals(p0Low.getPid(), list.get(0).getPid());
		assertEquals(p1High.getPid(), list.get(1).getPid());
		assertEquals(p3Low.getPid(), list.get(2).getPid());
	}

	@Test
	public void testKillAll() {
		this.addProcessesWithinCapacity(4, p0Low, p1High, p2Medium, p3Low);

		underTest.killAll();
		;

		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(0, list.size());
	}

	/**
	 * Utility method to add give processes to the task manager.
	 */
	private void addProcessesWithinCapacity(int maxCapacity, Process... processes) {
		if (processes.length > maxCapacity) {
			throw new IllegalArgumentException(
					"Not expected in this method to add more processes than defined max capacity");
		}

		this.underTest = new TaskManager(maxCapacity);

		// all processes can be added without exception
		Exception expectedException = null;
		try {
			for (Process p : processes) {
				underTest.add(p);
			}
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);
	}

}
