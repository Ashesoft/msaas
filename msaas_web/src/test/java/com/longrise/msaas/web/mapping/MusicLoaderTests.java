package com.longrise.msaas.web.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.IdWorker;
import com.longrise.msaas.mapping.AudioMapping;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MusicLoaderTests {
    private static final String MUSICSRC = "D:\\Music\\CloudMusic\\";
    private static List<EntityBean> beans = new ArrayList<>(100);

    private File file = new File(MUSICSRC);
    private File[] files = Optional.ofNullable(file.listFiles()).orElseGet(() -> new File[0]);

    private IdWorker idWorker = new IdWorker(1,1,1);

    @Autowired
    private AudioMapping audioMapping;

    /**
     * 批量新增音频信息文件到数据库
     */
    @Test
    public void insertAudio() {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
        System.out.println("=========================================music文件配置开始==================================================");
        try {
            for (var i = 0; i < files.length; i++) {
                if (files[i].isDirectory() || files[i].isHidden()) {
                    continue;
                }
                String songName, singer, album, imgsrc, path = "/music/CloudMusic/" + files[i].getName();
//                Map matedata = (Map) getBitrate(files[i].getAbsolutePath()).get("tags");
//                if(matedata != null){
//                    songName = (String) matedata.get("title");
//                    singer = (String) matedata.get("artist");
//                    album = (String) matedata.get("album");
//                }else{
//                    songName = files[i].getName();
//                    singer = "群星";
//                    album = "未知";
//                }

                MP3File audio = new MP3File(files[i]);
                AudioHeader head = audio.getAudioHeader();
                int duration = head.getTrackLength(); // 时长

                if (audio.getID3v2Tag() != null) {
                    songName = getInfoFromFrameMap(audio, "TIT2"); // 歌曲名
                    singer = getInfoFromFrameMap(audio, "TPE1"); // 歌手名
                    album = getInfoFromFrameMap(audio, "TALB"); // 专辑名
                    imgsrc = getInfoFromFrameMap(audio, "APIC");
                } else if (audio.getID3v1Tag() != null) {
                    songName = action(audio.getID3v1Tag().getTitle());
                    singer = action(audio.getID3v1Tag().getArtist());
                    album = action(audio.getID3v1Tag().getAlbum());
                    imgsrc = "/img/audio/player/red-qtbg-big.png";
                } else {
                    songName = audio.getFile().getName();
                    singer = audio.getFile().getName();
                    album = "未知";
                    imgsrc = "/img/audio/player/red-qtbg-big.png";
                }
                System.out.println(i + ", " + songName + ", " + singer + ", " + album + ", " + path);
                // 追加数据库
//                EntityBean audioinfo = new EntityBean();
//                audioinfo.put("createtime", LocalDateTime.now());
//                audioinfo.put("imgsrc", imgsrc);
//                audioinfo.put("audiosrc", path);
//                audioinfo.put("programtitle", songName);
//                audioinfo.put("podcaster", singer);
//                audioinfo.put("channeltitle", album);
//                audioinfo.put("timetotal", duration);
//                audioinfo.put("progress", 0);
//                beans.add(audioinfo);
            }
//            EntityBean[] _beans = new EntityBean[beans.size()];
//            beans.toArray(_beans);
            //audioMapping.insetAudioInfo(_beans);
            System.out.println("=========================================music文件配置成功==================================================");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 向数据库里添加音乐信息
     */
    @Test
    public void getAudioInfo() {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
        try {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() || files[i].isHidden()) {
                    continue;
                }
                String songName, singer, album, imgsrc, path = "/music/CloudMusic/" + files[i].getName();

                AudioFile audioFile = AudioFileIO.read(files[i]);
                AudioHeader header = audioFile.getAudioHeader();
                int duration = header.getTrackLength();

                Tag tag = audioFile.getTag();
                if(tag.isEmpty()){
                    songName = audioFile.getFile().getName();
                    singer = "群星";
                    album = "未知";
                    imgsrc = "/img/audio/player/red-qtbg-big.png";
                }else{
                    songName = coverEncode(tag.getFirst(FieldKey.TITLE));
                    singer = coverEncode(tag.getFirst(FieldKey.ARTIST));
                    album = coverEncode(tag.getFirst(FieldKey.ALBUM));
                    List<TagField> fields = tag.getFields(FieldKey.COVER_ART);
                    if (fields.isEmpty()) {
                        imgsrc = "/img/audio/player/red-qtbg-big.png";
                    }else {
                        TagField tagField = fields.get(0);
                        if (tagField instanceof MetadataBlockDataPicture){
                            byte[] imageData = ((MetadataBlockDataPicture) tagField).getImageData();
                            imgsrc = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
                        }else{
                            AbstractID3v2Frame frame = (AbstractID3v2Frame) tagField;
                            FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
                            byte[] imageData = body.getImageData();
                            imgsrc = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
                        }
                    }
                }
                System.out.println(i + ", " + songName + ", " + singer + ", " + album + ", " + path);
                // 追加数据库
                EntityBean audioinfo = new EntityBean();
                audioinfo.put("id", idWorker.nextId());
                audioinfo.put("createtime", LocalDateTime.now());
                audioinfo.put("imgsrc", imgsrc);
                audioinfo.put("audiosrc", path);
                audioinfo.put("programtitle", songName);
                audioinfo.put("podcaster", singer);
                audioinfo.put("channeltitle", album);
                audioinfo.put("timetotal", duration);
                audioinfo.put("progress", 0);
                beans.add(audioinfo);
            }
            EntityBean[] _beans = new EntityBean[beans.size()];
            beans.toArray(_beans);
            audioMapping.insetAudioInfo(_beans);
        } catch (Exception e) {
            System.err.println("e.getMessage() = " + e.getMessage());
        }
    }

    /**
     * 通过键值,获取歌曲中对应的字段信息
     *
     * @param mp3File mp3音乐文件
     * @param key     键值
     * @return 歌曲信息
     */
    private static String getInfoFromFrameMap(MP3File mp3File, String key) {
        HashMap map = mp3File.getID3v2Tag().frameMap;
        if (map.containsKey(key)) {
            AbstractID3v2Frame frame = (AbstractID3v2Frame) map.get(key);
            if ("APIC".equals(key)) {
                FrameBodyAPIC fba = (FrameBodyAPIC) frame.getBody();
                byte[] bytes = fba.getImageData();
                fba.getMimeType();
                /**
                 * data:text/plain,文本数据
                 * data:text/html,HTML代码
                 * data:text/html;base64,base64编码的HTML代码
                 * data:text/css,CSS代码
                 * data:text/css;base64,base64编码的CSS代码
                 * data:text/javascript,Javascript代码
                 * data:text/javascript;base64,base64编码的Javascript代码
                 * data:image/gif;base64,base64编码的gif图片数据
                 * data:image/png;base64,base64编码的png图片数据
                 * data:image/jpeg;base64,base64编码的jpeg图片数据
                 * data:image/x-icon;base64,base64编码的icon图片数据
                 */
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
            }
            try {
                if (frame.getContent().isBlank() || frame.getContent().isEmpty()) {
                    return "群星";
                } else if ("ISO-8859-1".equalsIgnoreCase(frame.getEncoding())) {
                    map.put("info", new String(frame.getContent().getBytes(StandardCharsets.ISO_8859_1), "GBK"));
                } else if (frame.getEncoding().startsWith("UTF")) {
                    map.put("info", frame.getContent());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            switch (key) {
                case "TIT2":
                case "TPE1":
                    map.put("info", mp3File.getFile().getName());
                    break;
                case "TALB":
                    map.put("info", "群星");
                    break;
                case "APIC":
                    map.put("info", "/img/audio/red-qtbg-big.png");
                    break;
            }
        }
        return map.get("info").toString();
    }

    private static String action(List<TagField> list) {
        if (list.size() != 0) {
            try {
                return new String(list.get(0).toString().getBytes(StandardCharsets.ISO_8859_1), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "群星";
    }

    public static Map getBitrate(String path) throws IOException, InterruptedException {
        String cmd = "D:\\Users\\Administrator\\ffmpeg\\bin/ffprobe.exe -v quiet -print_format json -show_format -i \"" + path + "\"";
        //System.out.println(cmd);
        Runtime run = Runtime.getRuntime();
        Process p = run.exec(cmd);
        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        String lineStr;
        while ((lineStr = inBr.readLine()) != null) {
            sb.append(lineStr);
        }
        if (p.waitFor() != 0 || p.exitValue() == 1) {
            System.err.println("命令执行失败!");
        }
        inBr.close();
        in.close();
        HashMap map = new ObjectMapper().readValue(sb.toString().toLowerCase(), HashMap.class);
        Map format = (Map) map.get("format");
        return format;
    }

    public static String coverEncode(String ostr) throws UnsupportedEncodingException {
        String nstr = new String(ostr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
        if (ostr.equals(nstr)){
            return new String(nstr.getBytes(StandardCharsets.ISO_8859_1), "GBK");
        }else{
            return ostr;
        }
    }
}