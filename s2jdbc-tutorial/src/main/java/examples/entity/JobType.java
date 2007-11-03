/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package examples.entity;

/**
 * 仕事のタイプです。
 * 
 * @author higa
 * 
 */
public enum JobType {

	/**
	 * クラークです。
	 */
	CLERK {
		@Override
		public JobStrategy createStrategy() {
			return new Clerk();
		}
	},
	/**
	 * セールスマンです。
	 */
	SALESMAN {
		@Override
		public JobStrategy createStrategy() {
			return new Salesman();
		}
	},
	/**
	 * マネージャです。
	 */
	MANAGER {
		@Override
		public JobStrategy createStrategy() {
			return new Manager();
		}
	},
	/**
	 * アナリストです。
	 */
	ANALYST {
		@Override
		public JobStrategy createStrategy() {
			return new Analyst();
		}
	},
	/**
	 * プレシデントです。
	 */
	PRESIDENT {
		@Override
		public JobStrategy createStrategy() {
			return new President();
		}
	};

	/**
	 * 仕事に応じた振る舞いを定義するストラテジを作成します。
	 * 
	 * @return 仕事ストラテジ
	 */
	public abstract JobStrategy createStrategy();
}