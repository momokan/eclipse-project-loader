
# Eclipse Project Loader

Eclipse プロジェクトのリソースを、プロジェクトの再読み込みなしで随時読み込むための ClassLoader。  
Eclipse のデバッグコンフィギュレーションで、vm の実行時引数として `-Djava.system.class.loader=net.chocolapod.eclipseprojectloader.EclipseProjectClassLoader` を指定することで利用することができる。  
 
Eclipse は実行時のクラスパスに Maven プロジェクトのレイアウトでいうところの `target/classes` を設定する。  
しかし、`src/main/resources` などのリソースディレクトリは Eclipse プロジェクトの再読み込み時などにしか `target/classes` に反映されないため、Eclipse 外でリソースファイルを編集した場合、毎回 Eclipse プロジェクトの再読み込みをする必要がある。  
EclipseProjectClassLoader は、発見した Java クラスファイル以外のリソースが `target/classes` 下にあった場合、対応する `src/main/resources` 下のリソースファイルとして扱う。  
これにより、Eclipse プロジェクトの再読み込みなしで、随時 `src/main/resources` 下のリソースファイルにアクセスできるようになる。 

# Eclipse プロジェクトのレイアウトを変更する

EclipseProjectClassLoader が書き換えるリソースのパスは、`.settings/eclipseProjectConfig.properties` ファイルにて、以下のプロパティとして指定できる。  
例えば Maven プロジェクトのレイアウトとして指定する場合、以下の設定となる。  

    # コンパイルしたクラスファイルなど、実行時に classpath に指定されるディレクトリ
    target.dir=target/classes/
    # リソースが配置されている本来のディレクトリ
    resources.dir=src/main/resources/
 
