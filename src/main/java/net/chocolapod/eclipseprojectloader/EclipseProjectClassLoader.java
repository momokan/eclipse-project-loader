package net.chocolapod.eclipseprojectloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *	Eclipse プロジェクトのリソースを、プロジェクトの再読み込みなしで随時読み込むための ClassLoader。
 *	Eclipse のデバッグコンフィギュレーションで、vm の実行時引数として "-Djava.system.class.loader=net.chocolapod.eclipseprojectloader.EclipseProjectClassLoader" を指定することで利用することができる。
 *
 *	Eclipse は実行時のクラスパスに Maven プロジェクトのレイアウトでいうところの target/classes を設定する。
 *	しかし、src/main/resources などのリソースディレクトリは Eclipse プロジェクトの再読み込み時などにしか target/classes に反映されないため、
 *	Eclipse 外でリソースファイルを編集した場合、毎回 Eclipse プロジェクトの再読み込みをする必要がある。
 *	EclipseProjectClassLoader　は、発見した Java クラスファイル以外のリソースが target/classes 下にあった場合、対応する src/main/resources 下のリソースファイルとして扱う。
 *	これにより、Eclipse プロジェクトの再読み込みなしで、随時 src/main/resources 下のリソースファイルにアクセスできるようになる。
 */
public class EclipseProjectClassLoader extends URLClassLoader {
	private static final String		CLASS_FILE_EXTENSION = ".class";
	private static final String		RESOURCES_DIR = "src/main/resources/";
	private static final String		TARGET_DIR = "target/classes/";
	private static final String		SYSTEM_PROPERTY_KEY_CLASSPATH = "java.class.path";

	public EclipseProjectClassLoader() {
		super(getClasspathUrls());
	}
	
	public EclipseProjectClassLoader(ClassLoader classLoader) {
		super(getClasspathUrls(), classLoader);
	}
	
	@Override
    public URL getResource(String name) {
		//	通常の方法でリソースを探す
		URL	url = super.getResource(name);

		if (url != null) {
			try {
				String		urlString = url.toString();
				String		reousrcesUrl = new File(RESOURCES_DIR).toURI().toURL().toString();
				String		targetUrl = new File(TARGET_DIR).toURI().toURL().toString();
				
				//	ターゲットディレクトリ下のクラスファイル以外のリソースは、かわりにリソースディレクトリ下から読み込む
				if (urlString.startsWith(targetUrl) && (!urlString.endsWith(CLASS_FILE_EXTENSION))) {
					String	path = urlString.substring(targetUrl.length());
					
					path = reousrcesUrl + path;
					
					url = new URI(path).toURL();
					System.err.println(urlString);
				}
			} catch (MalformedURLException | URISyntaxException e) {
				//	リソースディレクトリ内にリソースがなければ、存在しないものとして扱う
				url = null;
//				throw new RuntimeException(e);
			}

		}

		return url;
    }

	/**
	 *	システムに設定されている Classpath を URL 配列に変換して返す
	 */
	public static URL[] getClasspathUrls() {
		List<URL>	list = new ArrayList<>();

		for (String path: System.getProperty(SYSTEM_PROPERTY_KEY_CLASSPATH).split(File.pathSeparator)) {
			try {
				URL	url = new File(path).toURI().toURL();

				list.add(url);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return list.toArray(new URL[] {});
	}
	
}