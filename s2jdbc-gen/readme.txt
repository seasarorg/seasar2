Eclipse を利用する場合の注意．

s2jdbc-gen プロジェクトは Javadoc コメントを処理するためクラスパスに tools.jar が必要です．
以下の手順で JRE_CONTAINER に tools.jar を追加してください．

・[Window]-[Preferences] で Preferences を開く．
・[Java]-[Installed JRE] で使用するJDKを選択し，[Edit]ボタンを押す．
・[Add External JARs] ボタンを押してファイル選択ダイアログで JAVA_HOME/lib/tools.jar を選択する．
