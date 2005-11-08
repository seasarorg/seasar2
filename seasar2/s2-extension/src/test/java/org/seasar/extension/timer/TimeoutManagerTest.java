package org.seasar.extension.timer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.timer.TimeoutManager;
import org.seasar.extension.timer.TimeoutTarget;
import org.seasar.extension.timer.TimeoutTask;

public class TimeoutManagerTest extends TestCase {

	private int _expiredCount;

	public TimeoutManagerTest(String name) {
		super(name);
	}

	public void testExpired() throws Exception {
		TimeoutTask task =
			TimeoutManager.getInstance().addTimeoutTarget(new TimeoutTarget() {
			public void expired() {
				System.out.println("expired");
				_expiredCount++;
			}
		}, 1, true);

		Thread.sleep(7000);
		assertTrue("1", _expiredCount > 1);
		assertEquals(
			"2",
			1,
			TimeoutManager.getInstance().getTimeoutTaskCount());
		TimeoutManager.getInstance().stop();
		int count = _expiredCount;
		task.stop();
		TimeoutManager.getInstance().start();
		Thread.sleep(3000);
		assertEquals("3", count, _expiredCount);
		assertEquals(
			"4",
			1,
			TimeoutManager.getInstance().getTimeoutTaskCount());
		task.cancel();
		Thread.sleep(3000);
		assertEquals(
			"5",
			0,
			TimeoutManager.getInstance().getTimeoutTaskCount());
	}

	protected void setUp() throws Exception {
		_expiredCount = 0;
		TimeoutManager.getInstance().clear();
	}

	protected void tearDown() throws Exception {
		TimeoutManager.getInstance().clear();
	}

	public static Test suite() {
		return new TestSuite(TimeoutManagerTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { TimeoutManagerTest.class.getName()});
	}
}
