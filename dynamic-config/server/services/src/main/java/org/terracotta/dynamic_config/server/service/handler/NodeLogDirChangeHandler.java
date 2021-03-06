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
package org.terracotta.dynamic_config.server.service.handler;

import org.terracotta.dynamic_config.api.model.Configuration;
import org.terracotta.dynamic_config.api.model.NodeContext;
import org.terracotta.dynamic_config.api.service.IParameterSubstitutor;
import org.terracotta.dynamic_config.server.api.ConfigChangeHandler;
import org.terracotta.dynamic_config.server.api.InvalidConfigChangeException;
import org.terracotta.dynamic_config.server.api.PathResolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NodeLogDirChangeHandler implements ConfigChangeHandler {
  private final IParameterSubstitutor parameterSubstitutor;
  private final PathResolver pathResolver;

  public NodeLogDirChangeHandler(IParameterSubstitutor parameterSubstitutor, PathResolver pathResolver) {
    this.parameterSubstitutor = parameterSubstitutor;
    this.pathResolver = pathResolver;
  }

  @Override
  public void validate(NodeContext nodeContext, Configuration change) throws InvalidConfigChangeException {
    String logPath = change.getValue();
    if (logPath == null) {
      throw new InvalidConfigChangeException("Operation not supported");//unset not supported
    }

    try {
      Path substituted = substitute(Paths.get(logPath));
      Files.createDirectories(substituted);
    } catch (Exception e) {
      throw new InvalidConfigChangeException(e.toString(), e);
    }
  }

  private Path substitute(Path path) {
    return parameterSubstitutor.substitute(pathResolver.resolve(path)).normalize();
  }
}
