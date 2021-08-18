package com.taskmanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.taskmanager.model.AddOption;
import com.taskmanager.model.ListOption;
import com.taskmanager.model.MaxCapacityReachedException;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Process;
import com.taskmanager.model.TaskManager;

public class TestTaskManager {

	private TaskManager underTest = new TaskManager(4);
	private Process p0 = new Process(Priority.LOW);
	private Process p1 = new Process(Priority.HIGH);
	private Process p2 = new Process(Priority.MEDIUM);
	private Process p3 = new Process(Priority.LOW);

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
	public void testAddByDefault() {
		this.addAllProcesses();
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
		assertEquals(p0.getPid(), list.get(0).getPid());
		assertEquals(p1.getPid(), list.get(1).getPid());
		assertEquals(p2.getPid(), list.get(2).getPid());
		assertEquals(p3.getPid(), list.get(3).getPid());
	}

	@Test
	public void testAddByFifo() {
		this.addAllProcesses();
		Exception expectedException = null;

		Process newProcess = new Process(Priority.MEDIUM);
		// max capacity is reached, add causes an exception
		try {
			underTest.add(newProcess, AddOption.FIFO);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// the process list is the same before adding
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p1.getPid(), list.get(0).getPid());
		assertEquals(p2.getPid(), list.get(1).getPid());
		assertEquals(p3.getPid(), list.get(2).getPid());
		assertEquals(newProcess.getPid(), list.get(3).getPid());
	}

	@Test
	public void testAddByPriority_AddLowPriority() {
		this.addAllProcesses();
		Exception expectedException = null;

		Process newProcess = new Process(Priority.LOW);
		// max capacity is reached, add causes an exception
		try {
			underTest.add(newProcess, AddOption.PRIORITY);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// the process list is the same before adding, the new process is skipped due to low priority
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p0.getPid(), list.get(0).getPid());
		assertEquals(p1.getPid(), list.get(1).getPid());
		assertEquals(p2.getPid(), list.get(2).getPid());
		assertEquals(p3.getPid(), list.get(3).getPid());		
	}

	@Test
	public void testAddByPriority_AddMediumPriority() {
		this.addAllProcesses();
		Exception expectedException = null;

		Process newProcess = new Process(Priority.MEDIUM);
		// max capacity is reached, add causes an exception
		try {
			underTest.add(newProcess, AddOption.PRIORITY);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);

		// the process list is the same before adding, the new process is skipped due to low priority
		List<Process> list = underTest.list(ListOption.TIME);
		assertEquals(p1.getPid(), list.get(0).getPid());
		assertEquals(p2.getPid(), list.get(1).getPid());
		assertEquals(p3.getPid(), list.get(2).getPid());
		assertEquals(newProcess.getPid(), list.get(3).getPid());
	}

	
	
	private void addAllProcesses() {

		// all processes can be added without exception
		Exception expectedException = null;
		try {
			underTest.add(p0);
			underTest.add(p1);
			underTest.add(p2);
			underTest.add(p3);
		} catch (MaxCapacityReachedException e) {
			expectedException = e;
		}
		assertNull(expectedException);
	}

}
