package com.back;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private static final String baseDir = "db/wiseSaying";

    public WiseSayingRepository() {
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void save(WiseSaying ws) {
        try (FileWriter fw = new FileWriter(baseDir + "/" + ws.id + ".json")) {
            fw.write("{\n");
            fw.write("  \"id\": " + ws.id + ",\n");
            fw.write("  \"content\": \"" + escape(ws.content) + "\",\n");
            fw.write("  \"author\": \"" + escape(ws.author) + "\"\n");
            fw.write("}");

            saveLastId(WiseSaying.lastId);
        } catch (IOException e) {
            System.out.println("저장 실패: " + e.getMessage());
        }
    }

    public void delete(int id) {
        File file = new File(baseDir + "/" + id + ".json");
        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("파일 삭제 실패: " + id);
            }
        }
    }

    public List<WiseSaying> loadAll() {
        List<WiseSaying> list = new ArrayList<>();
        File dir = new File(baseDir);
        File[] files = dir.listFiles();

        if (files == null) return list;

        for (File file : files) {
            if (file.getName().endsWith(".json")) {
                WiseSaying ws = load(file);
                if (ws != null) {
                    list.add(ws);
                    if (ws.id > WiseSaying.lastId) {
                        WiseSaying.lastId = ws.id;
                    }
                }
            }
        }
        return list;
    }

    private WiseSaying load(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int id = 0;
            String content = "";
            String author = "";

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(line.split(":")[1].replace(",", "").trim());
                } else if (line.startsWith("\"content\"")) {
                    content = line.split(":")[1].trim().replaceAll("^\"|\",?$", "");
                } else if (line.startsWith("\"author\"")) {
                    author = line.split(":")[1].trim().replaceAll("^\"|\"$", "");
                }
            }
            WiseSaying ws = new WiseSaying(content, author);
            ws.id = id;
            return ws;
        } catch (IOException | NumberFormatException e) {
            System.out.println("파일 읽기 실패: " + file.getName());
            return null;
        }
    }

    private void saveLastId(int id) {
        try (FileWriter fw = new FileWriter(baseDir + "/lastId.txt")) {
            fw.write(id + "");
        } catch (IOException e) {
            System.out.println("lastId 저장 실패");
        }
    }

    private String escape(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public void buildDataJson(List<WiseSaying> list) {
        try (FileWriter fw = new FileWriter(baseDir + "/data.json")) {
            fw.write("[\n");

            for (int i = 0; i < list.size(); i++) {
                WiseSaying ws = list.get(i);
                fw.write("  {\n");
                fw.write("    \"id\": " + ws.id + ",\n");
                fw.write("    \"content\": \"" + escape(ws.content) + "\",\n");
                fw.write("    \"author\": \"" + escape(ws.author) + "\"\n");
                fw.write("  }");
                if (i != list.size() -1) {
                    fw.write(",\n");
                } else {
                    fw.write("\n");
                }
            }

            fw.write("]");
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (IOException e) {
            System.out.println("data.json 생성 실패: " + e.getMessage());
        }
    }


}
