package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {

            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                String urlPath = exchange.getRequestURI().toString();
                System.out.println("->" + urlPath + "<-");

                if (urlPath == null || urlPath.equals("/") || urlPath.isEmpty()) {
                    urlPath = "/index.html";
                }

                String filePath = "web" + urlPath;
                File actualFile = new File(filePath);

                if (!actualFile.exists()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(new File("web/HTML/404.html").toPath(), respBody);

                    respBody.close();
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(actualFile.toPath(), respBody);

                    respBody.close();
                }


                success = true;
            }
            if (!success) {

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();

            e.printStackTrace();
        }
    }
}
