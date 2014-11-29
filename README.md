
# Eclipse Project Loader

Eclipse プロジェクトのリソースを、プロジェクトの再読み込みなしで随時読み込むための ClassLoader。  
Eclipse のデバッグコンフィギュレーションで、vm の実行時引数として `-Djava.system.class.loader=net.chocolapod.eclipseprojectloader.EclipseProjectClassLoader` を指定することで利用することができる。  
 
Eclipse は実行時のクラスパスに Maven プロジェクトのレイアウトでいうところの `target/classes` を設定する。  
しかし、`src/main/resources` などのリソースディレクトリは Eclipse プロジェクトの再読み込み時などにしか `target/classes` に反映されないため、Eclipse 外でリソースファイルを編集した場合、毎回 Eclipse プロジェクトの再読み込みをする必要がある。  
EclipseProjectClassLoader は、発見した Java クラスファイル以外のリソースが `target/classes` 下にあった場合、対応する `src/main/resources` 下のリソースファイルとして扱う。  
これにより、Eclipse プロジェクトの再読み込みなしで、随時 `src/main/resources` 下のリソースファイルにアクセスできるようになる。 

## 使い方

Maven プロジェクトでの利用方法は以下の通り。

    $ git clone -b ver0.1 https://github.com/momokan/eclipse-project-loader.git
    $ cd eclipse-project-loader
    $ mvn clean install

Eclipse Project Loader を利用する側の Maven プロジェクトで、以下の依存性を追加する。

    <project>
    ...
      <dependencies>
    ...
        <dependency>
          <groupId>net.chocolapod</groupId>
          <artifactId>eclipseprojectloader</artifactId>
          <version>0.1</version>
        </dependency>
    ...
      <dependencies>
    ...
    <project>

その後 `mvn clean eclipse:eclipse` → Eclipse 上でプロジェクトの更新/再読み込みをする。
あとは、Eclipse の実行/デバッグコンフィギュレーションの VM 引数として `-Djava.system.class.loader=net.chocolapod.eclipseprojectloader.EclipseProjectClassLoader` を追加して実行する。

## Eclipse プロジェクトのレイアウトを変更する

EclipseProjectClassLoader が書き換えるリソースのパスは、`.settings/eclipseProjectConfig.properties` ファイルにて、以下のプロパティとして指定できる。  
例えば Maven プロジェクトのレイアウトとして指定する場合、以下の設定となる。  

    # コンパイルしたクラスファイルなど、実行時に classpath に指定されるディレクトリ
    target.dir=target/classes/
    # リソースが配置されている本来のディレクトリ
    resources.dir=src/main/resources/

## 参考

  [http://chocolapod.sakura.ne.jp/blog/entry/83](開発者ブログ)

 
