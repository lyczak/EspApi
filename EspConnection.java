import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.List;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EspConnection {
    private final String HOST;
    private final String BASE_URL;

    static final String COOKIES_HEADER = "Set-Cookie";

    private String cookies;

    public EspConnection(String host) {
        HOST = host;
        BASE_URL = "https://" + HOST;
    }

    public EspConnection() {
        this("hac.pvsd.org");
    }

    private static boolean isRedirect(int status) {
        boolean redirect = false;
        if(status != HttpsURLConnection.HTTP_OK) {
            if(status == HttpsURLConnection.HTTP_MOVED_TEMP
                    || status == HttpsURLConnection.HTTP_MOVED_PERM
                    || status == HttpsURLConnection.HTTP_SEE_OTHER)
                redirect = true;
        }
        return redirect;
    }

    private static String readHttpInputStream(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(
                              new InputStreamReader(is, "utf-8"));
	    String inputLine;
	    StringBuffer response = new StringBuffer();

	    while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
	    }
	    in.close();
        return response.toString();
    }

    public String logOn(String username, String password) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection)
                new URL(BASE_URL + "/HomeAccess/Account/LogOn?ReturnUrl=%2fHomeAccess%2fClasses%2fClasswork").openConnection();
        byte[] data = String.format(
        "Database=10&LogOnDetails.UserName=%s&LogOnDetails.Password=%s", username, password).getBytes("UTF-8");
        //conn.setReadTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.addRequestProperty("Origin", BASE_URL);
        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.9");
        conn.addRequestProperty("Upgrade-Insecure-Requests", "1");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko Firefox");
        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.addRequestProperty("Content-Length", String.valueOf(data.length));
        conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        conn.addRequestProperty("Cache-Control", "max-age=0");
        conn.addRequestProperty("Referer", BASE_URL + "/HomeAccess/Account/LogOn");
        conn.addRequestProperty("Connection", "keep-alive");

        DataOutputStream wr = new DataOutputStream (conn.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();

        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        StringBuilder cookiesBuilder = new StringBuilder();

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                if(!cookie.contains("=;"))
                    cookiesBuilder.append(cookie.split(";")[0] + "; ");
            }
        }
        cookies = cookiesBuilder.toString();
        cookies = cookies.substring(0, cookies.length() - 2);

        if(isRedirect(conn.getResponseCode())) {
            String newUrl = conn.getHeaderField("Location");

            conn = (HttpsURLConnection) new URL(BASE_URL + newUrl).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Host", HOST);
            conn.setRequestProperty("Cookie", cookies);

            conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.9");
            conn.addRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko Firefox");
            conn.addRequestProperty("Referer", BASE_URL + "/HomeAccess/Account/LogOn?ReturnUrl=%2fHomeAccess%2fClasses%2fClasswork");
            conn.addRequestProperty("Cache-Control", "max-age=0");
            conn.addRequestProperty("Connection", "keep-alive");
        }

        return readHttpInputStream(conn.getInputStream());
    }

    public String getAssignments() throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection)
                new URL(BASE_URL + "/HomeAccess/Content/Student/Assignments.aspx").openConnection();
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Host", HOST);
        conn.setRequestProperty("Cookie", cookies);
        conn.setRequestProperty("Referer", BASE_URL + "/HomeAccess/Classes/Classwork");
        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.9");
        conn.addRequestProperty("Upgrade-Insecure-Requests", "1");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko Firefox");
        conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        conn.addRequestProperty("Connection", "keep-alive");

	    return readHttpInputStream(conn.getInputStream());
    }
}
