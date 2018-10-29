import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UrlService {

    public static void sendGetNotification(Task task) throws IOException {
        HttpURLConnection connection = null;
        String urlParameters =
                "message=" + URLEncoder.encode(task.getMessage(), "UTF-8") +
                        "id=" + URLEncoder.encode(task.getExternalId(), "UTF-8");

        try {
            URL url = new URL(task.getExtraParams());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println("Got response from server " + response.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}