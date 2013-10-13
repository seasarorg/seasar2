<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
<#if packageName??>
package ${packageName};
</#if>

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.annotation.Generated;

<#if useS2junit4>
import org.junit.runner.RunWith;
</#if>
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.ConnectionUtil;
<#if !useS2junit4>
import org.seasar.extension.unit.S2TestCase;
</#if>
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
<#if useS2junit4>
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;
</#if>
import org.seasar.framework.util.InputStreamReaderUtil;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ReaderUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * SQLファイルのテストクラスです。
 * <p>
 * このファイルは修正されることを意図していません。
 * SQLファイルのテストを独自に行いたい場合は、サービスやエンティティのテストクラスを使用してください。
 * </p>
 *
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
<#if useS2junit4>
@RunWith(Seasar2.class)
</#if>
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
public class ${shortClassName} <#if !useS2junit4>extends S2TestCase </#if>{
<#if useS2junit4>

    private TestContext testContext;
</#if>

    private JdbcManager ${jdbcManagerName};

    /**
     * 事前処理をします。
     * 
     * @throws Exception
     */
<#if useS2junit4>
    public void before() throws Exception {
        testContext.setAutoIncluding(false);
        testContext.include("${configPath}");
    }
<#else>
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("${configPath}");
    }
</#if>
<#if sqlFilePathList?size == 0>

    /**
     * SQLファイルがひとつもありません。
     * ダミーのテストメソッドです。
     * 
     * @throws Exception
     */
    public void test() throws Exception {
    }
<#else>
  <#list sqlFilePathList as path>

    /**
     * SQLファイルをテストします。
     * 
     * @throws Exception
     */
    public void testSqlFile${path_index}<#if !useS2junit4>Tx</#if>() throws Exception {
        String path = "${path}";
        new SqlFile(path).execute();
    }
  </#list>
</#if>

    /**
     * SQLファイルを表すクラスです。
     * 
     * @author S2JDBC-Gen
     */
    public class SqlFile {

        /** SQL */
        protected String sql;

        /** 内部的なJDBCマネジャ */
        protected JdbcManagerImplementor implementor;

        /**
         * インスタンスを構築します。
         * 
         * @param path
         *            SQLファイルのパス
         */
        public SqlFile(String path) {
            implementor = JdbcManagerImplementor.class.cast(${jdbcManagerName});
            this.sql = getSql(path);
        }

        /**
         * SQLを返します。
         * 
         * @param path
         *            SQLファイルのパス
         * @return SQL
         */
        protected String getSql(String path) {
            if (path.endsWith(".sql")) {
                path = path.substring(0, path.length() - 4);
            }
            String dbmsName = implementor.getDialect().getName();
            if (dbmsName != null) {
                String sql = readSql(path + "_" + dbmsName);
                if (sql != null) {
                    return sql;
                }
            }
            String sql = readSql(path);
            if (sql != null) {
                return sql;
            }
            throw new ResourceNotFoundRuntimeException(path);
        }

        /**
         * SQLをファイルから読み取ります。
         * 
         * @param path
         *            SQLファイルのパス
         * @return SQL
         */
        protected String readSql(String path) {
            InputStream is = ResourceUtil.getResourceAsStreamNoException(path,
                    "sql");
            if (is == null) {
                return null;
            }
            Reader reader = InputStreamReaderUtil.create(is, "UTF-8");
            String sql = ReaderUtil.readText(reader);
            if (sql.length() > 0 && sql.charAt(0) == '\uFEFF') {
                sql = sql.substring(1);
            }
            sql = sql.trim();
            if (sql.endsWith(";")) {
                sql = sql.substring(0, sql.length() - 1);
            }
            return sql;
        }

        /**
         * SQLを実行します。
         */
        public void execute() {
            System.out.println(sql);
            Connection connection = org.seasar.extension.jdbc.util.DataSourceUtil
                    .getConnection(implementor.getDataSource());
            try {
                PreparedStatement ps = ConnectionUtil.prepareStatement(
                        connection, sql);
                try {
                    PreparedStatementUtil.execute(ps);
                } finally {
                    StatementUtil.close(ps);
                }
            } finally {
                ConnectionUtil.close(connection);
            }
        }
    }
}