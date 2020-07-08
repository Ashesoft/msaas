package com.longrise.msaas.web.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import com.longrise.msaas.mapping.AudioMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityBeanTest {
    @Autowired
    private JDBCExcutor jdbcExcutor;

    @Autowired
    private AudioMapping audioMapping;

    @Test
    public void t4(){
        String[] pks = audioMapping.getPK("users");
        System.out.println(Arrays.toString(pks));
    }

    @Test
    public void t3(){
        System.out.println("t3=====" + audioMapping.getAudioInfo());
    }

    @Test
    public void t2(){
        EntityBean[] beans = new EntityBean[2];
        beans[0] = new EntityBean(){{
            put("id", 334);
            put("createtime", LocalDateTime.now());
            put("imgsrc", "imgsrc1");
            put("audiosrc", "audiopath1");
            put("programtitle", "songName1");
            put("podcaster", "singer1");
            put("channeltitle", "album1");
            put("timetotal", 3001);
            put("progress", 01);
        }};
        beans[1] = new EntityBean(){{
            put("id", 337);
            put("createtime", LocalDateTime.now());
            put("imgsrc", "imgsrc2");
            put("audiosrc", "audiopath2");
            put("programtitle", "songName2");
            put("podcaster", "singer2");
            put("channeltitle", "album2");
            put("timetotal", 3002);
            put("progress", 02);
        }};
        String sql = "insert into audiolist (createtime, imgsrc, audiosrc, programtitle, podcaster, channeltitle, timetotal, progress) " +
                "values (:createtime, :imgsrc, :audiosrc, :programtitle, :podcaster, :channeltitle, :timetotal, :progress)";
        jdbcExcutor.insertUpdateDeletes(sql, beans);
    }

    @Test
    public void t1(){
        EntityBean audioinfo = new EntityBean("audiolist");
        audioinfo.put("createtime", LocalDateTime.now());
        audioinfo.put("imgsrc", "imgsrc");
        audioinfo.put("audiosrc", "audiopath");
        audioinfo.put("programtitle", "songName");
        audioinfo.put("podcaster", "singer");
        audioinfo.put("channeltitle", "album");
        audioinfo.put("timetotal", 300);
        audioinfo.put("progress", 0);
        System.out.println(audioinfo.delete());
    }
}
