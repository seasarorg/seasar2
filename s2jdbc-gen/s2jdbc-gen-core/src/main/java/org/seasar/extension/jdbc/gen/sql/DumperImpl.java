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
import java.util.List;

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
 * @author taedium
 * 
 */
public class DumperImpl implements Dumper {

    protected File dumpDir;

    protected String dumpFileEncoding;

    protected String dumpTemplateFileName;

    protected Generator generator;

    protected GenDialect dialect;

    protected List<TableDesc> tableDescList;

    protected DumpModelFactory dumpModelFactory;

    protected String extension = ".csv";

    protected char delimiter = ',';

    public DumperImpl(File dumpDir, String dumpFileEncoding,
            String dumpTemplateFileName, Generator generator,
            GenDialect dialect, List<TableDesc> tableDescList) {
        if (dumpDir == null) {
            throw new NullPointerException("dumpDir");
        }
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
        if (tableDescList == null) {
            throw new NullPointerException("tableDescList");
        }
        this.dumpDir = dumpDir;
        this.dumpFileEncoding = dumpFileEncoding;
        this.dumpTemplateFileName = dumpTemplateFileName;
        this.generator = generator;
        this.dialect = dialect;
        this.tableDescList = tableDescList;
        dumpModelFactory = createDumpModelFactory();
    }

    public void dump(SqlExecutionContext sqlExecutionContext) {
        for (TableDesc tableDesc : tableDescList) {
            DumpModel dumpModel = dumpModelFactory.getDumpModel(tableDesc,
                    sqlExecutionContext);
            GenerationContext genContext = createGenerationContext(dumpModel,
                    dumpDir);
            generator.generate(genContext);
        }
    }

    protected GenerationContext createGenerationContext(DumpModel model,
            File dumpDir) {
        String fileName = model.getName() + extension;
        return new GenerationContextImpl(model, dumpDir, new File(dumpDir,
                fileName), dumpTemplateFileName, dumpFileEncoding, true);
    }

    protected DumpModelFactory createDumpModelFactory() {
        return new DumpModelFactoryImpl(dialect, delimiter);
    }

}
