/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.sql;

import java.io.File;

import org.seasar.extension.jdbc.gen.DatabaseDesc;
import org.seasar.extension.jdbc.gen.DumpModel;
import org.seasar.extension.jdbc.gen.DumpModelFactory;
import org.seasar.extension.jdbc.gen.Dumper;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.model.DumpModelFactoryImpl;

/**
 * {@link Dumper}の実装クラスです。
 * 
 * @author taedium
 */
public class DumperImpl implements Dumper {

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding;

    /** ダンプのテンプレートファイル名 */
    protected String dumpTemplateFileName;

    /** ジェネレータ */
    protected Generator generator;

    /** 方言 */
    protected GenDialect dialect;

    /** ダンプモデルのファクトリ */
    protected DumpModelFactory dumpModelFactory;

    /** 拡張子 */
    protected String extension = ".csv";

    /** 区切り文字 */
    protected char delimiter = ',';

    /**
     * インスタンスを構築します。
     * 
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     * @param dumpTemplateFileName
     *            ダンプのテンプレートファイル名
     * @param generator
     *            ジェネレータ
     * @param dialect
     *            方言
     */
    public DumperImpl(String dumpFileEncoding, String dumpTemplateFileName,
            Generator generator, GenDialect dialect) {
        if (dumpFileEncoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        if (dumpTemplateFileName == null) {
            throw new NullPointerException("dumpTemplateFileName");
        }
        if (generator == null) {
            throw new NullPointerException("generator");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dumpFileEncoding = dumpFileEncoding;
        this.dumpTemplateFileName = dumpTemplateFileName;
        this.generator = generator;
        this.dialect = dialect;
        dumpModelFactory = createDumpModelFactory();
    }

    public void dump(SqlExecutionContext sqlExecutionContext,
            DatabaseDesc databaseDesc, File dumpDir) {
        for (TableDesc tableDesc : databaseDesc.getTableDescList()) {
            DumpModel dumpModel = dumpModelFactory.getDumpModel(tableDesc,
                    sqlExecutionContext);
            GenerationContext genContext = createGenerationContext(dumpModel,
                    dumpDir);
            generator.generate(genContext);
        }
    }

    /**
     * {@link GenerationContext}を作成します。
     * 
     * @param model
     *            ダンプモデル
     * @param dumpDir
     *            ダンプ先のディレクトリ
     * @return {@link GenerationContext}
     */
    protected GenerationContext createGenerationContext(DumpModel model,
            File dumpDir) {
        String fileName = model.getName() + extension;
        return new GenerationContextImpl(model, dumpDir, new File(dumpDir,
                fileName), dumpTemplateFileName, dumpFileEncoding, true);
    }

    /**
     * {@link DumpModelFactory}の実装を作成します。
     * 
     * @return {@link DumpModelFactory}の実装
     */
    protected DumpModelFactory createDumpModelFactory() {
        return new DumpModelFactoryImpl(dialect, delimiter);
    }

}
