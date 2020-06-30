package com.longrise.msaas.web.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.IdWorker;
import com.longrise.msaas.mapping.StoryMapping;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryTest {

    private String path = System.getProperty("user.dir") + "/src/test/resources/temp/";
    private IdWorker idWorker = new IdWorker(1,1,1);
    private List<EntityBean> list = new ArrayList<>(85);

    @Autowired
    private StoryMapping storyMapping;

    @Test
    public void readURLTxt() {
        //System.out.println(path);
        File file = new File(path, "url.txt");
        try (FileReader reader = new FileReader(file); BufferedReader buffer = new BufferedReader(reader);) {
            String row;
            while ((row = buffer.readLine()) != null) {
                String[] arys = row.split(" ==> ");
                EntityBean bean = new EntityBean(1){{
                    put("stitlename", arys[0]);
                    put("path", arys[1]);
                }};
                this.getContent(bean);
            }
            EntityBean[] beans = new EntityBean[list.size()];
            list.toArray(beans);
            storyMapping.insetBookInfo(beans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getContent(EntityBean node) {
        URI uri = URI.create(((String) node.get("path")));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(Charset.forName("GBK")));
            String html = response.body();
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select("#f_article p");
            node.put("scontent", elements.toString());
            node.put("sid", idWorker.nextId());
            node.put("sbookname", "hlm");
            node.put("createtime", LocalDateTime.now());
            System.out.println(node.toJsonString());
            list.add(node);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
