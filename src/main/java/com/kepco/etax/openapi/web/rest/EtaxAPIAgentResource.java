package com.kepco.etax.openapi.web.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EtaxAPIAgentResource {

    private final Logger log = LoggerFactory.getLogger(EtaxAPIAgentResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final String ETAX_STATUS_API_URL = "https://api.odcloud.kr/api/nts-businessman/v1/status";

    private final String ETAX_VALIDATE_API_URL = "https://api.odcloud.kr/api/nts-businessman/v1/validate";

    private final String API_ENCODING_KEY =
        "Hp%2FQS2VGiw1J%2FmoPr2LV6%2Bvs0%2Fm1tiDZbP5BtqtsdRBjXEFbEouNh2nDVVfIDF0UX2mZLLQEsHglWVVbpMqG8A%3D%3D";

    private final String API_DECODING_KEY = "Hp/QS2VGiw1J/moPr2LV6+vs0/m1tiDZbP5BtqtsdRBjXEFbEouNh2nDVVfIDF0UX2mZLLQEsHglWVVbpMqG8A==";

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/nts-businessman/v1/status")
    @ResponseBody
    public String etaxSaupStatusAPI(@RequestBody String data, @RequestParam("returnType") String returnType) {
        String requestData = data;

        String serviceURL = this.ETAX_STATUS_API_URL + "?serviceKey=" + this.API_ENCODING_KEY + "&returnType=" + returnType;

        String returnValue = postAPI(serviceURL, requestData);

        return returnValue;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/nts-businessman/v1/validate")
    @ResponseBody
    public String etaxSaupValidateAPI(@RequestBody String data, @RequestParam("returnType") String returnType) {
        String requestData = data;

        String serviceURL = this.ETAX_VALIDATE_API_URL + "?serviceKey=" + this.API_ENCODING_KEY + "&returnType=" + returnType;

        String returnValue = postAPI(serviceURL, requestData);

        return returnValue;
    }

    public String postAPI(String serviceURL, String requestData) {
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(serviceURL);
            //URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(10000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(10000); // InputStream 읽어 오는 Timeout 시간 설정

            con.setRequestMethod("POST");

            //json으로 message를 전달하고자 할 때
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
            //            con.setUseCaches(false);
            //            con.setDefaultUseCaches(false);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            //
            //            ObjectMapper objectMapper = new ObjectMapper();
            //
            //            List<String> arr = new ArrayList<String>();
            //            arr.add("1081921785");
            //
            //            JSONObject obj1 = new JSONObject();
            //            obj1.put("b_no", arr);
            //
            //            String jsonString = objectMapper.writeValueAsString(obj1);

            wr.write(requestData); //json 형식의 message 전달
            wr.flush();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                System.out.println("" + sb.toString());
                return sb.toString();
            } else {
                System.out.println(con.getResponseMessage());
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return sb.toString();
    }
}
