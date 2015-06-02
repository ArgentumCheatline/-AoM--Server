/**
 * This file is part of -AoM--Server, licensed under the APACHE License.
 *
 * Copyright (c) 2015 AoW Powered <https://github.com/AO-Modding>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.aom.core.scheduler;

import java.util.function.Consumer;

/**
 * Encapsulate an executor of {@link Task}s.
 */
public interface TaskExecutor {
    /**
     * Invokes a task to be executed.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     * @param delay    The delay in ticks to execute the task.
     * @param period   The period in ticks to repeat the task.
     * @param isAsync  True if the task runs parallel, false otherwise.
     */
    public Task invoke(Object owner, Consumer<Task> consumer, TaskPriority priority, long delay, long period, boolean isAsync);

    /**
     * Invokes a synchronised task with {@link TaskPriority#NORMAL} priority.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     */
    default public Task invokeTask(Object owner, Consumer<Task> consumer) {
        return invoke(owner, consumer, TaskPriority.NORMAL, 0, -1, false);
    }

    /**
     * Invokes a synchronised task with the given priority.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     */
    default public Task invokeTask(Object owner, Consumer<Task> consumer, TaskPriority priority) {
        return invoke(owner, consumer, priority, 0, -1, false);
    }

    /**
     * Invokes a synchronised task to be executed with the given priority and executed after the delay period.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     * @param delay    The delay in ticks to execute the task.
     */
    default public Task invokeDelayedTask(Object owner, Consumer<Task> consumer, TaskPriority priority, long delay) {
        return invoke(owner, consumer, priority, delay, -1, false);
    }

    /**
     * Invokes a repeating synchronised task with the given priority and executed after the delay period.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     * @param delay    The delay in ticks to execute the task.
     * @param period   The period in ticks to repeat the task.
     */
    default public Task invokeRepeatingTask(Object owner, Consumer<Task> consumer, TaskPriority priority, long delay, long period) {
        return invoke(owner, consumer, priority, delay, period, false);
    }

    /**
     * Invokes an asynchronous task with {@link TaskPriority#NORMAL} priority.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     */
    default public Task invokeAsyncTask(Object owner, Consumer<Task> consumer) {
        return invoke(owner, consumer, TaskPriority.NORMAL, 0, -1, true);
    }

    /**
     * Invokes an asynchronous task with the given priority.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     */
    default public Task invokeAsyncTask(Object owner, Consumer<Task> consumer, TaskPriority priority) {
        return invoke(owner, consumer, priority, 0, -1, true);
    }

    /**
     * Invokes an asynchronous task with the given priority and executed after the delay period.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     * @param delay    The delay in ticks to execute the task.
     */
    default public Task invokeDelayedAsyncTask(Object owner, Consumer<Task> consumer, TaskPriority priority, long delay) {
        return invoke(owner, consumer, priority, delay, -1, true);
    }

    /**
     * Invokes a repeating asynchronous task with the given priority and executed after the delay period.
     *
     * @param owner    The owner of the task to be executed.
     * @param consumer The executor method of the task.
     * @param priority The priority of the task.
     * @param delay    The delay in ticks to execute the task.
     * @param period   The period in ticks to repeat the task.
     */
    default public Task invokeRepeatingAsyncTask(Object owner, Consumer<Task> consumer, TaskPriority priority, long delay, long period) {
        return invoke(owner, consumer, priority, delay, period, true);
    }

    /**
     * Cancel the given {@link Task}.
     *
     * @param task The task to be cancelled.
     */
    public void cancelTask(Task task);

    /**
     * Cancel all tasks that belong to the given owner.
     *
     * @param owner The owner of the tasks.
     */
    public void cancelAllTasks(Object owner);

    /**
     * Cancel all tasks.
     */
    public void cancelAllTasks();
}
