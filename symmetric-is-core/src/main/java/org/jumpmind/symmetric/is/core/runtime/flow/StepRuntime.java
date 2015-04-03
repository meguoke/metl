package org.jumpmind.symmetric.is.core.runtime.flow;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jumpmind.symmetric.is.core.runtime.IExecutionTracker;
import org.jumpmind.symmetric.is.core.runtime.LogLevel;
import org.jumpmind.symmetric.is.core.runtime.Message;
import org.jumpmind.symmetric.is.core.runtime.ShutdownMessage;
import org.jumpmind.symmetric.is.core.runtime.component.IComponent;
import org.jumpmind.symmetric.is.core.runtime.resource.IResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepRuntime implements Runnable {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    protected BlockingQueue<Message> inQueue;

    boolean running = true;

    // TODO make this a setting for component
    int capacity = 10000;

    Exception error;

    IComponent component;

    List<StepRuntime> targetStepRuntimes;

    List<StepRuntime> sourceStepRuntimes;
    
    IExecutionTracker executionTracker;
    
    String executionId;
    
    public StepRuntime(String executionId, IComponent component, IExecutionTracker tracker) {
        this.executionId = executionId;
        this.executionTracker = tracker;
        this.component = component;
        inQueue = new LinkedBlockingQueue<Message>(capacity);
    }

    public boolean isStartStep() {
        return sourceStepRuntimes == null || sourceStepRuntimes.size() == 0;
    }

    public void setTargetStepRuntimes(List<StepRuntime> targetStepRuntimes) {
        this.targetStepRuntimes = targetStepRuntimes;
    }

    public void setSourceStepRuntimes(List<StepRuntime> sourceStepRuntimes) {
        this.sourceStepRuntimes = sourceStepRuntimes;
    }

    protected void put(Message message) throws InterruptedException {
        inQueue.put(message);
    }

    public void start(IExecutionTracker tracker, IResourceFactory resourceFactory) {
        component.start(tracker, resourceFactory);
    }

    @Override
    public void run() {
        try {

            MessageTarget target = new MessageTarget();
            /*
             * if we are a start step (don't have any input links), we'll only
             * get a single message which is the start message sent by the flow
             * runtime to kick things off. If we have input links, we must loop
             * until we get a shutdown message from one of our sources
             */
            while (running) {
                Message inputMessage = inQueue.take();
                if (inputMessage instanceof ShutdownMessage) {
                    String fromStepId = inputMessage.getHeader().getOriginatingStepId();
                    removeSourceStepRuntime(fromStepId);
                    /*
                     * When all of the source step runtimes have been removed or
                     * when the shutdown message comes from myself, then go
                     * ahead and shutdown
                     */
                    if (fromStepId == null || sourceStepRuntimes == null
                            || sourceStepRuntimes.size() == 0
                            || fromStepId.equals(component.getFlowStep().getId())) {
                        shutdown();
                    }
                } else {
                    try {
                    executionTracker.beforeHandle(executionId, component);
                    component.handle(executionId, inputMessage, target);
                    } catch (Exception ex) {
                        error = ex;
                        String msg = ex.getMessage();
                        if (isBlank(msg)) {
                            msg = ExceptionUtils.getFullStackTrace(ex);
                        }
                        executionTracker.log(executionId, LogLevel.ERROR, component, msg);
                        log.error("", ex);
                    }
                    executionTracker.afterHandle(executionId, component, error);
                    if (isStartStep()) {
                        shutdown();
                    }
                }
            }
        } catch (Exception ex) {
            // TODO: notify the flow runtime that we have an error and let it
            // gracefully shut things down
            log.error("", ex);
            error = ex;
        }
    }

    private void removeSourceStepRuntime(String stepId) {
        if (sourceStepRuntimes != null) {
            Iterator<StepRuntime> it = sourceStepRuntimes.iterator();
            while (it.hasNext()) {
                StepRuntime sourceRuntime = (StepRuntime) it.next();
                if (sourceRuntime.getComponent().getFlowStep().getId().equals(stepId)) {
                    it.remove();
                }
            }
        }
    }

    private void shutdown() throws InterruptedException {
        for (StepRuntime targetStepRuntime : targetStepRuntimes) {
            targetStepRuntime.put(new ShutdownMessage(component.getFlowStep().getId()));
        }
        this.component.stop();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() throws InterruptedException {
        this.inQueue.clear();
        this.inQueue.put(new ShutdownMessage(component.getFlowStep().getId()));
    }

    public IComponent getComponent() {
        return this.component;
    }

    class MessageTarget implements IMessageTarget {
        @Override
        public void put(Message message) {
            for (StepRuntime targetRuntime : targetStepRuntimes) {
                try {
                    targetRuntime.put(message);
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}