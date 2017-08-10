/*
 * Copyright Terracotta, Inc.
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
package org.terracotta.client.message.tracker;

import org.terracotta.entity.StateDumpable;

import java.util.Map;

/**
 * Keeps track of an entity's objects with their ids and their corresponding responses.
 * The decision on whether or not to track an object is taken using the {@link TrackerPolicy} of the entity
 */
public interface Tracker extends StateDumpable {

  /**
   * Tracks the provided response associated with the given id.
   * The response is tracked if and only if the provided {@code object} is trackable,
   * which is determined using the tracker policy of the entity.
   *
   * @param id Incoming entity object ID
   * @param object Incoming entity object
   * @param response Outgoing entity response
   */
  void track(long id, Object object, Object response);

  /**
   * Returns the tracked response associated with the given ID, null otherwise.
   *
   * @param id Tracked entity ID
   * @return Tracked entity response
   */
  <R> R getTrackedValues(long id);

  /**
   * Clears id-response mappings for all ids less than the provided id.
   *
   * @param id Incoming entity ID
   */
  void reconcile(long id);

  /**
   * Get all id - response mappings.

   * @return all tracked responses
   */
  Map<Long, Object> getTrackedValues();

  /**
   * Bulk load a set of ids, response mappings.
   * To be used by a passive entity when the active syncs its message tracker data.
   *
   * @param trackedResponses a map of id, response mappings
   */
  void loadOnSync(Map<Long, Object> trackedResponses);
}
