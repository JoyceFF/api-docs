package as.leap.maxwon.docs.server;

import as.leap.maxwon.docs.common.Jsons;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.apache.commons.cli.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class Bootstrap {
    public static void main(String[] args) {

//        final Options options = new Options()
//                .addOption("p", "port", true, "listen port between 0 and 65535. (default is 3000)");
//        final CommandLineParser parser = new PosixParser();
//        final CommandLine commandLine;
//        try {
//            commandLine = parser.parse(options, args);
//            final HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp("pangolin", options);
//        } catch (ParseException e) {
//
//            throw Throwables.propagate(e);
//        }

        Vertx vertx = Vertx.vertx();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        try {
            String config = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("config.json"), Charset.forName("utf-8"));
            deploymentOptions.setConfig(JsonObject.mapFrom(Jsons.objectFromJSONStr(config, Map.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Verticle verticle = new DocsServer();
        vertx.deployVerticle(verticle,deploymentOptions);
    }
}
