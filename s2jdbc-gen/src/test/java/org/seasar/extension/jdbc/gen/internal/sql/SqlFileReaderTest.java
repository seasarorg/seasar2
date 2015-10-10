/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.dialect.MssqlGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class SqlFileReaderTest {

    private SqlFileTokenizer tokenizer;

    private MssqlGenDialect dialect;

    /**
     * 
     */
    @Before
    public void setUp() {
        dialect = new MssqlGenDialect();
        tokenizer = new SqlFileTokenizer(';', "go");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_delimiter() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("aaa;\n");
                buf.append("bbb\n");
                buf.append("go\n");
                buf.append("ccc\n");
                buf.append("ddd\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("aaa", reader.readSql());
        assertEquals("bbb", reader.readSql());
        assertEquals("ccc ddd", reader.readSql());
        assertNull(reader.readSql());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_delimiterInLine() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("aaa; bbb; ccc;\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("aaa", reader.readSql());
        assertEquals("bbb", reader.readSql());
        assertEquals("ccc", reader.readSql());
        assertNull(reader.readSql());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_sqlBlock() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("begin aaa; end\n");
                buf.append("go\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("begin aaa; end", reader.readSql());
        assertNull(reader.readSql());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_sqlBlock_createTrigger() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("create trigger hoge begin aaa; end\n");
                buf.append("go\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("create trigger hoge begin aaa; end", reader.readSql());
        assertNull(reader.readSql());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_notSqlBlock() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("start aaa; end\n");
                buf.append("go\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("start aaa", reader.readSql());
        assertEquals("end", reader.readSql());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_commentBlock() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("select 1 ; /* aaa\n");
                buf.append("aaa */ select 2;");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertEquals("select 1", reader.readSql());
        assertEquals("select 2", reader.readSql());
        assertNull(reader.readSql());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testReadSql_lineNumber() throws Exception {
        SqlFileReader reader = new SqlFileReader(new File("dummy"), "UTF-8",
                tokenizer, dialect) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("/*\n");
                buf.append(" *\n");
                buf.append(" */\n");
                buf.append("select 1\n");
                buf.append("from \n");
                buf.append("hoge\n");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };
        assertNotNull(reader.readSql());
        assertEquals(4, reader.getLineNumber());
    }

}
