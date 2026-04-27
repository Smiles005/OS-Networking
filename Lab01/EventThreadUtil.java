//javadoc EventThreadUtil.java
/**
 EventThreadUtil is a base class that allows an application to compare java.awt.EventDispatchThread to Thread.currentThread and decide to run runnable now, later, or to wait.
 <p>
 EventThreadUtil loads the class java.awt.EventDispatchThread and returns the class object EVENT_DISPATCH_TREAD_CLASS.
 EVENT_DISPATCH_THREAD_CLASS is then used in subsequent methods in comparison with Thread.currentThread.
 <p>
 @author Nicholas tenBroek (code)
 @author Isabel Kliethermes (documentation)
 @version %I%, %G%
 @since 1.0
 **/
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

public class EventThreadUtil {
	protected static Class EVENT_DISPATCH_THREAD_CLASS;

	static {
		try {
			EVENT_DISPATCH_THREAD_CLASS = Class.forName("java.awt.EventDispatchThread");
			// This checks to see if is the EVENT_DISPATCH_THREAD_CLASS is the currentThread
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	/**
	 Tests whether the event dispatch thread is an instance of the current thread.
	 @return true if the event dispatch thread is an instance of the current thread, otherwise, false.
	 **/
	public static final boolean isCurrentThreadEventThread() {
		return EVENT_DISPATCH_THREAD_CLASS.isInstance(Thread.currentThread());
	}

	/**
	 Runs runnable if the event dispatch thread is an instance of the current thread, otherwise, waits.
	 @param runnable, an instance of the interface Runnable
	 @exception InterruptedException: Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 @exception InvocationTargetException: Checked exception that wraps an exception thrown by an invoked method or constructor.
	 **/
	public static final void runNowInEventThread(Runnable runnable) {
		if(runnable == null) {
			return;
		}
		if(isCurrentThreadEventThread()) {
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch(InterruptedException ie) {
			} catch(java.lang.reflect.InvocationTargetException ite) {}
		}
	}

	/**
	Runs runnable if the event dispatch thread is an instance of the current thread, otherwise, runs later.
	@param runnable, an instance of the interface Runnable
	**/
	public static final void runLaterInEventThread(Runnable runnable) {
		if(runnable == null) {
			return;
		}
		if(isCurrentThreadEventThread()) {
			runnable.run();
		} else {
			SwingUtilities.invokeLater(runnable);
		}
	}
	// public static final void main(String[] args) {
	// 	System.out.println("EventThreadUtil test");
	// 	System.out.println("Is event thread: " + isCurrentThreadEventThread());
	// 	runLaterInEventThread(new Runnable() {
	// 		public void run() {
	// 			System.out.println("In later runnable");
	// 			System.out.println("Is event thread: " + isCurrentThreadEventThread());
	// 		}
	// 	});
	// 	runNowInEventThread(new Runnable() {
	// 		public void run() {
	// 			System.out.println("In now runnable");
	// 			System.out.println("Is event thread: " + isCurrentThreadEventThread());
	// 		}
	// 	});
	// }
}

