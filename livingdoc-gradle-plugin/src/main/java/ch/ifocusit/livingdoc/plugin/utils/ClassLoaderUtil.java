/*
 * Living Documentation
 *
 * Copyright (C) 2020 Focus IT
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ch.ifocusit.livingdoc.plugin.utils;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency;
import org.gradle.api.plugins.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julien Boz
 */
public class ClassLoaderUtil {

    public static ClassLoader getRuntimeClassLoader(Project project) {
        try {
            project.getConfigurations().getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME);
            List<String> runtimeClasspathElements = project.getConfigurations().getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)
                    .getAllDependencies()
                    .withType(DefaultProjectDependency.class)
                    .stream()
                    .map(DefaultProjectDependency::getName)
                    .collect(Collectors.toList());

            // List<String> runtimeClasspathElements = project.getDependencies().
            // List<String> compileClasspathElements = project.getCompileClasspathElements();
            // URL[] runtimeUrls = new URL[runtimeClasspathElements.size() + compileClasspathElements.size()];
            URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
            for (int i = 0; i < runtimeClasspathElements.size(); i++) {
                String element = runtimeClasspathElements.get(i);
                runtimeUrls[i] = new File(element).toURI().toURL();
            }

//            int j = runtimeClasspathElements.size();
//
//            for (int i = 0; i < compileClasspathElements.size(); i++) {
//                String element = compileClasspathElements.get(i);
//                runtimeUrls[i + j] = new File(element).toURI().toURL();
//            }

            return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());

        } catch (Exception e) {
            throw new GradleException("Unable to load project runtime !", e);
        }
    }
}
