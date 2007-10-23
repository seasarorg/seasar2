■ビルド方法
このプロジェクトをビルドするには、SQL Server 2005 と Oralce のJDBCドライバをビルドパスに加えてください。

SQL Server 2005 のJDBCドライバは以下のURLからダウンロードできます。
ダウンロードしたファイルに含まれる sqljdbc.jar を使用してください。
http://www.microsoft.com/downloads/details.aspx?familyid=c47053eb-3b64-4794-950d-81e1ec91c1ba&displaylang=en

Oracle のJDBCドライバは以下のURLからダウンロードできます。
ojdbc5.jar を使用してください。
http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/htdocs/jdbc_111060.html

■特定のRDBMSを使用したテスト方法
このプロジェクトのテストケースは複数のRDBMSに対応しています。
特定のRDBMSを使用してテストを実行するには次の手順に従ってください。

1.xadatasource_xxx.diconを用意する。（xxxには任意の名称を指定してください）
  xadatasource_xxx.diconには次の2つのコンポーネントを指定してください。（xadatasource_hsqldb.diconが参考になります）
  a) org.seasar.extension.dbcp.impl.XADataSourceImpl:プロパティにはJDBCの接続文字列を指定してください。
  b) org.seasar.extension.jdbc.DbmsDialectの実装クラス:使用するRDBMSに対応した実装クラスを指定してください。

2.env_ut.txtをxadatasource_xxx.diconの「xxx」に指定した値で置き換える。

3．JUnitを実行する。