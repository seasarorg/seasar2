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

import static org.codehaus.plexus.util.StringUtils.join;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.plexus.util.FileUtils;

/**
 * @author hakoda-te-kun
 */
public class FileSet {
	// ※注意
	// Mojoに空リストをパラメータとして渡すことはできない。
	// <values></values>
	// のように渡すと、フィールド変数には空リストではなくnullが代入される。

	/**
	 * 対象となるリソースの存在するディレクトリを設定します。
	 * 
	 * @parameter
	 * @required
	 */
	private File directory;

	/**
	 * 設定したディレクトリに存在するファイルの中で、対象となるファイルのパターンを指定します。
	 * 
	 * @parameter
	 */
	private Set<String> includes;

	/**
	 * {@code includes}に指定したファイルの中で、対象から除外するファイルのパターンを指定します。
	 * 
	 * @parameter
	 */
	private Set<String> excludes;

	@SuppressWarnings("unchecked")
	public Set<File> getFiles() throws IOException {
		if (includes == null)
			includes = new HashSet<String>();
		if (excludes == null)
			excludes = new HashSet<String>();
		return new HashSet<File>(FileUtils.getFiles(directory,
				join(includes.iterator(), ","), join(excludes.iterator(), ",")));
	}
}
