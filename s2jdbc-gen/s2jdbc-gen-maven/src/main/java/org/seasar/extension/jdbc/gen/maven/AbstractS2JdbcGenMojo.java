/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.jdbc.gen.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.command.CommandInvoker;
import org.seasar.extension.jdbc.gen.internal.command.CommandInvokerImpl;
import org.seasar.extension.jdbc.gen.internal.util.ReflectUtil;

public abstract class AbstractS2JdbcGenMojo extends AbstractMojo {

	/**
	 * コマンドを呼び出すクラスの名前を指定します。
	 * 
	 * @parameter
	 */
	private String commandInvokerClassName;
	
	/**
	 * dicon（や、そのほかのリソースファイル）が含まれているディレクトリを指定します。
	 * 
	 * @parameter
	 */
	private String diconDir;

	protected abstract Command getCommand();

	protected abstract void doExecute();

	public AbstractS2JdbcGenMojo() {
		super();
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		final URL[] urls;
		if (diconDir != null) {
			final File dicons = new File(diconDir);
			getLog().info("dicon directory: " + dicons.getAbsolutePath());
			try {
				urls = new URL[] { dicons.toURI().toURL() };
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			urls = new URL[] {};
		}

		if (commandInvokerClassName == null)
			commandInvokerClassName = CommandInvokerImpl.class.getName();

		// クラスパスに含まれていないdiconファイルを読み取るために、新しいクラスローダを追加して実行
		final ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		final URLClassLoader newLoader = new URLClassLoader(urls, oldLoader);
		try {
			Thread.currentThread().setContextClassLoader(newLoader);
			
			doExecute();
			
			final CommandInvoker invoker = ReflectUtil.newInstance(CommandInvoker.class,
					commandInvokerClassName);
			invoker.invoke(getCommand());
		} finally {
			Thread.currentThread().setContextClassLoader(oldLoader);
		}
	}
}
