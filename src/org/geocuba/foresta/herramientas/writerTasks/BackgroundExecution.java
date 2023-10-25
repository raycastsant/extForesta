package org.geocuba.foresta.herramientas.writerTasks;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.Timer;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.utiles.swing.threads.IMonitorableTask;
import com.iver.utiles.swing.threads.IProgressMonitorIF;
import com.iver.utiles.swing.threads.TaskMonitorTimerListener;
 

public class BackgroundExecution {

	public static void cancelableBackgroundExecution(final IMonitorableTask task) {
		final com.iver.utiles.swing.threads.SwingWorker worker = new com.iver.utiles.swing.threads.SwingWorker() {
			public Object construct() {
				try {
					task.run();
					return task;
				} catch (Exception e) {
					NotificationManager.addError(null, e);
				}
				return null;
			}
		    /**
		     * Called on the event dispatching thread (not on the worker thread)
		     * after the <code>construct</code> method has returned.
		     */
			public void finished() {
				task.finished();
			}
		};

		Component mainFrame = (Component) PluginServices.getMainFrame();

		IProgressMonitorIF progressMonitor = null;
		String title = "Procesando...";
		progressMonitor = new BackgroundProgressBar((Frame) mainFrame, title);
		progressMonitor.setIndeterminated(!task.isDefined());
		progressMonitor.setInitialStep(task.getInitialStep());
		progressMonitor.setLastStep(task.getFinishStep());
		progressMonitor.setCurrentStep(task.getCurrentStep());
		progressMonitor.setMainTitleLabel(task.getStatusMessage());
		progressMonitor.setNote(task.getNote());
		progressMonitor.open();
		int delay = 500;
		TaskMonitorTimerListener timerListener = new TaskMonitorTimerListener(
				progressMonitor, task);
		Timer timer = new Timer(delay, timerListener);
		timerListener.setTimer(timer);
		timer.start();

		worker.start();

	}

}
