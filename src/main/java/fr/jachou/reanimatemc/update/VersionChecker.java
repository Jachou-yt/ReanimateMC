package fr.jachou.reanimatemc.update;

import fr.jachou.reanimatemc.ReanimateMC;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionChecker {
    private static String checkVersion() {
        String pasteBinUrl = "https://pastebin.com/raw/QRX6Y4C0";

        try {
            URL url = new URL(pasteBinUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                return content.toString();
            } else {
                System.out.println(ReanimateMC.PREFIX + "Error while check update : " + responseCode);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isUpToDate() {
        String version = checkVersion();
        if (version == null) {
            return true;
        }
        return version.equals(ReanimateMC.getInstance().getDescription().getVersion());
    }

    public static String getLatestVersion() {
        return checkVersion();
    }
}
