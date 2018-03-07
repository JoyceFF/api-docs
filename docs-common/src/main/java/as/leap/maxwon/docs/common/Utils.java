package as.leap.maxwon.docs.common;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Utils {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static ApiConfig apiConfig = new ApiConfig();

    public static Scheduler scheduler;
    static {
        scheduler = Schedulers.from(new ThreadPoolExecutor(200, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(5000), (r, executor) -> {
            if (!executor.isShutdown()) {
                logger.error(" the ama scheduler is error");
            }
        }));
    }




    public static List<MethodDeclaration> getMethods(File file) {
        try {
            List<MethodDeclaration> methods = new ArrayList<>();
            CompilationUnit cu = JavaParser.parse(file);
            for (TypeDeclaration<?> type : cu.getTypes()) {
                NodeList<BodyDeclaration<?>> members = type.getMembers();
                for (BodyDeclaration<?> member : members) {
                    if (member instanceof MethodDeclaration) {
                        MethodDeclaration method = (MethodDeclaration) member;
                        methods.add(method);
                    }
                }
            }
            return methods;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<File> findResources() {
        try {
            List<File> resources = findFiles(apiConfig.getProjectPath());

            return resources.stream()
                    .filter(resource -> resource.getName().indexOf("Resource") > 0)
                    .filter(resource -> resource.getName().indexOf(".class") < 0)
                    .collect(Collectors.toList());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<File> findFiles(String path) {
        try {
            List<File> resources = new ArrayList<>();

            File dirFile = new File(path);

            if (!dirFile.exists()) {
                throw new RuntimeException("do not exit");
            }

            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    List<File> fileList = findFiles(file.getCanonicalPath());
                    for (File file1 : fileList) {
                        resources.add(file1);
                    }
                } else if (file.isFile()) {
                    resources.add(file);
                }
            }

            return resources.stream()
                    .filter(resource -> resource.getName().indexOf(".java") > 0)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Optional<File> findFileByName(String name){
        List<File> files = findFiles(apiConfig.getProjectPath());
        return files.stream().filter(file -> file.getName().replaceAll(".java","").equals(name)).findFirst();
    }

    /**
     * 得到格式化json数据  退格用\t 换行用\r
     */
    public static String formatJsonView(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int i=0;i<jsonStr.length();i++){
            char c = jsonStr.charAt(i);
            if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c+"\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c+"\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }

    private static String getLevelStr(int level){
        StringBuffer levelStr = new StringBuffer();
        for(int levelI = 0;levelI<level ; levelI++){
            levelStr.append("   ");
        }
        return levelStr.toString();
    }
}
